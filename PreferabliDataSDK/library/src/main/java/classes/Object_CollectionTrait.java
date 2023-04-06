//
//  Object_CollectionTrait.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object_CollectionTrait extends Object_BaseObject {
    private int order;
    private String name;
    private String created_at;
    private String updated_at;
    private boolean restrict_to_ring_it;

    public Object_CollectionTrait(String name) {
        this.name = name;
    }

    public Object_CollectionTrait(long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public boolean isRestrictToRingIT() {
        return restrict_to_ring_it;
    }

    public Object_CollectionTrait() {}

    public static class TraitComparator implements Comparator<Object_CollectionTrait> {
        @Override
        public int compare(Object_CollectionTrait trait1, Object_CollectionTrait trait2) {
            if (trait1 == null && trait2 == null) {
                return 0;
            } else if (trait1 == null) {
                return 1;
            } else if (trait2 == null) {
                return -1;
            } else if (trait1.getOrder() == trait2.getOrder())
                return trait1.getName().compareToIgnoreCase(trait2.getName());
            return ((Integer) trait1.getOrder()).compareTo(trait2.getOrder());
        }
    }

    public static ArrayList<Object_CollectionTrait> sortTraits(ArrayList<Object_CollectionTrait> traits) {
        Collections.sort(traits, new TraitComparator());
        return traits;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.order);
        dest.writeString(this.name);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(this.restrict_to_ring_it ? (byte) 1 : (byte) 0);
    }

    protected Object_CollectionTrait(Parcel in) {
        super(in);
        this.order = in.readInt();
        this.name = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.restrict_to_ring_it = in.readByte() != 0;
    }

    public static final Creator<Object_CollectionTrait> CREATOR = new Creator<Object_CollectionTrait>() {
        @Override
        public Object_CollectionTrait createFromParcel(Parcel source) {return new Object_CollectionTrait(source);}

        @Override
        public Object_CollectionTrait[] newArray(int size) {return new Object_CollectionTrait[size];}
    };
}
