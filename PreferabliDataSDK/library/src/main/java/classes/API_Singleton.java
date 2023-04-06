//
//  API_Singleton.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/7/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.util.Log;
import com.google.gson.JsonObject;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Internal class used for interacting with our API.
 */
public class API_Singleton {

    private API_Service service;
    private Cache cache;
    private static API_Singleton sharedInstance;
    private String baseURL = "https://api.preferabli.com/api/6.2/";

    private void createService() {
        service = getRetrofit().create(API_Service.class);
    }

    private Retrofit getRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (Preferabli.isLoggingEnabled()) logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        else logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        cache = new Cache(Tools_PreferabliApp.getAppContext().getCacheDir(), 500 * 1024 * 1024);
        builder.cache(cache);
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + Tools_PreferabliTools.getKeyStore().getString("access_token", "")).addHeader("client_interface", Tools_PreferabliTools.getKeyStore().getString("CLIENT_INTERFACE", null)).addHeader("client_interface_version", Integer.toString(Preferabli.getVersionCode())).addHeader("Accept", "application/json").addHeader("api_token", Tools_PreferabliTools.getKeyStore().getString("api_token", "")).build();
            Response response = chain.proceed(request);
            if (response.code() == 401) {
                if (request.url().pathSegments().contains("sessions")) {
                    // Boot to login.
                    Tools_PreferabliTools.clearAllData();
                    EventBus.getDefault().post(401);
                } else {
                    // Refresh session.
                    try {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("user_id", Tools_PreferabliTools.getPreferabliUserId());
                        jsonObject.addProperty("token_refresh", Tools_PreferabliTools.getKeyStore().getString("refresh_token", ""));
                        retrofit2.Response<Object_SessionData> sessionResponse = getService(true).getSession(jsonObject).execute();
                        if (sessionResponse.isSuccessful()) {
                            Object_SessionData session = sessionResponse.body();
                            Tools_PreferabliTools.saveSession(session);
                            request = request.newBuilder().removeHeader("Authorization").addHeader("Authorization", "Bearer " + session.getAccessToken()).build();
                            response = chain.proceed(request);
                        } else {
                            // Boot to login.
                            Tools_PreferabliTools.clearAllData();
                            EventBus.getDefault().post(401);
                        }
                    } catch (API_PreferabliException e) {
                        // Could not refresh due to issue with session.
                    }
                }
            }
            return response;
        });
        builder.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseURL).client(builder.build()).build();
        return retrofit;
    }

    public static API_Singleton getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new API_Singleton();
        }
        return sharedInstance;
    }

    private void printHeaders(Headers headers) {
        for (Map.Entry<String, List<String>> header : headers.toMultimap().entrySet()) {
            Log.e(API_Singleton.class.getName(), header.getKey() + " " + header.getValue());
        }
    }

    public API_Service getService() throws API_PreferabliException {
        return getService(true);
    }

    public API_Service getService(boolean requiresAccessToken) throws API_PreferabliException {
        if (Tools_PreferabliTools.isNullOrWhitespace(Tools_PreferabliTools.getKeyStore().getString("CLIENT_INTERFACE", null))) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidClientInterface);
        }

        if (requiresAccessToken && Tools_PreferabliTools.isNullOrWhitespace(Tools_PreferabliTools.getKeyStore().getString("access_token", null))) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidAccessToken);
        }

        if (service == null) getSharedInstance().createService();

        return service;
    }

    public static API_Service getInstanceService() throws API_PreferabliException {
        return getSharedInstance().getService();
    }

    public static API_Service getInstanceService(boolean requiresAccessToken) throws API_PreferabliException {
        return getSharedInstance().getService(requiresAccessToken);
    }

    public static void refreshDefaults() {
        sharedInstance = null;
    }

    public void clearCache() {
        if (service == null) {
            createService();
        }
        if (cache != null) {
            try {
                cache.evictAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}