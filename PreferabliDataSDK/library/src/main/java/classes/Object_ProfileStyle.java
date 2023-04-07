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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private int strength;
    private long style_id;

    public Object_ProfileStyle(long id, boolean refine, boolean conflict, int order_recommend, int order_profile, String keywords, String name, String description, String type, int rating, String foods, String image, String product_category, String location, int strength, String created_at) {
        this.refine = refine;
        this.conflict = conflict;
        this.order_recommend = order_recommend;
        this.order_profile = order_profile;
        this.keywords = keywords;
        this.style = new Object_Style(id, name, type, description, foods, image, product_category, location);
        this.rating = rating;
        this.strength = strength;
        this.style_id = id;
        this.created_at = created_at;
    }

    public Object_ProfileStyle(Object_ProfileStyle objectProfileStyle) {
        this.refine = objectProfileStyle.isRefine();
        this.conflict = objectProfileStyle.isConflict();
        this.order_recommend = objectProfileStyle.getOrderRecommend();
        this.order_profile = objectProfileStyle.getOrderProfile();
        this.created_at = objectProfileStyle.getCreatedAt();
        this.updated_at = objectProfileStyle.getUpdatedAt();
        this.style = objectProfileStyle.getStyle();
        this.rating = objectProfileStyle.getRating();
        this.keywords = objectProfileStyle.getKeywords();
        this.style_id = objectProfileStyle.getStyle_id();
    }

    public Object_ProfileStyle(Object_Style style) {
        setStyle(style);
    }

    public long getStyle_id() {
        return style_id;
    }

    public long getId() {
        return getStyle_id();
    }

    public int getRating() {
        return rating;
    }

    public String getImage() {
        return style.getImage();
    }

    public Other_RatingType getRatingType() {
        return Other_RatingType.getRatingTypeBasedOffTagValue(Integer.toString(rating));
    }

    public void setStyle(Object_Style style) {
        this.style = style;
    }

    public int getStrength() {
        return strength;
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

    public Object_ProfileStyle() {
    }

    public static ArrayList<Object_ProfileStyle> sortRecommendableStyles(ArrayList<Object_ProfileStyle> objectProfileStyles) {
        Collections.sort(objectProfileStyles, new PreferenceStyleComparator(false, false, true));
        return objectProfileStyles;
    }

    public static ArrayList<Object_ProfileStyle> sortPreferenceStyles(ArrayList<Object_ProfileStyle> objectProfileStyles) {
        Collections.sort(objectProfileStyles, new PreferenceStyleComparator(true, false, false));
        return objectProfileStyles;
    }


    public static ArrayList<Object_ProfileStyle> sortPreferenceStylesAlpha(ArrayList<Object_ProfileStyle> objectProfileStyles) {
        Collections.sort(objectProfileStyles, new PreferenceStyleAlphaComparator());
        return objectProfileStyles;
    }

    public static ArrayList<Object_ProfileStyle> sortPreferenceStylesDate(ArrayList<Object_ProfileStyle> objectProfileStyles) {
        Collections.sort(objectProfileStyles, new PreferenceStyleDateComparator());
        return objectProfileStyles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Object_ProfileStyle style = (Object_ProfileStyle) o;

        return getStyle_id() == style.getStyle_id();
    }

    @Override
    public int hashCode() {
        return (int) (getStyle_id() ^ (getStyle_id() >>> 32));
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
            else return false;
        }
        return true;
    }

    public static class PreferenceStyleDateComparator implements Comparator<Object_ProfileStyle> {
        @Override
        public int compare(Object_ProfileStyle ps1, Object_ProfileStyle ps2) {
            if (ps1 == null && ps2 == null) {
                return 0;
            } else if (ps1 == null) {
                return 1;
            } else if (ps2 == null) {
                return -1;
            }

            return ps2.getCreatedAt().compareTo(ps1.getCreatedAt());
        }
    }

    public static class PreferenceStyleAlphaComparator implements Comparator<Object_ProfileStyle> {
        @Override
        public int compare(Object_ProfileStyle ps1, Object_ProfileStyle ps2) {
            if (ps1 == null && ps2 == null) {
                return 0;
            } else if (ps1 == null) {
                return 1;
            } else if (ps2 == null) {
                return -1;
            }

            return Tools_Preferabli.alphaSortIgnoreThe(ps1.getName(), ps2.getName());
        }
    }

    public static class PreferenceStyleComparator implements Comparator<Object_ProfileStyle> {

        private boolean ratingMatters;
        private boolean wineTypeMatters;
        private boolean orderRecommend;

        public PreferenceStyleComparator(boolean ratingMatters, boolean wineTypeMatters, boolean orderRecommend) {
            this.ratingMatters = ratingMatters;
            this.wineTypeMatters = wineTypeMatters;
            this.orderRecommend = orderRecommend;
        }

        @Override
        public int compare(Object_ProfileStyle ps1, Object_ProfileStyle ps2) {
            if (ps1 == null && ps2 == null) {
                return 0;
            } else if (ps1 == null) {
                return 1;
            } else if (ps2 == null) {
                return -1;
            } else if (ratingMatters) {
                if (ps1.getRatingType() != ps2.getRatingType())
                    return ps1.getRatingType().compareTo(ps2.getRatingType());
            }

            if (orderRecommend) {
                if (ps1.getOrderRecommend() != ps2.getOrderRecommend()) {
                    return ((Integer) ps1.getOrderRecommend()).compareTo(ps2.getOrderRecommend());
                }
            } else if (ps1.getOrderProfile() != ps2.getOrderProfile()) {
                return ((Integer) ps1.getOrderProfile()).compareTo(ps2.getOrderProfile());
            }

            return Tools_Preferabli.alphaSortIgnoreThe(ps1.getName(), ps2.getName());
        }
    }

    public boolean isUnappealing() {
        return getRatingType() == Other_RatingType.DISLIKE || getRatingType() == Other_RatingType.SOSO;
    }

    public boolean isAppealing() {
        return getRatingType() == Other_RatingType.LOVE || getRatingType() == Other_RatingType.LIKE;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeInt(this.strength);
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
        this.strength = in.readInt();
        this.style_id = in.readLong();
    }

    public static final Creator<Object_ProfileStyle> CREATOR = new Creator<Object_ProfileStyle>() {
        @Override
        public Object_ProfileStyle createFromParcel(Parcel source) {return new Object_ProfileStyle(source);}

        @Override
        public Object_ProfileStyle[] newArray(int size) {return new Object_ProfileStyle[size];}
    };
}
