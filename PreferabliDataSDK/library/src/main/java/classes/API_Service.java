//
//  API_Service.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/7/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/** These are our API routes */
interface API_Service {

    @POST("sessions")
    Call<Object_SessionData> getSession(@Body JsonObject body);

    @GET("channels/{id}/customers/{customer_id}")
    Call<Object_Customer> getCustomer(@Path("id") long id, @Path("customer_id") long customer_id);

    @GET("users/{user_id}")
    Call<Object_PreferabliUser> getUser(@Path("user_id") long user_id);

    @POST("collections/{collection_id}/tags")
    Call<Object_Tag> createCollectionTag(@Path("collection_id") long collection_id, @Body JsonObject body);

    @PUT("collections/{collection_id}/tags/{tag_id}")
    Call<Object_Tag> updateCollectionTag(@Path("collection_id") long collection_id, @Path("tag_id") long tag_id, @Body JsonObject body);

    @GET("integrations/{id}")
    Call<JsonObject> getIntegration(@Path("id") long id);

    @POST("integrations/{id}/lookups")
    Call<JsonArray> lookupConversion(@Path("id") long id, @Body JsonArray jsonArray);

    @GET("channels/{id}/customers/{customer_id}/profile")
    Call<Object_Profile> getCustomerProfile(@Path("id") long id, @Path("customer_id") long customer_id);

    @GET("channels/{id}/customers/{customer_id}/tags")
    Call<ArrayList<Object_Tag>> getCustomerTags(@Path("id") long id, @Path("customer_id") long customer_id, @QueryMap Map<String, Object> options);

    @GET("users/{user_id}/usercollections")
    Call<ArrayList<Object_UserCollection>> getUserCollections(@Path("user_id") long user_id, @Query("limit") int limit, @Query("offset") int offset);

    @GET("resetpassword")
    Call<ResponseBody> resetPassword(@Query("email") String email);
    @Multipart
    @POST("media")
    Call<Object_Media> uploadImage(@Part("file\"; filename=\"file.png\" ") RequestBody file);
    @Multipart
    @POST("media")
    Call<Object_Media> uploadImage(@Part("user_id") RequestBody user_id, @Part("file\"; filename=\"file.png\" ") RequestBody file);

    @GET("collections/{collection_id}")
    Call<Object_Collection> getCollection(@Path("collection_id") long collection_id);

    @GET("wheretobuy")
    Call<JsonArray> whereToBuy(@QueryMap Map<String, Object> options);

    @GET("lttt")
    Call<JsonObject> lttt(@QueryMap Map<String, Object> options);

    @GET("search")
    Call<JsonObject> search(@QueryMap Map<String, Object> options, @Query("product_categories[]") ArrayList<String> product_categories, @Query("product_types[]") ArrayList<String> product_types);

    @GET("foods")
    Call<ArrayList<Object_Food>> getFoods();

    @GET("products")
    Call<ArrayList<Object_Product>> getProducts(@Query("variant_ids[]") ArrayList<Long> variant_ids);

    @GET("collections/{id}/tags")
    Call<ArrayList<Object_Tag>> getTags(@Path("id") long id, @Query("tag_ids[]") ArrayList<Long> tag_ids);

    @GET("collections/{id}/tags")
    Call<ArrayList<Object_Tag>> getTags(@Path("id") long id, @Query("offset") Integer offset, @Query("limit") Integer limit);

    @POST("recs")
    Call<JsonObject> getRecs(@Body JsonObject body);

    @POST("query")
    Call<JsonObject> guidedRecResults(@Body JsonObject body);

    @POST("query")
    Call<JsonObject> guidedRecResultsWithCollection(@Query("override_collection_ids[]") long id, @Body JsonObject body);

    @GET("wili")
    Call<Object_PreferenceData> getWili(@QueryMap Map<String, Object> options);

    @GET("collections/{collectionId}/versions/{versionId}/groups/{groupId}/orderings")
    Call<ArrayList<Object_Collection.Object_CollectionOrder>> getOrderings(@Path("collectionId") long collectionId, @Path("versionId") long versionId, @Path("groupId") long groupId, @Query("limit") int limit, @Query("offset") int offset);

    @GET("users/{user_id}/profile?include_styles=false")
    Call<Object_Profile> getProfile(@Path("user_id") long user_id);

    @GET("styles")
    Call<ArrayList<Object_Style>> getStyles(@Query("style_ids[]") ArrayList<Long> ids);

    @POST("collections/{collection_id}/tags")
    Call<Object_Tag> createTag(@Path("collection_id") long collection_id, @Body JsonObject body);

    @DELETE("users/{user_id}/tags/{tag_id}")
    Call<ResponseBody> deleteTag(@Path("user_id") long user_id, @Path("tag_id") long tag_id);

    @GET("questionnaire/{id}")
    Call<Object_GuidedRec> getGuidedRec(@Path("id") long id);

    @GET("imagerec")
    Call<ArrayList<Object_LabelRecResults.Object_LabelRecResult>> imageRec(@Query("media_id") long media_id);

    @POST("channels/{channel_id}/customers/{customer_id}/tags")
    Call<Object_Tag> createCustomerTag(@Path("channel_id") long channel_id, @Path("customer_id") long customerId, @Body JsonObject body);

    @POST("channels/{channel_id}/customers/{customer_id}/tags/{tag_id}")
    Call<Object_Tag> updateCustomerTag(@Path("channel_id") long channel_id, @Path("customer_id") long customerId, @Path("tag_id") long tagId, @Body JsonObject body);

    @DELETE("channels/{channel_id}/customers/{customer_id}/tags/{tag_id}")
    Call<ResponseBody> deleteCustomerTag(@Path("channel_id") long channel_id, @Path("customer_id") long customerId, @Path("tag_id") long tagId);
}