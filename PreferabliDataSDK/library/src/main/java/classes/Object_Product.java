//
//  Object_Product.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.net.Uri;
import android.os.Parcel;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private boolean dirty;
    private boolean isChecked;
    private double brand_lat;
    private double brand_lon;
    private String category;
    private String subcategory;
    private String hash;

    // we will generate this only one time.
    private transient String price;
    private transient Other_RatingType ratingType;
    private transient Object_Variant mostRecentVariant;
    private transient Object_Tag mostRecentRating;
    private transient Object_Tag wishlistTag;
    private transient Object_Tag skippedTag;
    private transient HashSet<Object_Tag> ratingsTags;
    private transient HashSet<Object_Tag> collectionTags;
    private transient HashSet<Object_Tag> personalCellarTags;
    private transient HashSet<Object_Tag> purchaseTags;
    private transient WineGroup wineGroup;
    private transient int totalPosition;
    private transient int positionInGroup;
    private transient Object_Collection collection;
    private transient Object_Tag mostRecentPurchase;
    private transient String rateSourceLocation;
    // need old id for matching up dirty products after label rec submission
    private transient long oldIdForEqualityPurposes;
    private transient Object_PreferenceData wili;

    public Object_Product() {
        super(-System.currentTimeMillis());
    }

    public Object_Product(boolean dirty) {
        this();
        this.dirty = dirty;
    }

    public Object_Product(Object_Collection.Object_Version.Object_Group group, Object_Collection.Object_Version.Object_Group.Object_Ordering ordering) {
        this();
        setWineGroup(new WineGroup(group, ordering));
    }

    public Object_Product(String name, String category, Object image, String rateSourceLocation) {
        this();
        this.name = name;
        this.category = category;
        this.addImage(image);
        this.dirty = true;
        this.show_year_dropdown = true;
        this.rateSourceLocation = rateSourceLocation;
    }

    public Object_Product(String name, String brand, String region, String grape, String type, Object frontImage, Object backImage) {
        this();
        this.name = name;
        this.grape = grape;
        this.type = type;
        this.brand = brand;
        this.region = region;
        this.dirty = true;
        addImage(backImage, "back");
        addImage(frontImage, "front");
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
        this.dirty = isDirty;
        this.primary_image = Tools_Preferabli.convertJsonToObject(image, Object_Media.class);
        this.back_image = Tools_Preferabli.convertJsonToObject(backImage, Object_Media.class);
        this.brand_lat = brand_lat;
        this.brand_lon = brand_lon;
        this.category = category;
        this.rateSourceLocation = rateSourceLocation;
        this.subcategory = subcategory;
        this.hash = hash;
    }

    public Object_Product(Object_Product product) {
        // copy for duplicated products. leave out some things like wine group that shouldn't be copied.
        super(product.getId());
        this.name = product.getName();
        this.grape = product.getGrape();
        this.decant = product.isDecant();
        this.show_year_dropdown = product.isNonVariant();
        this.type = product.getType();
        this.category = product.getCategory();
        this.brand_lat = product.getBrand_lat();
        this.brand_lon = product.getBrand_lon();
        this.back_image = product.getBackImage();
        this.brand = product.getBrand();
        this.brand_lat = product.getBrand_lat();
        this.brand_lon = product.getBrand_lon();
        this.region = product.getRegion();
        this.created_at = product.getCreatedAt();
        this.updated_at = product.getUpdatedAt();
        this.primary_image = product.getPrimaryImage();
        this.price = product.getPrice();
        this.dirty = product.isDirty();
        this.variants = product.getVariants();
        this.collection = product.getCollection();
        this.hash = product.getHash();
        this.subcategory = product.getSubcategory();
        this.rateSourceLocation = product.getRateSourceLocation();
    }

    public String getHash() {
        return hash;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setGrape(String grape) {
        this.grape = grape;
    }

    public int getTotalPosition() {
        return totalPosition;
    }

    public void setTotalPosition(int totalPosition) {
        this.totalPosition = totalPosition;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isWineDirtyOrChildren() {
        return dirty || getId() < 0 || (getWineGroup() != null && getOrdering().isDirty());
    }

    public Other_ProductType getProductType() {
        Other_ProductType productType = Other_ProductType.getProductTypeFromString(getType());
        return productType;
    }

    public Other_ProductCategory getProductCategory() {
        return Other_ProductCategory.getProductCategoryFromString(getCategory());
    }

    public boolean isOnlyWineDirty() {
        return dirty || getId() < 0;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public int getPositionInGroup() {
        return positionInGroup;
    }

    public String getCategory() {
        return category;
    }

    public void setCollection(Object_Collection collection) {
        this.collection = collection;
    }

    public Object_Collection getCollection() {
        return collection;
    }

    public void setPositionInGroup(int positionInGroup) {
        this.positionInGroup = positionInGroup;
    }

    public void getTags() {
        ratingsTags = new HashSet<>();
        collectionTags = new HashSet<>();
        purchaseTags = new HashSet<>();
        for (Object_Variant variant : getVariants()) {
            for (Object_Tag tag : variant.getTags()) {
                if (tag.isRating()) ratingsTags.add(tag);
                else if (tag.isWishlist()) wishlistTag = tag;
                else if (tag.isCollection()) collectionTags.add(tag);
                else if (tag.isSkipped()) skippedTag = tag;
                else if (tag.isPurchase()) purchaseTags.add(tag);
            }
        }
    }

    public Object_Tag getTagWithId(long id) {
        for (Object_Variant variant : getVariants()) {
            for (Object_Tag tag : variant.getTags()) {
                if (tag.getId() == id) {
                    return tag;
                }
            }
        }

        return null;
    }

    public String getRateSourceLocation() {
        return rateSourceLocation;
    }

    public boolean isShow_year_dropdown() {
        return show_year_dropdown;
    }

    public Object_Media getBackImage() {
        return back_image;
    }

    public double getBrand_lat() {
        return brand_lat;
    }

    public double getBrand_lon() {
        return brand_lon;
    }

    public void setOldIdForEqualityPurposes(long oldIdForEqualityPurposes) {
        this.oldIdForEqualityPurposes = oldIdForEqualityPurposes;
    }

    public long getOldIdForEqualityPurposes() {
        return oldIdForEqualityPurposes;
    }

    public void addImage(Object object) {
        addImage(0, object);
    }

    public void addImage(Object object, String position) {
        addImage(0, object, position);
    }

    public void addImage(long id, Object object) {
        addImage(id, object, "front");
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addImage(long id, Object object, String position) {
        if (object != null) {
            Object_Media image;
            if (object instanceof Object_Media) image = (Object_Media) object;
            else if (object instanceof String) image = new Object_Media(id, (String) object, position);
            else if (object instanceof Uri) image = new Object_Media(id, (object).toString(), position);
            else if (object instanceof File)
                image = new Object_Media(id, ((File) object).getAbsolutePath(), position);
            else return;
            if (position.equalsIgnoreCase("front")) {
                primary_image = image;
            } else {
                back_image = image;
            }
        }
    }

    public static ArrayList<Object_Product> sortProducts(ArrayList<Object_Product> products) {
        Collections.sort(products, new GroupComparator(true));
        return products;
    }

    public static class WineComparator implements Comparator<Object_Product> {
        public boolean ascending;

        public boolean isAscending() {
            return ascending;
        }

        public void setAscending(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Object_Product product, Object_Product t1) {
            return 0;
        }
    }

    public static class GroupComparator extends WineComparator {

        public GroupComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Object_Product product1, Object_Product product2) {
            if (product1 == null && product2 == null) {
                return 0;
            } else if (product1 == null) {
                return 1;
            } else if (product2 == null) {
                return -1;
            } else if (ascending) {
                if (product1.getWineGroup().getGroup().getOrder() == product2.getWineGroup().getGroup().getOrder())
                    return ((Integer) product1.getWineGroup().getOrdering().getOrder()).compareTo(product2.getWineGroup().getOrdering().getOrder());

                return ((Integer) product1.getWineGroup().getGroup().getOrder()).compareTo(product2.getWineGroup().getGroup().getOrder());
            } else {
                if (product2.getWineGroup().getGroup().getOrder() == product1.getWineGroup().getGroup().getOrder())
                    return ((Integer) product2.getWineGroup().getOrdering().getOrder()).compareTo(product1.getWineGroup().getOrdering().getOrder());

                return ((Integer) product2.getWineGroup().getGroup().getOrder()).compareTo(product1.getWineGroup().getGroup().getOrder());
            }
        }
    }

    public boolean isOnWishlist() {
        return getWishlistTag() != null;
    }

    public void setBackImage(Object_Media back_image) {
        this.back_image = back_image;
    }

    public HashSet<Object_Tag> getTimelineTags() {
        HashSet<Object_Tag> timelineTags = new HashSet<>();
        HashSet<Object_Tag> ratingsTags = getRatingsTags();
        timelineTags.addAll(ratingsTags);
        HashSet<Object_Tag> purchaseTags = getPurchaseTags();
        timelineTags.addAll(purchaseTags);
        Object_Tag wishlistTag = getWishlistTag();
        if (wishlistTag != null) {
            timelineTags.add(wishlistTag);
        }
        HashSet<Object_Tag> personalCellarTags = getPersonalCellarTags();
        timelineTags.addAll(personalCellarTags);
        return timelineTags;
    }

    public HashSet<Object_Tag> getRatingsTags() {
        if (ratingsTags == null) getTags();
        return ratingsTags;
    }

    public boolean isRatedInCollection(long collectionId, long variantId) {
        for (Object_Tag tag : getRatingsTags()) {
            if (tag.getVariantId() == variantId && tag.getTaggedInCollectionId() == collectionId) {
                return true;
            }
        }

        return false;
    }

    public HashSet<Object_Tag>  getPersonalCellarTags() {
        Set<String> collectionIds = Tools_Preferabli.getKeyStore().getStringSet("cellarIds", new HashSet<>());
        if (personalCellarTags == null){
            HashSet<Object_Tag> collectionTags = getCollectionTags();
            personalCellarTags = new HashSet<>();
            for (Object_Tag tag : collectionTags) {
                if (collectionIds.contains(Long.toString(tag.getCollectionId()))) {
                    personalCellarTags.add(tag);
                }
            }
        }

        return personalCellarTags;
    }

    public HashSet<Object_Tag> getCollectionTags() {
        if (collectionTags == null) getTags();
        return collectionTags;
    }


    public HashSet<Object_Tag> getPurchaseTags() {
        if (purchaseTags == null) getTags();
        return purchaseTags;
    }

    public boolean hasBeenPurchasedAtMerchant(long channelId) {
        HashSet<Object_Tag> purchaseTags = getPurchaseTags();
        for (Object_Tag tag : purchaseTags) {
            if (tag.getChannelId() == channelId) {
                return true;
            }
        }

        return false;
    }

    public void setWineGroup(WineGroup wineGroup) {
        this.wineGroup = wineGroup;
    }

    public WineGroup getWineGroup() {
        return wineGroup;
    }

    public Object_Collection.Object_Version.Object_Group getGroup() {
        return getWineGroup().getGroup();
    }

    public Object_Collection.Object_Version.Object_Group.Object_Ordering getOrdering() {
        if (getWineGroup() == null) {
            return null;
        }
        return getWineGroup().getOrdering();
    }

    public boolean hasBeenRated() {
        return getMostRecentRating() != null;
    }

    public void addVariant(Object_Variant variant) {
        clearTransients();
        if (variants == null) variants = new ArrayList<>();
        variants.add(variant);
    }

    public void addVariants(ArrayList<Object_Variant> variants) {
        clearTransients();
        if (this.variants == null) this.variants = new ArrayList<>();
        this.variants.addAll(variants);
    }

    public void clearTransients() {
        ratingsTags = null;
        collectionTags = null;
        purchaseTags = null;
        mostRecentVariant = null;
        mostRecentRating = null;
        mostRecentPurchase = null;
        ratingType = null;
        personalCellarTags = null;
    }

    public String getImage() {
        if (primary_image != null && !Tools_Preferabli.isNullOrWhitespace(primary_image.getPath()) && !primary_image.getPath().contains("placeholder"))
            return primary_image.getPath();
        else if (getMostRecentVariant().getPrimaryImagePath() != null && !getMostRecentVariant().getPrimaryImagePath().contains("placeholder")) {
            return getMostRecentVariant().getPrimaryImagePath();
        }

        return null;
    }

    public String getPrimaryImagePath() {
        if (primary_image != null)
            return primary_image.getPath();

        return null;
    }

    public Object_Media getPrimaryImage() {
        return primary_image;
    }

    public String getShareLink() {
        if (getMostRecentRating() != null && getMostRecentRating().getSharing() != null && getMostRecentRating().getSharing().size() > 0) {
            return getMostRecentRating().getSharing().get(0).getUrl();
        }

        return null;
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

    public Object_Variant getVariantWithId(long id) {
        for (Object_Variant variant : getVariants()) {
            if (variant.getId() == id) {
                return variant;
            }
        }

        return null;
    }

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

    public String getGrape() {
        if (Tools_Preferabli.isNullOrWhitespace(grape) || grape.toLowerCase().contains("identifying"))
            return "";
        return grape;
    }

    public String getGrapeForDB() {
        return grape;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public boolean isBeingIdentified() {
        if (Tools_Preferabli.isNullOrWhitespace(getBrand())) {
            return true;
        }
        return getBrand().toLowerCase().contains("identified");
    }

    public String getRegion() {
        if (Tools_Preferabli.isNullOrWhitespace(region) || region.toLowerCase().contains("entered"))
            return "";
        return region;
    }

    public String getRegionForDB() {
        return region;
    }

    public String getPrice() {
        if (price == null) {
            if (getMostRecentVariant() != null) {
                price = getPriceFromSigns(getMostRecentVariant().getNumDollarSigns());
            }
        }

        return price;
    }

    public static String getPriceFromSigns(int numDollarSigns) {
        String dollarSigns = "";
        for (int i = 0; i < numDollarSigns; i++)
            dollarSigns = dollarSigns + "$";
        return dollarSigns;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public Object_Variant getMostRecentVariant() {
        if (mostRecentVariant == null) {
            int mostRecentYear = -2;
            for (Object_Variant variant : getVariants()) {
                if (variant.getYear() > mostRecentYear && variant.getId() > 0L) {
                    mostRecentYear = variant.getYear();
                    mostRecentVariant = variant;
                }
            }

            if (mostRecentVariant == null) {
                mostRecentVariant = new Object_Variant(getId(), -1);
            }
        }


        return mostRecentVariant;
    }

    void setLatestVariantNumDollarSigns(int num_dollar_signs) {
        getMostRecentVariant().setNumDollarSigns(num_dollar_signs);
    }

    public Object_Variant getVariantForUpload() {
        if (getVariants().size() > 0) {
            return getVariants().get(0);
        }

        return new Object_Variant(getId(), -1);
    }

    public Object_Tag getMostRecentRating() {
        if (mostRecentRating == null) {
            Date date = new Date(0);
            for (Object_Tag tag : getRatingsTags()) {
                Date compareToDate = Tools_Preferabli.convertAPITimeStampToDate(tag.getCreatedAt());
                if (compareToDate.after(date)) {
                    date = compareToDate;
                    mostRecentRating = tag;
                }
            }
        }

        return mostRecentRating;
    }

    public Object_Tag getWishlistTag() {
        if (wishlistTag == null && ratingsTags == null) getTags();
        return wishlistTag;
    }

    public Object_Tag getCollectionTag() {
//        return null;
        if (getWineGroup() == null || getWineGroup().getOrdering() == null || getWineGroup().getOrdering().getTag() == null) {
            return null;
        }

//            // this way is slower. only do this if we don't have the tag saved in the ordering for whatever reason (one is due to sort by preference)
//            if (collectionTags == null && ratingsTags == null) getTags();
//            return collectionTags.iterator().hasNext() ? collectionTags.iterator().next() : new Tag();
//        }
        return getWineGroup().getOrdering().getTag();
    }

    public Object_Tag getSkippedTag() {
        if (skippedTag == null && ratingsTags == null) getTags();
        return skippedTag;
    }

    public long getCollectionId() {
        if (getCollectionTag() == null) return 0;
        return getCollectionTag().getCollectionId();
    }

    public void updateTag(Object_Tag tag) {
        removeTag(tag);
        for (Object_Variant variant : getVariants()) {
            if (variant.getId() == tag.getVariantId()) {
                variant.updateTag(tag);
                return;
            }
        }

        addVariant(new Object_Variant(tag));
    }


    public void nullifyTemporaryValues() {
        ratingType = null;
        ratingsTags = null;
        price = null;
        mostRecentVariant = null;
        mostRecentRating = null;
        wishlistTag = null;
    }

    public void removeTag(Object_Tag tag) {
        nullifyTemporaryValues();

        for (Object_Variant variant : getVariants())
            variant.removeTag(tag);
    }

    public void removeVariant(Object_Variant variant) {
        variants.remove(variant);
    }

    public Other_RatingType getRatingType() {
        if (getMostRecentRating() == null) return Other_RatingType.NONE;
        else if (ratingType == null) {
            Object_Tag tag = getMostRecentRating();
            ratingType = Other_RatingType.getRatingTypeBasedOffTagValue(tag.getValue());
        }

        return ratingType;
    }

    public Date getMyLastRatingDate() {
        if (getMostRecentRating() != null) return getMostRecentRating().getDate();
        return new Date(0);
    }

    public Date getWishlistDate() {
        if (getWishlistTag() != null) return getWishlistTag().getDate();
        return new Date(0);
    }

    public Date getMyMostRecentPurchaseDate() {
        if (getMyMostRecentPurchase() != null) return getMyMostRecentPurchase().getPurchasedAtDate();
        return new Date(0);
    }

    public Date getMostRecentJournalEntry() {
        Date myLastRatingDate = getMyLastRatingDate();
        Date wishlistDate = getWishlistDate();
        Date mostRecentPurchase = getMyMostRecentPurchaseDate();

        if (myLastRatingDate.after(wishlistDate)) {
            if (mostRecentPurchase.after(myLastRatingDate)) {
                return mostRecentPurchase;
            } else {
                return myLastRatingDate;
            }
        } else {
            if (mostRecentPurchase.after(wishlistDate)) {
                return mostRecentPurchase;
            } else {
                return wishlistDate;
            }
        }
    }

    public Object_Tag getMostRecentJournalTag() {
        Date myLastRatingDate = getMyLastRatingDate();
        Date wishlistDate = getWishlistDate();
        Date mostRecentPurchase = getMyMostRecentPurchaseDate();

        if (myLastRatingDate.after(wishlistDate)) {
            if (mostRecentPurchase.after(myLastRatingDate)) {
                return getMyMostRecentPurchase();
            } else {
                return getMostRecentRating();
            }
        } else {
            if (mostRecentPurchase.after(wishlistDate)) {
                return getMyMostRecentPurchase();
            } else {
                return getWishlistTag();
            }
        }
    }

    public Object_Tag getMyMostRecentPurchase() {
        if (mostRecentPurchase == null) {
            ArrayList<Object_Tag> purchaseTags = new ArrayList<>(getPurchaseTags());
            Date mostRecentPurchase = new Date(0);
            for (Object_Tag tag : purchaseTags) {
                Date purchasedAt = tag.getPurchasedAtDate();
                if (purchasedAt != null) {
                    if (purchasedAt.after(mostRecentPurchase)) {
                        mostRecentPurchase = purchasedAt;
                        this.mostRecentPurchase = tag;
                    }
                }
            }
        }

        return mostRecentPurchase;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (!o.getClass().isAssignableFrom(getClass()) && !getClass().isAssignableFrom(o.getClass()))) return false;

        Object_Product product = (Object_Product) o;

        if (getOrdering() != null && product.getOrdering() != null)
            return getOrdering().getId() == product.getOrdering().getId();

        // need old id for matching up dirty products after label rec submission
        if ((oldIdForEqualityPurposes != 0 || product.oldIdForEqualityPurposes != 0) && (product.getId() == oldIdForEqualityPurposes || getId() == product.oldIdForEqualityPurposes))
            return true;
        return super.equals(o);
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
            else if (getType() != null && getType().toLowerCase().contains(term))
                continue;
            else if (getType() != null && getType().equalsIgnoreCase("rosé") && term.equalsIgnoreCase("rose"))
                continue;
            else if (getWineGroup() != null && getWineGroup().getGroup().getName().toLowerCase().contains(term))
                continue;
            else if (getRatingsTags() != null) {
                for (Object_Tag tag : getRatingsTags()) {
                    if (tag.getComment() != null && tag.getComment().toLowerCase().contains(term))
                        continue innerloop;
                    else if (tag.getLocation() != null && tag.getLocation().toLowerCase().contains(term))
                        continue innerloop;
                }
                return false;
            } else return false;
        }
        return true;
    }

    public static class WineGroup {
        private Object_Collection.Object_Version.Object_Group group;
        private Object_Collection.Object_Version.Object_Group.Object_Ordering ordering;

        public WineGroup(Object_Collection.Object_Version.Object_Group group, Object_Collection.Object_Version.Object_Group.Object_Ordering ordering) {
            this.group = group;
            this.ordering = ordering;
        }

        public Object_Collection.Object_Version.Object_Group.Object_Ordering getOrdering() {
            return ordering;
        }

        public void setOrdering(Object_Collection.Object_Version.Object_Group.Object_Ordering ordering) {
            this.ordering = ordering;
        }

        public Object_Collection.Object_Version.Object_Group getGroup() {
            return group;
        }

        public void setGroup(Object_Collection.Object_Version.Object_Group group) {
            this.group = group;
        }
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeInt(this.positionInGroup);
        dest.writeInt(this.totalPosition);
        dest.writeParcelable(this.collection, flags);
        dest.writeByte(this.dirty ? (byte) 1 : (byte) 0);
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
        this.positionInGroup = in.readInt();
        this.totalPosition = in.readInt();
        this.collection = in.readParcelable(Object_Collection.class.getClassLoader());
        this.dirty = in.readByte() != 0;
        this.brand_lat = in.readDouble();
        this.brand_lon = in.readDouble();
        this.category = in.readString();
        this.subcategory = in.readString();
    }

    public void setRateSourceLocation(String rateSourceLocation) {
        this.rateSourceLocation = rateSourceLocation;
    }
}
