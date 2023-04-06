package classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class Object_BaseObject implements Parcelable {
    private long id;

    protected Object_BaseObject() {
        // Nothing to see here.
    }

    protected Object_BaseObject(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Object_BaseObject other = (Object_BaseObject) o;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
    }

    public Object_BaseObject(Parcel in) {
        this.id = in.readLong();
    }

    public static final Creator<Object_BaseObject> CREATOR = new Creator<Object_BaseObject>() {
        @Override
        public Object_BaseObject createFromParcel(Parcel source) {return new Object_BaseObject(source);}

        @Override
        public Object_BaseObject[] newArray(int size) {return new Object_BaseObject[size];}
    };

    public boolean filter(Serializable serializable) {
        return true;
    }
}
