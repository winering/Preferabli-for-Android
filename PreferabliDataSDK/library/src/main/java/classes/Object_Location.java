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
    private String zip_code;

    public Object_Location(long id, double latitude, double longitude) {
        super(id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Object_Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Object_Location(String zip_code) {
        this.zip_code = zip_code;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getZipCode() {
        return zip_code;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.zip_code);
    }

    protected Object_Location(Parcel in) {
        super(in);
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.zip_code = in.readString();
    }

    public static final Creator<Object_Location> CREATOR = new Creator<Object_Location>() {
        @Override
        public Object_Location createFromParcel(Parcel source) {return new Object_Location(source);}

        @Override
        public Object_Location[] newArray(int size) {return new Object_Location[size];}
    };
}
