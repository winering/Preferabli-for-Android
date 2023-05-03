//
//  Object_Tag.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/12/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;

/**
 * Chronicles a user's interaction with a {@link Object_Product}. Is one of a type {@link Other_TagType}.
 */
public class Object_Tag extends Object_BaseObject {
    private long user_id;
    private long variant_id;
    private long product_id;
    private int year;
    private long collection_id;
    private long tagged_in_collection_id;
    private String tagged_in_channel_name;
    private long tagged_in_collection_version_id;
    private long channel_id;
    private int format_ml;
    private double price;
    private int quantity;
    private String type;
    private String value;
    private String comment;
    private String location;
    private String created_at;
    private String badge;
    private String updated_at;
    private String bin;

    private transient Other_RatingLevel rating_level;
    private transient Other_TagType tag_type;
    private transient Object_Variant variant;
    private transient Object_Collection.Object_CollectionOrder ordering;

    public Object_Tag() {
        super(-System.currentTimeMillis());
        this.created_at = Tools_Preferabli.getSimpleDateFormat().format(new Date());
        this.updated_at = Tools_Preferabli.getSimpleDateFormat().format(new Date());
    }


    public Object_Tag(String type, int year, long product_id, long variant_id, long collection_id, long tagged_in_collection_id) {
        this();
        this.type = type;
        this.year = year;
        this.product_id = product_id;
        this.variant_id = variant_id;
        this.collection_id = collection_id;
        this.tagged_in_collection_id = tagged_in_collection_id;
    }

    public Object_Tag(String type, long product_id, long variant_id, int year, long collection_id, String value, String location, String comment, long tagged_in_collection_id, double price, int quantity, int format_ml) {
        this();
        this.type = type;
        this.product_id = product_id;
        this.tagged_in_collection_id = tagged_in_collection_id;
        this.variant_id = variant_id;
        this.year = year;
        this.collection_id = collection_id;
        this.value = value;
        this.location = location;
        this.comment = comment;
        this.price = price;
        this.quantity = quantity;
        this.format_ml = format_ml;
    }

    public int getYear() {
        return year;
    }

    public Object_Variant getVariant() {
        return variant;
    }

    public void setVariant(Object_Variant variant) {
        this.variant = variant;
    }

    public Object_Tag(long id, long user_id, long channel_id, long variant_id, long product_id, long collection_id, long tagged_in_collection_id, long tagged_in_collection_version_id, String type, String value, String comment, String location, String created_at, String updated_at, String sharing, boolean dirty, String badge, String parameters, String tagged_in_channel_name, int format_ml, double price, int quantity, String bin) {
        super(id);
        this.user_id = user_id;
        this.channel_id = channel_id;
        this.variant_id = variant_id;
        this.product_id = product_id;
        this.collection_id = collection_id;
        this.tagged_in_collection_id = tagged_in_collection_id;
        this.tagged_in_collection_version_id = tagged_in_collection_version_id;
        this.type = type;
        this.value = value;
        this.comment = comment;
        this.location = location;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.badge = badge;
        this.tagged_in_channel_name = tagged_in_channel_name;
        this.format_ml = format_ml;
        this.price = price;
        this.quantity = quantity;
        this.bin = bin;
    }

    /**
     * Gets the formatted version of {@link Object_Tag#price}.
     * @param currencyCode code of the currency you would like to use for formatting.
     * @return a currency formatted price.
     */
    public String getPrice(String currencyCode) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (!Tools_Preferabli.isNullOrWhitespace(currencyCode)) {
            format.setCurrency(Currency.getInstance(currencyCode));
        }

