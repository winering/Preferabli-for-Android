//
//  Tools_JournalTools.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import retrofit2.Response;

public class Tools_JournalTools {

    private static Tools_JournalTools journalTools;
    private Semaphore getItemsSempahore;
    private Semaphore journalToolsSemaphore;
    private boolean error;

    public Tools_JournalTools() {
        journalToolsSemaphore = new Semaphore(1);
        getItemsSempahore = new Semaphore(10);
    }

    public static Tools_JournalTools getInstance() {
        if (journalTools == null) journalTools = new Tools_JournalTools();
        return journalTools;
    }

    public void clearData() {
        journalToolsSemaphore.release();
        journalTools = null;
    }

    public void getProductsInitial() throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        getProducts(false);
    }

    public ArrayList<Object_Product> getProducts(boolean forceRefresh) throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        if (forceRefresh || !Tools_PreferabliTools.getKeyStore().getBoolean("hasCalledRatings", false) || !Tools_PreferabliTools.getKeyStore().getBoolean("hasCalledWishlist", false)) {
            journalToolsSemaphore.acquire();
            loadJournalFromAPI(Other_TagType.RATING);
            loadJournalFromAPI(Other_TagType.WISHLIST);
            loadPurchasesFromAPI(forceRefresh);
            journalToolsSemaphore.release();
        } else {
            if (Tools_PreferabliTools.hasDaysPassed(5, Tools_PreferabliTools.getKeyStore().getLong("lastGrabbedRatings", 0))) {
                Tools_PreferabliTools.startNewWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadJournalFromAPI(Other_TagType.RATING);
                            EventBus.getDefault().post(Tools_JournalTools.this);
                        } catch (Exception e) {
                            // catch exception so that we can still pull up cached data
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
            }


            if (Tools_PreferabliTools.hasDaysPassed(5, Tools_PreferabliTools.getKeyStore().getLong("lastGrabbedWishlist", 0))) {
                Tools_PreferabliTools.startNewWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadJournalFromAPI(Other_TagType.WISHLIST);
                            EventBus.getDefault().post(Tools_JournalTools.this);
                        } catch (Exception e) {
                            // catch exception so that we can still pull up cached data
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
            }

            if (Tools_PreferabliTools.hasDaysPassed(5, Tools_PreferabliTools.getKeyStore().getLong("lastGrabbedPurchaseHistory", 0))) {
                Tools_PreferabliTools.startNewWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadPurchasesFromAPI(false);
                            EventBus.getDefault().post(Tools_JournalTools.this);
                        } catch (Exception e) {
                            // catch exception so that we can still pull up cached data
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
            }
        }

        return getProductsfromDB();

    }

    public ArrayList<Object_Product> getProductsfromDB() {
        ArrayList<Object_Product> products;
        Tools_DBHelper.getInstance().openDatabase();
        products = Tools_DBHelper.getInstance().getJournalProducts();
        Tools_DBHelper.getInstance().closeDatabase();
        return products;
    }

    public void loadJournalFromAPI(Other_TagType journalType) throws API_PreferabliException, NullPointerException, InterruptedException {
        try {
            getProductsFromAPI(0);


        } catch (API_PreferabliException e) {
            throw e;
        }
    }

    public void loadPurchasesFromAPI(boolean forceRefresh) throws API_PreferabliException, NullPointerException, InterruptedException, IOException {
        Tools_PreferabliTools.getKeyStore().edit().putLong("lastGrabbedPurchaseHistory", System.currentTimeMillis()).apply();
        ArrayList<Object_UserCollection> userCollections = Tools_UserCollectionsTools.getInstance().getUserCollections(forceRefresh, "purchase");
        for (Object_UserCollection userCollection : userCollections) {
            getProductsFromAPI(userCollection.getCollection_id());
        }

    }

    public void getProductsFromAPI(long collectionId) throws API_PreferabliException, NullPointerException, InterruptedException {
        try {
            error = false;

            Tools_DBHelper.getInstance().openDatabase();
            Tools_DBHelper.getInstance().clearTagTable(collectionId, true);
            Tools_DBHelper.getInstance().closeDatabase();

            loadCollectionViaTags(collectionId);

            while (getItemsSempahore.availablePermits() != 10) {
                Thread.sleep(100);
            }

            if (error) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
            }

        } catch (API_PreferabliException e) {
            journalToolsSemaphore.release();
            throw e;
        }
    }

    public void loadCollectionViaTags(long collectionId) throws InterruptedException, API_PreferabliException {
        Tools_DBHelper.getInstance().openDatabase();
        Object_Collection collection = Tools_DBHelper.getInstance().getCollection(collectionId);
        Tools_DBHelper.getInstance().closeDatabase();

        if (collection == null) {
            // We don't have the collection in question
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.UnknownError);
        }

        int limit = 50;
        int offsetOverall = -limit;
        while (offsetOverall <= collection.getWineCount()) {
            getItemsSempahore.acquire();
            offsetOverall = offsetOverall + limit;
            final int offset = offsetOverall;
            Tools_PreferabliTools.startNewWorkThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<ArrayList<Object_Tag>> tagsResponse = API_Singleton.getInstanceService().getTags(collectionId, offset, limit).execute();
                        if (!tagsResponse.isSuccessful())
                            throw new API_PreferabliException(tagsResponse.errorBody());
                        ArrayList<Object_Tag> tags = tagsResponse.body();

                        if (tags.size() > 0) {
                            Tools_DBHelper.getInstance().openDatabase();
                            ArrayList<Long> variant_ids = new ArrayList<>();
                            for (Object_Tag tag : tags) {
                                variant_ids.add(tag.getVariantId());
                                Tools_DBHelper.getInstance().updateTagTable(tag);
                            }
                            Tools_DBHelper.getInstance().closeDatabase();

                            Response<ArrayList<Object_Product>> productsResponse = API_Singleton.getInstanceService().getProducts(variant_ids).execute();
                            if (!productsResponse.isSuccessful())
                                throw new API_PreferabliException(productsResponse.errorBody());
                            ArrayList<Object_Product> products = productsResponse.body();

                            Tools_DBHelper.getInstance().openDatabase();
                            for (Object_Product product : products) {
                                Tools_DBHelper.getInstance().updateWineTable(product);
                            }
                            Tools_DBHelper.getInstance().closeDatabase();

                        }
                    } catch (API_PreferabliException | IOException e) {
                        error = true;
                    } finally {
                        getItemsSempahore.release();
                    }
                }
            });
        }
    }

    // Check if we are loading.
    public boolean isLoading() {
        return journalToolsSemaphore.availablePermits() == 0;
    }
}
