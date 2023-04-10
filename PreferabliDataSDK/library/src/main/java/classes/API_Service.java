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

public interface API_Service {

    @POST("sessions")
    Call<Object_SessionData> getSession(@Body JsonObject jsonData);

    @POST("users")
    Call<Object_PreferabliUser> createUser(@Body JsonObject jsonData);

    @POST("collections")
    Call<Object_Collection> createCollection(@Body JsonObject jsonData);

    @GET("channels/{id}/customers/{customerId}")
    Call<Object_Customer> getCustomer(@Path("id") long id, @Path("customerId") long customerId);

    @GET("users/{user_id}")
    Call<Object_PreferabliUser> getUser(@Path("user_id") long user_id);

    @GET("integrations/{id}")
    Call<JsonObject> getIntegration(@Path("id") long id);

    @POST("integrations/{id}/lookups")
    Call<JsonArray> lookupConversion(@Path("id") long id, @Body JsonArray jsonArray);

    @GET("users/{user_id}/usercollections")
    Call<ArrayList<Object_UserCollection>> getUserCollections(@Path("user_id") long user_id, @Query("limit") int limit, @Query("offset") int offset);

    @POST("users/{user_id}/usercollections")
    Call<Object_UserCollection> createUserCollection(@Path("user_id") long user_id, @Body JsonObject jsonObject);

    @GET("resetpassword")
    Call<ResponseBody> resetPassword(@Query("email") String email);

    @PUT("users/{user_id}")
    Call<Object_PreferabliUser> updateUser(@Path("user_id") long user_id, @Body Object_PreferabliUser user);

    @Multipart
    @POST("media")
    Call<Object_Media> uploadImage(@Part("file\"; filename=\"file.png\" ") RequestBody file);
    @Multipart
    @POST("media")
    Call<Object_Media> uploadImage(@Part("user_id") RequestBody user_id, @Part("file\"; filename=\"file.png\" ") RequestBody file);

    @Multipart
    @POST("media")
    Call<Object_Media> uploadImage(@Part("position") RequestBody position, @Part("user_id") RequestBody user_id, @Part("file\"; filename=\"file.png\" ") RequestBody file);

    @GET("collections/{collection_id}")
    Call<Object_Collection> getCollection(@Path("collection_id") long collection_id);

    @GET("collections/{id}/products")
    Call<ArrayList<Object_Product>> getProducts(@Path("id") long id, @Query("limit") int limit, @Query("offset") int offset);

    @GET("wheretobuy")
    Call<JsonArray> whereToBuy(@Query("lat") double lat, @Query("long") double lon, @Query("product_id") long product_id, @Query("distance_miles") int distance_miles, @Query("merge_products") boolean merge_products, @Query("sort_by") String sort_by, @Query("pickup") boolean pickup, @Query("local_delivery") boolean local_delivery, @Query("standard_shipping") boolean standard_shipping, @Query("years[]") ArrayList<Integer> years, @Query("channel_ids[]") ArrayList<Long> channel_ids, @Query("in_stock_anywhere") boolean in_stock_anywhere, @Query("append_nonconforming_results") boolean append_nonconforming_results, @Query("limit") int limit, @Query("offset") int offset, @Query("venue_id") Long venue_id, @Query("lookup_id") Long lookup_id);

    @GET("wheretobuy")
    Call<JsonArray> whereToBuy(@Query("zip_code") String zip_code, @Query("product_id") long product_id, @Query("distance_miles") int distance_miles, @Query("merge_products") boolean merge_products, @Query("sort_by") String sort_by, @Query("pickup") boolean pickup, @Query("local_delivery") boolean local_delivery, @Query("standard_shipping") boolean standard_shipping, @Query("years[]") ArrayList<Integer> years, @Query("channel_ids[]") ArrayList<Long> channel_ids, @Query("in_stock_anywhere") boolean in_stock_anywhere, @Query("append_nonconforming_results") boolean append_nonconforming_results, @Query("limit") int limit, @Query("offset") int offset, @Query("venue_id") Long venue_id, @Query("lookup_id") Long lookup_id);

