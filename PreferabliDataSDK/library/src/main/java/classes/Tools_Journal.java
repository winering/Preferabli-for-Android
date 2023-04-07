//
//  Tools_Journal.java
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

public class Tools_Journal {

    private static Tools_Journal journalTools;
    private Semaphore getItemsSempahore;
    private Semaphore journalToolsSemaphore;
    private boolean error;

    public Tools_Journal() {
        journalToolsSemaphore = new Semaphore(1);
        getItemsSempahore = new Semaphore(10);
    }

    public static Tools_Journal getInstance() {
        if (journalTools == null) journalTools = new Tools_Journal();
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
        if (forceRefresh || !Tools_Preferabli.getKeyStore().getBoolean("hasCalledRatings", false) || !Tools_Preferabli.getKeyStore().getBoolean("hasCalledWishlist", false)) {
            journalToolsSemaphore.acquire();
            loadJournalFromAPI(Other_TagType.RATING);
            loadJournalFromAPI(Other_TagType.WISHLIST);
            loadPurchasesFromAPI(forceRefresh);
            journalToolsSemaphore.release();
        } else {
            if (Tools_Preferabli.hasDaysPassed(5, Tools_Preferabli.getKeyStore().getLong("lastGrabbedRatings", 0))) {
                Tools_Preferabli.startNewWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadJournalFromAPI(Other_TagType.RATING);
                            EventBus.getDefault().post(Tools_Journal.this);
                        } catch (Exception e) {
                            // catch exception so that we can still pull up cached data
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
            }


            if (Tools_Preferabli.hasDaysPassed(5, Tools_Preferabli.getKeyStore().getLong("lastGrabbedWishlist", 0))) {
                Tools_Preferabli.startNewWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadJournalFromAPI(Other_TagType.WISHLIST);
                            EventBus.getDefault().post(Tools_Journal.this);
                        } catch (Exception e) {
                            // catch exception so that we can still pull up cached data
                            e.printStackTrace();
                        } finally {
                        }
                    }
                });
            }

            if (Tools_Preferabli.hasDaysPassed(5, Tools_Preferabli.getKeyStore().getLong("lastGrabbedPurchaseHistory", 0))) {
                Tools_Preferabli.startNewWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadPurchasesFromAPI(false);
                            EventBus.getDefault().post(Tools_Journal.this);
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
        Tools_Database.getInstance().openDatabase();
        products = Tools_Database.getInstance().getJournalProducts();
        Tools_Database.getInstance().closeDatabase();
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
        Tools_Preferabli.getKeyStore().edit().putLong("lastGrabbedPurchaseHistory", System.currentTimeMillis()).apply();
        ArrayList<Object_UserCollection> userCollections = Tools_UserCollections.getInstance().getUserCollections(forceRefresh, "purchase");
        for (Object_UserCollection userCollection : userCollections) {
            getProductsFromAPI(userCollection.getCollection_id());
        }

    }

    public void getProductsFromAPI(long collectionId) throws API_PreferabliException, NullPointerException, InterruptedException {
        try {
            error = false;

            Tools_Database.getInstance().openDatabase();
            Tools_Database.getInstance().clearTagTable(collectionId, true);
            Tools_Database.getInstance().closeDatabase();

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
        Tools_Database.getInstance().openDatabase();
        Object_Collection collection = Tools_Database.getInstance().getCollection(collectionId);
        Tools_Database.getInstance().closeDatabase();

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
            Tools_Preferabli.startNewWorkThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<ArrayList<Object_Tag>> tagsResponse = API_Singleton.getInstanceService().getTags(collectionId, offset, limit).execute();
                        if (!tagsResponse.isSuccessful())
                            throw new API_PreferabliException(tagsResponse.errorBody());
                        ArrayList<Object_Tag> tags = tagsResponse.body();

                        if (tags.size() > 0) {
                            Tools_Database.getInstance().openDatabase();
                            ArrayList<Long> variant_ids = new ArrayList<>();
                            for (Object_Tag tag : tags) {
                                variant_ids.add(tag.getVariantId());
                                Tools_Database.getInstance().updateTagTable(tag);
                            }
                            Tools_Database.getInstance().closeDatabase();

                            Response<ArrayList<Object_Product>> productsResponse = API_Singleton.getInstanceService().getProducts(variant_ids).execute();
                            if (!productsResponse.isSuccessful())
                                throw new API_PreferabliException(productsResponse.errorBody());
                            ArrayList<Object_Product> products = productsResponse.body();

                            Tools_Database.getInstance().openDatabase();
                            for (Object_Product product : products) {
                                Tools_Database.getInstance().updateWineTable(product);
                            }
                            Tools_Database.getInstance().closeDatabase();

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
