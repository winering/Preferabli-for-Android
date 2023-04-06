//
//  Tools_LoadCollectionTools.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import retrofit2.Response;

public class Tools_LoadCollectionTools {

    private static Tools_LoadCollectionTools loadCollectionTools;

    public Tools_LoadCollectionTools() {
        // nada
    }

    public static Tools_LoadCollectionTools getInstance() {
        if (loadCollectionTools == null) loadCollectionTools = new Tools_LoadCollectionTools();
        return loadCollectionTools;
    }

    public void clearData() {
        loadCollectionTools = null;
    }

    public Object_Collection loadCollection(long collectionId) throws IOException, API_PreferabliException {
        Response<Object_Collection> collectionResponse = API_Singleton.getInstanceService().getCollection(collectionId).execute();
        if (!collectionResponse.isSuccessful()) throw new API_PreferabliException(collectionResponse.errorBody());
        Tools_PreferabliTools.saveCollectionEtag(collectionResponse, collectionId);
        Object_Collection collection = collectionResponse.body();
        Tools_DBHelper.getInstance().openDatabase();
        Tools_DBHelper.getInstance().deleteCollection(collection);
        Tools_DBHelper.getInstance().updateCollectionTable(collection);
        collection = Tools_DBHelper.getInstance().getCollection(collectionId);
        Tools_DBHelper.getInstance().closeDatabase();
        return collection;
    }

    public void loadCollectionViaTags(long collectionId, int priority) throws InterruptedException, API_PreferabliException {
        Tools_DBHelper.getInstance().openDatabase();
        Object_Collection collection = Tools_DBHelper.getInstance().getCollection(collectionId);
        Tools_DBHelper.getInstance().clearTagTable(collectionId, false);
        Tools_DBHelper.getInstance().closeDatabase();

        if (collection == null) {
            // We don't have the collection in question
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.UnknownError);
        }

