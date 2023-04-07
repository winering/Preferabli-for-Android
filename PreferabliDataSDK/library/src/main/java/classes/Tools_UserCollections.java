//
//  Tools_UserCollections.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

import retrofit2.Response;

public class Tools_UserCollections {

    private static Tools_UserCollections userCollectionsTools;
    private Semaphore getItemsSempahore;
    private Semaphore userCollectionsToolsSemaphore;
    private Semaphore cellarsSemaphore;
    private boolean keepLoading;
    private boolean error;

    public Tools_UserCollections() {
        userCollectionsToolsSemaphore = new Semaphore(1);
        cellarsSemaphore = new Semaphore(1);
        getItemsSempahore = new Semaphore(5);
    }

    public static Tools_UserCollections getInstance() {
        if (userCollectionsTools == null) userCollectionsTools = new Tools_UserCollections();
        return userCollectionsTools;
    }

    public void clearData() {
        userCollectionsToolsSemaphore.release();
        cellarsSemaphore.release();
        userCollectionsTools = null;
    }

    public void getUserCollectionsInitial() throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        getUserCollections(false, "N/A");
    }

    public void getCellarsInitial() throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        getCellars(false);
    }

    public ArrayList<Object_UserCollection> getUserCollections(boolean forceRefresh, String relationshipType) throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        if (forceRefresh || !Tools_Preferabli.getKeyStore().getBoolean("hasCalledUserCollections", false)) {
            userCollectionsToolsSemaphore.acquire();
            getUserCollectionsFromAPI();
            userCollectionsToolsSemaphore.release();
        } else if (Tools_Preferabli.hasDaysPassed(5, Tools_Preferabli.getKeyStore().getLong("lastGrabbedUserCollections", 0))) {
            Tools_Preferabli.startNewWorkThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        getUserCollectionsFromAPI();
                        EventBus.getDefault().post(Tools_UserCollections.this);
                    } catch (Exception e) {
                        // catch exception so that we can still pull up cached data
                        e.printStackTrace();
                    }
                }
            });
        }

        return getUserCollectionsFromDB(relationshipType);
    }

    public void getCellars(boolean forceRefresh) throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        if (forceRefresh || !Tools_Preferabli.getKeyStore().getBoolean("hasCalledPersonalCellars", false)) {
            cellarsSemaphore.acquire();
            Tools_Preferabli.startNewWorkThread(1, new Runnable() {
                @Override
                public void run() {
                    try {
                        loadPersonalCellarsFromAPI();
                    } catch (Exception e) {
                        // catch exception so that we can still pull up cached data
                        e.printStackTrace();
                    } finally {
                        cellarsSemaphore.release();
                    }
                }
            });
        } else if (Tools_Preferabli.hasDaysPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalledPersonalCellars", 0))) {
            cellarsSemaphore.acquire();
            Tools_Preferabli.startNewWorkThread(1, new Runnable() {
                @Override
                public void run() {
                    try {
                        loadPersonalCellarsFromAPI();
                    } catch (Exception e) {
                        // catch exception so that we can still pull up cached data
                        e.printStackTrace();
                    } finally {
                        cellarsSemaphore.release();
                    }
                }
            });
        }
    }

    public ArrayList<Object_UserCollection> getUserCollectionsFromDB(String relationshipType) {
        Tools_Database.getInstance().openDatabase();
        ArrayList<Object_UserCollection> userCollections = Tools_Database.getInstance().getUserCollections(relationshipType);
        Tools_Database.getInstance().closeDatabase();
        return userCollections;
    }

    public void getUserCollectionsFromAPI() throws API_PreferabliException, IOException, NullPointerException, InterruptedException {
        Tools_Preferabli.getKeyStore().edit().putLong("lastGrabbedUserCollections", System.currentTimeMillis()).apply();
        try {
            keepLoading = true;
            error = false;

            Tools_Database.getInstance().openDatabase();
            Tools_Database.getInstance().clearUserCollectionTable();
            Tools_Database.getInstance().closeDatabase();

            getItems();

            while (getItemsSempahore.availablePermits() != 5) {
                Thread.sleep(100);
            }

            if (error) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError);
            }

            handleCellars();
            Tools_Preferabli.getKeyStore().edit().putBoolean("hasCalledUserCollections", true).apply();
        } catch (API_PreferabliException e) {
            userCollectionsToolsSemaphore.release();
            throw e;
        }
    }

    public void handleCellars() {
        Tools_Database.getInstance().openDatabase();
        int personalCellarCount = Tools_Database.getInstance().getPersonalCellarCount();
        Tools_Database.getInstance().closeDatabase();
        Tools_Preferabli.getKeyStore().edit().putInt("personalCellarCount", personalCellarCount).commit();

        if (Tools_Preferabli.getKeyStore().getBoolean("isSKS", false) && personalCellarCount == 0) {
            EventBus.getDefault().post(1000);
        }
    }

    public void getItems() throws InterruptedException {
        int limit = 50;
        int offsetOverall = -limit;
        while (keepLoading  && !error) {
            getItemsSempahore.acquire();
            offsetOverall = offsetOverall + limit;
            final int offset = offsetOverall;
            Tools_Preferabli.startNewWorkThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response<ArrayList<Object_UserCollection>> userCollectionsResponse = API_Singleton.getInstanceService().getUserCollections(Tools_Preferabli.getPreferabliUserId(), limit, offset).execute();
                        if (!userCollectionsResponse.isSuccessful())
                            throw new API_PreferabliException(userCollectionsResponse.errorBody());
                        ArrayList<Object_UserCollection> userCollections = userCollectionsResponse.body();
                        Tools_Database.getInstance().openDatabase();

                        ArrayList<Long> channelIds = new ArrayList<>();
                        for (Object_UserCollection userCollection : userCollections) {

                            if (userCollection.getCollection().getChannelId() != 0) {
                                channelIds.add(userCollection.getCollection().getChannelId());
                            }

                            if (userCollection.getRelationship_type().equalsIgnoreCase("mycellar")) {
                                Set<String> collectionIds = Tools_Preferabli.getKeyStore().getStringSet("cellarIds", new HashSet<>());
                                collectionIds.add(Long.toString(userCollection.getCollection_id()));
                                Tools_Preferabli.getKeyStore().edit().putStringSet("cellarIds", collectionIds).commit();
                            } else if (userCollection.getRelationship_type().equalsIgnoreCase("rating")) {
                                Tools_Preferabli.getKeyStore().edit().putLong("ratings_id", userCollection.getCollection_id()).commit();
                            } else if (userCollection.getRelationship_type().equalsIgnoreCase("wishlist")) {
                                Tools_Preferabli.getKeyStore().edit().putLong("wishlist_id", userCollection.getCollection_id()).commit();
                            } else if (userCollection.getRelationship_type().equalsIgnoreCase("skip")) {
                                Tools_Preferabli.getKeyStore().edit().putLong("skips_id", userCollection.getCollection_id()).commit();
                            } else if (userCollection.getRelationship_type().equalsIgnoreCase("fasttrack")) {
                                Tools_Preferabli.getKeyStore().edit().putBoolean("hasFastTracks", true).commit();
                            } else if (userCollection.getRelationship_type().equalsIgnoreCase("purchase")) {
                                Tools_Preferabli.getKeyStore().edit().putBoolean("hasPurchaseHistory", true).commit();
                            } else if (userCollection.getRelationship_type().equalsIgnoreCase("jumpstart")) {
                                Tools_Preferabli.getKeyStore().edit().putBoolean("hasJumpstarts", true).commit();
                            }

                            Tools_Database.getInstance().updateUserCollectionTable(userCollection, true);
                        }

                        Tools_Database.getInstance().closeDatabase();

                        if (userCollections.size() != limit) {
                            keepLoading = false;
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

    public void loadPersonalCellarsFromAPI() throws API_PreferabliException, NullPointerException, InterruptedException, IOException {
        Tools_Preferabli.getKeyStore().edit().putLong("lastCalledPersonalCellars", System.currentTimeMillis()).apply();
        ArrayList<Object_UserCollection> userCollections = Tools_UserCollections.getInstance().getUserCollections(false, "mycellar");

        for (Object_UserCollection userCollection : userCollections) {
            Tools_LoadCollection.getInstance().loadCollectionViaOrderings(userCollection.getCollection_id(), 1);
        }
    }
}