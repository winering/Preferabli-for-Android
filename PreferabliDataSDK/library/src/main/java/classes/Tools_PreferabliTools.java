//
//  Tools_PreferabliTools.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.CursorWindow;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.text.Spanned;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class Tools_PreferabliTools {

    private static SharedPreferences sharedPreferences;
    private static Gson gson;
    private static Semaphore tenMaxSem;

    static void clearValues() {
        tenMaxSem = null;
        gson = null;
        sharedPreferences = null;
    }

    static Semaphore getTenMaxSem() {
        if (tenMaxSem == null) {
            tenMaxSem = new Semaphore(10);
        }
        return tenMaxSem;
    }

    static Thread startNewWorkThread(Runnable runnable) {
        return startNewWorkThread(Preferabli.PRIORITY_REGULAR, runnable);
    }

    static Thread startNewWorkThread(int priority, Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        thread.setPriority(priority);
        thread.start();
        return thread;
    }

    static Thread startNewAPIWorkThread(Runnable runnable) {
        return startNewAPIWorkThread(Preferabli.PRIORITY_LOW, runnable);
    }

    static Thread startNewAPIWorkThread(int priority, Runnable runnable) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    getTenMaxSem().acquire();
                    runnable.run();
                    getTenMaxSem().release();
                } catch (InterruptedException e) {
                    // Do not run the thread if interrupted.
                }
            }
        };
        thread.setPriority(priority);
        thread.start();
        return thread;
    }

    static Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    static <T> T convertJsonToObject(String json, Type typeOfT) {
        try {
            return getGson().fromJson(json, typeOfT);
        } catch (JsonParseException e) {
            // CATCH DON'T CRASH!
            return null;
        }
    }

    static <T> T convertJsonToObject(JsonElement json, Type typeOfT) {
        try {
            return getGson().fromJson(json, typeOfT);
        } catch (JsonParseException e) {
            // CATCH DON'T CRASH!
            return null;
        }
    }

    static String convertDateToAPITimeStamp(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
    }

    static boolean hasBeenLoaded(retrofit2.Response response, long collectionId) {
        if (Tools_PreferabliTools.getKeyStore().getBoolean("hasLoaded" + collectionId, false)) {
            HashSet<String> etags = (HashSet<String>) Tools_PreferabliTools.getKeyStore().getStringSet("collection_etags_" + collectionId, new HashSet<>());
            String etag = response.headers().get("collection_etag");
            return etags.contains(etag);
        }

        return false;
    }

    static void saveCollectionEtag(retrofit2.Response response, long collectionId) {
        if (!Tools_PreferabliTools.isNullOrWhitespace(response.headers().get("collection_etag"))) {
            HashSet<String> etags = (HashSet<String>) Tools_PreferabliTools.getKeyStore().getStringSet("collection_etags_" + collectionId, new HashSet<>());
            etags.add(response.headers().get("collection_etag"));
            Tools_PreferabliTools.getKeyStore().edit().putStringSet("collection_etags_" + collectionId, etags).commit();
        }
    }

    static int calculateDistanceInMiles(double lat1, double lon1, double lat2, double lon2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        float distanceInMeters = loc1.distanceTo(loc2);
        double distanceInMiles = distanceInMeters / 1609.344;
        return (int) Math.round(distanceInMiles);
    }

    static String getTime(Date date, String timezone) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
            if (timezone == null) {
                timezone = TimeZone.getDefault().getID();
            }
            format.setTimeZone(TimeZone.getTimeZone(timezone));
            return format.format(date);
        } catch (Exception e) {
            Log.e(Tools_PreferabliTools.class.getName(), "Error formatting date.", e);
            return null;
        }
    }

    static Date convertAPITimeStampToDate(String timestamp) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(timestamp);
        } catch (Exception e) {
            return new Date();
        }
    }

    static SharedPreferences getKeyStore() {
        if (sharedPreferences == null)
            sharedPreferences = Tools_PreferabliApp.getAppContext().getSharedPreferences("PreferabliPrefs", Context.MODE_PRIVATE);

        return sharedPreferences;
    }

    static long getPreferabliUserId() {
        return getKeyStore().getLong("user_id", 0);
    }

    static long getCustomerId() {
        return getKeyStore().getLong("customer_id", 0);
    }

    static boolean isNullOrWhitespace(Spanned string) {
        if (string == null) return true;
        return string.toString().replaceAll(" ", "").length() == 0;
    }

    static boolean isNullOrWhitespace(String string) {
        if (string == null) return true;
        return string.replaceAll(" ", "").length() == 0;
    }

    static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    static String getSymbolForCurrencyCode(String code) {
        if (Tools_PreferabliTools.isNullOrWhitespace(code))
            return "$";
        return Currency.getInstance(code).getSymbol();
    }

    static int alphaSortIgnoreThe(String x, String y) {
        if (Tools_PreferabliTools.isNullOrWhitespace(x) && Tools_PreferabliTools.isNullOrWhitespace(y)) {
            return 0;
        } else if (Tools_PreferabliTools.isNullOrWhitespace(x)) {
            return 1;
        } else if (Tools_PreferabliTools.isNullOrWhitespace(y)) {
            return -1;
        }
        if (x.startsWith("The ")) {
            x = x.substring(4);
        }
        if (y.startsWith("The ")) {
            y = y.substring(4);
        }

        return x.compareToIgnoreCase(y);
    }

    static void saveSession(Object_SessionData session) {
        SharedPreferences.Editor editor = Tools_PreferabliTools.getKeyStore().edit();
        editor.putString("access_token", session.getAccessToken());
        editor.putString("refresh_token", session.getRefreshToken());
        editor.putString("intercom_hmac", session.getIntercomHmac());
        editor.apply();
    }

    static void saveUser(Object_PreferabliUser user) {
        SharedPreferences.Editor editor = Tools_PreferabliTools.getKeyStore().edit();
        editor.putLong("user_id", user.getId());
        editor.putString("display_name", user.getDisplayName());
        editor.putString("avatar", user.getAvatar());
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName", user.getLastName());
        editor.putString("country", user.getCountry());
        editor.putInt("birthYear", user.getBirthYear());
        editor.putString("gender", user.getGender());
        editor.putString("email", user.getEmail());
        editor.putInt("accountLevel", user.getAccountLevel());
        editor.putString("zipCode", user.getZipCode());
        editor.putBoolean("subscribed", user.isSubscribed());
        editor.putBoolean("isTeamRingIT", user.isTeamRingIT());
        editor.putString("intercom_hmac", user.getIntercom_hmac());
        editor.putLong("ratings_id", user.getRating_collection_id());
        editor.putLong("wishlist_id", user.getWishlist_collection_id());
        editor.apply();
    }

    static void saveCustomer(Object_Customer customer) {
        SharedPreferences.Editor editor = Tools_PreferabliTools.getKeyStore().edit();
        editor.putLong("customer_id", customer.getId());
        editor.putString("email", customer.getEmail());
        editor.putLong("ratings_id", customer.getRatingsCollectionId());
        editor.apply();
    }

    static boolean hasDaysPassed(int days, long last) {
        long lastPlusDay = last + TimeUnit.DAYS.toMillis(days);
        long current = System.currentTimeMillis();
        return lastPlusDay < current;
    }

    static boolean hasMinutesPassed(int minutes, long last) {
        long lastPlusDay = last + TimeUnit.MINUTES.toMillis(minutes);
        long current = System.currentTimeMillis();
        return lastPlusDay < current;
    }

    public String getCurrencySymbol(String code) {
        if (Tools_PreferabliTools.isNullOrWhitespace(code)) {
            return "";
        }

        Currency currency = Currency.getInstance(code);
        return currency.getSymbol();
    }

    static void clearAllData() {
        Tools_PreferabliTools.clearValues();

        int integration_id = Tools_PreferabliTools.getKeyStore().getInt("INTEGRATION_ID", 0);
        String client_interface = Tools_PreferabliTools.getKeyStore().getString("CLIENT_INTERFACE", "");

        API_Singleton.getSharedInstance().clearCache();
        API_Singleton.refreshDefaults();
        Tools_DBHelper.getInstance().openDatabase();
        Tools_DBHelper.getInstance().deleteDatabase();
        Tools_DBHelper.getInstance().closeDatabase();
        Tools_PreferabliTools.getKeyStore().edit().clear().apply();
        Tools_UserCollectionsTools.getInstance().clearData();
        Tools_JournalTools.getInstance().clearData();
        Tools_LoadCollectionTools.getInstance().clearData();
        Tools_PreferencesTools.getInstance().clearData();

        Tools_PreferabliTools.getKeyStore().edit().putInt("INTEGRATION_ID", integration_id).apply();
        Tools_PreferabliTools.getKeyStore().edit().putString("CLIENT_INTERFACE", client_interface).apply();
    }

    static int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return Tools_PreferabliApp.getAppContext().getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    static void calibrateTextSize(String text, Paint paint, float min, float max, float boxWidth) {
        paint.setTextSize(10);
        paint.setTextSize(Math.max(Math.min((boxWidth / paint.measureText(text)) * 10, max), min));
    }

    static boolean isPreferabliUserLoggedIn() {
        return !Tools_PreferabliTools.isNullOrWhitespace(Tools_PreferabliTools.getKeyStore().getString("access_token", "")) && Tools_PreferabliTools.getPreferabliUserId() != 0 && Tools_PreferabliTools.getPreferabliUserId() != 0L;
    }

    static boolean isCustomerLoggedIn() {
        return !Tools_PreferabliTools.isNullOrWhitespace(Tools_PreferabliTools.getKeyStore().getString("access_token", "")) && Tools_PreferabliTools.getCustomerId() != 0 && Tools_PreferabliTools.getCustomerId() != 0L;
    }

    static void closeStream(Closeable stream) {
        try {
            stream.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    static String formatFloat(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    static boolean isImageUploaded(Object_Media image) {
        return image != null && !Tools_PreferabliTools.isNullOrWhitespace(image.getPath()) && image.getPath().startsWith("http");
    }

    static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(Tools_PreferabliApp.getAppContext().getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(Tools_PreferabliApp.getAppContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !Tools_PreferabliTools.isNullOrWhitespace(locationProviders);
        }
    }

    static void handleUpgrade() {
        int versionCode = Preferabli.getVersionCode();
        int savedVersionCode = getKeyStore().getInt("versionCode", 0);

        if (savedVersionCode != versionCode) {
            if (savedVersionCode == 0) {
                // new user do nothing for now
            } else {
                // user has upgraded the app always pull new data
                Tools_DBHelper.getInstance().makeSureWeUpgradeDB();
            }
            // we handled either possible situation so update the version code to current version
            getKeyStore().edit().putInt("versionCode", versionCode).apply();
        }
    }

    static void handleCursorSize() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            try {
                // fixed bug where cursor is not big enough for getting certain collections.
                Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
                field.setAccessible(true);
                field.set(null, 100 * 1024 * 1024); // 100MB is the new size
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void addSDKProperties() {
        long id = isPreferabliUserLoggedIn() ? getPreferabliUserId() : getCustomerId();
        String email = getKeyStore().getString("email", null);
        String phone = getKeyStore().getString("phone", null);
        String display_name = getKeyStore().getString("displayName", null);
        boolean isTeamRingIt = getKeyStore().getBoolean("isTeamRingIt", false);

        if (id != 0 && id != 0L) {
            try {
                Tools_PreferabliApp.getMixpanel().identify(Long.toString(id));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(isPreferabliUserLoggedIn() ? "user_id" : "customer_id", id);
                jsonObject.put("is_team_ringit", isTeamRingIt);
                if (!isNullOrWhitespace(email)) {
                    jsonObject.put("$email", email);
                }
                if (!isNullOrWhitespace(phone)) {
                    jsonObject.put("phone", phone);
                }
                if (!isNullOrWhitespace(display_name)) {
                    jsonObject.put("display_name", display_name);
                }
                Tools_PreferabliApp.getMixpanel().getPeople().set(jsonObject);
            } catch (JSONException e) {
                // fail
            }
        }
    }

    static void createAnalyticsPost(String event) {
        try {
            JSONObject object = new JSONObject();
            object.put("analyticsType", "PreferabliDataSDKAnalytics");
            object.put("event", event);
            EventBus.getDefault().post(object);
        } catch (JSONException e) {
            Log.e(Tools_PreferabliTools.class.getSimpleName(), e.toString());
        }
    }
}
