//
//  Object_Style.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Styles express how product characteristics synthesize in the context of human perception and define the nature of consumer taste preferences. These are not unique for each customer, which are represented as {@link Object_ProfileStyle}.
 */
public class Object_Style extends Object_BaseObject {
    private String name;
    private String type;
    private String product_category;
    private String description;
    private int order;
    private ArrayList<Object_Food> foods;
    private String primary_image_url;
    private String created_at;
    private String updated_at;
    private ArrayList<Object_Location> locations;

    public Object_Style(long id, String name, String type, String description, String foods, String primary_image_url, String product_category, String location) {
        super(id);
        this.name = name;
        this.type = type;
        this.description = description;
        this.foods = Tools_Preferabli.convertJsonToObject(foods, new TypeToken<ArrayList<Object_Food>>() {
        }.getType());
        this.primary_image_url = primary_image_url;
        this.product_category = product_category;
        this.locations = Tools_Preferabli.convertJsonToObject(location, new TypeToken<ArrayList<Object_Location>>() {
        }.getType());
    }


    /**
     * The product category of a style.
     *
     * @return product category.
     */
    public Object_Product.Other_ProductCategory getProductCategory() {
        return Object_Product.Other_ProductCategory.getProductCategoryFromString(product_category);
    }

    public String getName() {
        return name;
    }

    /**
     * The product type of a style (e.g., Red). Only for wines.
     *
     * @return product type.
     */
    public Object_Product.Other_ProductType getProductType() {
        Object_Product.Other_ProductType productType = Object_Product.Other_ProductType.getProductTypeFromString(type);
        return productType;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public ArrayList<Object_Location> getLocations() {
        return locations;
    }

    public ArrayList<Object_Food> getFoods() {
        return foods;
    }

    public String getImage() {
        return primary_image_url;
    }

    /**
     * Get the style image.
     * @param width returns an image with the specified width in pixels.
     * @param height returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getImage(int width, int height, int quality) {
        return Tools_Preferabli.getImageUrl(primary_image_url, width, height, quality);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.description);
        dest.writeInt(this.order);
        dest.writeTypedList(this.foods);
        dest.writeString(this.primary_image_url);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.product_category);
        dest.writeTypedList(this.locations);
    }

    protected Object_Style(Parcel in) {
        super(in);
        this.name = in.readString();
        this.type = in.readString();
        this.description = in.readString();
        this.order = in.readInt();
        this.foods = in.createTypedArrayList(Object_Food.CREATOR);
        this.primary_image_url = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.product_category = in.readString();
        this.locations = in.createTypedArrayList(Object_Location.CREATOR);
    }
}
