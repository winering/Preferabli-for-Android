//
//  Preferabli.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ringit.datasdk.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    public static long PRIMARY_INVENTORY_ID;
    /**
     * The channel id of your integration.
     */
    public static long CHANNEL_ID;
    /**
     * The id of your integration.
     */
    public static long INTEGRATION_ID;

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
     * Call this in your Application onCreate with your supplied information. Contact us if you do not have your <STRONG>client_interface</STRONG> and/or <STRONG>integration_id</STRONG>.
     *
     * @param application      pass an instance of your application context.
     * @param client_interface your unique identifier - provided by Preferabli.
     * @param integration_id   your integration id - provided by Preferabli. You may have more than one integration for different segments of your business (depending on how your account is set up)
     */
    public static void initialize(Application application, String client_interface, long integration_id) {
        initialize(application, client_interface, integration_id, false);
    }

    /**
     * Call this in your Application onCreate with your supplied information. Contact us if you do not have your <STRONG>client_interface</STRONG> and/or <STRONG>integration_id</STRONG>.
     *
     * @param application      pass an instance of your application context.
     * @param client_interface your unique identifier - provided by Preferabli.
     * @param integration_id   your integration id - provided by Preferabli. You may have more than one integration for different segments of your business (depending on how your account is set up).
     * @param logging_enabled  pass true for full logging. Defaults to <STRONG>false</STRONG>.
     */
    public static void initialize(Application application, String client_interface, long integration_id, boolean logging_enabled) {
        try {
            Tools_PreferabliApp.setAppContext(application);

            PRIMARY_INVENTORY_ID = Tools_Preferabli.getKeyStore().getLong("PRIMARY_INVENTORY_ID", 0);
            CHANNEL_ID = Tools_Preferabli.getKeyStore().getLong("CHANNEL_ID", 0);
            INTEGRATION_ID = Tools_Preferabli.getKeyStore().getLong("INTEGRATION_ID", 0);

            loggingEnabled = logging_enabled;
            hasBeenInitialized = true;

            Tools_Preferabli.getKeyStore().edit().putLong("INTEGRATION_ID", integration_id).commit();
            Tools_Preferabli.getKeyStore().edit().putString("CLIENT_INTERFACE", client_interface).commit();
            api = API_Singleton.getInstanceService(false);
            Tools_Database.initializeInstance(new Tools_Database.SQLiteOpenHelper());

            Tools_PreferabliApp.setupMixpanel(client_interface, integration_id);

            Tools_Preferabli.startNewWorkThread(PRIORITY_REGULAR, () -> {
                handleStartupActions();
            });


        } catch (API_PreferabliException e) {
            // This should never happen.
        }
    }

    private void loadUserData() {
        if (isPreferabliUserLoggedIn() || isCustomerLoggedIn()) {
            Tools_Preferabli.startNewWorkThread(PRIORITY_REGULAR, () -> {
                getProfile(PRIORITY_REGULAR, false, null);
                getRatedProducts(PRIORITY_REGULAR, false, false, null);
//                getWishlistProducts(PRIORITY_REGULAR, false, false, null);
                getPurchasedProducts(PRIORITY_REGULAR, false, true, false, null);
            });
        }
    }

    private static void handleStartupActions() {
        try {
            startupThreadRunning = true;
            createAnonymousSession();
            getIntegration();
            startupThreadRunning = false;
            main().loadUserData();
        } catch (API_PreferabliException e) {
            // Don't worry about it here but it will be checked and run before any calls to SDK can be made.
            startupThreadRunning = false;
        }
    }

    private static void createAnonymousSession() throws API_PreferabliException {
        if (Tools_Preferabli.isNullOrWhitespace(Tools_Preferabli.getKeyStore().getString("access_token", null))) {
            try {
                JsonObject sessionObject = new JsonObject();
                sessionObject.addProperty("login_as_anonymous", true);
                Response<Object_SessionData> sessionResponse = api.getSession(sessionObject).execute();
                if (!sessionResponse.isSuccessful())
                    throw new API_PreferabliException(sessionResponse.errorBody());
                Object_SessionData session = sessionResponse.body();
                Tools_Preferabli.saveSession(session);
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
            CHANNEL_ID = integrationResponse.body().get("channel_id").getAsLong();
            PRIMARY_INVENTORY_ID = integrationResponse.body().get("primary_collection_id").getAsLong();
            Tools_Preferabli.getKeyStore().edit().putLong("CHANNEL_ID", CHANNEL_ID).apply();
            Tools_Preferabli.getKeyStore().edit().putLong("PRIMARY_INVENTORY_ID", PRIMARY_INVENTORY_ID).apply();
        } catch (IOException e) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidIntegrationId, e.getMessage(), 0);
        }
    }

    /**
     * Get the Powered By Preferabli logo for use in your app.
     *
     * @param light_background pass true if you want the version suitable for a light background. Pass false for the dark background version.
     * @return Powered By Preferabli logo.
     */
    static public Drawable getPoweredByPreferabliLogo(boolean light_background) {
        return AppCompatResources.getDrawable(Tools_PreferabliApp.getAppContext(), light_background ? R.drawable.powered_by_light_bg : R.drawable.powered_by_dark_bg);
    }

    /**
     * Will let you know if a user is logged in or not.
     *
     * @return bool.
     */
    static public boolean isPreferabliUserLoggedIn() {
        return Tools_Preferabli.isPreferabliUserLoggedIn();
    }

    /**
     * Will let you know if a customer is logged in or not.
     *
     * @return bool.
     */
    static public boolean isCustomerLoggedIn() {
        return Tools_Preferabli.isCustomerLoggedIn();
    }

    /**
     * Login an existing Preferabli user.
     *
     * @param email    user's email address.
     * @param password user's password.
     * @param handler  returns {@link Object_PreferabliUser} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void loginPreferabliUser(String email, String password, API_ResultHandler<Object_PreferabliUser> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("login_user");

                Tools_Preferabli.clearAllData();

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
                Tools_Preferabli.saveSession(session);
                Response<Object_PreferabliUser> userResponse = api.getUser(session.getUserId()).execute();
                if (!userResponse.isSuccessful())
                    throw new API_PreferabliException(userResponse.errorBody());
                Object_PreferabliUser user = userResponse.body();
                if (user == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }
                Tools_Preferabli.saveUser(user);

                handleSuccess(handler, user);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Link an existing customer or create a new one if they are not in our system.
     *
     * @param merchant_customer_identification unique identifier for your customer. Usually an email address or a phone number.
     * @param merchant_customer_verification   authentication key given to you by your API.
     * @param handler                          returns {@link Object_Customer} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void loginCustomer(String merchant_customer_identification, String merchant_customer_verification, API_ResultHandler<Object_Customer> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("login_customer");

                Tools_Preferabli.clearAllData();

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
                Tools_Preferabli.saveSession(session);
                Response<Object_Customer> customerResponse = api.getCustomer(CHANNEL_ID, session.getCustomerId()).execute();
                if (!customerResponse.isSuccessful())
                    throw new API_PreferabliException(customerResponse.errorBody());
                Object_Customer customer = customerResponse.body();
                if (customer == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }
                Tools_Preferabli.saveCustomer(customer);
                handleSuccess(handler, customer);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Logout a customer / Preferabli user.
     *
     * @param handler returns true if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void logout(API_ResultHandler<Boolean> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(true);
                Tools_Preferabli.createAnalyticsPost("logout");

                Tools_Preferabli.clearAllData();
                handleSuccess(handler, true);

            } catch (API_PreferabliException e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Resets the password of an existing Preferabli user.
     *
     * @param email   user's email address.
     * @param handler returns true if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void forgotPassword(String email, API_ResultHandler<Boolean> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("forgot_password");

                Response<ResponseBody> responseResponse = api.resetPassword(email).execute();
                if (!responseResponse.isSuccessful())
                    throw new API_PreferabliException(responseResponse.errorBody());
                handleSuccess(handler, true);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Search for a {@link Object_Product}.
     *
     * @param query                  your search query.
     * @param lock_to_integration    pass true if you only want to draw results from your integration. Defaults to true.
     * @param product_categories     pass any {@link Other_ProductCategory} that you would like the results to conform to. Pass null for all results.
     * @param product_types          pass any {@link Other_ProductType} that you would like the results to conform to. Pass null for all results.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns an array of {@link Object_Product} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void searchProducts(String query, Boolean lock_to_integration, ArrayList<Other_ProductCategory> product_categories, ArrayList<Other_ProductType> product_types, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("search_products");

                Map<String, Object> map = new HashMap<>();
                map.put("search", query);
                if (lock_to_integration == null || lock_to_integration) {
                    map.put("channel_id", CHANNEL_ID);
                    map.put("search_types[]", "tags");
                } else {
                    map.put("search_types[]", "products");
                }

                ArrayList<String> categories_as_strings = new ArrayList<>();
                if (product_categories != null && product_categories.size() > 0) {
                    categories_as_strings = new ArrayList<>(product_categories.stream().map(x -> x.getName()).collect(Collectors.toList()));
                }

                ArrayList<String> types_as_strings = new ArrayList<>();
                if (product_types != null && product_types.size() > 0) {
                    types_as_strings = new ArrayList<>(product_types.stream().map(x -> x.getName()).collect(Collectors.toList()));
                }

                Response<JsonObject> searchResponse = api.search(map, categories_as_strings, types_as_strings).execute();
                if (!searchResponse.isSuccessful())
                    throw new API_PreferabliException(searchResponse.errorBody());

                JsonObject jsonObject = searchResponse.body();

                if (jsonObject == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }

                ArrayList<Object_Product> products_to_return = new ArrayList<>();
                Tools_Database.getInstance().openDatabase();

                if (jsonObject.has("products")) {
                    JsonArray items = jsonObject.getAsJsonArray("products");
                    for (JsonElement item : items) {
                        Object_Product product_from_api = Tools_Preferabli.convertJsonToObject(item.toString(), Object_Product.class);
                        Tools_Database.getInstance().updateProductTable(product_from_api);
                        Object_Product product_from_db = Tools_Database.getInstance().getProductWithId(product_from_api.getId());
                        product_from_db.setLatestVariantNumDollarSigns(item.getAsJsonObject().get("latest_variant_num_dollar_signs").getAsInt());
                        products_to_return.add(product_from_db);
                    }
                } else {
                    JsonArray items = jsonObject.getAsJsonArray("tags");
                    for (JsonElement item : items) {
                        Object_Product product_from_api = Tools_Database.getInstance().getProductWithId(item.getAsJsonObject().get("product_id").getAsLong());
                        if (product_from_api == null) {
                            product_from_api = Tools_Preferabli.convertJsonToObject(item.toString(), Object_Product.class);
                        }
                        product_from_api.setName(item.getAsJsonObject().get("product_name").getAsString());
                        product_from_api.setType(item.getAsJsonObject().get("product_type").getAsString());
                        product_from_api.setCategory(item.getAsJsonObject().get("product_category").getAsString());
                        product_from_api.setId(item.getAsJsonObject().get("product_id").getAsLong());

                        Object_Variant variant = Tools_Database.getInstance().getVariant(item.getAsJsonObject().get("variant_id").getAsLong());
                        if (variant == null) {
                            variant = new Object_Variant(item.getAsJsonObject().get("variant_id").getAsLong(), product_from_api.getId());
                        }
                        variant.setPrice(item.getAsJsonObject().get("price").getAsDouble());
                        variant.setNumDollarSigns(item.getAsJsonObject().get("num_dollar_signs").getAsInt());
                        product_from_api.addVariant(variant);

                        Tools_Database.getInstance().updateProductTable(product_from_api);
                        Object_Product product_from_db = new Object_Product(Tools_Database.getInstance().getProductWithId(product_from_api.getId()));
                        products_to_return.add(product_from_db);
                    }
                }

                Tools_Database.getInstance().closeDatabase();

                if (include_merchant_links == null || include_merchant_links) {
                    addMerchantDataToProducts(products_to_return);
                }

                handleSuccess(handler, products_to_return);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Performs label recognition on a supplied image. Returns any {@link Object_Product} matches.
     *
     * @param label_file             label image you want to search for.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns {@link Object_LabelRecResults} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void labelRecognition(File label_file, Boolean include_merchant_links, API_ResultHandler<Object_LabelRecResults> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("label_rec");

                RequestBody file = RequestBody.create(label_file, MediaType.parse("image/*"));
                Response<Object_Media> imageResponse;

                if (isPreferabliUserLoggedIn()) {
                    RequestBody user_id = RequestBody.create(Long.toString(Tools_Preferabli.getPreferabliUserId()), MediaType.parse("text/plain"));
                    imageResponse = api.uploadImage(user_id, file).execute();
                } else {
                    imageResponse = api.uploadImage(file).execute();
                }

                if (!imageResponse.isSuccessful())
                    throw new API_PreferabliException(imageResponse.errorBody());

                Object_Media media = imageResponse.body();

                if (media == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }

                Response<ArrayList<Object_LabelRecResult>> imageRecResponse = api.imageRec(imageResponse.body().getId()).execute();
                if (!imageRecResponse.isSuccessful())
                    throw new API_PreferabliException(imageRecResponse.errorBody());

                ArrayList<Object_LabelRecResult> labelRecResults = imageRecResponse.body();

                if (labelRecResults == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }

                if (include_merchant_links == null || include_merchant_links) {
                    ArrayList<Object_Product> products = new ArrayList<>(labelRecResults.stream().map(x -> x.getProduct()).collect(Collectors.toList()));
                    addMerchantDataToProducts(products);
                }

                handleSuccess(handler, new Object_LabelRecResults(media, labelRecResults));

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get all the questions and choices needed to run a Guided Rec. Present the questions to the user, then pass the answers to {@link Preferabli#getGuidedRecResults(long, ArrayList, Integer, Integer, Long, Boolean, API_ResultHandler)}.
     *
     * @param guided_rec_id id of the Guided Rec you wish to run. See {@link Object_GuidedRec} for all the default Guided Rec options.
     * @param handler       returns {@link Object_GuidedRec} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getGuidedRec(long guided_rec_id, API_ResultHandler<Object_GuidedRec> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("get_guided_rec");

                Response<Object_GuidedRec> guidedRecResponse = api.getGuidedRec(guided_rec_id).execute();
                if (!guidedRecResponse.isSuccessful())
                    throw new API_PreferabliException(guidedRecResponse.errorBody());

                Object_GuidedRec guidedRec = guidedRecResponse.body();

                if (guidedRec == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }

                handleSuccess(handler, guidedRec);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get Guided Rec results based on the selected {@link Object_GuidedRec.Object_GuidedRecChoice}.
     *
     * @param guided_rec_id          id of the Guided Rec you wish to run.
     * @param selected_choice_ids    an array of selected {@link Object_GuidedRec.Object_GuidedRecChoice} ids.
     * @param price_min              pass if you want to lock results to a minimum price. Pass null to ignore.
     * @param price_max              pass if you want to lock results to a maximum price. Pass null to ignore.
     * @param collection_id          the id of a specific {@link Object_Collection} that you want to draw results from. Pass {@link Preferabli#PRIMARY_INVENTORY_ID} for results from your primary inventory. Pass null for results from anywhere.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns an array of {@link Object_Product} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getGuidedRecResults(long guided_rec_id, ArrayList<Long> selected_choice_ids, Integer price_min, Integer price_max, Long collection_id, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("get_guided_rec_results");

                JsonObject dictionary = new JsonObject();
                dictionary.addProperty("limit", 8);
                dictionary.addProperty("sort_by", "preference");
                dictionary.addProperty("questionnaire_id", guided_rec_id);
                dictionary.addProperty("offset", 0);

                JsonArray questionnaire_choice_ids = new JsonArray();
                for (long choice_id : selected_choice_ids) {
                    questionnaire_choice_ids.add(choice_id);
                }
                dictionary.add("questionnaire_choice_ids", questionnaire_choice_ids);

                JsonArray filters = new JsonArray();
                if (price_min != null) {
                    JsonObject minObject = new JsonObject();
                    minObject.addProperty("key", "price_min");
                    minObject.addProperty("value", price_min);
                    filters.add(minObject);
                }

                if (price_max != null) {
                    JsonObject maxObject = new JsonObject();
                    maxObject.addProperty("key", "price_max");
                    maxObject.addProperty("value", price_max);
                    filters.add(maxObject);
                }

                if (filters.size() > 0) {
                    dictionary.add("filters", filters);
                }

                Response<JsonObject> recResponse;
                if (collection_id != null) {
                    recResponse = api.guidedRecResultsWithCollection(collection_id, dictionary).execute();
                } else {
                    recResponse = api.guidedRecResults(dictionary).execute();
                }

                if (!recResponse.isSuccessful())
                    throw new API_PreferabliException(recResponse.errorBody());

                if (recResponse.body() == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }

                JsonArray types = recResponse.body().getAsJsonArray("types");

                ArrayList<Object_Product> products_to_return = new ArrayList<>();

                for (JsonElement type : types) {
                    ArrayList<Long> variantIds = new ArrayList<>();

                    JsonObject object = type.getAsJsonObject();
                    JsonArray results = object.getAsJsonArray("results");
                    for (JsonElement result : results) {
                        variantIds.add(result.getAsJsonObject().get("variant_id").getAsLong());
                    }

                    Response<ArrayList<Object_Product>> productsResponse = api.getProducts(variantIds).execute();
                    if (!productsResponse.isSuccessful())
                        throw new API_PreferabliException(productsResponse.errorBody());

                    if (productsResponse.body() == null) {
                        throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                    }

                    Tools_Database.getInstance().openDatabase();
                    for (Object_Product product : productsResponse.body()) {
                        Tools_Database.getInstance().updateProductTable(product);
                        Object_Product product_from_db = Tools_Database.getInstance().getProductWithId(product.getId());
                        products_to_return.add(product_from_db);
                    }
                    Tools_Database.getInstance().closeDatabase();
                }

                if (include_merchant_links) {
                    addMerchantDataToProducts(products_to_return);
                }

                handleSuccess(handler, products_to_return);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get help finding out where a {@link Object_Product} is in stock.
     *
     * @param product_id                   id of the starting {@link Object_Product}. Only pass a Preferabli product id. If necessary, call {@link Preferabli#getPreferabliProductId(String, String, API_ResultHandler)} to convert your product id into a Preferabli product id.
     * @param fulfill_sort                 pass {@link Other_FulfillSort} for sorting & filtering options. If sorting by distance, {@link Object_Location} MUST be present!
     * @param append_nonconforming_results pass true if you want results that DO NOT conform to all filtering & sorting parameters to be returned. Useful so that something is returned even if the user's filter parameters are too narrow. All results that do not conform contain nonconforming_result = true within. Defaults to true.
     * @param lock_to_integration          pass true if you only want to draw results from your integration. Defaults to true.
     * @param handler                      returns {@link Object_WhereToBuy} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void whereToBuy(long product_id, Other_FulfillSort fulfill_sort, Boolean append_nonconforming_results, Boolean lock_to_integration, API_ResultHandler<Object_WhereToBuy> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("where_to_buy");

                Other_FulfillSort sort = fulfill_sort;
                if (sort == null) {
                    sort = Other_FulfillSort.getDefaultFulfillSort();
                }

                String sort_by;
                if (sort.getType() == Other_Sort.SortType.DISTANCE && sort.isAscending()) {
                    sort_by = "nearest_first";
                } else if (sort.getType() == Other_Sort.SortType.DISTANCE) {
                    sort_by = "furthest_first";
                } else if (sort.isAscending()) {
                    sort_by = "price_asc";
                } else {
                    sort_by = "price_desc";
                }

                Map<String, Object> map = new HashMap<>();
                map.put("product_id", product_id);
                map.put("sort_by", sort_by);
                map.put("merge_products", true);
                map.put("pickup", sort.isPickup());
                map.put("local_delivery", sort.isDelivery());
                map.put("standard_shipping", sort.isShipping());
                map.put("append_nonconforming_results", append_nonconforming_results == null || append_nonconforming_results);
                map.put("limit", 1000);
                map.put("offset", 0);
                map.put("distance_miles", sort.getDistanceMiles());

                if (sort.getType() == Other_Sort.SortType.DISTANCE && sort.getLocation() == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.OtherError, "Sort by distance requires a location.");
                } else if (sort.getLocation() != null) {
                    if (Tools_Preferabli.isNullOrWhitespace(sort.getLocation().getZipCode())) {
                        map.put("lat", sort.getLocation().getLatitude());
                        map.put("long", sort.getLocation().getLongitude());
                    } else {
                        map.put("zip_code", sort.getLocation().getZipCode());
                    }
                } else {
                    map.put("in_stock_anywhere", true);
                }

                ArrayList<Integer> years = new ArrayList<>();
                if (sort.getVariantYear() != Object_Variant.NON_VARIANT) {
                    years.add(sort.getVariantYear());
                }

                if (lock_to_integration == null || lock_to_integration) {
                    map.put("channel_ids[]", CHANNEL_ID);
                }

                Response<JsonArray> wtbResponse = api.whereToBuy(map).execute();
                if (!wtbResponse.isSuccessful())
                    throw new API_PreferabliException(wtbResponse.errorBody());

                JsonArray array = wtbResponse.body();

                ArrayList<Object_MerchantProductLink> links = new ArrayList<>();
                ArrayList<Object_Venue> venues = new ArrayList<>();

                if (array.size() > 0) {
                    JsonObject object = array.get(0).getAsJsonObject();
                    if (object.has("lookup_results")) {
                        JsonArray lookups = object.getAsJsonArray("lookup_results");
                        for (JsonElement lookup : lookups) {
                            Object_MerchantProductLink link = Tools_Preferabli.convertJsonToObject(lookup.toString(), Object_MerchantProductLink.class);
                            links.add(link);
                        }
                    } else if (object.has("venue_results")) {
                        JsonArray venue_array = object.getAsJsonArray("venue_results");
                        for (JsonElement venue_string : venue_array) {
                            Object_Venue venue = Tools_Preferabli.convertJsonToObject(venue_string.toString(), Object_Venue.class);
                            venues.add(venue);
                        }
                    }
                }


                Object_WhereToBuy whereToBuy = new Object_WhereToBuy(links, venues);
                handleSuccess(handler, whereToBuy);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get a Like This, Try That recommendation. Start with a {@link Object_Product}, get similar tasting results. This function will return personalized results if a user is logged in.
     *
     * @param product_id             id of the starting {@link Object_Product}. Only pass a Preferabli product id. If necessary, call {@link Preferabli#getPreferabliProductId(String, String, API_ResultHandler)} to convert your product id into a Preferabli product id.
     * @param year                   year of the {@link Object_Variant} that you want to get results on. Defaults to {@link Object_Variant#CURRENT_VARIANT_YEAR}.
     * @param collection_id          the id of a specific {@link Object_Collection} that you want to draw results from. Defaults to {@link Preferabli#PRIMARY_INVENTORY_ID}.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns an array of {@link Object_Product} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void lttt(long product_id, Integer year, Long collection_id, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("lttt");

                HashMap<String, Object> options = new HashMap<>();
                if (isPreferabliUserLoggedIn()) {
                    options.put("user_id", Tools_Preferabli.getPreferabliUserId());
                } else if (isCustomerLoggedIn()) {
                    options.put("channel_customer_id", Tools_Preferabli.getCustomerId());
                }
                options.put("product_id", product_id);
                options.put("year", year == null ? Object_Variant.CURRENT_VARIANT_YEAR : year);
                options.put("collection_id", collection_id == null ? PRIMARY_INVENTORY_ID : collection_id);

                Response<JsonObject> response = api.lttt(options).execute();

                if (!response.isSuccessful())
                    throw new API_PreferabliException(response.errorBody());

                JsonObject object = response.body();

                if (object == null || object.get("results") == null) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
                }

                Tools_Database.getInstance().openDatabase();
                ArrayList<Object_Product> products_to_return = new ArrayList<>();
                JsonArray jsonArray = object.get("results").getAsJsonArray();

                for (JsonElement element : jsonArray) {
                    JsonObject objectHere = element.getAsJsonObject();
                    Object_Product product = Tools_Preferabli.getGson().fromJson(objectHere.get("product").toString(), new TypeToken<Object_Product>() {
                    }.getType());
                    Tools_Database.getInstance().updateProductTable(product);
                    Object_Product product_from_db = Tools_Database.getInstance().getProductWithId(product.getId());
                    if (objectHere.has("formatted_predict_rating") && !(objectHere.get("formatted_predict_rating") instanceof JsonNull)) {
                        Object_PreferenceData data = new Object_PreferenceData(objectHere.get("formatted_predict_rating").getAsInt());
                        product_from_db.getMostRecentVariant().setPreferenceData(data);
                    }
                    products_to_return.add(product_from_db);
                }

                if (include_merchant_links == null || include_merchant_links) {
                    addMerchantDataToProducts(products_to_return);
                }

                handleSuccess(handler, products_to_return);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get the Preference Profile of the customer / user. Customer / user must be logged in to run this call.
     *
     * @param force_refresh pass true if you want to force a refresh from the API and wait for the results to return. Otherwise, the call will load locally if available and run a background refresh only if one has not been initiated in the past 5 minutes. Defaults to false.
     * @param handler       returns {@link Object_Profile} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getProfile(Boolean force_refresh, API_ResultHandler<Object_Profile> handler) {
        getProfile(PRIORITY_HIGH, force_refresh, handler);
    }

    private void getProfile(int priority, Boolean force_refresh, API_ResultHandler<Object_Profile> handler) {
        Tools_Preferabli.startNewWorkThread(priority, () -> {
            try {
                canWeContinue(true);
                Tools_Preferabli.createAnalyticsPost("get_profile");

                if ((force_refresh != null && force_refresh) || !Tools_Preferabli.getKeyStore().getBoolean("hasLoadedProfile", false)) {
                    getProfileActual(force_refresh != null && force_refresh);
                } else if (Tools_Preferabli.hasMinutesPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalledProfile", 0))) {
                    Tools_Preferabli.startNewWorkThread(PRIORITY_LOW, () -> {
                        try {
                            getProfileActual(false);
                        } catch (Exception e) {
                            // catching any issues here so that we can still pull up our saved data
                            if (isLoggingEnabled()) {
                                Log.e(getClass().getName(), e.getMessage());
                            }
                        }
                    });
                }

                Tools_Database.getInstance().openDatabase();
                ArrayList<Object_ProfileStyle> profile_styles = Tools_Database.getInstance().getStyles();
                Tools_Database.getInstance().closeDatabase();
                Object_Profile profile = new Object_Profile(profile_styles);

                handleSuccess(handler, profile);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    private void getProfileActual(Boolean force_refresh) throws IOException, API_PreferabliException {
        Response<Object_Profile> profilesResponse;
        if (isPreferabliUserLoggedIn()) {
            profilesResponse = api.getProfile(Tools_Preferabli.getPreferabliUserId()).execute();
        } else {
            profilesResponse = api.getCustomerProfile(CHANNEL_ID, Tools_Preferabli.getCustomerId()).execute();
        }

        if (!profilesResponse.isSuccessful())
            throw new API_PreferabliException(profilesResponse.errorBody());

        Object_Profile profile = profilesResponse.body();

        if (profile == null) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
        }

        Tools_Database.getInstance().openDatabase();

        ArrayList<Long> style_ids = new ArrayList<>();
        HashMap<Long, Object_ProfileStyle> styleMap = new HashMap<>();
        for (Object_ProfileStyle objectProfileStyle : profile.getProfileStyles()) {
            if (force_refresh) {
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
            Response<ArrayList<Object_Style>> stylesResponse = api.getStyles(style_ids).execute();
            if (!stylesResponse.isSuccessful())
                throw new API_PreferabliException(stylesResponse.errorBody());

            if (stylesResponse.body() == null) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
            }

            for (Object_Style style : stylesResponse.body()) {
                styleMap.get(style.getId()).setStyle(style);
            }
        }

        Tools_Database.getInstance().openDatabase();
        if (force_refresh) {
            Tools_Database.getInstance().clearStyleTable();
        }
        for (Object_ProfileStyle objectProfileStyle : profile.getProfileStyles()) {
            Tools_Database.getInstance().updateStyleTable(objectProfileStyle);
        }
        Tools_Database.getInstance().closeDatabase();

        Tools_Preferabli.getKeyStore().edit().putLong("lastCalledProfile", System.currentTimeMillis()).apply();
        Tools_Preferabli.getKeyStore().edit().putBoolean("hasLoadedProfile", true).apply();
    }

    /**
     * Get rated products. Customer / Preferabli user must be logged in to run this call.
     *
     * @param force_refresh          pass true if you want to force a refresh from the API and wait for the results to return. Otherwise, the call will load locally if available and run a background refresh only if one has not been initiated in the past 5 minutes. Defaults to false.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns an array of {@link Object_Product} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getRatedProducts(Boolean force_refresh, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        getRatedProducts(PRIORITY_HIGH, force_refresh, include_merchant_links, handler);
    }

    private void getRatedProducts(int priority, Boolean force_refresh, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Tools_Preferabli.startNewWorkThread(priority, () -> {
            try {
                canWeContinue(true);
                Tools_Preferabli.createAnalyticsPost("get_rated_products");

                ArrayList<Object_Product> products_to_return;
                if (isPreferabliUserLoggedIn()) {
                    products_to_return = getProductsInCollection(priority, force_refresh, Tools_Preferabli.getKeyStore().getLong("ratings_id", 0));
                } else {
                    products_to_return = getCustomerTags(force_refresh, "rating");
                }

                if (include_merchant_links == null || include_merchant_links) {
                    addMerchantDataToProducts(products_to_return);
                }

                canWeContinue(true);

                handleSuccess(handler, products_to_return);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get wishlisted products. Customer / Preferabli user must be logged in to run this call.
     *
     * @param force_refresh          pass true if you want to force a refresh from the API and wait for the results to return. Otherwise, the call will load locally if available and run a background refresh only if one has not been initiated in the past 5 minutes. Defaults to false.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns an array of {@link Object_Product} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getWishlistProducts(Boolean force_refresh, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        getWishlistedProducts(PRIORITY_HIGH, force_refresh, include_merchant_links, handler);
    }

    private void getWishlistedProducts(int priority, Boolean force_refresh, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Tools_Preferabli.startNewWorkThread(priority, () -> {
            try {
                canWeContinue(true);
                Tools_Preferabli.createAnalyticsPost("get_wishlist_products");

                ArrayList<Object_Product> products_to_return;
                if (isPreferabliUserLoggedIn()) {
                    products_to_return = getProductsInCollection(priority, force_refresh, Tools_Preferabli.getKeyStore().getLong("wishlist_id", 0));
                } else {
                    products_to_return = getCustomerTags(force_refresh, "wishlist");
                }

                if (include_merchant_links == null || include_merchant_links) {
                    addMerchantDataToProducts(products_to_return);
                }

                canWeContinue(true);

                handleSuccess(handler, products_to_return);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    /**
     * Get purchased products. Customer / Preferabli user must be logged in to run this call.
     *
     * @param force_refresh          pass true if you want to force a refresh from the API and wait for the results to return. Otherwise, the call will load locally if available and run a background refresh only if one has not been initiated in the past 5 minutes. Defaults to false.
     * @param lock_to_integration    pass true if you only want to draw results from your integration. Defaults to true.
     * @param include_merchant_links pass true if you want the results to include an array of {@link Object_MerchantProductLink} embedded in {@link Object_Variant}. These connect Preferabli products to your own. Passing true requires additional resources and therefore will take longer. Defaults to true.
     * @param handler                returns an array of {@link Object_Product} if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getPurchasedProducts(Boolean force_refresh, Boolean lock_to_integration, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        getPurchasedProducts(PRIORITY_HIGH, force_refresh, include_merchant_links, handler);
    }

    private void getPurchasedProducts(int priority, Boolean force_refresh, Boolean lock_to_integration, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Tools_Preferabli.startNewWorkThread(priority, () -> {
            try {
                canWeContinue(true);
                Tools_Preferabli.createAnalyticsPost("get_purchase_history");

                ArrayList<Object_Product> products_to_return;
                if (isPreferabliUserLoggedIn()) {
                    need tow ork on this
                } else {
                    products_to_return = getCustomerTags(force_refresh, "purchase");
                }

                if (include_merchant_links == null || include_merchant_links) {
                    addMerchantDataToProducts(products_to_return);
                }

                canWeContinue(true);

                handleSuccess(handler, products_to_return);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    private ArrayList<Object_Product> getProductsInCollection(int priority, Boolean force_refresh, long collection_id) throws Exception {
        if ((force_refresh != null && force_refresh) || !Tools_Preferabli.getKeyStore().getBoolean("hasLoaded" + collection_id, false)) {
            Tools_LoadCollection.getInstance().loadCollectionViaTags(collection_id, priority);
        } else if (Tools_Preferabli.hasMinutesPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalled" + collection_id, 0))) {
            Tools_Preferabli.startNewWorkThread(PRIORITY_LOW, () -> {
                try {
                    Tools_LoadCollection.getInstance().loadCollectionViaTags(collection_id, PRIORITY_LOW);
                } catch (Exception e) {
                    // catching any issues here so that we can still pull up our saved data
                    if (isLoggingEnabled()) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                }
            });
        }

        Tools_Database.getInstance().openDatabase();
        Object_Collection collection = Tools_Database.getInstance().getCollection(collection_id);
        Tools_Database.getInstance().closeDatabase();

        ArrayList<Object_Product> products_to_return = Tools_LoadCollection.getInstance().getCachedProducts(collection, false);

        canWeContinue(true);

        return products_to_return;
    }

    private ArrayList<Object_Product> getCustomerTags(Boolean force_refresh, String tag_type) throws Exception {
        if ((force_refresh != null && force_refresh) || !Tools_Preferabli.getKeyStore().getBoolean("hasLoaded" + (tag_type == null ? "AllCustomerTags" : tag_type), false)) {
            getCustomerTagsActual(tag_type);
        } else if (Tools_Preferabli.hasMinutesPassed(5, Tools_Preferabli.getKeyStore().getLong("lastCalled" + (tag_type == null ? "AllCustomerTags" : tag_type), 0))) {
            Tools_Preferabli.startNewWorkThread(PRIORITY_LOW, () -> {
                try {
                    getCustomerTagsActual(tag_type);
                } catch (Exception e) {
                    // catching any issues here so that we can still pull up our saved data
                    if (isLoggingEnabled()) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                }
            });
        }

        Tools_Database.getInstance().openDatabase();
        ArrayList<Object_Product> products_to_return = Tools_Database.getInstance().getCustomerTags(Tools_Preferabli.getCustomerId());
        Tools_Database.getInstance().closeDatabase();

        canWeContinue(true);

        return products_to_return;
    }

    private ArrayList<Object_Product> getCustomerTagsActual(String tag_type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (!Tools_Preferabli.isNullOrWhitespace(tag_type)) {
            map.put("tag_type", tag_type);
        }
        map.put("offset", 0);
        map.put("limit", 9999);

        Response<ArrayList<Object_Tag>> historyResponse = api.getCustomerTags(CHANNEL_ID, Tools_Preferabli.getCustomerId(), map).execute();
        if (!historyResponse.isSuccessful())
            throw new API_PreferabliException(historyResponse.errorBody());

        ArrayList<Object_Tag> tags = historyResponse.body();

        Tools_Database.getInstance().openDatabase();
        ArrayList<Long> variant_ids = new ArrayList<>();
        for (Object_Tag tag : tags) {
            Tools_Database.getInstance().updateTagTable(Tools_Preferabli.getCustomerId(), null, tag);
            variant_ids.add(tag.getVariantId());
        }
        Tools_Database.getInstance().closeDatabase();

        Response<ArrayList<Object_Product>> productsResponse = api.getProducts(variant_ids).execute();
        if (!productsResponse.isSuccessful())
            throw new API_PreferabliException(productsResponse.errorBody());

        ArrayList<Object_Product> products = productsResponse.body();

        if (products == null) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.BadData);
        }

        Tools_Database.getInstance().openDatabase();
        for (Object_Product product : products) {
            Tools_Database.getInstance().updateProductTable(product);
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

        return products;
    }

    /**
     * Call this to convert your merchant product / variant id to the Preferabli product id for use with our functions.
     *
     * @param merchant_product_id the id of your product (as it appears in your system). Either this or merchant_variant_id is required.
     * @param merchant_variant_id the id of your product variant (as it appears in your system). Used only if you have a hierarchical database format for your products.
     * @param handler             returns product id as a long if the call was successful. Returns {@link API_PreferabliException} if the call fails.
     */
    public void getPreferabliProductId(String merchant_product_id, String merchant_variant_id, API_ResultHandler<Long> handler) {
        Tools_Preferabli.startNewWorkThread(PRIORITY_HIGH, () -> {
            try {
                canWeContinue(false);
                Tools_Preferabli.createAnalyticsPost("get_preferabli_id");

                if (Tools_Preferabli.isNullOrWhitespace(merchant_product_id) && Tools_Preferabli.isNullOrWhitespace(merchant_variant_id)) {
                    throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.MappingNotFound);
                }

                JsonArray array = new JsonArray();
                JsonObject dictionary = new JsonObject();
                dictionary.addProperty("number", 1);
                if (!Tools_Preferabli.isNullOrWhitespace(merchant_product_id)) {
                    JsonArray nested_array = new JsonArray();
                    nested_array.add(merchant_product_id);
                    dictionary.add("merchant_product_ids", nested_array);
                }
                if (!Tools_Preferabli.isNullOrWhitespace(merchant_variant_id)) {
                    JsonArray nested_array = new JsonArray();
                    nested_array.add(merchant_variant_id);
                    dictionary.add("merchant_variant_ids", nested_array);
                }
                array.add(dictionary);

                Response<JsonArray> conversionResponse = api.lookupConversion(INTEGRATION_ID, array).execute();
                if (!conversionResponse.isSuccessful())
                    throw new API_PreferabliException(conversionResponse.errorBody());

                JsonArray jsonArray = conversionResponse.body();

                for (JsonElement element : jsonArray) {
                    JsonObject object = element.getAsJsonObject();
                    JsonArray lookups = object.getAsJsonArray("lookups");
                    for (JsonElement lookup : lookups) {
                        Object_MerchantProductLink merchant_link = Tools_Preferabli.convertJsonToObject(lookup.toString(), Object_MerchantProductLink.class);
                        handleSuccess(handler, merchant_link.getProductId());
                        return;
                    }
                }

                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.MappingNotFound);

            } catch (Exception e) {
                handleError(e, handler);
            }
        });
    }

    private void addMerchantDataToProducts(ArrayList<Object_Product> products) throws API_PreferabliException, IOException {
        if (products.size() == 0) {
            return;
        }

        JsonArray dictionaries = new JsonArray();
        for (Object_Product product : products) {
            for (Object_Variant variant : product.getVariants()) {
                JsonObject dictionary = new JsonObject();
                dictionary.addProperty("number", variant.getId());
                JsonArray variant_ids = new JsonArray();
                variant_ids.add(variant.getId());
                dictionary.add("variant_ids", variant_ids);
                dictionaries.add(dictionary);
            }
        }

        Response<JsonArray> conversionResponse = api.lookupConversion(INTEGRATION_ID, dictionaries).execute();
        if (!conversionResponse.isSuccessful())
            throw new API_PreferabliException(conversionResponse.errorBody());

        JsonArray jsonArray = conversionResponse.body();

        outerloop:
        for (JsonElement element : jsonArray) {
            JsonObject object = element.getAsJsonObject();
            long variant_id = object.get("number").getAsLong();
            for (Object_Product product : products) {
                for (Object_Variant variant : product.getVariants()) {
                    if (variant.getId() == variant_id) {
                        JsonArray lookups = object.getAsJsonArray("lookups");
                        ArrayList<Object_MerchantProductLink> merchant_links = new ArrayList<>();
                        for (JsonElement lookup : lookups) {
                            Object_MerchantProductLink merchant_link = Tools_Preferabli.convertJsonToObject(lookup.toString(), Object_MerchantProductLink.class);
                            merchant_links.add(merchant_link);
                        }
                        variant.setMerchantLinks(merchant_links);
                        continue outerloop;
                    }
                }
            }
        }
    }

    private void handleError(Exception e, API_ResultHandler handler) {
        API_PreferabliException preferabliException;
        if (e instanceof API_PreferabliException) {
            preferabliException = (API_PreferabliException) e;
        } else {
            preferabliException = new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.NetworkError, e.getMessage());
        }

        if (loggingEnabled) {
            Log.e(getClass().getName(), preferabliException.getMessage());
        }

        if (handler != null) {
            new android.os.Handler(Looper.getMainLooper()).post(() -> handler.onFailure(preferabliException));
        }
    }

    private void handleSuccess(API_ResultHandler handler, Object data) {
        if (handler != null) {
            new android.os.Handler(Looper.getMainLooper()).post(() -> handler.onSuccess(data));
        }
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
        } else if (!Tools_Preferabli.getKeyStore().contains("access_token") && !startupThreadRunning) {
            handleStartupActions();
            canWeContinue(needsToBeLoggedIn);
        } else if (!Tools_Preferabli.getKeyStore().contains("access_token")) {
            try {
                Thread.sleep(1000);
                canWeContinue(needsToBeLoggedIn);
            } catch (InterruptedException e) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidAccessToken);
            }
        } else if (!Tools_Preferabli.getKeyStore().contains("CHANNEL_ID") && !startupThreadRunning) {
            handleStartupActions();
            canWeContinue(needsToBeLoggedIn);
        } else if (!Tools_Preferabli.getKeyStore().contains("CHANNEL_ID")) {
            try {
                Thread.sleep(1000);
                canWeContinue(needsToBeLoggedIn);
            } catch (InterruptedException e) {
                throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidClientInterface);
            }
        } else if (needsToBeLoggedIn && !Tools_Preferabli.isPreferabliUserLoggedIn() && !Tools_Preferabli.isCustomerLoggedIn()) {
            throw new API_PreferabliException(API_PreferabliException.PreferabliExceptionType.InvalidAccessToken);
        }
    }
}
