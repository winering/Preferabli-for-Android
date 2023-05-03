//
//  Object_Venue.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A venue represents the details for a specific location. If returned as part of {@link Preferabli#whereToBuy(long, Other_FulfillSort, Boolean, Boolean, API_ResultHandler)}, will contain an array of {@link Object_MerchantProductLink}s as {@link Object_Venue#links}.
 */
public class Object_Venue extends Object_BaseObject {
    private String name;
    private String display_name;
    private String address_l1;
    private String address_l2;
    private String city;
    private String state;
    private String zip_code;
    private String country;
    private double lat;
    private double lon;
    private String phone;
    private String url;
    private String created_at;
    private String updated_at;
    private String notes;
    private ArrayList<Object_DeliveryMethod> active_delivery_methods;
    private String email_address;
    private String url_facebook;
    private String url_instagram;
    private String url_twitter;
    private String url_youtube;
    private long primary_inventory_id;
    private long featured_collection_id;
    private ArrayList<Object_VenueHour> hours;
    private boolean is_virtual;
    private ArrayList<Object_Media> images;
    @SerializedName("lookups")
    private ArrayList<Object_MerchantProductLink> links;

    private transient int distanceInMiles = -1;

    @Override
    public boolean filter(Serializable serializable) {
        return filter(serializable, true);
    }

