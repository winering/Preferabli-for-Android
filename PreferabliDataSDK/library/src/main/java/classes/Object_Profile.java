//
//  Object_Profile.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * A user's preference profile represents appealing and unappealing {@link Object_ProfileStyle}s for a particular user.
 */
public class Object_Profile extends Object_BaseObject {
    private long user_id;
    private long customer_id;
    private int score;
    @SerializedName("preference_styles")
    private ArrayList<Object_ProfileStyle> profile_styles;
    private String created_at;
    private String updated_at;

    public Object_Profile(ArrayList<Object_ProfileStyle> profile_styles) {
        if (Tools_Preferabli.isPreferabliUserLoggedIn()) {
            this.user_id = Tools_Preferabli.getPreferabliUserId();
        } else {
            this.customer_id = Tools_Preferabli.getCustomerId();
        }
        this.profile_styles = profile_styles;
    }

    public long getUserId() {
        return user_id;
    }

    public long getCustomerId() {
        return customer_id;
    }

    public ArrayList<Object_ProfileStyle> getProfileStyles() {
        return profile_styles;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.user_id);
        dest.writeLong(this.customer_id);
        dest.writeInt(this.score);
        dest.writeTypedList(this.profile_styles);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected Object_Profile(Parcel in) {
        super(in);
        this.user_id = in.readLong();
        this.customer_id = in.readLong();
        this.score = in.readInt();
        this.profile_styles = in.createTypedArrayList(Object_ProfileStyle.CREATOR);
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<Object_Profile> CREATOR = new Creator<Object_Profile>() {
        @Override
        public Object_Profile createFromParcel(Parcel source) {
            return new Object_Profile(source);
        }

        @Override
        public Object_Profile[] newArray(int size) {
            return new Object_Profile[size];
        }
    };
}
