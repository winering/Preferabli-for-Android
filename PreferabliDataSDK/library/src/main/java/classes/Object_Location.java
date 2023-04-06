//
//  Object_Location.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Object_Location extends Object_BaseObject {
    private double latitude;
    private double longitude;

    public Object_Location(long id, double latitude, double longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected Object_Location(Parcel in) {
        super(in);
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<Object_Location> CREATOR = new Creator<Object_Location>() {
        @Override
        public Object_Location createFromParcel(Parcel source) {return new Object_Location(source);}

        @Override
        public Object_Location[] newArray(int size) {return new Object_Location[size];}
    };
}
