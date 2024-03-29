//
//  Object_Customer.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A logged in merchant customer.
 */
public class Object_Customer extends Object_BaseObject {
    private String avatar_url;
    private String merchant_user_email_address;
    private String merchant_user_id;
    private String merchant_user_name;
    private String merchant_user_display_name;
    private String claim_code;
    private String role;
    private boolean has_profile;
    private long ratings_collection_id;

    public Object_Customer(long id, String avatar_url, String merchant_user_email_address, String merchant_user_id, String merchant_user_name, String role, boolean has_profile, String merchant_user_display_name, String claim_code, int order) {
        super(id);
        this.avatar_url = avatar_url;
        this.merchant_user_email_address = merchant_user_email_address;
        this.merchant_user_id = merchant_user_id;
        this.merchant_user_name = merchant_user_name;
        this.role = role;
        this.has_profile = has_profile;
        this.merchant_user_display_name = merchant_user_display_name;
        this.claim_code = claim_code;
    }

    /**
     * Get the customer's avatar.
     * @param width returns an image with the specified width in pixels.
     * @param height returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getAvatar(int width, int height, int quality) {
        return Tools_Preferabli.getImageUrl(avatar_url, width, height, quality);
    }

    String getAvatarUrl() {
        return avatar_url;
    }

    public String getMerchantUserDisplayName() {
        return merchant_user_display_name;
    }

    public String getClaimCode() {
        return claim_code;
    }

    public String getEmail() {
        return merchant_user_email_address;
    }

    public String getMerchantUserId() {
        return merchant_user_id;
    }

    public String getMerchantUserName() {
        return merchant_user_name;
    }

    public String getRole() {
        return role;
    }

    /**
     * Get a customer's display name.
     * @return the name as a string.
     */
    public String getName() {
        if (!Tools_Preferabli.isNullOrWhitespace(merchant_user_display_name)) {
            return merchant_user_display_name;
        } else if (!Tools_Preferabli.isNullOrWhitespace(merchant_user_name)) {
            return merchant_user_name;
        } else if (!Tools_Preferabli.isNullOrWhitespace(merchant_user_email_address)) {
            return merchant_user_email_address;
        } else if (!Tools_Preferabli.isNullOrWhitespace(merchant_user_id)) {
            return merchant_user_id;
        }

        return "";
    }

    public boolean hasProfile() {
        return has_profile;
    }

    @Override
    public boolean filter(Serializable serializable) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        innerloop:
        for (String term : terms) {
            if (getEmail() != null && getEmail().toLowerCase().contains(term))
                continue;
            else if (getMerchantUserId() != null && getMerchantUserId().toLowerCase().contains(term))
                continue;
            else if (getName() != null && getName().toLowerCase().contains(term))
                continue;

            return false;
        }
        return true;
    }

    public long getRatingsCollectionId() {
        return ratings_collection_id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.avatar_url);
        dest.writeString(this.merchant_user_email_address);
        dest.writeString(this.merchant_user_id);
        dest.writeString(this.merchant_user_name);
        dest.writeString(this.merchant_user_display_name);
        dest.writeString(this.claim_code);
        dest.writeString(this.role);
        dest.writeByte(this.has_profile ? (byte) 1 : (byte) 0);
        dest.writeLong(ratings_collection_id);
    }

    protected Object_Customer(Parcel in) {
        super(in);
        this.avatar_url = in.readString();
        this.merchant_user_email_address = in.readString();
        this.merchant_user_id = in.readString();
        this.merchant_user_name = in.readString();
        this.merchant_user_display_name = in.readString();
        this.claim_code = in.readString();
        this.role = in.readString();
        this.has_profile = in.readByte() != 0;
        this.ratings_collection_id = in.readLong();
    }

    public static final Parcelable.Creator<Object_Customer> CREATOR = new Parcelable.Creator<Object_Customer>() {
        @Override
        public Object_Customer createFromParcel(Parcel source) {
            return new Object_Customer(source);
        }

        @Override
        public Object_Customer[] newArray(int size) {
            return new Object_Customer[size];
        }
    };
}
