//
//  Tools_PreferabliUser.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Response;

/**
 * Contains methods that help load {@link Object_PreferabliUser} data.
 */
class Tools_PreferabliUser {

    private static Tools_PreferabliUser userCollectionsTools;

    static Tools_PreferabliUser getInstance() {
        if (userCollectionsTools == null) userCollectionsTools = new Tools_PreferabliUser();
        return userCollectionsTools;
    }

    void clearData() {
        userCollectionsTools = null;
    }

    ArrayList<Object_UserCollection> getUserCollections(int priority, boolean force_refresh, String relationship_type) throws API_PreferabliException, InterruptedException, NullPointerException {
        if (force_refresh || !Tools_Preferabli.getKeyStore().getBoolean("hasLoadedUserCollections", false)) {
            getUserCollectionsFromAPI(priority);
        } else if (Tools_Preferabli.hasMinutesPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalledUserCollections", 0))) {
            Tools_Preferabli.startNewWorkThread(() -> {
                try {
                    getUserCollectionsFromAPI(Preferabli.PRIORITY_LOW);
                    EventBus.getDefault().post(Tools_PreferabliUser.this);
                } catch (Exception e) {
                    // catching any issues here so that we can still pull up our saved data
                    if (Preferabli.isLoggingEnabled()) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                }
            });
        }

        Tools_Database.getInstance().openDatabase();
        ArrayList<Object_UserCollection> userCollections = Tools_Database.getInstance().getUserCollections(relationship_type);
        Tools_Database.getInstance().closeDatabase();

        return userCollections;
    }

    private void getUserCollectionsFromAPI(int priority) throws API_PreferabliException, NullPointerException, InterruptedException {
        Tools_Preferabli.getKeyStore().edit().putLong("lastGrabbedUserCollections", System.currentTimeMillis()).apply();
        try {
            Tools_Database.getInstance().openDatabase();
            Tools_Database.getInstance().clearUserCollectionTable();
            Tools_Database.getInstance().closeDatabase();

            if (getUserCollectionsLoop(priority)) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
            }

            Tools_Preferabli.getKeyStore().edit().putLong("lastCalledUserCollections", System.currentTimeMillis()).apply();
            Tools_Preferabli.getKeyStore().edit().putBoolean("hasLoadedUserCollections", true).apply();

        } catch (API_PreferabliException e) {
            throw e;
        }
    }

    private boolean getUserCollectionsLoop(int priority) throws InterruptedException {
        int limit = 50;
        int offsetOverall = -limit;
        AtomicBoolean keep_loading = new AtomicBoolean(true);
        AtomicBoolean error = new AtomicBoolean(false);
        Semaphore semaphore = new Semaphore(5);

        while (keep_loading.get() && !error.get()) {
            semaphore.acquire();
            offsetOverall = offsetOverall + limit;
            final int offset = offsetOverall;
            Tools_Preferabli.startNewAPIWorkThread(priority, () -> {
                try {
                    Response<ArrayList<Object_UserCollection>> userCollectionsResponse = API_Singleton.getInstanceService().getUserCollections(Tools_Preferabli.getPreferabliUserId(), limit, offset).execute();
                    if (!userCollectionsResponse.isSuccessful())
                        throw new API_PreferabliException(userCollectionsResponse.errorBody());
                    ArrayList<Object_UserCollection> userCollections = userCollectionsResponse.body();
                    Tools_Database.getInstance().openDatabase();

                    for (Object_UserCollection userCollection : userCollections) {
                        Tools_Database.getInstance().updateUserCollectionTable(userCollection);
                    }

                    Tools_Database.getInstance().closeDatabase();

                    if (userCollections.size() != limit) {
                        keep_loading.set(false);
                    }

                } catch (API_PreferabliException | IOException e) {
                    error.set(false);
                } finally {
                    semaphore.release();
                }
            });
        }

        while (semaphore.availablePermits() != 5) {
            Thread.sleep(100);
        }

        return error.get();
    }

    ArrayList<Object_Product> getPurchaseHistory(int priority, boolean force_refresh, boolean lock_to_integration) throws Exception {
        if (force_refresh || !Tools_Preferabli.getKeyStore().getBoolean("hasLoadedPurchaseHistory", false)) {
            loadPurchaseHistoryFromAPI(priority, force_refresh);
        } else if (Tools_Preferabli.hasMinutesPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalledPurchaseHistory", 0))) {
            Tools_Preferabli.startNewWorkThread(() -> {
                try {
                    loadPurchaseHistoryFromAPI(Preferabli.PRIORITY_LOW, false);
                } catch (Exception e) {
                    // catching any issues here so that we can still pull up our saved data
                    if (Preferabli.isLoggingEnabled()) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                }
            });
        }

        Tools_Database.getInstance().openDatabase();
        ArrayList<Object_Product> purchased_products = Tools_Database.getInstance().getPurchasedProducts(lock_to_integration);
        Tools_Database.getInstance().closeDatabase();

        return purchased_products;
    }

    private void loadPurchaseHistoryFromAPI(int priority, boolean force_refresh) throws Exception {
        ArrayList<Object_UserCollection> userCollections = Tools_PreferabliUser.getInstance().getUserCollections(priority, force_refresh, "purchase");

        for (Object_UserCollection userCollection : userCollections) {
            Tools_LoadCollection.getInstance().loadCollectionViaTags(priority, force_refresh, userCollection.getCollectionId());
        }

        Tools_Preferabli.getKeyStore().edit().putLong("lastCalledPurchaseHistory", System.currentTimeMillis()).apply();
        Tools_Preferabli.getKeyStore().edit().putBoolean("hasLoadedPurchaseHistory", true).apply();
    }
}