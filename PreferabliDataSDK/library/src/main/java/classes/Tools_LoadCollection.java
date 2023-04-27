//
//  Tools_LoadCollection.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import retrofit2.Response;

/**
 * Contains methods that help load {@link Object_Collection}s.
 */
class Tools_LoadCollection {

    private static Tools_LoadCollection loadCollectionTools;

    static Tools_LoadCollection getInstance() {
        if (loadCollectionTools == null) loadCollectionTools = new Tools_LoadCollection();
        return loadCollectionTools;
    }

    void clearData() {
        loadCollectionTools = null;
    }

     Object_Collection getCollection(boolean force_refresh, long collectionId) throws IOException, API_PreferabliException {
        Tools_Database.getInstance().openDatabase();
        Object_Collection collection = Tools_Database.getInstance().getCollection(collectionId);
        Tools_Database.getInstance().closeDatabase();

        if (collection == null || force_refresh) {
            Response<Object_Collection> collectionResponse = API_Singleton.getInstanceService().getCollection(collectionId).execute();
            if (!collectionResponse.isSuccessful())
                throw new API_PreferabliException(collectionResponse.errorBody());
            Tools_Preferabli.saveCollectionEtag(collectionResponse, collectionId);
            collection = collectionResponse.body();
            Tools_Database.getInstance().openDatabase();
            Tools_Database.getInstance().deleteCollection(collection);
            Tools_Database.getInstance().updateCollectionTable(collection);
            collection = Tools_Database.getInstance().getCollection(collectionId);
            Tools_Database.getInstance().closeDatabase();
        }

        return collection;
    }

    void loadCollectionViaTags(int priority, boolean force_refresh, long collection_id) throws Exception {
        if (force_refresh || !Tools_Preferabli.getKeyStore().getBoolean("hasLoaded" + collection_id, false)) {
            Tools_LoadCollection.getInstance().getTagsAndProducts(collection_id, priority, force_refresh);
        } else if (Tools_Preferabli.hasMinutesPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalled" + collection_id, 0))) {
            Tools_Preferabli.startNewWorkThread(Preferabli.PRIORITY_LOW, () -> {
                try {
                    Tools_LoadCollection.getInstance().getTagsAndProducts(collection_id, Preferabli.PRIORITY_LOW, false);
                } catch (Exception e) {
                    // catching any issues here so that we can still pull up our saved data
                    if (Preferabli.isLoggingEnabled()) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                }
            });
        }
    }

