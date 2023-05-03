//
//  Object_LabelRecResults.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Container object returned by {@link Preferabli#labelRecognition(File, Boolean, API_ResultHandler)}.
 */
public class Object_LabelRecResults {
    private ArrayList<Object_LabelRecResult> results;
    private Object_Media media;

    public Object_LabelRecResults(Object_Media media, ArrayList<Object_LabelRecResult> results) {
        this.media = media;
        this.results = results;
    }

    public Object_Media getMedia() {
        return media;
    }

    public ArrayList<Object_LabelRecResult> getResults() {
        return results;
    }

    /**
     * The result container returned by {@link Preferabli#labelRecognition(File, Boolean, API_ResultHandler)}.
     */
    public static class Object_LabelRecResult implements Parcelable {

        private long product_id;
        /**
         * A score on a scale of 0 - 100  representing the degree of difference between the submitted image and the matching image. Results with higher scores ore more likely a matching {@link Object_Product}.
         */
        private double score;
        private Object_Product product;

        public double getScore() {
            return score;
        }

        public Object_Product getProduct() {
            return product;
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

        private static class LabelRecComparator implements Comparator<Object_LabelRecResult> {
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

        public static final Creator<Object_LabelRecResult> CREATOR = new Creator<Object_LabelRecResult>() {
            @Override
            public Object_LabelRecResult createFromParcel(Parcel source) {return new Object_LabelRecResult(source);}

            @Override
            public Object_LabelRecResult[] newArray(int size) {return new Object_LabelRecResult[size];}
        };
    }
}
