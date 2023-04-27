//
//  Object_Product.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Represents a product within the Preferabli SDK. A product may have one or more {@link Object_Variant}s stored as {@link Object_Product#variants}. To see how a product is mapped to your own object(s), see {@link Object_Variant#getMerchantLinks()}. To see a user's interaction with the product, see {@link Object_Variant#getTags()}.
 */
public class Object_Product extends Object_BaseObject {

    private ArrayList<Object_Variant> variants;
    private String name;
    private String grape;
    private boolean decant;
    private boolean show_year_dropdown;
    private String type;
    private String brand;
    private String region;
    private String created_at;
    private String updated_at;
    private Object_Media primary_image;
    private Object_Media back_image;
    private double brand_lat;
    private double brand_lon;
    private String category;
    private String subcategory;
    private String hash;

    // we will generate this only one time.
    private transient String price;
    private transient Object_Tag.Other_RatingLevel rating_level;
    private transient Object_Variant most_recent_variant;
    private transient Object_Tag most_recent_rating;
    private transient Object_Tag wishlist_tag;
    private transient HashSet<Object_Tag> rating_tags;
    private transient HashSet<Object_Tag> cellar_tags;
    private transient HashSet<Object_Tag> purchase_tags;

    public Object_Product() {
        super(-System.currentTimeMillis());
    }

    public Object_Product(long id, String name, String grape, boolean decant, boolean show_year_dropdown, String type, String image, String backImage, String brand, String region, String created_at, String updated_at, boolean isDirty, double brand_lat, double brand_lon, String category, String rateSourceLocation, String subcategory, String hash) {
        super(id);
        this.name = name;
        this.grape = grape;
        this.decant = decant;
        this.show_year_dropdown = show_year_dropdown;
        this.type = type;
        this.brand = brand;
        this.region = region;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.primary_image = Tools_Preferabli.convertJsonToObject(image, Object_Media.class);
        this.back_image = Tools_Preferabli.convertJsonToObject(backImage, Object_Media.class);
        this.brand_lat = brand_lat;
        this.brand_lon = brand_lon;
        this.category = category;
        this.subcategory = subcategory;
        this.hash = hash;
    }

    void setName(String name) {
        this.name = name;
    }

    void setType(String type) {
        this.type = type;
    }

    void setCategory(String category) {
        this.category = category;
    }

    public String getHash() {
        return hash;
    }

    /**
     * The type of a product (e.g., Red). Only for wines.
     *
     * @return product type.
     */
    public Other_ProductType getProductType() {
        Other_ProductType productType = Other_ProductType.getProductTypeFromString(type);
        return productType;
    }

    /**
     * The category of a product.
     *
     * @return product category.
     */
    public Other_ProductCategory getProductCategory() {
        return Other_ProductCategory.getProductCategoryFromString(category);
    }

    private void getTags() {
        rating_tags = new HashSet<>();
        cellar_tags = new HashSet<>();
        purchase_tags = new HashSet<>();
        for (Object_Variant variant : getVariants()) {
            for (Object_Tag tag : variant.getTags()) {
                if (tag.getTagType() == Object_Tag.Other_TagType.RATING) rating_tags.add(tag);
                else if (tag.getTagType() == Object_Tag.Other_TagType.WISHLIST) wishlist_tag = tag;
                else if (tag.getTagType() == Object_Tag.Other_TagType.CELLAR) cellar_tags.add(tag);
                else if (tag.getTagType() == Object_Tag.Other_TagType.PURCHASE) purchase_tags.add(tag);
            }
        }
    }

    public boolean isShowYearDropdown() {
        return show_year_dropdown;
    }

    public Object_Media getBackImage() {
        return back_image;
    }

    public double getBrandLat() {
        return brand_lat;
    }

    public double getBrandLon() {
        return brand_lon;
    }

    public boolean isOnWishlist() {
        return getWishlistTag() != null;
    }

