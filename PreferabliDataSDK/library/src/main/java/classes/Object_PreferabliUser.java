//
//  Object_PreferabliUser.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/6/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

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
    private String password;
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

    public String getIntercom_hmac() {
        return intercom_hmac;
    }

    public long getRating_collection_id() {
        return rating_collection_id;
    }

    public long getWishlist_collection_id() {
        return wishlist_collection_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClaimCode(String claim_code) {
        this.use_user_claim_code = claim_code;
    }

    public String getClaimCode() {
        return claim_code;
    }

    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }

    public void setAccountLevel(int account_level) {
        this.account_level = account_level;
    }

    public boolean isLocked() {
        return account_level != 2;
    }

    public void setEnableImageRec(boolean enable_image_rec) {
        this.enable_image_rec = enable_image_rec;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst(String fname) {
        this.fname = fname;
    }

    public void setLast(String lname) {
        this.lname = lname;
    }

    public boolean isHas_where_to_buy() {
        return has_where_to_buy;
    }

    public void setHas_where_to_buy(boolean has_where_to_buy) {
        this.has_where_to_buy = has_where_to_buy;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public void setZipCode(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRatingsCount(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
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
        if (Tools_PreferabliTools.isNullOrWhitespace(fname)) {
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

    public void setIsTeamRingIT(boolean is_team_ringit) {
        this.is_team_ringit = is_team_ringit;
    }

    public String getAvatar() {
        if (avatar != null && !avatar.getPath().contains("connections_user_placeholder")) return avatar.getPath();
        return null;
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
        dest.writeString(this.password);
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
        this.password = in.readString();
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