        int limit = 50;
        Semaphore getItemsSempahore = new Semaphore(10);
        final boolean[] error = { false };
        int offsetOverall = -limit;
        while (offsetOverall <= collection.getWineCount()) {
            getItemsSempahore.acquire();
            offsetOverall = offsetOverall + limit;
            final int offset = offsetOverall;
            Tools_PreferabliTools.startNewWorkThread(priority, new Runnable() {
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
                        error[0] = true;
                    } finally {
                        getItemsSempahore.release();
                    }
                }
            });

            if (getItemsSempahore.availablePermits() != 10) {
                Thread.sleep(100);
            }

            if (error[0]) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
            }
        }
    }

    public void loadCollectionViaOrderings(long collectionId, int priority) throws InterruptedException, API_PreferabliException {
        Tools_DBHelper.getInstance().openDatabase();
        Object_Collection collection = Tools_DBHelper.getInstance().getCollection(collectionId);
        Tools_DBHelper.getInstance().closeDatabase();

        if (collection == null) {
            // We don't have the collection in question
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.UnknownError);
        }

        Object_Collection.Version version = collection.getFirstVersion();
        final int limit = 100;
        Semaphore getItemsSempahore = new Semaphore(10);
        final boolean[] error = { false };

        HashMap<Object_Collection.Version.Group, ArrayList<Object_Collection.Version.Group.Ordering>> orderingMap = new HashMap<>();
        for (Object_Collection.Version.Group group : version.getGroups()) {
            int offset = 0;
            ArrayList<Object_Collection.Version.Group.Ordering> orderingsToInsert = new ArrayList<>();
            Semaphore addSem = new Semaphore(1);
            orderingMap.put(group, orderingsToInsert);
            while (offset <= group.getOrderingsCount()) {
                final int finalOffset = offset;
                Tools_PreferabliTools.startNewWorkThread(priority, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<Object_Collection.Version.Group.Ordering> orderings = getGroupItems(collection, version, group, limit, finalOffset, 0);
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
        Tools_DBHelper.getInstance().openDatabase();
        for (Object_Collection.Version.Group group : version.getGroups()) {
            ArrayList<Object_Collection.Version.Group.Ordering> orderingsToInsert = orderingMap.get(group);
            Tools_DBHelper.getInstance().clearGroupOrderings(group);
            Tools_DBHelper.getInstance().updateOrderingTable(orderingsToInsert);
        }
        Tools_DBHelper.getInstance().closeDatabase();
    }

    public ArrayList<Object_Collection.Version.Group.Ordering> getGroupItems(Object_Collection collection, Object_Collection.Version version, Object_Collection.Version.Group group, int limit, int offset, int failCount) throws API_PreferabliException {
        try {
            if (Thread.interrupted()) return new ArrayList<>();
            Response<ArrayList<Object_Collection.Version.Group.Ordering>> orderingResponse = API_Singleton.getInstanceService().getOrderings(collection.getId(), version.getId(), group.getId(), limit, offset).execute();
            if (Thread.interrupted()) return new ArrayList<>();
            if (!orderingResponse.isSuccessful())
                throw new API_PreferabliException(orderingResponse.errorBody());
            Tools_PreferabliTools.saveCollectionEtag(orderingResponse, collection.getId());
            ArrayList<Object_Collection.Version.Group.Ordering> orderings = orderingResponse.body();

            if (orderings.size() != 0) {
                ArrayList<Long> tagIds = new ArrayList<>();
                for (Object_Collection.Version.Group.Ordering ordering : orderings) {
                    tagIds.add(ordering.getCollectionVariantTagId());
                    ordering.setGroupId(group.getId());
                }

                Response<ArrayList<Object_Tag>> tagsResponse = API_Singleton.getInstanceService().getTags(collection.getId(), tagIds).execute();
                if (Thread.interrupted())
                    return new ArrayList<>();
                if (!tagsResponse.isSuccessful())
                    throw new API_PreferabliException(tagsResponse.errorBody());
                Tools_PreferabliTools.saveCollectionEtag(tagsResponse, collection.getId());
                ArrayList<Object_Tag> tags = tagsResponse.body();

                Tools_DBHelper.getInstance().openDatabase();
                ArrayList<Long> variantIds = new ArrayList<>();
                for (Object_Tag tag : tags) {
                    tag.setLocation(collection.getName());
                    for (Object_Collection.Version.Group.Ordering ordering : orderings) {
                        if (ordering.getCollectionVariantTagId() == tag.getId()) {
                            Tools_DBHelper.getInstance().updateTagTable(ordering, tag);
                            ordering.setTag(tag);
                            break;
                        }
                    }
                    variantIds.add(tag.getVariantId());
                }
                Tools_DBHelper.getInstance().closeDatabase();

                Response<ArrayList<Object_Product>> productsResponse = API_Singleton.getInstanceService().getProducts(variantIds).execute();
                if (Thread.interrupted())
                    return new ArrayList<>();
                if (!productsResponse.isSuccessful())
                    throw new API_PreferabliException(productsResponse.errorBody());
                ArrayList<Object_Product> products = productsResponse.body();
                Tools_DBHelper.getInstance().openDatabase();
                for (Object_Product product : products) {
                    Tools_DBHelper.getInstance().updateWineTable(product);
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
                Tools_DBHelper.getInstance().closeDatabase();
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

    public ArrayList<Object_Product> getCachedProducts(Object_Collection collection, boolean manage) {
        ArrayList<Object_Product> cachedProducts = new ArrayList<>();
        ArrayList<Object_Collection.Version.Group> groups = collection.getFirstVersion().getGroups();
        int totalPosition = 1;
        for (Object_Collection.Version.Group group : groups) {
            int positionInGroup = 1;
            ArrayList<Object_Collection.Version.Group.Ordering> sortedOrders = group.getOrderings();
            if (sortedOrders.size() == 0 && manage) {
                Object_Product product = new Object_Product();
                product.setWineGroup(new Object_Product.WineGroup(group, new Object_Collection.Version.Group.Ordering()));
                product.setPositionInGroup(positionInGroup);
                product.setTotalPosition(totalPosition);
                cachedProducts.add(product);
            } else {
                for (Object_Collection.Version.Group.Ordering ordering : sortedOrders) {
                    Object_Tag tag = ordering.getTag();
                    Object_Variant variant = tag.getVariant();
                    Object_Product product = new Object_Product(variant.getWine());
                    product.setWineGroup(new Object_Product.WineGroup(group, ordering));
                    product.setPositionInGroup(positionInGroup);
                    product.setTotalPosition(totalPosition);
                    variant.setProduct(product);
                    cachedProducts.add(product);
                    positionInGroup++;
                    totalPosition++;
                }
            }
        }
        return cachedProducts;
    }
}