    @GET("wheretobuy")
    Call<JsonArray> whereToBuy(@Query("product_id") long product_id, @Query("merge_products") boolean merge_products, @Query("sort_by") String sort_by, @Query("channel_ids[]") ArrayList<Long> channel_ids, @Query("in_stock_anywhere") boolean in_stock_anywhere, @Query("limit") int limit);

    @GET("lttt")
    Call<JsonObject> lttt(@QueryMap Map<String, Object> options);

    @GET("search")
    Call<JsonObject> search(@QueryMap Map<String, Object> options);

    @GET("search")
    Call<JsonObject> search(@QueryMap Map<String, Object> options, @Query("product_categories[]") ArrayList<String> product_categories, @Query("product_types[]") ArrayList<String> product_types);

    @GET("foods")
    Call<ArrayList<Object_Food>> getFoods();

    @GET("products")
    Call<ArrayList<Object_Product>> getProducts(@Query("variant_ids[]") ArrayList<Long> variant_ids);

    @GET("search")
    Call<JsonObject> search(@Query("search") String search, @Query("search_types[]") String[] search_types);

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

    @GET("collections/{collectionId}/versions/{versionId}/groups/{groupId}/orderings")
    Call<ArrayList<Object_Collection.Version.Group.Ordering>> getOrderings(@Path("collectionId") long collectionId, @Path("versionId") long versionId, @Path("groupId") long groupId, @Query("limit") int limit, @Query("offset") int offset);

    @GET("wili")
    Call<Object_PreferenceData> wili(@QueryMap Map<String, Object> options);

    @GET("users/{user_id}/profile?include_styles=false")
    Call<Object_Profile> getProfile(@Path("user_id") long user_id);

    @GET("styles")
    Call<ArrayList<Object_Style>> getStyles(@Query("style_ids[]") ArrayList<Long> ids);

    @POST("users/{user_id}/tags")
    Call<Object_Tag> createTag(@Path("user_id") long user_id, @Body Object_Tag tag);

    @POST("collections/{collection_id}/tags")
    Call<Object_Tag> createTag(@Path("collection_id") long collection_id, @Body JsonObject tag);

    @PUT("collections/{collection_id}")
    Call<Object_Collection> updateCollection(@Path("collection_id") long collection_id, @Body JsonObject collection);

    @DELETE("collections/{collectionId}/versions/{versionId}/groups/{groupId}/orderings/{orderingId}")
    Call<ResponseBody> deleteOrdering(@Path("collectionId") long collectionId, @Path("versionId") long versionId, @Path("groupId") long groupId, @Path("orderingId") long orderingId);

    @GET("styles/{style_id}")
    Call<Object_Style> getStyle(@Path("style_id") long style_id);

    @POST("products/{product_id}/variants")
    Call<Object_Variant> createVariant(@Path("product_id") long product_id, @Body JsonObject variant);

    @PUT("media/{id}")
    Call<Object_Media> updateMedia(@Path("id") long id, @Body JsonObject jsonObject);

    @PUT("users/{user_id}/tags/{tag_id}")
    Call<Object_Tag> updateTag(@Path("user_id") long user_id, @Path("tag_id") long tag_id, @Body JsonObject tag);

    @DELETE("users/{user_id}/tags/{tag_id}")
    Call<ResponseBody> deleteTag(@Path("user_id") long user_id, @Path("tag_id") long tag_id);

    @POST("products")
    Call<Object_Product> createProduct(@Body JsonObject product);

    @GET("products/{product_id}")
    Call<Object_Product> getProduct(@Path("product_id") long product_id);

    @GET("questionnaire/{id}")
    Call<Object_GuidedRec> getGuidedRec(@Path("id") long id);

    @GET("imagerec")
    Call<ArrayList<Object_LabelRecResult>> imageRec(@Query("media_id") long media_id);
}