        return format.format(price);
    }

    public String getBin() {
        return bin;
    }

    public long getChannelId() {
        return channel_id;
    }

    public Object_Collection.Object_CollectionOrder getOrdering() {
        return ordering;
    }

    public void setOrdering(Object_Collection.Object_CollectionOrder ordering) {
        this.ordering = ordering;
    }

    public long getProductId() {
        return product_id;
    }

    public String getBadge() {
        return badge;
    }

    /**
     * The rating level of the tag. Only for tags of type {@link Other_TagType#RATING}.
     * @return rating level.
     */
    public Other_RatingLevel getRatingLevel() {
        if (rating_level == null) rating_level = Other_RatingLevel.getRatingLevelBasedOffTagValue(value);
        return rating_level;
    }

    /**
     * The type of the tag.
     * @return tag type.
     */
    public Other_TagType getTagType() {
        if (tag_type == null) tag_type = Other_TagType.getTagTypeBasedOffDatabaseName(type);
        return tag_type;
    }

    public long getUserId() {
        return user_id;
    }

    public long getVariantId() {
        return variant_id;
    }

    public long getCollectionId() {
        return collection_id;
    }

    public long getTaggedInCollectionId() {
        return tagged_in_collection_id;
    }

    public long getTaggedInCollectionVersionId() {
        return tagged_in_collection_version_id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public String getLocation() {
        return location;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    private static class TagComparator implements Comparator<Object_Tag> {
        @Override
        public int compare(Object_Tag tag1, Object_Tag tag2) {
            if (tag1 == null && tag2 == null) {
                return 0;
            } else if (tag1 == null) {
                return 1;
            } else if (tag2 == null) {
                return -1;
            } else
                return tag2.getCreatedAt().compareTo(tag1.getCreatedAt());
        }
    }

    void setLocation(String location) {
        this.location = location;
    }

    public static ArrayList<Object_Tag> sortTags(HashSet<Object_Tag> uniqueTags) {
        ArrayList<Object_Tag> tags = new ArrayList<>(uniqueTags);
        Collections.sort(tags, new TagComparator());
        return tags;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       super.writeToParcel(dest, flags);
        dest.writeLong(this.user_id);
        dest.writeLong(this.variant_id);
        dest.writeLong(this.product_id);
        dest.writeInt(this.year);
        dest.writeLong(this.collection_id);
        dest.writeLong(this.tagged_in_collection_id);
        dest.writeLong(this.tagged_in_collection_version_id);
        dest.writeLong(this.channel_id);
        dest.writeString(this.type);
        dest.writeString(this.value);
        dest.writeString(this.comment);
        dest.writeString(this.location);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.badge);
        dest.writeString(this.tagged_in_channel_name);
        dest.writeInt(format_ml);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(bin);
    }

    public int getFormatMl() {
        return format_ml;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTaggedInChannelName() {
        return tagged_in_channel_name;
    }

    protected Object_Tag(Parcel in) {
        super(in);
        this.user_id = in.readLong();
        this.variant_id = in.readLong();
        this.product_id = in.readLong();
        this.year = in.readInt();
        this.collection_id = in.readLong();
        this.tagged_in_collection_id = in.readLong();
        this.tagged_in_collection_version_id = in.readLong();
        this.channel_id = in.readLong();
        this.type = in.readString();
        this.value = in.readString();
        this.comment = in.readString();
        this.location = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.badge = in.readString();
        this.tagged_in_channel_name = in.readString();
        this.format_ml = in.readInt();
        this.price = in.readDouble();
        this.quantity = in.readInt();
        this.bin = in.readString();
    }

    public static final Creator<Object_Tag> CREATOR = new Creator<Object_Tag>() {
        @Override
        public Object_Tag createFromParcel(Parcel source) {return new Object_Tag(source);}

        @Override
        public Object_Tag[] newArray(int size) {return new Object_Tag[size];}
    };

    /**
     * See {@link Preferabli#deleteTag(long, API_ResultHandler)}.
     */
    public void delete(API_ResultHandler<Object_Product> handler) {
        Preferabli.main().deleteTag(getId(), handler);
    }

    /**
     * See {@link Preferabli#editTag(long, Other_TagType, int, Other_RatingLevel, String, String, Double, Integer, Integer, API_ResultHandler)}.
     */
    public void edit(int year, Object_Tag.Other_RatingLevel rating, String location, String notes, Double price, Integer quantity, Integer format_ml, API_ResultHandler<Object_Product> handler) {
        Preferabli.main().editTag(getId(), tag_type, year, rating, location, notes, price, quantity, format_ml, handler);
    }

    /**
     * Type of a {@link Object_Tag}. Tags may can contain different information depending on it's type.
     */
    public enum Other_TagType {
        RATING,
        WISHLIST,
        CELLAR,
        PURCHASE,
        OTHER;

        public static Other_TagType getTagTypeBasedOffDatabaseName(String type) {
            if (type != null) switch (type) {
                case "rating":
                    return Other_TagType.RATING;
                case "wishlist":
                    return Other_TagType.WISHLIST;
                case "collection":
                    return Other_TagType.CELLAR;
                case "purchase":
                    return Other_TagType.PURCHASE;
                default:
                    return Other_TagType.OTHER;
            }

            return null;
        }

        public String getDatabaseName() {
            switch (this) {
                case RATING:
                    return "rating";
                case WISHLIST:
                    return "wishlist";
                case CELLAR:
                    return "collection";
                case PURCHASE:
                    return "purchase";
                case OTHER:
                    return "other";
            }

            return null;
        }

    }

    /**
     * The degree of appeal for a product as identified by a {@link Object_Tag}.
     */
    public enum Other_RatingLevel {
        LOVE,
        LIKE,
        SOSO,
        DISLIKE,
        NONE;

        public static Other_RatingLevel getRatingLevelBasedOffTagValue(String value) {
            if (value != null) switch (value) {
                case "0":
                    return Other_RatingLevel.NONE;
                case "1":
                    return Other_RatingLevel.DISLIKE;
                case "2":
                    return Other_RatingLevel.SOSO;
                case "3":
                    return Other_RatingLevel.LIKE;
                case "4":
                    return Other_RatingLevel.LOVE;
            }

            return Other_RatingLevel.NONE;
        }

        public String getValue() {
            switch (this) {
                case LOVE:
                    return "4";
                case LIKE:
                    return "3";
                case SOSO:
                    return "2";
                case DISLIKE:
                    return "1";
                case NONE:
                    return "0";
            }

            return "0";
        }
    }


}
