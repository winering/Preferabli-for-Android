//
//  Object_LabelRecResult.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object_LabelRecResult implements Parcelable {

    private long product_id;
    private double score;
    private Object_Product product;

    private transient boolean isChecked;

    public double getScore() {
        return score;
    }

    public Object_Product getWine() {
        return product;
    }

    public Object_LabelRecResult(Object_Product product) {
        setWine(product);
    }

    public void setWine(Object_Product product) {
        this.product = product;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Object_LabelRecResult imageRec = (Object_LabelRecResult) o;

        return product_id == imageRec.product_id;
    }

    @Override
    public int hashCode() {
        return (int) (product_id ^ (product_id >>> 32));
    }

    public static ArrayList<Object_LabelRecResult> sortLabelRecs(ArrayList<Object_LabelRecResult> imageRecs) {
        Collections.sort(imageRecs, new LabelRecComparator());
        return imageRecs;
    }

    public static class LabelRecComparator implements Comparator<Object_LabelRecResult> {
        @Override
        public int compare(Object_LabelRecResult labelRec1, Object_LabelRecResult labelRec2) {
            if (labelRec1 == null && labelRec2 == null) {
                return 0;
            } else if (labelRec1 == null) {
                return 1;
            } else if (labelRec2 == null) {
                return -1;
            } else {
                return (int) (labelRec1.getScore() - labelRec2.getScore());
            }
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.product_id);
        dest.writeDouble(this.score);
        dest.writeParcelable(this.product, flags);
    }

    protected Object_LabelRecResult(Parcel in) {
        this.product_id = in.readLong();
        this.score = in.readDouble();
        this.product = in.readParcelable(Object_Product.class.getClassLoader());
    }

    public static final Parcelable.Creator<Object_LabelRecResult> CREATOR = new Parcelable.Creator<Object_LabelRecResult>() {
        @Override
        public Object_LabelRecResult createFromParcel(Parcel source) {return new Object_LabelRecResult(source);}

        @Override
        public Object_LabelRecResult[] newArray(int size) {return new Object_LabelRecResult[size];}
    };
}