    /**
     * All of the product tags of type {@link Object_Tag.Other_TagType#RATING} for the current user.
     *
     * @return an array of tags.
     */
    public HashSet<Object_Tag> getRatingTags() {
        if (rating_tags == null) getTags();
        return rating_tags;
    }

    /**
     * All of the product tags of type {@link Object_Tag.Other_TagType#CELLAR} for the current user.
     *
     * @return an array of tags.
     */
    public HashSet<Object_Tag> getCellarTags() {
        if (cellar_tags == null) getTags();
        return cellar_tags;
    }

    /**
     * All of the product tags of type {@link Object_Tag.Other_TagType#PURCHASE} for the current user.
     *
     * @return an array of tags.
     */
    public HashSet<Object_Tag> getPurchaseTags() {
        if (purchase_tags == null) getTags();
        return purchase_tags;
    }

    public void addVariant(Object_Variant variant) {
        clearTransients();
        if (variants == null) variants = new ArrayList<>();
        variants.add(variant);
    }

    private void clearTransients() {
        rating_tags = null;
        cellar_tags = null;
        purchase_tags = null;
        most_recent_variant = null;
        most_recent_rating = null;
        rating_level = null;
    }

    /**
     * Get the product's image.
     *
     * @param width   returns an image with the specified width in pixels.
     * @param height  returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getImage(int width, int height, int quality) {
        if (primary_image != null && !Tools_Preferabli.isNullOrWhitespace(primary_image.getPath()) && !primary_image.getPath().contains("placeholder"))
            return Tools_Preferabli.getImageUrl(primary_image, width, height, quality);
        else if (getMostRecentVariant().getPrimaryImage() != null && !getMostRecentVariant().getPrimaryImage().getPath().contains("placeholder")) {
            return getMostRecentVariant().getImage(width, height, quality);
        }

        return null;
    }

    public Object_Media getPrimaryImage() {
        return primary_image;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public ArrayList<Object_Variant> getVariants() {
        if (variants == null) {
            variants = new ArrayList<Object_Variant>();
        }
        return variants;
    }

    /**
     * Get a {@link Object_Variant} of a product by its id.
     *
     * @param id a variant id.
     * @return the corresponding variant. Returns null if this product does not contain the variant.
     */
    public Object_Variant getVariantWithId(long id) {
        for (Object_Variant variant : getVariants()) {
            if (variant.getId() == id) {
                return variant;
            }
        }

        return null;
    }

    /**
     * Get a {@link Object_Variant} of a product by its year.
     *
     * @param year a variant year.
     * @return the corresponding variant. Returns null if this product does not contain the variant.
     */
    public Object_Variant getVariantWithYear(int year) {
        for (Object_Variant variant : variants) {
            if (variant.getYear() == year) {
                return variant;
            }
        }

        return null;
    }

    public boolean isDecant() {
        return decant;
    }

