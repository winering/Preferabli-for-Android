//
//  Preferabli.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * This is the primary class you will utilize to access the Preferabli Data SDK.
 */
public class Preferabli {

    /// Used internally for prioritizing queues.
    static int PRIORITY_HIGH = 10;
    static int PRIORITY_REGULAR = 5;
    static int PRIORITY_LOW = 1;

    /**
     * Use this instance to make Preferabli API calls.
     */
    private static Preferabli main;
    private static boolean loggingEnabled = false;
    private static int versionCode = 1;
    private static boolean hasBeenInitialized = false;
    private static boolean startupThreadRunning = false;
    private static API_Service api;

    /**
     * The primary inventory id of your integration.
     */
    public static long PRIMARY_INVENTORY_ID = Tools_PreferabliTools.getKeyStore().getLong("PRIMARY_INVENTORY_ID", 0);
    /**
     * The channel id of your integration.
     */
    public static long CHANNEL_ID = Tools_PreferabliTools.getKeyStore().getLong("CHANNEL_ID", 0);
    /**
     * The id of your integration.
     */
    public static long INTEGRATION_ID = Tools_PreferabliTools.getKeyStore().getLong("INTEGRATION_ID", 0);

    /**
     * Get and use this instance to make Preferabli API calls.
     *
     * @return an instance of {@link Preferabli}
     */
    public static Preferabli main() {
        if (main == null) main = new Preferabli();
        return main;
    }

    /**
     * Call this in your app's onCreate with your supplied information. Contact us if you do not have your <STRONG>client_interface</STRONG> and/or <STRONG>integration_id</STRONG>.
     *
     * @param client_interface your unique identifier - provided by Preferabli.
     * @param integration_id   your integration id - provided by Preferabli. You may have more than one integration for different segments of your business (depending on how your account is set up).
     * @param logging_enabled  pass true for full logging. Defaults to <STRONG>false</STRONG>.
     */
    public void initialize(String client_interface, long integration_id, boolean logging_enabled) {
        try {
            loggingEnabled = logging_enabled;
            hasBeenInitialized = true;

            Tools_PreferabliTools.getKeyStore().edit().putLong("INTEGRATION_ID", integration_id).commit();
            Tools_PreferabliTools.getKeyStore().edit().putString("CLIENT_INTERFACE", client_interface).commit();
            api = API_Singleton.getInstanceService(false);

            Tools_PreferabliApp.setupMixpanel(client_interface, integration_id);

            Tools_PreferabliTools.startNewWorkThread(PRIORITY_REGULAR, () -> {
                handleStartupActions();
            });


        } catch (API_PreferabliException e) {
            // This should never happen.
        }
    }

    private static void handleStartupActions() {
        try {
            startupThreadRunning = true;
            createAnonymousSession();
            getIntegration();
            startupThreadRunning = false;

        } catch (API_PreferabliException e) {
            // Don't worry about it here but it will be checked and run before any calls to SDK can be made.
            startupThreadRunning = false;
        }
    }

