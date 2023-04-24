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
import android.preference.PreferenceDataStore;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This is the link between a Preferabli {@link Object_Product} and a merchant product. If returned as part of {@link Object_WhereToBuy}, will contain an array of {@link Object_Venue} as {@link Object_MerchantProductLink#venues}.
 */
public class Object_MerchantProductLink extends Object_BaseObject {

    private Long variant_id;
    private Long product_id;
    private Integer variant_year;
    private String mapping_name;
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
    private String merchant_product_id;
    private String merchant_variant_id;

    public String getExcludeReason() {
        return exclude_reason;
    }

    public Integer getVariantYear() {
        return variant_year;
    }

    public double getFormatML() {
        return format_ml;
    }

    public boolean isExcludeFromSystem() {
        return exclude_from_system;
    }

    public long getChannelId() {
        return channel_id;
    }

    public String getChannelName() {
        return channel_name;
    }

    public long getProductId() {
        return product_id;
    }

    public ArrayList<Object_Venue> getVenues() {
        if (venues == null) {
            venues = new ArrayList<>();
        }
        return venues;
    }

    public long getVariantId() {
        return variant_id;
    }

    public String getPriceCurrency() {
        return price_currency;
    }

    public String getMappingName() {
        return mapping_name;
    }

    public String getValue() {
        return value;
    }

    public String getLandingUrl() {
        return landing_url;
    }

    public String getProductName() {
        return product_name;
    }

    public String getIntroText() {
        return intro_text;
    }

    /**
     * Get the link's image.
     * @param width returns an image with the specified width in pixels.
     * @param height returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getImage(int width, int height, int quality) {
        return Tools_Preferabli.getImageUrl(image_url, width, height, quality);
    }

    public String getBottlePrice() {
        DecimalFormat df = new DecimalFormat("0.00");
        if (!Tools_Preferabli.isNullOrWhitespace(bottle_price)) {
            double doublePrice = Double.parseDouble(bottle_price);
            return df.format(doublePrice);
        }
        return bottle_price;
    }

    public String getWebsiteDataVerifiedAt() {
        return website_data_verified_at;
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
            if (getMappingName() != null && getMappingName().toLowerCase().contains(term))
                continue;
            else if (getProductName() != null && getProductName().toLowerCase().contains(term))
                continue;
            else if (getVenues() != null && filterVenues) {
                for (Object_Venue venue : getVenues()) {
                    if (venue.filter(serializable, false))
                        continue innerloop;
                }
                return false;
            }

            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isNonconformingResult() {
        return nonconforming_result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.variant_id);
        dest.writeLong(this.product_id);
        dest.writeString(this.mapping_name);
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
        dest.writeString(this.merchant_product_id);
        dest.writeString(this.merchant_variant_id);
    }

    protected Object_MerchantProductLink(Parcel in) {
        super(in);
        this.variant_id = in.readLong();
        this.product_id = in.readLong();
        this.mapping_name = in.readString();
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
        this.merchant_product_id = in.readString();
        this.merchant_variant_id = in.readString();
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

    /**
     * See {@link Preferabli#whereToBuy(long, Other_FulfillSort, Boolean, Boolean, API_ResultHandler)}.
     */
    public void whereToBuy(Other_FulfillSort fulfill_sort, Boolean append_nonconforming_results, Boolean lock_to_integration, API_ResultHandler<Object_WhereToBuy> handler) {
        if (product_id != null) {
            Preferabli.main().whereToBuy(product_id, fulfill_sort, append_nonconforming_results, lock_to_integration, handler);
        }
    }

    /**
     * See {@link Preferabli#rateProduct(long, int, Other_RatingLevel, String, String, Double, Integer, Integer, API_ResultHandler)}.
     */
    public void rate(Other_RatingLevel rating, String location, String notes, Double price, Integer quantity, Integer format_ml, API_ResultHandler<Object_Product> handler) {
        if (product_id != null) {
            if (variant_year == null) {
                variant_year = Object_Variant.CURRENT_VARIANT_YEAR;
            }
            Preferabli.main().rateProduct(product_id, variant_year, rating, location, notes, price, quantity, format_ml, handler);
        }
    }

    /**
     * See {@link Preferabli#lttt(long, Integer, Long, Boolean, API_ResultHandler)}.
     */
    public void lttt(Long collection_id, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        if (product_id != null) {
            if (variant_year == null) {
                variant_year = Object_Variant.CURRENT_VARIANT_YEAR;
            }
            Preferabli.main().lttt(product_id, variant_year, collection_id, include_merchant_links, handler);
        }
    }

    /**
     * See {@link Preferabli#getPreferabliScore(long, Integer, API_ResultHandler)}.
     */
    public void getPreferabliScore(API_ResultHandler<Object_PreferenceData> handler) {
        if (product_id != null) {
            if (variant_year == null) {
                variant_year = Object_Variant.CURRENT_VARIANT_YEAR;
            }
            Preferabli.main().getPreferabliScore(product_id, variant_year, handler);
        }
    }
}


