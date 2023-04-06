//
//  Object_Variant.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/12/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.net.Uri;
import android.os.Parcel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Object_Variant extends Object_BaseObject {
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
    private transient Other_RatingType ratingType;
    private transient Object_Tag mostRecentRating;
    private transient HashSet<Object_Tag> ratingsTags;

    public void clearTransients() {
        ratingsTags = null;
        mostRecentRating = null;
        ratingType = null;
    }

    public Object_Variant(Object_Tag tag) {
        super(-System.currentTimeMillis());
        this.year = 0;
        this.product_id = tag.getWineId();
        addTag(tag);
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
        this.addImage(Tools_PreferabliTools.convertJsonToObject(image, Object_Media.class));
    }

    public Other_RatingType getRatingType() {
        if (getMostRecentRating() == null) return Other_RatingType.NONE;
        else if (ratingType == null) {
            Object_Tag tag = getMostRecentRating();
            ratingType = Other_RatingType.getRatingTypeBasedOffTagValue(tag.getValue());
        }

        return ratingType;
    }

    public Object_Tag getMostRecentRating() {
        if (mostRecentRating == null) {
            Date date = new Date(0);
            for (Object_Tag tag : getRatingsTags()) {
                Date compareToDate = Tools_PreferabliTools.convertAPITimeStampToDate(tag.getCreatedAt());
                if (compareToDate.after(date)) {
                    date = compareToDate;
                    mostRecentRating = tag;
                }
            }
        }

        return mostRecentRating;
    }

    public HashSet<Object_Tag> getRatingsTags() {
        if (ratingsTags == null) getTags();
        return ratingsTags;
    }

    public Object_Product getWine() {
        return product;
    }

    public void setProduct(Object_Product product) {
        this.product = product;
    }

    public Object_Media getPrimaryImage() {
        return primary_image;
    }

    public void addTag(Object_Tag tag) {
        clearTransients();
        if (tags == null) tags = new ArrayList<>();
        tags.add(tag);
    }

    public void updateTag(Object_Tag tag) {
        int index = getTags().indexOf(tag);
        if (index != -1) {
            getTags().remove(index);
        }
        addTag(tag);
    }

    public void addTags(ArrayList<Object_Tag> tags) {
        clearTransients();
        if (this.tags == null) this.tags = new ArrayList<>();
        this.tags.addAll(tags);
    }

    public String getImage() {
        if (primary_image != null && !Tools_PreferabliTools.isNullOrWhitespace(primary_image.getPath()) && !primary_image.getPath().contains("placeholder"))
            return primary_image.getPath();
        else if (getWine() != null && getWine().getPrimaryImagePath() != null && !getWine().getPrimaryImagePath().contains("placeholder")) {
            return getWine().getPrimaryImagePath();
        }
        return null;
    }

    public String getPrimaryImagePath() {
        if (primary_image != null)
            return primary_image.getPath();

        return null;
    }

    public long getImageId() {
        if (primary_image != null) return primary_image.getId();
        return 0;
    }

    public void addImage(Object object) {
        if (image_ids == null) image_ids = new HashSet<>();
        if (object != null) {
            Object_Media image;
            if (object instanceof Object_Media) image = (Object_Media) object;
            else if (object instanceof String) image = new Object_Media((String) object);
            else if (object instanceof Uri) image = new Object_Media((object).toString());
            else if (object instanceof File) image = new Object_Media(((File) object).getAbsolutePath());
            else return;
            image_ids.add(image.getId());
            primary_image_id = image.getId();
            primary_image = image;
        }
    }

    public void clearImages() {
        if (image_ids == null) image_ids = new HashSet<>();
        image_ids.clear();
        primary_image_id = 0;
        primary_image = null;
    }

    public HashSet<Long> getImageIds() {
        return image_ids;
    }

    public void removeTag(Object_Tag tag) {
        if (tags != null) tags.remove(tag);
    }

    public long getWineId() {
        return product_id;
    }

    public void setWineId(long product_id) {
        this.product_id = product_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isFresh() {
        return fresh;
    }

    public boolean isRecommendable() {
        return recommendable;
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

        ratingsTags = new HashSet<>();
        for (Object_Tag tag : tags) {
            if (tag.isRating()) ratingsTags.add(tag);
        }

        return tags;
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
        public Object_Variant createFromParcel(Parcel source) {return new Object_Variant(source);}

        @Override
        public Object_Variant[] newArray(int size) {return new Object_Variant[size];}
    };
}