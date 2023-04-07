//
//  Object_MerchantProductLink.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Object_MerchantProductLink extends Object_BaseObject {
    private long variant_id;
    private Integer variant_year;
    private String mapping_name;
    private long id;
    private String value;
    private String landing_url;
    private String image_url;
    private String product_name;
    private String intro_text;
    private String bottle_price;
    private String price_currency;
    private String website_data_verified_at;
    private double format_ml;
    private long channel_id;
    private String channel_name;
    private ArrayList<Object_Venue> venues;
    private boolean nonconforming_result;
    private boolean exclude_from_system;
    private String exclude_reason;

    private transient Object_Variant variant;

    public void setVariant(Object_Variant variant) {
        this.variant = variant;
    }

    public Object_Variant getVariant() {
        return variant;
    }

    public String getExclude_reason() {
        return exclude_reason;
    }

    public Integer getVariant_year() {
        return variant_year;
    }

    public double getFormat_ml() {
        return format_ml;
    }

    public boolean isExclude_from_system() {
        return exclude_from_system;
    }

    public void setFormat_ml(int format_ml) {
        this.format_ml = format_ml;
    }

    public long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(long channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public ArrayList<Object_Venue> getVenues() {
        if (venues == null) {
            venues = new ArrayList<>();
        }
        return venues;
    }

    public void setVenues(ArrayList<Object_Venue> venues) {
        this.venues = venues;
    }

    private transient int position;

    public long getVariant_id() {
        return variant_id;
    }

    public String getPrice_currency() {
        return price_currency;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setVariant_id(long variant_id) {
        this.variant_id = variant_id;
    }

    public String getMapping_name() {
        return mapping_name;
    }

    public void setMapping_name(String mapping_name) {
        this.mapping_name = mapping_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanding_url() {
        return landing_url;
    }

    public void setLanding_url(String landing_url) {
        this.landing_url = landing_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getIntro_text() {
        return intro_text;
    }

    public void setIntro_text(String intro_text) {
        this.intro_text = intro_text;
    }

    public String getBottle_price() {
        DecimalFormat df = new DecimalFormat("0.00");
        if (!Tools_Preferabli.isNullOrWhitespace(bottle_price)) {
            double doublePrice = Double.parseDouble(bottle_price);
            return df.format(doublePrice);
        }
        return bottle_price;
    }

    public void setBottle_price(String bottle_price) {
        this.bottle_price = bottle_price;
    }

    public String getWebsite_data_verified_at() {
        return website_data_verified_at;
    }

    public void setWebsite_data_verified_at(String website_data_verified_at) {
        this.website_data_verified_at = website_data_verified_at;
    }

    @Override
    public boolean filter(Serializable serializable) {
        return filter(serializable, true);
    }

    public boolean filter(Serializable serializable, boolean filterVenues) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        innerloop:
        for (String term : terms) {
            if (getMapping_name() != null && getMapping_name().toLowerCase().contains(term))
                continue;
            else if (getProduct_name() != null && getProduct_name().toLowerCase().contains(term))
                continue;
            else if (getVenues() != null && filterVenues) {
                for (Object_Venue venue : getVenues()) {
                    if (venue.filter(serializable, false))
                        continue innerloop;
                }
                return false;
            } else return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isNonconforming_result() {
        return nonconforming_result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof Object_MerchantProductLink) {
            Object_MerchantProductLink that = (Object_MerchantProductLink) o;
            return that.getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.variant_id);
        dest.writeString(this.mapping_name);
        dest.writeLong(this.id);
        dest.writeString(this.value);
        dest.writeString(this.landing_url);
        dest.writeString(this.image_url);
        dest.writeString(this.product_name);
        dest.writeString(this.intro_text);
        dest.writeString(this.bottle_price);
        dest.writeString(this.website_data_verified_at);
        dest.writeTypedList(this.venues);
        dest.writeString(this.price_currency);
        dest.writeDouble(this.format_ml);
        dest.writeLong(this.channel_id);
        dest.writeInt(this.variant_year);
        dest.writeInt(this.nonconforming_result ? 1 : 0);
        dest.writeInt(this.exclude_from_system ? 1 : 0);
        dest.writeString(this.exclude_reason);
    }

    public Object_MerchantProductLink() {
    }

    protected Object_MerchantProductLink(Parcel in) {
        super(in);
        this.variant_id = in.readLong();
        this.mapping_name = in.readString();
        this.id = in.readLong();
        this.value = in.readString();
        this.landing_url = in.readString();
        this.image_url = in.readString();
        this.product_name = in.readString();
        this.intro_text = in.readString();
        this.bottle_price = in.readString();
        this.website_data_verified_at = in.readString();
        this.venues = in.createTypedArrayList(Object_Venue.CREATOR);
        this.price_currency = in.readString();
        this.format_ml = in.readDouble();
        this.channel_id = in.readLong();
        this.variant_year = in.readInt();
        this.nonconforming_result = in.readInt() == 1;
        this.exclude_from_system = in.readInt() == 1;
        this.exclude_reason = in.readString();
    }

    public static final Parcelable.Creator<Object_MerchantProductLink> CREATOR = new Parcelable.Creator<Object_MerchantProductLink>() {
        @Override
        public Object_MerchantProductLink createFromParcel(Parcel source) {
            return new Object_MerchantProductLink(source);
        }

        @Override
        public Object_MerchantProductLink[] newArray(int size) {
            return new Object_MerchantProductLink[size];
        }
    };
}
