//
//  Object_ProfileStyle.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import java.io.Serializable;

/**
 * The profile style object identifies a specific {@link Object_Style} as included in a user's {@link Object_Profile}. A profile style represents a unique representation of a Style to a particular user.
 */
public class Object_ProfileStyle extends Object_BaseObject {
    private boolean refine;
    private boolean conflict;
    private int order_recommend;
    private int order_profile;
    private String created_at;
    private String updated_at;
    private Object_Style style;
    private int rating;
    private String keywords;
    private long style_id;

    public Object_ProfileStyle(long id, boolean refine, boolean conflict, int order_recommend, int order_profile, String keywords, String name, String description, String type, int rating, String foods, String image, String product_category, String location, String created_at) {
        this.refine = refine;
        this.conflict = conflict;
        this.order_recommend = order_recommend;
        this.order_profile = order_profile;
        this.keywords = keywords;
        this.style = new Object_Style(id, name, type, description, foods, image, product_category, location);
        this.rating = rating;
        this.style_id = id;
        this.created_at = created_at;
    }

    public long getStyleId() {
        return style_id;
    }

    public int getRating() {
        return rating;
    }

    public String getImage() {
        return style.getImage();
    }

    /**
     * The {@link Other_RatingLevel} of a specific profile style.
     * @return the rating level.
     */
    public Other_RatingLevel getRatingLevel() {
        return Other_RatingLevel.getRatingLevelBasedOffTagValue(Integer.toString(rating));
    }

    public void setStyle(Object_Style style) {
        this.style = style;
    }

    public String getKeywords() {
        return keywords;
    }

    public int getOrderProfile() {
        return order_profile;
    }

    public Other_ProductType getProductType() {
        return style.getProductType();
    }

    public String getName() {
        return style.getName();
    }

    public boolean isRefine() {
        return refine;
    }

    public boolean isConflict() {
        return conflict;
    }

    public int getOrderRecommend() {
        return order_recommend;
    }

    public String getCreatedAt() {
        if (created_at == null) {
            created_at = "";
        }
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public Object_Style getStyle() {
        return style;
    }

    @Override
    public boolean filter(Serializable serializable) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        for (String term : terms) {
            if (getName() != null && getName().toLowerCase().contains(term))
                continue;
            else if (getKeywords() != null && getKeywords().toLowerCase().contains(term))
                continue;

            return false;
        }
        return true;
    }

    /**
     * Is a profile style unappealing?
     * @return true if unappealing.
     */
    public boolean isUnappealing() {
        return getRatingLevel() == Other_RatingLevel.DISLIKE || getRatingLevel() == Other_RatingLevel.SOSO;
    }

    /**
     * Is a profile style appealing?
     * @return true if appealing.
     */
    public boolean isAppealing() {
        return getRatingLevel() == Other_RatingLevel.LOVE || getRatingLevel() == Other_RatingLevel.LIKE;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.refine ? (byte) 1 : (byte) 0);
        dest.writeByte(this.conflict ? (byte) 1 : (byte) 0);
        dest.writeInt(this.order_recommend);
        dest.writeInt(this.order_profile);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.style, flags);
        dest.writeInt(this.rating);
        dest.writeString(this.keywords);
        dest.writeLong(this.style_id);
    }

    protected Object_ProfileStyle(Parcel in) {
        super(in);
        this.refine = in.readByte() != 0;
        this.conflict = in.readByte() != 0;
        this.order_recommend = in.readInt();
        this.order_profile = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.style = in.readParcelable(Object_Style.class.getClassLoader());
        this.rating = in.readInt();
        this.keywords = in.readString();
        this.style_id = in.readLong();
    }

    public static final Creator<Object_ProfileStyle> CREATOR = new Creator<Object_ProfileStyle>() {
        @Override
        public Object_ProfileStyle createFromParcel(Parcel source) {return new Object_ProfileStyle(source);}

        @Override
        public Object_ProfileStyle[] newArray(int size) {return new Object_ProfileStyle[size];}
    };
}
