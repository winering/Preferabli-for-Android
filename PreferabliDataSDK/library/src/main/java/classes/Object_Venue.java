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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private ArrayList<Object_MerchantProductLink> lookups;
    private String notes;
    private ArrayList<DeliveryMethod> active_delivery_methods;
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

    private transient int distanceInMiles = -1;
    private transient ArrayList<Object_MerchantProductLink> lookupContainers;

    @Override
    public boolean filter(Serializable serializable) {
        return filter(serializable, true);
    }

    public boolean filter(Serializable serializable, boolean filterLookups) {
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
            } else if (getLookups() != null && filterLookups) {
                for (Object_MerchantProductLink lookup : getLookups()) {
                    if (lookup.filter(serializable, false))
                        continue innerloop;
                }
                return false;
            } else return false;
        }
        return true;
    }

    public String getUrl_facebook() {
        return url_facebook;
    }

    public String getUrl_instagram() {
        return url_instagram;
    }

    public String getUrl_twitter() {
        return url_twitter;
    }

    public String getUrl_youtube() {
        return url_youtube;
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


    public void setHours(ArrayList<Object_VenueHour> hours) {
        this.hours = hours;
    }

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

    public ArrayList<DeliveryMethod> getActive_delivery_methods() {
        if (active_delivery_methods == null) {
            active_delivery_methods = new ArrayList<>();
        }
        return active_delivery_methods;
    }

    public void removeImage(Object_Media media) {
        images.remove(media);
    }

    public ArrayList<Object_VenueHour> getVenueHours() {
        if (hours == null) {
            hours = new ArrayList<>();
        }
        return hours;
    }

    public boolean getIsClosed(String weekday) {
        for (Object_VenueHour hour : getVenueHours()) {
            if (hour.getWeekday().equalsIgnoreCase(weekday)) {
                return hour.isIs_closed();
            }
        }

        return false;
    }

    public String getOpenTime(String weekday) {
        for (Object_VenueHour hour : getVenueHours()) {
            if (hour.getWeekday().equalsIgnoreCase(weekday)) {
                return hour.getOpen_time();
            }
        }

        return "";
    }

    public String getCloseTime(String weekday) {
        for (Object_VenueHour hour : getVenueHours()) {
            if (hour.getWeekday().equalsIgnoreCase(weekday)) {
                return hour.getClose_time();
            }
        }

        return "";
    }

    public ArrayList<Object_MerchantProductLink> getLookups() {
        if (lookups == null) {
            lookups = new ArrayList<>();
        }

        return lookups;
    }

    public long getPrimary_inventory_id() {
        return primary_inventory_id;
    }

    public long getFeatured_collection_id() {
        return featured_collection_id;
    }

    public static class VenueComparator implements Comparator<Object_Venue> {

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

    public static ArrayList<Object_Venue> sortVenues(double lat1, double lon1, ArrayList<Object_Venue> venues) {
        Collections.sort(venues, new Object_Venue.VenueComparator(lat1, lon1));
        return venues;
    }

    public int getDistanceInMiles(double lat1, double lon1) {
        if (distanceInMiles == -1) {
            distanceInMiles = Tools_Preferabli.calculateDistanceInMiles(lat1, lon1, lat, lon);
        }

        return distanceInMiles;
    }

    public boolean hasLocalDelivery() {
        for (DeliveryMethod method : getActive_delivery_methods()) {
            if (method.getShipping_type().equalsIgnoreCase("local_delivery")) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPickup() {
        for (DeliveryMethod method : getActive_delivery_methods()) {
            if (method.getShipping_type().equalsIgnoreCase("pickup")) {
                return true;
            }
        }

        return false;
    }

    public boolean hasShipping() {
        for (DeliveryMethod method : getActive_delivery_methods()) {
            if (method.getShipping_type().equalsIgnoreCase("standard_shipping")) {
                return true;
            }
        }

        return false;
    }

    public String getShippingSpeedNote() {
        for (DeliveryMethod method : getActive_delivery_methods()) {
            if (method.getShipping_type().equalsIgnoreCase("standard_shipping")) {
                return method.getShipping_speed_note();
            }
        }

        return "";
    }

    public String getShippingCostNote() {
        for (DeliveryMethod method : getActive_delivery_methods()) {
            if (method.getShipping_type().equalsIgnoreCase("standard_shipping")) {
                return method.getShipping_cost_note();
            }
        }

        return "";
    }

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

    public boolean isIs_virtual() {
        return is_virtual;
    }

    public ArrayList<Object_Media> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    public String getEmail_address() {
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
    public int describeContents() {
        return 0;
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
        dest.writeTypedList(this.lookups);
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

    public Object_Venue() {
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
        this.lookups = in.createTypedArrayList(Object_MerchantProductLink.CREATOR);
        this.notes = in.readString();
        this.active_delivery_methods = in.createTypedArrayList(DeliveryMethod.CREATOR);
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

    public static class DeliveryMethod implements Parcelable {
        private String shipping_type;
        private String state_abbreviation;
        private String state_display_name;
        private String country;
        private String shipping_cost_note;
        private String shipping_speed_note;
        private long id;

        public String getShipping_type() {
            return shipping_type;
        }

        public void setShipping_type(String shipping_type) {
            this.shipping_type = shipping_type;
        }

        public String getState_abbreviation() {
            return state_abbreviation;
        }

        public void setState_abbreviation(String state_abbreviation) {
            this.state_abbreviation = state_abbreviation;
        }

        public String getState_display_name() {
            return state_display_name;
        }

        public void setState_display_name(String state_display_name) {
            this.state_display_name = state_display_name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getShipping_cost_note() {
            return shipping_cost_note;
        }

        public void setShipping_cost_note(String shipping_cost_note) {
            this.shipping_cost_note = shipping_cost_note;
        }

        public String getShipping_speed_note() {
            return shipping_speed_note;
        }

        public void setShipping_speed_note(String shipping_speed_note) {
            this.shipping_speed_note = shipping_speed_note;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.shipping_type);
            dest.writeString(this.state_abbreviation);
            dest.writeString(this.state_display_name);
            dest.writeString(this.country);
            dest.writeString(this.shipping_cost_note);
            dest.writeString(this.shipping_speed_note);
        }

        public DeliveryMethod() {
        }

        protected DeliveryMethod(Parcel in) {
            this.id = in.readLong();
            this.shipping_type = in.readString();
            this.state_abbreviation = in.readString();
            this.state_display_name = in.readString();
            this.country = in.readString();
            this.shipping_cost_note = in.readString();
            this.shipping_speed_note = in.readString();
        }

        public static final Parcelable.Creator<DeliveryMethod> CREATOR = new Parcelable.Creator<DeliveryMethod>() {
            @Override
            public DeliveryMethod createFromParcel(Parcel source) {
                return new DeliveryMethod(source);
            }

            @Override
            public DeliveryMethod[] newArray(int size) {
                return new DeliveryMethod[size];
            }
        };

    }
}
