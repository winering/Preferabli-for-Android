//
//  Object_Variant.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/12/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * A Variant is a particular representation of a {@link Object_Variant}. For example, a specific vintage of a particular wine.
 */
public class Object_Variant extends Object_BaseObject {

    public static int CURRENT_VARIANT_YEAR = -1;
    public static int NON_VARIANT = 0;

    private long product_id;
    private int year;
    private boolean fresh;
    private boolean recommendable;
    private double price;
    private int num_dollar_signs;
    private ArrayList<Object_Tag> tags;
    private String created_at;
    private String updated_at;
    private HashSet<Long> image_ids;
    private long primary_image_id;
    private Object_Media primary_image;

    private transient Object_Product product;
    private transient Object_Tag.Other_RatingLevel rating_level;
    private transient Object_Tag most_recent_rating;
    private transient HashSet<Object_Tag> rating_tags;
    private transient ArrayList<Object_MerchantProductLink> merchant_links;
    private transient Object_PreferenceData preference_data;
    private transient Object_Tag wishlist_tag;
    private transient HashSet<Object_Tag> cellar_tags;
    private transient HashSet<Object_Tag> purchase_tags;

    void clearTransients() {
        rating_tags = null;
        most_recent_rating = null;
        rating_level = null;
        cellar_tags = null;
        purchase_tags = null;
    }

    public Object_Variant(long id, long product_id) {
        super(id);
        this.product_id = product_id;
    }

    public Object_Variant(long product_id, int year) {
        super(-System.currentTimeMillis());
        this.product_id = product_id;
        this.year = year;
    }

    public Object_Variant(long id, int year, boolean fresh, boolean recommendable, double price, int num_dollar_signs, String image, String created_at, String updated_at, long product_id) {
        super(id);
        this.year = year;
        this.fresh = fresh;
        this.recommendable = recommendable;
        this.price = price;
        this.num_dollar_signs = num_dollar_signs;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.product_id = product_id;
        this.primary_image = Tools_Preferabli.convertJsonToObject(image, Object_Media.class);
    }

    /**
     * The {@link Object_Tag.Other_RatingLevel} of the most recent rating of a specific variant for the current user.
     *
     * @return rating level.
     */
    public Object_Tag.Other_RatingLevel getRatingLevel() {
        if (getMostRecentRating() == null) return Object_Tag.Other_RatingLevel.NONE;
        else if (rating_level == null) {
            Object_Tag tag = getMostRecentRating();
            rating_level = Object_Tag.Other_RatingLevel.getRatingLevelBasedOffTagValue(tag.getValue());
        }

        return rating_level;
    }

    /**
     * The most recent variant tag of type {@link Object_Tag.Other_TagType#RATING} for the current user.
     *
     * @return a tag.
     */
    public Object_Tag getMostRecentRating() {
        if (most_recent_rating == null) {
            Date date = new Date(0);
            for (Object_Tag tag : getRatingTags()) {
                Date compareToDate = Tools_Preferabli.convertAPITimeStampToDate(tag.getCreatedAt());
                if (compareToDate.after(date)) {
                    date = compareToDate;
                    most_recent_rating = tag;
                }
            }
        }

        return most_recent_rating;
    }

    /**
     * All of the variant tags of type {@link Object_Tag.Other_TagType#RATING} for the current user.
     *
     * @return an array of tags.
     */
    public HashSet<Object_Tag> getRatingTags() {
        if (rating_tags == null) getTags();
        return rating_tags;
    }

    void setProduct(Object_Product product) {
        this.product = product;
    }

    public Object_Product getProduct() {
        return product;
    }

    public Object_Media getPrimaryImage() {
        return primary_image;
    }

    public ArrayList<Object_MerchantProductLink> getMerchantLinks() {
        return merchant_links;
    }

    public void addTag(Object_Tag tag) {
        clearTransients();
        if (tags == null) tags = new ArrayList<>();
        tags.add(tag);
    }

    void setMerchantLinks(ArrayList<Object_MerchantProductLink> merchant_links) {
        this.merchant_links = merchant_links;
    }


    void setPreferenceData(Object_PreferenceData preference_data) {
        this.preference_data = preference_data;
    }

    public Object_PreferenceData getPreferenceData() {
        return preference_data;
    }

