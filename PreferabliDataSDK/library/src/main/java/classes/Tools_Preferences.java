//
//  Tools_Preferences.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import retrofit2.Response;

public class Tools_Preferences {

    private static Tools_Preferences preferencesTools;
    private Semaphore preferencesToolsSemaphore;

    public Tools_Preferences() {
        preferencesToolsSemaphore = new Semaphore(1);
    }

    public static Tools_Preferences getInstance() {
        if (preferencesTools == null) preferencesTools = new Tools_Preferences();
        return preferencesTools;
    }

    public void clearData() {
        preferencesToolsSemaphore.release();
        preferencesTools = null;
    }

    public void getPreferencesInitial() throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        getStyles(Other_ProductType.OTHER, false, false);
    }

    public ArrayList<Object_ProfileStyle> getStyles(Object wineOrRefineType, boolean forceRefresh, boolean refreshInBackground) throws API_PreferabliException, IOException, InterruptedException, NullPointerException {
        if (forceRefresh || !Tools_Preferabli.getKeyStore().getBoolean("hasCalledStyles", false)) {
            preferencesToolsSemaphore.acquire();
            getPreferencesFromAPI(true);
            preferencesToolsSemaphore.release();
        } else if (refreshInBackground && Tools_Preferabli.hasDaysPassed(5, Tools_Preferabli.getKeyStore().getLong("lastGrabbedStyles", 0))) {
            Tools_Preferabli.getKeyStore().edit().putLong("lastGrabbedStyles", System.currentTimeMillis()).apply();
            Tools_Preferabli.startNewWorkThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        getPreferencesFromAPI(false);
                        getStylesFromDB();
                        EventBus.getDefault().post(Tools_Preferences.this);
                    } catch (Exception e) {
                        // catch exception so that we can still pull up cached data
                        e.printStackTrace();
                    }
                }
            });
        }

        return getStylesFromDB(wineOrRefineType);
    }

    public ArrayList<Object_ProfileStyle> getStylesFromDB() {
        Tools_Database.getInstance().openDatabase();
        ArrayList<Object_ProfileStyle> allObjectProfileStyles = Tools_Database.getInstance().getStyles();
        Tools_Database.getInstance().closeDatabase();
        return allObjectProfileStyles;
    }

    public ArrayList<Object_ProfileStyle> getStylesFromDB(Object wineOrRefineType) {
        ArrayList<Object_ProfileStyle> allObjectProfileStyles = getStylesFromDB();

        ArrayList<Object_ProfileStyle> conflicts = new ArrayList<>();
        ArrayList<Object_ProfileStyle> needsExperience = new ArrayList<>();
        ArrayList<Object_ProfileStyle> redStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> whiteStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> roseStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> fortifiedStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> sparklingStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> whiskeyStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> tequilaStyles = new ArrayList<>();
        ArrayList<Object_ProfileStyle> beerStyles = new ArrayList<>();

        JSONArray appealingStyleIds = new JSONArray();
        JSONArray unappealingStyleIds = new JSONArray();
        JSONArray conflictingStyleIds = new JSONArray();

        Object_ProfileStyle.sortPreferenceStyles(allObjectProfileStyles);


        for (Object_ProfileStyle objectProfileStyle : allObjectProfileStyles) {
            if (objectProfileStyle.getStyle() == null || Tools_Preferabli.isNullOrWhitespace(objectProfileStyle.getName()) || (objectProfileStyle.getOrderProfile() == 0 && objectProfileStyle.getOrderRecommend() == 0 && objectProfileStyle.getRating() == 0 && objectProfileStyle.getStrength() == 0)) {
                // fix Andrew bug. Don't allow preferences styles without styles to be returned
                continue;
            }

            if (objectProfileStyle.isRefine()) needsExperience.add(objectProfileStyle);
            if (objectProfileStyle.isConflict()) {
                conflicts.add(objectProfileStyle);
                conflictingStyleIds.put(objectProfileStyle.getId());
            }

            if (wineOrRefineType instanceof Other_ProductType) {
                Other_ProductType productType = (Other_ProductType) wineOrRefineType;
                switch (productType) {
                    case RED:
                        return redStyles;
                    case WHITE:
                        return whiteStyles;
                    case ROSE:
                        return roseStyles;
                    case SPARKLING:
                        return sparklingStyles;
                    case FORTIFIED:
                        return fortifiedStyles;
                }
            } else if (wineOrRefineType instanceof Other_ProductCategory) {
                Other_ProductCategory productCategory = (Other_ProductCategory) wineOrRefineType;
                switch (productCategory) {
                    case BEER:
                        return beerStyles;
                    case MEZCAL:
                        return tequilaStyles;
                    case WHISKEY:
                        return whiskeyStyles;
                }
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public void getPreferencesFromAPI(boolean forceRefresh) throws API_PreferabliException, IOException, NullPointerException {
        Tools_Preferabli.getKeyStore().edit().putLong("lastGrabbedStyles", System.currentTimeMillis()).apply();
        try {
            Response<Object_Profile> profilesResponse = API_Singleton.getInstanceService().getProfile(Tools_Preferabli.getPreferabliUserId()).execute();
            if (profilesResponse.isSuccessful()) {
                saveStylesToDB(profilesResponse.body().getPreferenceStyles(), forceRefresh);
                Tools_Preferabli.getKeyStore().edit().putBoolean("hasCalledStyles", true).apply();
            } else throw new API_PreferabliException(profilesResponse.errorBody());
        } catch (API_PreferabliException | IOException e) {
            preferencesToolsSemaphore.release();
            throw e;
        }
    }

    public void saveStylesToDB(ArrayList<Object_ProfileStyle> objectProfileStyles, boolean forceRefresh) throws API_PreferabliException, IOException, NullPointerException {
        Tools_Database.getInstance().openDatabase();

        ArrayList<Long> style_ids = new ArrayList<>();
        HashMap<Long, Object_ProfileStyle> styleMap = new HashMap<>();
        for (Object_ProfileStyle objectProfileStyle : objectProfileStyles) {
            if (forceRefresh) {
                style_ids.add(objectProfileStyle.getStyle_id());
                styleMap.put(objectProfileStyle.getStyle_id(), objectProfileStyle);
            } else {
                Object_ProfileStyle objectProfileStyleFromDB = Tools_Database.getInstance().getStyleById(objectProfileStyle.getId());
                if (objectProfileStyleFromDB == null || objectProfileStyleFromDB.getStyle() == null) {
                    style_ids.add(objectProfileStyle.getStyle_id());
                    styleMap.put(objectProfileStyle.getStyle_id(), objectProfileStyle);
                }
            }
        }

        Tools_Database.getInstance().closeDatabase();

        if (style_ids.size() > 0) {
            Response<ArrayList<Object_Style>> stylesResponse = API_Singleton.getInstanceService().getStyles(style_ids).execute();
            for (Object_Style style : stylesResponse.body()) {
                styleMap.get(style.getId()).setStyle(style);
            }
        }

        Tools_Database.getInstance().openDatabase();
        if (forceRefresh) {
            Tools_Database.getInstance().clearStyleTable();
        }
        for (Object_ProfileStyle objectProfileStyle : objectProfileStyles) {
            Tools_Database.getInstance().updateStyleTable(objectProfileStyle);
        }
        Tools_Database.getInstance().closeDatabase();
    }


    public boolean isLoading() {
        return preferencesToolsSemaphore.availablePermits() == 0;
    }
}
