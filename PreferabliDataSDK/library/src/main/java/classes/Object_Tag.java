//
//  Object_Tag.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/12/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.reflect.TypeToken;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

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
    private long primary_image_id;
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
    private ArrayList<Share> sharing;
    private ArrayList<Parameter> parameters;
    private Other_RatingType ratingType;
    private Other_TagType tag_type;
    private boolean dirty;
    private Object_Collection.Version.Group.Ordering ordering;

    // only created once
    private transient Date date;
    private transient Object_Variant variant;

    public static HashMap<Long, Object_Variant> _parentsVariant = new HashMap<Long, Object_Variant>();

    public Object_Tag() {
        super(-System.currentTimeMillis());
        this.created_at = Tools_Preferabli.getSimpleDateFormat().format(new Date());
        this.updated_at = Tools_Preferabli.getSimpleDateFormat().format(new Date());
    }

    public Object_Tag(String type) {
        this();
        this.type = type;
    }

    public Object_Tag(String type, int year, long collection_id) {
        // creates a manager tag
        this();
        this.type = type;
        this.product_id = product_id;
        this.variant_id = variant_id;
        this.year = year;
        this.collection_id = collection_id;
    }

    public Object_Tag(String type, int year, long product_id, long variant_id, long collection_id, long tagged_in_collection_id) {
        // creates a consumer tag
        this();
        this.type = type;
        this.product_id = product_id;
        this.variant_id = variant_id;
        this.collection_id = collection_id;
        this.tagged_in_collection_id = tagged_in_collection_id;
        this.dirty = true;
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
        this.dirty = true;
        this.price = price;
        this.quantity = quantity;
        this.format_ml = format_ml;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrimary_image_id(long primary_image_id) {
        this.primary_image_id = primary_image_id;
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
        this.sharing = Tools_Preferabli.convertJsonToObject(sharing, new TypeToken<ArrayList<Share>>() {
        }.getType());
        this.dirty = dirty;
        this.badge = badge;
        this.parameters = Tools_Preferabli.convertJsonToObject(parameters, new TypeToken<ArrayList<Parameter>>() {
        }.getType());
        this.tagged_in_channel_name = tagged_in_channel_name;
        this.format_ml = format_ml;
        this.price = price;
        this.quantity = quantity;
        this.bin = bin;
    }

    public void setCollectionId(long collection_id) {
        this.collection_id = collection_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice(Object_Collection collection) {
        return getPrice(collection.getCurrency());
    }

    public String getPrice(String currencyCode) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        if (!Tools_Preferabli.isNullOrWhitespace(currencyCode)) {
            format.setCurrency(Currency.getInstance(currencyCode));
        }

        return format.format(price);
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public String getBin() {
        return bin;
    }

    public long getChannelId() {
        return channel_id;
    }

    public Date getPurchasedAtDate() {
        return Tools_Preferabli.convertAPITimeStampToDate(created_at);
    }

    public Object_Collection.Version.Group.Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Object_Collection.Version.Group.Ordering ordering) {
        this.ordering = ordering;
    }

    public static Object_Tag newWishlistTag(long product_id, long variant_id, int year, long tagged_in_collection_id) {
        return new Object_Tag("wishlist", year, product_id, variant_id, Tools_Preferabli.getKeyStore().getLong("wishlist_id", 0), tagged_in_collection_id);
    }

    public static Object_Tag newRatingTag(long product_id, int year, String value, String location, String comment, long tagged_in_collection_id, double price, int quantity, int format_ml) {
        return new Object_Tag("rating", product_id, 0, year, Tools_Preferabli.getKeyStore().getLong("ratings_id", 0), value, location, comment, tagged_in_collection_id, price, quantity, format_ml);
    }

    public static Object_Tag newSkippedTag(long product_id, long variant_id, int year, long tagged_in_collection_id) {
        return new Object_Tag("skipped", year, product_id, variant_id, Tools_Preferabli.getKeyStore().getLong("skips_id", 0), tagged_in_collection_id);
    }

    public void addImage(Object_Media image) {
        if (image != null) {
            parameters = new ArrayList<>();
            parameters.add(new Parameter("image_id", Long.toString(image.getId())));
        }
    }

    public boolean isDirty() {
        return dirty || getId() < 0;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void setWineId(long product_id) {
        this.product_id = product_id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVariantId(long variant_id) {
        this.variant_id = variant_id;
    }

    public void setChannelId(long channel_id) {
        this.channel_id = channel_id;
    }

    public long getWineId() {
        return product_id;
    }

    public Object_Tag getMostRecentRatingFromSameCollection() {
        HashSet<Object_Tag> ratingsTags = getVariant().getRatingsTags();
        Date date = new Date(0);
        Object_Tag mostRecentRatingFromCollection = null;
        for (Object_Tag tag : ratingsTags) {
            Date compareToDate = Tools_Preferabli.convertAPITimeStampToDate(tag.getCreatedAt());
            if (compareToDate.after(date) && tag.tagged_in_collection_id == collection_id) {
                date = compareToDate;
                mostRecentRatingFromCollection = tag;
            }
        }

        if (mostRecentRatingFromCollection == null) {
            return getVariant().getMostRecentRating();
        }

        return mostRecentRatingFromCollection;
    }

    public Other_RatingType getMostRecentRatingTypeFromSameCollection() {
        Object_Tag mostRecentRatingFromCollection = getMostRecentRatingFromSameCollection();
        if (mostRecentRatingFromCollection != null) {
            return mostRecentRatingFromCollection.getRatingType();
        }

        return Other_RatingType.NONE;
    }

    public Date getDate() {
        if (date == null) date = Tools_Preferabli.convertAPITimeStampToDate(created_at);
        return date;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

//    public ArrayList<Parameter> getParameters() {
//        return parameters;
//    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Other_RatingType getRatingType() {
        if (ratingType == null) ratingType = Other_RatingType.getRatingTypeBasedOffTagValue(value);

        return ratingType;
    }

    public Other_TagType getTagType() {
        if (tag_type == null) tag_type = Other_TagType.getTagTypeBasedOffDatabaseName(type);
        return tag_type;
    }

    public boolean isRating() {
        return getTagType() == Other_TagType.RATING;
    }

    public boolean isWishlist() {
        return getTagType() == Other_TagType.WISHLIST;
    }

    public boolean isCollection() {
        return type != null && type.equalsIgnoreCase("collection");
    }

    public boolean isSkipped() {
        return type != null && type.equalsIgnoreCase("skipped");
    }

    public boolean isPurchase() {
        return type != null && type.equalsIgnoreCase("purchase");
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

    public ArrayList<Share> getSharing() {
        if (sharing == null) {
            sharing = new ArrayList<>();
        }
        return sharing;
    }

    public static class Parameter implements Parcelable {
        transient private long id;
        private String key;
        private String value;

        public Parameter(String key, String value) {
            this.value = value;
            this.key = key;
        }

        public long getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.key);
            dest.writeString(this.value);
        }

        public Parameter() {
        }

        public Parameter(Parcel in) {
            this.id = in.readLong();
            this.key = in.readString();
            this.value = in.readString();
        }

        public static final Creator<Parameter> CREATOR = new Creator<Parameter>() {
            @Override
            public Parameter createFromParcel(Parcel source) {
                return new Parameter(source);
            }

            @Override
            public Parameter[] newArray(int size) {
                return new Parameter[size];
            }
        };
    }

    public static class Share implements Parcelable {
        private long id;
        private int user_id;
        private int type_id;
        private String url;
        private String social_platform;
        private String type;
        private String hash;
        private String created_at;
        private String updated_at;

        public long getId() {
            return id;
        }

        public int getUserId() {
            return user_id;
        }

        public int getTypeId() {
            return type_id;
        }

        public String getUrl() {
            return url;
        }

        public String getSocialPlatform() {
            return social_platform;
        }

        public String getType() {
            return type;
        }

        public String getHash() {
            return hash;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public String getUpdatedAt() {
            return updated_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeInt(this.user_id);
            dest.writeInt(this.type_id);
            dest.writeString(this.url);
            dest.writeString(this.social_platform);
            dest.writeString(this.type);
            dest.writeString(this.hash);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
        }

        public Share() {
        }

        protected Share(Parcel in) {
            this.id = in.readLong();
            this.user_id = in.readInt();
            this.type_id = in.readInt();
            this.url = in.readString();
            this.social_platform = in.readString();
            this.type = in.readString();
            this.hash = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
        }

        public static final Creator<Share> CREATOR = new Creator<Share>() {
            @Override
            public Share createFromParcel(Parcel source) {
                return new Share(source);
            }

            @Override
            public Share[] newArray(int size) {
                return new Share[size];
            }
        };
    }

    public static class TagComparator implements Comparator<Object_Tag> {
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

    public static class PurchaseComparator implements Comparator<Object_Tag> {

        @Override
        public int compare(Object_Tag tag1, Object_Tag tag2) {
            if (tag1 == null && tag2 == null) {
                return 0;
            } else if (tag1 == null) {
                return 1;
            } else if (tag2 == null) {
                return -1;
            }
            return tag1.getPurchasedAtDate().compareTo(tag2.getPurchasedAtDate());
        }
    }

    public static ArrayList<Object_Tag> sortTags(HashSet<Object_Tag> uniqueTags) {
        ArrayList<Object_Tag> tags = new ArrayList<>(uniqueTags);
        Collections.sort(tags, new TagComparator());
        return tags;
    }

    public static ArrayList<Object_Tag> sortTagsByPurchase(HashSet<Object_Tag> uniqueTags) {
        ArrayList<Object_Tag> tags = new ArrayList<>(uniqueTags);
        Collections.sort(tags, new PurchaseComparator());
        return tags;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeTypedList(this.sharing);
        dest.writeTypedList(this.parameters);
        dest.writeByte(this.dirty ? (byte) 1 : (byte) 0);
        _parentsVariant.put(getId(), variant);
        dest.writeString(this.tagged_in_channel_name);
        dest.writeInt(format_ml);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(bin);
    }

    public int getFormat_ml() {
        return format_ml;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTagged_in_channel_name() {
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
        this.sharing = in.createTypedArrayList(Share.CREATOR);
        this.parameters = in.createTypedArrayList(Parameter.CREATOR);
        this.dirty = in.readByte() != 0;
        this.variant = _parentsVariant.get(this.getId());
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
}