    /**
     * Get the variant's image.
     *
     * @param width   returns an image with the specified width in pixels.
     * @param height  returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getImage(int width, int height, int quality) {
        if (primary_image != null && !Tools_Preferabli.isNullOrWhitespace(primary_image.getPath()) && !primary_image.getPath().contains("placeholder"))
            return Tools_Preferabli.getImageUrl(primary_image, width, height, quality);
        else if (getProduct() != null && getProduct().getPrimaryImage() != null && !getProduct().getPrimaryImage().getPath().contains("placeholder")) {
            return getProduct().getImage(width, height, quality);
        }

        return null;
    }

    public long getProductId() {
        return product_id;
    }

    public int getYear() {
        return year;
    }

    public boolean isFresh() {
        return fresh;
    }

    public boolean isRecommendable() {
        return recommendable;
    }

    void setNumDollarSigns(int num_dollar_signs) {
        this.num_dollar_signs = num_dollar_signs;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getNumDollarSigns() {
        return num_dollar_signs;
    }

    public ArrayList<Object_Tag> getTags() {
        if (tags == null) {
            tags = new ArrayList<Object_Tag>();
        }

        rating_tags = new HashSet<>();
        cellar_tags = new HashSet<>();
        purchase_tags = new HashSet<>();

        for (Object_Tag tag : tags) {
            if (tag.getTagType() == Object_Tag.Other_TagType.RATING) rating_tags.add(tag);
            else if (tag.getTagType() == Object_Tag.Other_TagType.WISHLIST) wishlist_tag = tag;
            else if (tag.getTagType() == Object_Tag.Other_TagType.CELLAR) cellar_tags.add(tag);
            else if (tag.getTagType() == Object_Tag.Other_TagType.PURCHASE) purchase_tags.add(tag);
        }

        return tags;
    }

    /**
     * The first instance within the product of tag type {@link Object_Tag.Other_TagType#WISHLIST} for the current user.
     *
     * @return a tag.
     */
    public Object_Tag getWishlistTag() {
        if (wishlist_tag == null && rating_tags == null) getTags();
        return wishlist_tag;
    }

    public boolean isOnWishlist() {
        return getWishlistTag() != null;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.product_id);
        dest.writeInt(this.year);
        dest.writeByte(this.fresh ? (byte) 1 : (byte) 0);
        dest.writeByte(this.recommendable ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.price);
        dest.writeInt(this.num_dollar_signs);
        dest.writeTypedList(this.tags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeList(new ArrayList(this.image_ids));
        dest.writeParcelable(this.primary_image, flags);
        dest.writeLong(this.primary_image_id);
    }

    protected Object_Variant(Parcel in) {
        super(in);
        this.product_id = in.readLong();
        this.year = in.readInt();
        this.fresh = in.readByte() != 0;
        this.recommendable = in.readByte() != 0;
        this.price = in.readDouble();
        this.num_dollar_signs = in.readInt();
        this.tags = in.createTypedArrayList(Object_Tag.CREATOR);
        this.created_at = in.readString();
        this.updated_at = in.readString();
        ArrayList<Long> image_ids = new ArrayList<>();
        in.readList(image_ids, Long.class.getClassLoader());
        this.image_ids = new HashSet<>(image_ids);
        this.primary_image = in.readParcelable(Object_Media.class.getClassLoader());
        this.primary_image_id = in.readLong();
    }

    public static final Creator<Object_Variant> CREATOR = new Creator<Object_Variant>() {
        @Override
        public Object_Variant createFromParcel(Parcel source) {
            return new Object_Variant(source);
        }

        @Override
        public Object_Variant[] newArray(int size) {
            return new Object_Variant[size];
        }
    };

    /**
     * See {@link Preferabli#whereToBuy(long, Other_FulfillSort, Boolean, Boolean, API_ResultHandler)}.
     */
    public void whereToBuy(Other_FulfillSort fulfill_sort, Boolean append_nonconforming_results, Boolean lock_to_integration, API_ResultHandler<Object_WhereToBuy> handler) {
        Preferabli.main().whereToBuy(product_id, fulfill_sort, append_nonconforming_results, lock_to_integration, handler);
    }

    /**
     * See {@link Preferabli#rateProduct(long, int, Object_Tag.Other_RatingLevel, String, String, Double, Integer, Integer, API_ResultHandler)}.
     */
    public void rate(Object_Tag.Other_RatingLevel rating, String location, String notes, Double price, Integer quantity, Integer format_ml, API_ResultHandler<Object_Product> handler) {
        Preferabli.main().rateProduct(product_id, year, rating, location, notes, price, quantity, format_ml, handler);
    }


    /**
     * See {@link Preferabli#wishlistProduct(long, int, String, String, Double, Integer, Integer, API_ResultHandler)}.
     */
    public void toggleWishlist(API_ResultHandler<Object_Product> handler) {
        if (isOnWishlist()) {
            Preferabli.main().deleteTag(getId(), handler);
        } else {
            Preferabli.main().wishlistProduct(product_id, getYear(), null, null, null, null, null, handler);
        }
    }

    /**
     * See {@link Preferabli#lttt(long, Integer, Long, Boolean, API_ResultHandler)}.
     */
    public void lttt(Long collection_id, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        Preferabli.main().lttt(product_id, year, collection_id, include_merchant_links, handler);
    }

    /**
     * See {@link Preferabli#getPreferabliScore(long, Integer, API_ResultHandler)}.
     */
    public void getPreferabliScore(API_ResultHandler<Object_PreferenceData> handler) {
        Preferabli.main().getPreferabliScore(product_id, year, handler);
    }
}