    public boolean filter(Serializable serializable, boolean filter_links) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        innerloop:
        for (String term : terms) {
            if (getDisplayName() != null && getDisplayName().toLowerCase().contains(term)) {
                continue;
            } else if (getAddressLine1() != null && getAddressLine1().toLowerCase().contains(term)) {
                continue;
            } else if (getAddressLine2() != null && getAddressLine2().toLowerCase().contains(term)) {
                continue;
            } else if (getZipCode() != null && getZipCode().toLowerCase().contains(term)) {
                continue;
            } else if (getCountry() != null && getCountry().toLowerCase().contains(term)) {
                continue;
            } else if (getCity() != null && getCity().toLowerCase().contains(term)) {
                continue;
            } else if (getState() != null && getState().toLowerCase().contains(term)) {
                continue;
            } else if (getUrl() != null && getUrl().toLowerCase().contains(term)) {
                continue;
            } else if (getLinks() != null && filter_links) {
                for (Object_MerchantProductLink lookup : getLinks()) {
                    if (lookup.filter(serializable, false))
                        continue innerloop;
                }
                return false;
            }

            return false;
        }
        return true;
    }

    public String getYoutubeLink() {
        return "https://www.youtube.com/" + url_youtube;
    }

    public String getFacebookLink() {
        return "https://www.facebook.com/" + url_facebook;
    }

    public String getInstagramLink() {
        return "https://www.instagram.com/" + url_instagram;
    }

    public String getTwitterLink() {
        return "https://www.twitter.com/" + url_twitter;
    }

    public String getNotes() {
        return notes;
    }

    /**
     * Get a formatted return of the address for a specific venue.
     * @param oneLine pass true if you want the address returned in one line. False returns the address in a multiline format.
     * @return the venue's full formatted address.
     */
    public String getFormattedAddress(boolean oneLine) {
        String newLine = "\n";
        if (oneLine) {
            newLine = " | ";
        }
        String firstTwo = (Tools_Preferabli.isNullOrWhitespace(address_l1) ? "" : (address_l1 + newLine)) + (Tools_Preferabli.isNullOrWhitespace(address_l2) ? "" : (address_l2 + newLine));
        String third = Tools_Preferabli.isNullOrWhitespace(city) ? "" : (city + ", ");
        third = third + (Tools_Preferabli.isNullOrWhitespace(state) ? "" : state) + " " + (Tools_Preferabli.isNullOrWhitespace(zip_code) ? "" : zip_code);
        String fourth = Tools_Preferabli.isNullOrWhitespace(country) ? "" : (" " + country);
        return firstTwo + third + fourth;
    }

    public ArrayList<Object_DeliveryMethod> getActiveDeliveryMethods() {
        if (active_delivery_methods == null) {
            active_delivery_methods = new ArrayList<>();
        }
        return active_delivery_methods;
    }

    public ArrayList<Object_VenueHour> getHours() {
        if (hours == null) {
            hours = new ArrayList<>();
        }
        return hours;
    }

    public boolean getIsClosed(Other_Weekday weekday) {
        for (Object_VenueHour hour : getHours()) {
            if (hour.getWeekday().equals(weekday)) {
                return hour.isClosed();
            }
        }

        return false;
    }

    public String getOpenTime(Other_Weekday weekday) {
        for (Object_VenueHour hour : getHours()) {
            if (hour.getWeekday().equals(weekday)) {
                return hour.getOpenTime();
            }
        }

        return "";
    }

    public String getCloseTime(Other_Weekday weekday) {
        for (Object_VenueHour hour : getHours()) {
            if (hour.getWeekday().equals(weekday)) {
                return hour.getCloseTime();
            }
        }

        return "";
    }

    public ArrayList<Object_MerchantProductLink> getLinks() {
        if (links == null) {
            links = new ArrayList<>();
        }

        return links;
    }

    public long getPrimaryInventoryId() {
        return primary_inventory_id;
    }

    public long getFeaturedCollectionId() {
        return featured_collection_id;
    }

    private static class VenueComparator implements Comparator<Object_Venue> {

        double lat1;
        double lon1;

        public VenueComparator(double lat1, double lon1) {
            this.lat1 = lat1;
            this.lon1 = lon1;
        }

        @Override
        public int compare(Object_Venue venue1, Object_Venue venue2) {
            if (venue1 == null && venue2 == null) {
                return 0;
            } else if (venue1 == null) {
                return 1;
            } else if (venue2 == null) {
                return -1;
            }

            if (venue1.getDistanceInMiles(lat1, lon1) == venue2.getDistanceInMiles(lat1, lon1)) {
                return Tools_Preferabli.alphaSortIgnoreThe(venue1.getName(), venue2.getName());
            }

            return Integer.compare(venue1.getDistanceInMiles(lat1, lon1), venue2.getDistanceInMiles(lat1, lon1));
        }
    }

    /**
     * Sort an array of venues by their distance from the user.
     * @param lat1 user's latitude as a double.
     * @param lon1 user's longitude as a double.
     * @param venues an array of venues to be sorted.
     * @return a sorted array of venues.
     */
    public static ArrayList<Object_Venue> sortVenuesByDistance(double lat1, double lon1, ArrayList<Object_Venue> venues) {
        Collections.sort(venues, new Object_Venue.VenueComparator(lat1, lon1));
        return venues;
    }

    /**
     * Get your distance in miles to the venue.
     * @param lat1 user's latitude as a double.
     * @param lon1 user's longitude as a double.
     * @return user's distance to the venue in miles.
     */
    public int getDistanceInMiles(double lat1, double lon1) {
        if (distanceInMiles == -1) {
            distanceInMiles = Tools_Preferabli.calculateDistanceInMiles(lat1, lon1, lat, lon);
        }

        return distanceInMiles;
    }

    /**
     * Does the venue offer local delivery?
     * @return true if the venue can deliver locally to the user.
     */
    public boolean hasLocalDelivery() {
        for (Object_DeliveryMethod method : getActiveDeliveryMethods()) {
            if (method.getShippingType().equals(Other_ShippingType.LOCAL_DELIVERY)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Does the venue offer local pickup?
     * @return true if the user can pickup at the venue.
     */
    public boolean hasPickup() {
        for (Object_DeliveryMethod method : getActiveDeliveryMethods()) {
            if (method.getShippingType().equals(Other_ShippingType.PICKUP)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Does the venue offer shipping
     * @return true if the venue can ship to the user.
     */
    public boolean hasShipping() {
        for (Object_DeliveryMethod method : getActiveDeliveryMethods()) {
            if (method.getShippingType().equals(Other_ShippingType.SHIPPING)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the venue's shipping speed.
     * @return the venue's notes about shipping speed.
     */
    public String getShippingSpeedNote() {
        for (Object_DeliveryMethod method : getActiveDeliveryMethods()) {
            if (method.getShippingType().equals(Other_ShippingType.SHIPPING)) {
                return method.getShippingSpeedNote();
            }
        }

        return "";
    }

    /**
     * Get the venue's shipping cost.
     * @return the venue's notes about its shipping cost.
     */
    public String getShippingCostNote() {
        for (Object_DeliveryMethod method : getActiveDeliveryMethods()) {
            if (method.getShippingType().equals(Other_ShippingType.SHIPPING)) {
                return method.getShippingCostNote();
            }
        }

        return "";
    }

    /**
     * Get the venue's city and state.
     * @return city, state, city and state, or a blank string depending on the information available.
     */
    public String getCityState() {
        if (Tools_Preferabli.isNullOrWhitespace(city + state)) {
            return "";
        } else if (Tools_Preferabli.isNullOrWhitespace(city)) {
            return state;
        } else if (Tools_Preferabli.isNullOrWhitespace(state)) {
            return city;
        } else {
            return city+ ", " + state;
        }
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getAddressLine1() {
        return address_l1;
    }

    public String getAddressLine2() {
        return address_l2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zip_code;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return lat;
    }

    public boolean isVirtual() {
        return is_virtual;
    }

    public ArrayList<Object_Media> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    public String getEmailAddress() {
        return email_address;
    }

    public double getLon() {
        return lon;
    }

    public String getPhone() {
        return phone;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.display_name);
        dest.writeString(this.address_l1);
        dest.writeString(this.address_l2);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.zip_code);
        dest.writeString(this.country);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.phone);
        dest.writeString(this.url);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeTypedList(this.links);
        dest.writeString(this.notes);
        dest.writeTypedList(this.active_delivery_methods);
        dest.writeString(this.email_address);
        dest.writeLong(this.primary_inventory_id);
        dest.writeLong(this.featured_collection_id);
        dest.writeTypedList(this.hours);
        dest.writeByte(this.is_virtual ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.images);
        dest.writeString(this.url_facebook);
        dest.writeString(this.url_instagram);
        dest.writeString(this.url_twitter);
        dest.writeString(this.url_youtube);
    }

    protected Object_Venue(Parcel in) {
        super(in);
        this.name = in.readString();
        this.display_name = in.readString();
        this.address_l1 = in.readString();
        this.address_l2 = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.zip_code = in.readString();
        this.country = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.phone = in.readString();
        this.url = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.links = in.createTypedArrayList(Object_MerchantProductLink.CREATOR);
        this.notes = in.readString();
        this.active_delivery_methods = in.createTypedArrayList(Object_DeliveryMethod.CREATOR);
        this.email_address = in.readString();
        this.primary_inventory_id = in.readLong();
        this.featured_collection_id = in.readLong();
        this.hours = in.createTypedArrayList(Object_VenueHour.CREATOR);
        this.is_virtual = in.readByte() != 0;
        this.images = in.createTypedArrayList(Object_Media.CREATOR);
        this.url_facebook = in.readString();
        this.url_instagram = in.readString();
        this.url_twitter = in.readString();
        this.url_youtube = in.readString();
    }

    public static final Parcelable.Creator<Object_Venue> CREATOR = new Parcelable.Creator<Object_Venue>() {
        @Override
        public Object_Venue createFromParcel(Parcel source) {
            return new Object_Venue(source);
        }

        @Override
        public Object_Venue[] newArray(int size) {
            return new Object_Venue[size];
        }
    };

    /**
     * Indicates that a {@link Object_Venue} provides a specified delivery method ({@link Other_ShippingType}).
     */
    public static class Object_DeliveryMethod extends Object_BaseObject {
        private String shipping_type;
        private String state_abbreviation;
        private String state_display_name;
        private String country;
        private String shipping_cost_note;
        private String shipping_speed_note;

        public Other_ShippingType getShippingType() {
            return Other_ShippingType.getShippingTypeBasedOffDatabaseName(shipping_type);
        }

        public String getStateAbbreviation() {
            return state_abbreviation;
        }

        public String getStateDisplayName() {
            return state_display_name;
        }

        public String getCountry() {
            return country;
        }

        public String getShippingCostNote() {
            return shipping_cost_note;
        }

        public String getShippingSpeedNote() {
            return shipping_speed_note;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.shipping_type);
            dest.writeString(this.state_abbreviation);
            dest.writeString(this.state_display_name);
            dest.writeString(this.country);
            dest.writeString(this.shipping_cost_note);
            dest.writeString(this.shipping_speed_note);
        }

        protected Object_DeliveryMethod(Parcel in) {
            super(in);
            this.shipping_type = in.readString();
            this.state_abbreviation = in.readString();
            this.state_display_name = in.readString();
            this.country = in.readString();
            this.shipping_cost_note = in.readString();
            this.shipping_speed_note = in.readString();
        }

        public static final Parcelable.Creator<Object_DeliveryMethod> CREATOR = new Parcelable.Creator<Object_DeliveryMethod>() {
            @Override
            public Object_DeliveryMethod createFromParcel(Parcel source) {
                return new Object_DeliveryMethod(source);
            }

            @Override
            public Object_DeliveryMethod[] newArray(int size) {
                return new Object_DeliveryMethod[size];
            }
        };

    }

    /**
     * Represents a {@link Object_Venue}s open and close times for a given day. Each day of the week has separate corresponding values.
     */
    public static class Object_VenueHour extends Object_BaseObject {
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

        public Other_Weekday getWeekday() {
            return Other_Weekday.getWeekdayFromString(weekday);
        }

        public String getOpenTime() {
            if (open_time == null) {
                return "";
            }
            return open_time;
        }

        public String getCloseTime() {
            if (close_time == null) {
                return "";
            }
            return close_time;
        }

        public boolean isClosed() {
            return is_closed;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.weekday);
            dest.writeString(this.open_time);
            dest.writeString(this.close_time);
            dest.writeByte(this.is_closed ? (byte) 1 : (byte) 0);
        }

        protected Object_VenueHour(Parcel in) {
            super(in);
            this.weekday = in.readString();
            this.open_time = in.readString();
            this.close_time = in.readString();
            this.is_closed = in.readByte() != 0;
        }

        public static final Creator<Object_VenueHour> CREATOR = new Creator<Object_VenueHour>() {
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

    /**
     * Represents a fulfillment method for a {@link Object_Venue}. Contained within {@link Object_DeliveryMethod}.
     */
    public enum Other_ShippingType {
        SHIPPING,
        LOCAL_DELIVERY,
        PICKUP;

        public static Other_ShippingType getShippingTypeBasedOffDatabaseName(String type) {
            if (type != null) switch (type) {
                case "standard_shipping":
                    return Other_ShippingType.SHIPPING;
                case "local_delivery":
                    return Other_ShippingType.LOCAL_DELIVERY;
                case "pickup":
                    return Other_ShippingType.PICKUP;
            }

            return null;
        }

        public String getDatabaseName() {
            switch (this) {
                case SHIPPING:
                    return "standard_shipping";
                case LOCAL_DELIVERY:
                    return "local_delivery";
                case PICKUP:
                    return "pickup";
            }

            return null;
        }

    }

    /**
     * Represents a day of the week for use within {@link Object_VenueHour}.
     */
    public enum Other_Weekday {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY,
        NONE;

        public static Other_Weekday getWeekdayFromString(String weekday) {
            if (weekday != null) switch (weekday) {
                case "monday":
                    return MONDAY;
                case "tuesday":
                    return TUESDAY;
                case "wednesday":
                    return WEDNESDAY;
                case "thursday":
                    return THURSDAY;
                case "friday":
                    return FRIDAY;
                case "saturday":
                    return SATURDAY;
                case "sunday":
                    return SUNDAY;
                default:
                    return NONE;
            }

            return NONE;
        }

        public String getStringFromWeekday() {
            switch (this) {
                case MONDAY:
                    return "monday";
                case TUESDAY:
                    return "tuesday";
                case WEDNESDAY:
                    return "wednesday";
                case THURSDAY:
                    return "thursday";
                case FRIDAY:
                    return "friday";
                case SATURDAY:
                    return "saturday";
                case SUNDAY:
                    return "sunday";
                case NONE:
                    return "none";
            }

            return "none";
        }
    }
}