    private void getTagsAndProducts(long collectionId, int priority, boolean force_refresh) throws InterruptedException, API_PreferabliException, IOException {
        Object_Collection collection = getCollection(force_refresh, collectionId);

        Tools_Database.getInstance().openDatabase();
        Tools_Database.getInstance().clearTagTable(collectionId, false);
        Tools_Database.getInstance().closeDatabase();

        int limit = 50;
        Semaphore getItemsSempahore = new Semaphore(10);
        final boolean[] error = { false };
        int offsetOverall = -limit;
        while (offsetOverall <= collection.getProductCount()) {
            getItemsSempahore.acquire();
            offsetOverall = offsetOverall + limit;
            final int offset = offsetOverall;
            Tools_Preferabli.startNewAPIWorkThread(priority, () -> {
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
                            Tools_Database.getInstance().updateProductTable(product);
                        }
                        Tools_Database.getInstance().closeDatabase();

                    }
                } catch (API_PreferabliException | IOException e) {
                    error[0] = true;
                } finally {
                    getItemsSempahore.release();
                }
            });

            if (getItemsSempahore.availablePermits() != 10) {
                Thread.sleep(100);
            }

            if (error[0]) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
            }
        }

        Tools_Preferabli.getKeyStore().edit().putLong("lastCalled" + collectionId, System.currentTimeMillis()).apply();
        Tools_Preferabli.getKeyStore().edit().putBoolean("hasLoaded" + collectionId, true).apply();
    }

    void loadCollectionViaOrderings(long collectionId, int priority) throws InterruptedException, API_PreferabliException {
        Tools_Database.getInstance().openDatabase();
        Object_Collection collection = Tools_Database.getInstance().getCollection(collectionId);
        Tools_Database.getInstance().closeDatabase();

        if (collection == null) {
            // We don't have the collection in question
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.OtherError);
        }

        Object_Collection.Object_CollectionVersion version = collection.getFirstVersion();
        final int limit = 100;
        Semaphore getItemsSempahore = new Semaphore(10);
        final boolean[] error = { false };

        HashMap<Object_Collection.Object_CollectionGroup, ArrayList<Object_Collection.Object_CollectionOrder>> orderingMap = new HashMap<>();
        for (Object_Collection.Object_CollectionGroup group : version.getGroups()) {
            int offset = 0;
            ArrayList<Object_Collection.Object_CollectionOrder> orderingsToInsert = new ArrayList<>();
            Semaphore addSem = new Semaphore(1);
            orderingMap.put(group, orderingsToInsert);
            while (offset <= group.getOrderingsCount()) {
                final int finalOffset = offset;
                Tools_Preferabli.startNewWorkThread(priority, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<Object_Collection.Object_CollectionOrder> orderings = getGroupItems(collection, version, group, limit, finalOffset, 0);
                            addSem.acquire();
                            orderingsToInsert.addAll(orderings);
                            addSem.release();
                        } catch (API_PreferabliException | InterruptedException e) {
                            e.printStackTrace();
                            error[0] = true;
                        } finally {
                            getItemsSempahore.release();
                        }
                    }
                });
                getItemsSempahore.acquire();
                offset = offset + limit;
            }
        }

        while (getItemsSempahore.availablePermits() != 10) {
            Thread.sleep(100);
        }

        if (error[0]) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
        }

        // we need to insert all orderings together after the old are cleared after all is done so to prevent data loss.
        Tools_Database.getInstance().openDatabase();
        for (Object_Collection.Object_CollectionGroup group : version.getGroups()) {
            ArrayList<Object_Collection.Object_CollectionOrder> orderingsToInsert = orderingMap.get(group);
            Tools_Database.getInstance().clearGroupOrderings(group);
            Tools_Database.getInstance().updateOrderingTable(orderingsToInsert);
        }
        Tools_Database.getInstance().closeDatabase();

        Tools_Preferabli.getKeyStore().edit().putLong("lastCalled" + collectionId, System.currentTimeMillis()).apply();
        Tools_Preferabli.getKeyStore().edit().putBoolean("hasLoaded" + collectionId, true).apply();
    }

    private ArrayList<Object_Collection.Object_CollectionOrder> getGroupItems(Object_Collection collection, Object_Collection.Object_CollectionVersion version, Object_Collection.Object_CollectionGroup group, int limit, int offset, int failCount) throws API_PreferabliException {
        try {
            if (Thread.interrupted()) return new ArrayList<>();
            Response<ArrayList<Object_Collection.Object_CollectionOrder>> orderingResponse = API_Singleton.getInstanceService().getOrderings(collection.getId(), version.getId(), group.getId(), limit, offset).execute();
            if (Thread.interrupted()) return new ArrayList<>();
            if (!orderingResponse.isSuccessful())
                throw new API_PreferabliException(orderingResponse.errorBody());
            Tools_Preferabli.saveCollectionEtag(orderingResponse, collection.getId());
            ArrayList<Object_Collection.Object_CollectionOrder> orderings = orderingResponse.body();

            if (orderings.size() != 0) {
                ArrayList<Long> tagIds = new ArrayList<>();
                for (Object_Collection.Object_CollectionOrder ordering : orderings) {
                    tagIds.add(ordering.getTagId());
                    ordering.setGroupId(group.getId());
                }

                Response<ArrayList<Object_Tag>> tagsResponse = API_Singleton.getInstanceService().getTags(collection.getId(), tagIds).execute();
                if (Thread.interrupted())
                    return new ArrayList<>();
                if (!tagsResponse.isSuccessful())
                    throw new API_PreferabliException(tagsResponse.errorBody());
                Tools_Preferabli.saveCollectionEtag(tagsResponse, collection.getId());
                ArrayList<Object_Tag> tags = tagsResponse.body();

                Tools_Database.getInstance().openDatabase();
                ArrayList<Long> variantIds = new ArrayList<>();
                for (Object_Tag tag : tags) {
                    tag.setLocation(collection.getName());
                    for (Object_Collection.Object_CollectionOrder ordering : orderings) {
                        if (ordering.getTagId() == tag.getId()) {
                            Tools_Database.getInstance().updateTagTable(ordering, tag);
                            ordering.setTag(tag);
                            break;
                        }
                    }
                    variantIds.add(tag.getVariantId());
                }
                Tools_Database.getInstance().closeDatabase();

                Response<ArrayList<Object_Product>> productsResponse = API_Singleton.getInstanceService().getProducts(variantIds).execute();
                if (Thread.interrupted())
                    return new ArrayList<>();
                if (!productsResponse.isSuccessful())
                    throw new API_PreferabliException(productsResponse.errorBody());
                ArrayList<Object_Product> products = productsResponse.body();
                Tools_Database.getInstance().openDatabase();
                for (Object_Product product : products) {
                    Tools_Database.getInstance().updateProductTable(product);
                    // add cached tags to products here and also the collection tag
                    for (Object_Variant variant : product.getVariants()) {
                        variant.setProduct(product);
                        for (Object_Tag tag : tags) {
                            if (variant.getId() == tag.getVariantId()) {
                                variant.addTag(tag);
                                tag.setVariant(variant);
                            }
                        }
                    }
                }
                Tools_Database.getInstance().closeDatabase();
            }

            return orderings;

        } catch (Exception e) {
            if (failCount > 1) {
                if (Thread.interrupted())
                    return new ArrayList<>();
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
            } else {
                return getGroupItems(collection, version, group, limit, offset, failCount + 1);
            }
        }
    }

    ArrayList<Object_Product> getCachedProducts(Object_Collection collection, boolean manage) {
        ArrayList<Object_Product> cachedProducts = new ArrayList<>();
        ArrayList<Object_Collection.Object_CollectionGroup> groups = collection.getFirstVersion().getGroups();
        for (Object_Collection.Object_CollectionGroup group : groups) {
            ArrayList<Object_Collection.Object_CollectionOrder> sortedOrders = group.getOrderings();
            if (sortedOrders.size() == 0 && manage) {
                Object_Product product = new Object_Product();
                cachedProducts.add(product);
            } else {
                for (Object_Collection.Object_CollectionOrder ordering : sortedOrders) {
                    Object_Tag tag = ordering.getTag();
                    Object_Variant variant = tag.getVariant();
                    Object_Product product = variant.getProduct();
                    variant.setProduct(product);
                    cachedProducts.add(product);
                }
            }
        }
        return cachedProducts;
    }
}
