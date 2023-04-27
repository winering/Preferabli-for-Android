//
//  Object_PreferabliUser.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/6/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

/**
 * A logged in Preferabli user. Most SDK installations will never use this.
 */
public class Object_PreferabliUser extends Object_BaseObject {

    private String display_name;
    private int account_level;
    private boolean enable_image_rec;
    private String email;
    private String fname;
    private String lname;
    private Object_Media avatar;
    private int birthyear;
    private String zip_code;
    private String country;
    private String gender;
    private boolean active;
    private boolean subscribed;
    private String created_at;
    private String location;
    private int ratings_count;
    private String updated_at;
    private int admin;
    private long avatar_id;
    private boolean has_merchant_access;
    private boolean has_personal_cellar;
    private boolean is_team_ringit;
    private String claim_code;
    private String use_user_claim_code;
    private boolean has_where_to_buy;
    private String intercom_hmac;
    private long rating_collection_id;
    private long wishlist_collection_id;

    public String getIntercomHmac() {
        return intercom_hmac;
    }

    public long getRatingCollectionId() {
        return rating_collection_id;
    }

    public long getWishlistCollectionId() {
        return wishlist_collection_id;
    }

    public String getClaimCode() {
        return claim_code;
    }

    public boolean isLocked() {
        return account_level != 2;
    }

    public Object_Media getAvatar() {
        return avatar;
    }

    public boolean hasWhereToBuy() {
        return has_where_to_buy;
    }

    public String getDisplayName() {
        return display_name;
    }

    public int getAccountLevel() {
        return account_level;
    }

    public boolean isEnableImageRec() {
        return enable_image_rec;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        if (Tools_Preferabli.isNullOrWhitespace(fname)) {
            return display_name;
        }
        return fname;
    }

    public String getFname() {
        return fname;
    }

    public String getLastName() {
        return lname;
    }

    public boolean isTeamRingIT() {
        return is_team_ringit;
    }

    /**
     * Get the user's avatar.
     * @param width returns an image with the specified width in pixels.
     * @param height returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getAvatar(int width, int height, int quality) {
        return Tools_Preferabli.getImageUrl(avatar, width, height, quality);
    }

    public String getLocation() {
        return location;
    }

    public int getBirthYear() {
        return birthyear;
    }

    public String getZipCode() {
        return zip_code;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public int getAdmin() {
        return admin;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.display_name);
        dest.writeInt(this.account_level);
        dest.writeByte(this.enable_image_rec ? (byte) 1 : (byte) 0);
        dest.writeString(this.email);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeParcelable(this.avatar, flags);
        dest.writeInt(this.birthyear);
        dest.writeString(this.zip_code);
        dest.writeString(this.country);
        dest.writeString(this.gender);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeByte(this.subscribed ? (byte) 1 : (byte) 0);
        dest.writeString(this.created_at);
        dest.writeString(this.location);
        dest.writeInt(this.ratings_count);
        dest.writeString(this.updated_at);
        dest.writeInt(this.admin);
        dest.writeLong(this.avatar_id);
        dest.writeByte(this.has_merchant_access ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_personal_cellar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_team_ringit ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_where_to_buy ? (byte) 1 : (byte) 0);
        dest.writeLong(this.rating_collection_id);
        dest.writeLong(this.wishlist_collection_id);
    }

    protected Object_PreferabliUser(Parcel in) {
        super(in);
        this.display_name = in.readString();
        this.account_level = in.readInt();
        this.enable_image_rec = in.readByte() != 0;
        this.email = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.avatar = in.readParcelable(Object_Media.class.getClassLoader());
        this.birthyear = in.readInt();
        this.zip_code = in.readString();
        this.country = in.readString();
        this.gender = in.readString();
        this.active = in.readByte() != 0;
        this.subscribed = in.readByte() != 0;
        this.created_at = in.readString();
        this.location = in.readString();
        this.ratings_count = in.readInt();
        this.updated_at = in.readString();
        this.admin = in.readInt();
        this.avatar_id = in.readLong();
        this.has_merchant_access = in.readByte() != 0;
        this.has_personal_cellar = in.readByte() != 0;
        this.is_team_ringit = in.readByte() != 0;
        this.has_where_to_buy = in.readByte() != 0;
        this.rating_collection_id = in.readLong();
        this.wishlist_collection_id = in.readLong();
    }

    public static final Creator<Object_PreferabliUser> CREATOR = new Creator<Object_PreferabliUser>() {
        @Override
        public Object_PreferabliUser createFromParcel(Parcel source) {
            return new Object_PreferabliUser(source);
        }

        @Override
        public Object_PreferabliUser[] newArray(int size) {
            return new Object_PreferabliUser[size];
        }
    };
}