    private static void createAnonymousSession() throws API_PreferabliException {
        if (Tools_PreferabliTools.isNullOrWhitespace(Tools_PreferabliTools.getKeyStore().getString("access_token", null))) {
            try {
                JsonObject sessionObject = new JsonObject();
                sessionObject.addProperty("login_as_anonymous", true);
                Response<Object_SessionData> sessionResponse = api.getSession(sessionObject).execute();
                if (!sessionResponse.isSuccessful())
                    throw new API_PreferabliException(sessionResponse.errorBody());
                Object_SessionData session = sessionResponse.body();
                Tools_PreferabliTools.saveSession(session);
            } catch (IOException e) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.APIError, e.getMessage(), 0);
            }
        }
    }

    private static void getIntegration() throws API_PreferabliException {
        long integration_id = INTEGRATION_ID;
        try {
            Response<JsonObject> integrationResponse = api.getIntegration(integration_id).execute();
            if (!integrationResponse.isSuccessful())
                throw new API_PreferabliException(integrationResponse.errorBody());
            CHANNEL_ID = integrationResponse.body().get("CHANNEL_ID").getAsLong();
            PRIMARY_INVENTORY_ID = integrationResponse.body().get("PRIMARY_INVENTORY_ID").getAsLong();
            Tools_PreferabliTools.getKeyStore().edit().putLong("CHANNEL_ID", CHANNEL_ID).apply();
            Tools_PreferabliTools.getKeyStore().edit().putLong("PRIMARY_INVENTORY_ID", PRIMARY_INVENTORY_ID).apply();
        } catch (IOException e) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidIntegrationId, e.getMessage(), 0);
        }
    }

    /**
     * Login an existing Preferabli user.
     *
     * @param email    user's email address.
     * @param password user's password.
     * @param handler  returns {@link Object_PreferabliUser} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void loginPreferabliUser(String email, String password, API_ResultHandler<Object_PreferabliUser> handler) {
        Tools_PreferabliTools.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_PreferabliTools.createAnalyticsPost("login_user");

                Tools_PreferabliTools.clearAllData();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email);
                jsonObject.addProperty("password", password);

                Response<Object_SessionData> sessionResponse = api.getSession(jsonObject).execute();
                if (!sessionResponse.isSuccessful())
                    throw new API_PreferabliException(sessionResponse.errorBody());
                Object_SessionData session = sessionResponse.body();
                if (session == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }
                Tools_PreferabliTools.saveSession(session);
                Response<Object_PreferabliUser> userResponse = api.getUser(session.getUserId()).execute();
                if (!userResponse.isSuccessful())
                    throw new API_PreferabliException(userResponse.errorBody());
                Object_PreferabliUser user = userResponse.body();
                if (user == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }
                Tools_PreferabliTools.saveUser(user);
                handler.onSuccess(user);

            } catch (API_PreferabliException | IOException e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Link an existing customer or create a new one if they are not in our system.
     * @param merchant_customer_identification unique identifier for your customer. Usually an email address or a phone number.
     * @param merchant_customer_verification authentication key given to you by your API.
     * @param handler returns {@link Object_Customer} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void loginCustomer(String merchant_customer_identification, String merchant_customer_verification, API_ResultHandler<Object_Customer> handler) {
        Tools_PreferabliTools.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_PreferabliTools.createAnalyticsPost("login_customer");

                Tools_PreferabliTools.clearAllData();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("merchant_customer_identification", merchant_customer_identification);
                jsonObject.addProperty("merchant_customer_verification", merchant_customer_verification);
                jsonObject.addProperty("merchant_channel_id", CHANNEL_ID);

                Response<Object_SessionData> sessionResponse = api.getSession(jsonObject).execute();
                if (!sessionResponse.isSuccessful())
                    throw new API_PreferabliException(sessionResponse.errorBody());
                Object_SessionData session = sessionResponse.body();
                if (session == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }
                Tools_PreferabliTools.saveSession(session);
                Response<Object_Customer> customerResponse = api.getCustomer(CHANNEL_ID, session.getCustomerId()).execute();
                if (!customerResponse.isSuccessful())
                    throw new API_PreferabliException(customerResponse.errorBody());
                Object_Customer customer = customerResponse.body();
                if (customer == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }
                Tools_PreferabliTools.saveCustomer(customer);
                handler.onSuccess(customer);

            } catch (API_PreferabliException | IOException e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Logout a customer / Preferabli user.
     * @param handler returns true if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void logout(API_ResultHandler<Boolean> handler) {
        Tools_PreferabliTools.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(true);
                Tools_PreferabliTools.createAnalyticsPost("logout");

                Tools_PreferabliTools.clearAllData();
                handler.onSuccess(true);

            } catch (API_PreferabliException e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Resets the password of an existing Preferabli user.
     * @param email user's email address.
     * @param handler returns true if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void forgotPassword(String email, API_ResultHandler<Boolean> handler) {
        Tools_PreferabliTools.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_PreferabliTools.createAnalyticsPost("forgot_password");

                Response<ResponseBody> responseResponse = api.resetPassword(email).execute();
                if (!responseResponse.isSuccessful())
                    throw new API_PreferabliException(responseResponse.errorBody());
                handler.onSuccess(true);

            } catch (API_PreferabliException | IOException e) {
                handleError(e, handler);
            }
        });
    }

    private void handleError(Exception e, API_ResultHandler handler) {
        API_PreferabliException preferabliException;
        if (e instanceof API_PreferabliException) {
            preferabliException = (API_PreferabliException) e;
        } else {
            preferabliException = new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError, e.getMessage(), 0);
        }

        handler.onFailure(preferabliException);
    }

    /**
     * Is logging enabled for the SDK?
     *
     * @return true if logging is enabled.
     */
    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * Get the version code of the SDK.
     *
     * @return the version code as an int.
     */
    public static int getVersionCode() {
        return versionCode;
    }

    private void canWeContinue(boolean needsToBeLoggedIn) throws API_PreferabliException {
        if (!hasBeenInitialized) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidClientInterface);
        } else if (!Tools_PreferabliTools.getKeyStore().contains("access_token") && !startupThreadRunning) {
            handleStartupActions();
            canWeContinue(needsToBeLoggedIn);
        } else if (!Tools_PreferabliTools.getKeyStore().contains("access_token")) {
            try {
                Thread.sleep(1000);
                canWeContinue(needsToBeLoggedIn);
            } catch (InterruptedException e) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidAccessToken);
            }
        } else if (!Tools_PreferabliTools.getKeyStore().contains("CHANNEL_ID") && !startupThreadRunning) {
            handleStartupActions();
            canWeContinue(needsToBeLoggedIn);
        } else if (!Tools_PreferabliTools.getKeyStore().contains("CHANNEL_ID")) {
            try {
                Thread.sleep(1000);
                canWeContinue(needsToBeLoggedIn);
            } catch (InterruptedException e) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidClientInterface);
            }
        } else if (needsToBeLoggedIn && !Tools_PreferabliTools.isPreferabliUserLoggedIn() && !Tools_PreferabliTools.isCustomerLoggedIn()) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidAccessToken);
        }
    }
}
