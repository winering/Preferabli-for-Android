//
//  Other_FulfillSort.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public class Other_FulfillSort extends Other_Sort {

    private boolean shipping;
    private boolean delivery;
    private boolean pickup;
    private int variant_year;
    private int distance_miles;
    private Object_Location location;

    public Other_FulfillSort(SortType type) {
        super(type, true);
        this.variant_year = Object_Variant.NON_VARIANT;
        this.shipping = true;
        this.delivery = true;
        this.pickup = true;
        this.distance_miles = 75;
    }

    public Object_Location getLocation() {
        return location;
    }

    public void setLocation(Object_Location location) {
        this.location = location;
    }

    public int getDistanceMiles() {
        return distance_miles;
    }

    public void setDistanceMiles(int distance_miles) {
        this.distance_miles = distance_miles;
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

    public int getVariantYear() {
        return variant_year;
    }

    public void setVariant_year(int variant_year) {
        this.variant_year = variant_year;
    }

    public static Other_FulfillSort getDefaultFulfillSort() {
        Other_FulfillSort whereToBuySortType = new Other_FulfillSort(SortType.PRICE);
        return whereToBuySortType;
    }

    public boolean isDefault() {
        return getType() == SortType.PRICE && isAscending();
    }
}