//
//  Object_Customer.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object_Customer extends Object_BaseObject {
    private String avatar_url;
    private String merchant_user_email_address;
    private String merchant_user_id;
    private String merchant_user_name;
    private String merchant_user_display_name;
    private String claim_code;
    private String role;
    private int order;
    private boolean has_profile;
    private long ratings_collection_id;

    public Object_Customer(Object_Customer customer) {
        super(customer.getId());
        this.avatar_url = customer.getAvatar_url();
        this.merchant_user_email_address = customer.getEmail();
        this.merchant_user_id = customer.getMerchant_user_id();
        this.merchant_user_name = customer.getMerchant_user_name();
        this.role = customer.getRole();
        this.has_profile = customer.isHas_profile();
        this.merchant_user_display_name = customer.getMerchant_user_display_name();
        this.claim_code = customer.getClaim_code();
        this.order = customer.getOrder();
    }

    public Object_Customer(long id, String avatar_url, String merchant_user_email_address, String merchant_user_id, String merchant_user_name, String role, boolean has_profile, String merchant_user_display_name, String claim_code, int order) {
        super(id);
        this.avatar_url = avatar_url;
        this.merchant_user_email_address = merchant_user_email_address;
        this.merchant_user_id = merchant_user_id;
        this.merchant_user_name = merchant_user_name;
        this.role = role;
        this.has_profile = has_profile;
        this.merchant_user_display_name = merchant_user_display_name;
        this.claim_code = claim_code;
        this.order = order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setMerchant_user_display_name(String merchant_user_display_name) {
        this.merchant_user_display_name = merchant_user_display_name;
    }

    public String getMerchant_user_display_name() {
        return merchant_user_display_name;
    }

    public String getClaim_code() {
        return claim_code;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getEmail() {
        return merchant_user_email_address;
    }

    public String getMerchant_user_id() {
        return merchant_user_id;
    }

    public String getMerchant_user_name() {
        return merchant_user_name;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        if (!Tools_PreferabliTools.isNullOrWhitespace(merchant_user_display_name)) {
            return merchant_user_display_name;
        } else if (!Tools_PreferabliTools.isNullOrWhitespace(merchant_user_name)) {
            return merchant_user_name;
        } else if (!Tools_PreferabliTools.isNullOrWhitespace(merchant_user_email_address)) {
            return merchant_user_email_address;
        } else if (!Tools_PreferabliTools.isNullOrWhitespace(merchant_user_id)) {
            return merchant_user_id;
        }

        return "";
    }

    public String getMerchantName() {
        if (!Tools_PreferabliTools.isNullOrWhitespace(merchant_user_name)) {
            return merchant_user_name;
        } else if (!Tools_PreferabliTools.isNullOrWhitespace(merchant_user_email_address)) {
            return merchant_user_email_address;
        }

        return "";
    }

    public int getOrder() {
        return order;
    }

    public static class CustomerComparator implements Comparator<Object_Customer> {
        @Override
        public int compare(Object_Customer customer1, Object_Customer customer2) {
            if (customer1 == null && customer2 == null) {
                return 0;
            } else if (customer1 == null) {
                return 1;
            } else if (customer2 == null) {
                return -1;
            }

            return Tools_PreferabliTools.alphaSortIgnoreThe(customer1.getName(), customer2.getName());
        }
    }

    public static class CustomerOrderComparator implements Comparator<Object_Customer> {
        @Override
        public int compare(Object_Customer customer1, Object_Customer customer2) {
            if (customer1 == null && customer2 == null) {
                return 0;
            } else if (customer1 == null) {
                return 1;
            } else if (customer2 == null) {
                return -1;
            }

            return ((Integer) customer1.getOrder()).compareTo(customer2.getOrder());
        }
    }

    public static ArrayList<Object_Customer> sortCustomers(ArrayList<Object_Customer> customers) {
        Collections.sort(customers, new CustomerComparator());
        return customers;
    }

    public static ArrayList<Object_Customer> sortCustomersByOrder(ArrayList<Object_Customer> customers) {
        Collections.sort(customers, new CustomerOrderComparator());
        return customers;
    }

    public boolean isHas_profile() {
        return has_profile;
    }

    @Override
    public boolean filter(Serializable serializable) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        innerloop:
        for (String term : terms) {
            if (getEmail() != null && getEmail().toLowerCase().contains(term))
                continue;
            else if (getMerchant_user_id() != null && getMerchant_user_id().toLowerCase().contains(term))
                continue;
            else if (getMerchant_user_name() != null && getMerchant_user_name().toLowerCase().contains(term))
                continue;
            else return false;
        }
        return true;
    }

    public long getRatingsCollectionId() {
        return ratings_collection_id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.avatar_url);
        dest.writeString(this.merchant_user_email_address);
        dest.writeString(this.merchant_user_id);
        dest.writeString(this.merchant_user_name);
        dest.writeString(this.merchant_user_display_name);
        dest.writeString(this.claim_code);
        dest.writeString(this.role);
        dest.writeInt(this.order);
        dest.writeByte(this.has_profile ? (byte) 1 : (byte) 0);
        dest.writeLong(ratings_collection_id);
    }

    protected Object_Customer(Parcel in) {
        super(in);
        this.avatar_url = in.readString();
        this.merchant_user_email_address = in.readString();
        this.merchant_user_id = in.readString();
        this.merchant_user_name = in.readString();
        this.merchant_user_display_name = in.readString();
        this.claim_code = in.readString();
        this.role = in.readString();
        this.order = in.readInt();
        this.has_profile = in.readByte() != 0;
        this.ratings_collection_id = in.readLong();
    }

    public static final Parcelable.Creator<Object_Customer> CREATOR = new Parcelable.Creator<Object_Customer>() {
        @Override
        public Object_Customer createFromParcel(Parcel source) {
            return new Object_Customer(source);
        }

        @Override
        public Object_Customer[] newArray(int size) {
            return new Object_Customer[size];
        }
    };
}