    public boolean isNonVariant() {
        return show_year_dropdown;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    /**
     * Identifies if a product is still being curated.
     *
     * @return true if the product has not been curated.
     */
    public boolean isBeingIdentified() {
        if (Tools_Preferabli.isNullOrWhitespace(getBrand())) {
            return true;
        }
        return getBrand().toLowerCase().contains("identified");
    }

    public String getRegion() {
        return region;
    }

    public String getGrape() {
        return grape;
    }

    /**
     * Gets the price range of the most recent {@link Object_Variant}.
     * <p></p>
     * Prices represent Retail | Restaurant
     * $ = Less than $12 | < $30
     * $$ = $12 to $19.99 | $30 - $45
     * $$$ = $20 to $49.99 | $45 - $110
     * $$$$ = $50 to $74.99 | $110 - $160
     * $$$$$ = $75 and up | > $160
     *
     * @return price range represented by dollar signs in a string.
     */
    public String getPrice() {
        if (price == null) {
            if (getMostRecentVariant() != null) {
                price = getPriceFromSigns(getMostRecentVariant().getNumDollarSigns());
            }
        }

        return price;
    }

    static String getPriceFromSigns(int numDollarSigns) {
        String dollarSigns = "";
        for (int i = 0; i < numDollarSigns; i++)
            dollarSigns = dollarSigns + "$";
        return dollarSigns;
    }

    public String getSubcategory() {
        return subcategory;
    }

    /**
     * The most recent {@link Object_Variant} for a product.
     *
     * @return a product variant.
     */
    public Object_Variant getMostRecentVariant() {
        if (most_recent_variant == null) {
            int mostRecentYear = -2;
            for (Object_Variant variant : getVariants()) {
                if (variant.getYear() > mostRecentYear && variant.getId() > 0L) {
                    mostRecentYear = variant.getYear();
                    most_recent_variant = variant;
                }
            }

            if (most_recent_variant == null) {
                most_recent_variant = new Object_Variant(getId(), Object_Variant.CURRENT_VARIANT_YEAR);
            }
        }


        return most_recent_variant;
    }

    /**
     * The most recent product tag of type {@link Object_Tag.Other_TagType#RATING} for the current user.
     *
     * @return a tag.
     */
    public Object_Tag getMostRecentRating() {
        if (most_recent_rating == null) {
            Date date = new Date(0);
            for (Object_Tag tag : getRatingTags()) {
                Date compareToDate = Tools_Preferabli.convertAPITimeStampToDate(tag.getCreatedAt());
                if (compareToDate.after(date)) {
                    date = compareToDate;
                    most_recent_rating = tag;
                }
            }
        }

        return most_recent_rating;
    }

    /**
     * The first instance within the product of tag type {@link Object_Tag.Other_TagType#WISHLIST} for the current user.
     *
     * @return a tag.
     */
    public Object_Tag getWishlistTag() {
        if (wishlist_tag == null && rating_tags == null) getTags();
        return wishlist_tag;
    }

    /**
     * The {@link Object_Tag.Other_RatingLevel} of the most recent rating of a specific product for the current user.
     *
     * @return rating level.
     */
    public Object_Tag.Other_RatingLevel getRatingLevel() {
        if (getMostRecentRating() == null) return Object_Tag.Other_RatingLevel.NONE;
        else if (rating_level == null) {
            Object_Tag tag = getMostRecentRating();
            rating_level = Object_Tag.Other_RatingLevel.getRatingLevelBasedOffTagValue(tag.getValue());
        }

        return rating_level;
    }

    @Override
    public boolean filter(Serializable serializable) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        innerloop:
        for (String term : terms) {
            if (getName() != null && getName().toLowerCase().contains(term))
                continue;
            else if (getRegion() != null && getRegion().toLowerCase().contains(term))
                continue;
            else if (getBrand() != null && getBrand().toLowerCase().contains(term))
                continue;
            else if (getGrape() != null && getGrape().toLowerCase().contains(term))
                continue;
            else if (getRatingTags() != null) {
                for (Object_Tag tag : getRatingTags()) {
                    if (tag.getComment() != null && tag.getComment().toLowerCase().contains(term))
                        continue innerloop;
                    else if (tag.getLocation() != null && tag.getLocation().toLowerCase().contains(term))
                        continue innerloop;
                }
                return false;
            }

            return false;
        }
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.variants);
        dest.writeString(this.name);
        dest.writeString(this.grape);
        dest.writeByte(this.decant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.show_year_dropdown ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeString(this.brand);
        dest.writeString(this.region);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.primary_image, flags);
        dest.writeDouble(brand_lat);
        dest.writeDouble(brand_lon);
        dest.writeString(category);
        dest.writeString(subcategory);
    }

    protected Object_Product(Parcel in) {
        super(in);
        this.variants = in.createTypedArrayList(Object_Variant.CREATOR);
        this.name = in.readString();
        this.grape = in.readString();
        this.decant = in.readByte() != 0;
        this.show_year_dropdown = in.readByte() != 0;
        this.type = in.readString();
        this.brand = in.readString();
        this.region = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.primary_image = in.readParcelable(Object_Media.class.getClassLoader());
        this.brand_lat = in.readDouble();
        this.brand_lon = in.readDouble();
        this.category = in.readString();
        this.subcategory = in.readString();
    }

    /**
     * See {@link Preferabli#whereToBuy(long, Other_FulfillSort, Boolean, Boolean, API_ResultHandler)}.
     */
    public void whereToBuy(Other_FulfillSort fulfill_sort, Boolean append_nonconforming_results, Boolean lock_to_integration, API_ResultHandler<Object_WhereToBuy> handler) {
        getMostRecentVariant().whereToBuy(fulfill_sort, append_nonconforming_results, lock_to_integration, handler);
    }

    /**
     * See {@link Preferabli#rateProduct(long, int, Object_Tag.Other_RatingLevel, String, String, Double, Integer, Integer, API_ResultHandler)}.
     */
    public void rate(Object_Tag.Other_RatingLevel rating, String location, String notes, Double price, Integer quantity, Integer format_ml, API_ResultHandler<Object_Product> handler) {
        getMostRecentVariant().rate(rating, location, notes, price, quantity, format_ml, handler);
    }

    /**
     * See {@link Preferabli#wishlistProduct(long, int, String, String, Double, Integer, Integer, API_ResultHandler)}.
     */
    public void toggleWishlist(API_ResultHandler<Object_Product> handler) {
        Object_Tag tag = getWishlistTag();
        Object_Variant variant = getMostRecentVariant();

        if (tag != null) {
            variant = tag.getVariant();
        }

        variant.toggleWishlist(handler);
    }

    /**
     * See {@link Preferabli#lttt(long, Integer, Long, Boolean, API_ResultHandler)}.
     */
    public void lttt(Long collection_id, Boolean include_merchant_links, API_ResultHandler<ArrayList<Object_Product>> handler) {
        getMostRecentVariant().lttt(collection_id, include_merchant_links, handler);
    }

    /**
     * See {@link Preferabli#getPreferabliScore(long, Integer, API_ResultHandler)}.
     */
    public void getPreferabliScore(API_ResultHandler<Object_PreferenceData> handler) {
        getMostRecentVariant().getPreferabliScore(handler);
    }

    /**
     * The recognized type of a {@link Object_Product}. At this time, non-wine products use the type {@link Other_ProductType#OTHER}.
     */
    public enum Other_ProductType {
        RED("red"),
        WHITE("white"),
        ROSE("rosé"),
        SPARKLING("sparkling"),
        FORTIFIED("fortified"),
        OTHER("other");

        private String name;

        Other_ProductType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Other_ProductType getProductTypeFromString(String wineType) {
            if (Tools_Preferabli.isNullOrWhitespace(wineType)) {
                return OTHER;
            } else if (wineType.equalsIgnoreCase("red")) {
                return RED;
            } else if (wineType.equalsIgnoreCase("white")) {
                return WHITE;
            } else if (wineType.equalsIgnoreCase("rosé")) {
                return ROSE;
            } else if (wineType.equalsIgnoreCase("fortified")) {
                return FORTIFIED;
            } else if (wineType.equalsIgnoreCase("sparkling")) {
                return SPARKLING;
            }

            return OTHER;
        }
    }

    /**
     * The category of a {@link Object_Product}.
     */
    public enum Other_ProductCategory {
        WHISKEY("whiskey"),
        MEZCAL("tequila"),
        BEER("beer"),
        WINE("wine"),
        NONE("");

        private String name;

        Other_ProductCategory(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Other_ProductCategory getProductCategoryFromString(String category) {
            if (Tools_Preferabli.isNullOrWhitespace(category)) {
                return NONE;
            } else if (category.equalsIgnoreCase("whiskey")) {
                return WHISKEY;
            } else if (category.equalsIgnoreCase("tequila")) {
                return MEZCAL;
            } else if (category.equalsIgnoreCase("beer")) {
                return BEER;
            } else if (category.equalsIgnoreCase("wine")) {
                return WINE;
            }

            return NONE;
        }
    }
}
