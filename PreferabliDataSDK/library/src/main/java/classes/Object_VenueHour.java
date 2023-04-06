//
//  Object_VenueHour.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Object_VenueHour extends Object_BaseObject {
    private String weekday;
    private String open_time;
    private String close_time;
    private boolean is_closed;

    public Object_VenueHour(long id, String weekday, String open_time, String close_time, boolean is_closed) {
        super(id);
        this.weekday = weekday;
        this.open_time = open_time;
        this.close_time = close_time;
        this.is_closed = is_closed;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getOpen_time() {
        if (open_time == null) {
            return "";
        }
        return open_time;
    }

    public String getClose_time() {
        if (close_time == null) {
            return "";
        }
        return close_time;
    }

    public boolean isIs_closed() {
        return is_closed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.weekday);
        dest.writeString(this.open_time);
        dest.writeString(this.close_time);
        dest.writeByte(this.is_closed ? (byte) 1 : (byte) 0);
    }

    public Object_VenueHour() {
    }

    protected Object_VenueHour(Parcel in) {
        super(in);
        this.weekday = in.readString();
        this.open_time = in.readString();
        this.close_time = in.readString();
        this.is_closed = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Object_VenueHour> CREATOR = new Parcelable.Creator<Object_VenueHour>() {
        @Override
        public Object_VenueHour createFromParcel(Parcel source) {
            return new Object_VenueHour(source);
        }

        @Override
        public Object_VenueHour[] newArray(int size) {
            return new Object_VenueHour[size];
        }
    };
}
