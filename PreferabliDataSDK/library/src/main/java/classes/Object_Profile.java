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
    private long id;
    private long user_id;
    private int score;
    private ArrayList<Object_ProfileStyle> preference_styles;
    private String created_at;
    private String updated_at;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return user_id;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Object_ProfileStyle> getPreferenceStyles() {
        return preference_styles;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.id);
        dest.writeLong(this.user_id);
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
        this.score = in.readInt();
        this.preference_styles = in.createTypedArrayList(Object_ProfileStyle.CREATOR);
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }
}
