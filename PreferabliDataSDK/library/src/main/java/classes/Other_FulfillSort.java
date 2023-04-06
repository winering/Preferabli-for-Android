//
//  Other_FulfillSort.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import com.ringit.datasdk.R;

public class Other_FulfillSort implements Parcelable {

    private boolean shipping;
    private boolean delivery;
    private boolean pickup;
    private boolean ascending;
    private int variant_year;
    private String type;

    public Other_FulfillSort(String type) {
        this.type = type;
        this.variant_year = -1;
        this.shipping = true;
        this.delivery = true;
        this.pickup = true;
        this.ascending = true;
    }

    public boolean isShipping() {
        return shipping;
    }

    public void setShipping(boolean shipping) {
        this.shipping = shipping;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public int getVariant_year() {
        return variant_year;
    }

    public void setVariant_year(int variant_year) {
        this.variant_year = variant_year;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Other_FulfillSort getDefaultWhereToBuySortType() {
        Other_FulfillSort whereToBuySortType = new Other_FulfillSort("price");
        whereToBuySortType.setAscending(true);
        whereToBuySortType.setShipping(true);
        whereToBuySortType.setDelivery(true);
        whereToBuySortType.setPickup(true);
        whereToBuySortType.setVariant_year(-1);
        return whereToBuySortType;
    }

    public boolean isDefault(boolean filter) {
        if (filter) {
            return isShipping() && isDelivery() && isPickup() && getVariant_year() == -1;
        }
        return type == "price" && isAscending();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeInt(this.variant_year);
        dest.writeInt(this.pickup ? 1 : 0);
        dest.writeInt(this.delivery ? 1 : 0);
        dest.writeInt(this.shipping ? 1 : 0);
        dest.writeInt(this.ascending ? 1 : 0);
    }

    public Other_FulfillSort() {
    }

    protected Other_FulfillSort(Parcel in) {
        this.type = in.readString();
        this.variant_year = in.readInt();
        this.pickup = in.readInt() == 1;
        this.delivery = in.readInt() == 1;
        this.shipping = in.readInt() == 1;
        this.ascending = in.readInt() == 1;
    }

    public static final Parcelable.Creator<Other_FulfillSort> CREATOR = new Parcelable.Creator<Other_FulfillSort>() {
        @Override
        public Other_FulfillSort createFromParcel(Parcel source) {
            return new Other_FulfillSort(source);
        }

        @Override
        public Other_FulfillSort[] newArray(int size) {
            return new Other_FulfillSort[size];
        }
    };
}