//
//  Object_Profile.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import java.util.ArrayList;

public class Object_Profile extends Object_BaseObject {
    private long user_id;
    private long customer_id;
    private int score;
    private ArrayList<Object_ProfileStyle> preference_styles;
    private String created_at;
    private String updated_at;

    public Object_Profile(ArrayList<Object_ProfileStyle> profile_styles) {
        if (Tools_Preferabli.isPreferabliUserLoggedIn()) {
            this.user_id = Tools_Preferabli.getPreferabliUserId();
        } else {
            this.customer_id = Tools_Preferabli.getCustomerId();
        }
        this.preference_styles = profile_styles;
    }

    public long getUserId() {
        return user_id;
    }

    public long getCustomerId() {
        return customer_id;
    }

    public ArrayList<Object_ProfileStyle> getProfileStyles() {
        return preference_styles;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.user_id);
        dest.writeLong(this.customer_id);
        dest.writeInt(this.score);
        dest.writeTypedList(this.preference_styles);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public Object_Profile() {
    }

    protected Object_Profile(Parcel in) {
        super(in);
        this.user_id = in.readLong();
        this.customer_id = in.readLong();
        this.score = in.readInt();
        this.preference_styles = in.createTypedArrayList(Object_ProfileStyle.CREATOR);
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }
}
