//
//  Object_Collection.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A collection is a selection of {@link Object_Product}, organized into one or more {@link Object_CollectionGroup}.  For example, a collection can represent an inventory for a store or just a subset of products, such as selection of products that are currently on sale or a selection of private-label products.
 * <p></p>
 * In general, a collection will be an {@link Other_CollectionType#INVENTORY} or an {@link Other_CollectionType#EVENT}. Events are temporal in nature, such as a tasting events or weekly promotions. Inventories, whether entire inventories or subsets of an inventory, are meant to change from time to time but are not specifically temporal in nature.
 * <p></p>
 * A Collection may also be a {@link Other_CollectionType#CELLAR} type (e.g., a {@link Object_Customer}'s personal cellar) or {@link Other_CollectionType#OTHER} type.
 * <p></p>
 * Collections are structured as follows: a collection has one or more {@link Object_CollectionVersion}s. Each version has one or more {@link Object_CollectionGroup}s. And each group contains one or more {@link Object_CollectionOrder}s, which link directly to a {@link Object_Tag} and thus by reference a {@link Object_Product}.
 */
public class Object_Collection extends Object_BaseObject {

    private long channel_id;
    private long sort_channel_id;
    private String name;
    private String display_name;
    private String description;
    private String code;
    private String timezone;
    private boolean display_price;
    private boolean display_bin;
    private boolean display_quantity;
    private boolean display_time;
    private boolean display_group_headings;
    private boolean is_blind;
    private boolean has_predict_order;
    private String currency;
    private String badge_method;
    private boolean published;
    private boolean is_my_cellar;
    private String start_date;
    private String end_date;
    private String comment;
    private String created_at;
    private String updated_at;
    private ArrayList<Object_CollectionVersion> versions;
    private ArrayList<Object_CollectionTrait> traits;
    private Object_Venue venue;
    private Object_Media primary_image;
    private String sort_channel_name;
    private int product_count;
    @SerializedName("public")
    public boolean isPublic;
    private boolean is_browsable;
    private boolean archived;

    /**
     * Collection Type of a specific collection. In general, a Collection will be an {@link Other_CollectionType#INVENTORY} or an {@link Other_CollectionType#EVENT}. Events are temporal in nature, such as a tasting events or weekly promotions. Inventories, whether entire inventories or subsets of an inventory, are meant to change from time to time but are not specifically temporal in nature.
     * @return {@link Other_CollectionType}
     */
    public Other_CollectionType getCollectionType() {
        return Other_CollectionType.getCollectionTypeBasedOffCollection(this);
    }

    Object_Media getPrimaryImage() {
        return primary_image;
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isBrowsable() {
        return is_browsable;
    }

    public int getProductCount() {
        return product_count;
    }

    public boolean hasPredictOrder() {
        return has_predict_order;
    }

    public boolean isMyCellar() {
        return is_my_cellar;
    }

    public String getTimezone() {
        return timezone;
    }

    public boolean isDisplayPrice() {
        return display_price;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isDisplayBin() {
        return display_bin;
    }

    public String getSortChannelName() {
        return sort_channel_name;
    }

    public boolean isDisplayQuantity() {
        return display_quantity;
    }

    public boolean isDisplayTime() {
        return display_time;
    }

    public boolean isBlind() {
        return is_blind;
    }

    public String getCurrency() {
        return currency;
    }

    public String getBadgeMethod() {
        if (badge_method == null) {
            return "bign";
        }
        return badge_method;
    }

    public long getSortChannelId() {
        return sort_channel_id;
    }

    public ArrayList<Object_CollectionVersion> getVersions() {
        if (versions == null) {
            versions = new ArrayList<>();
        }
        return versions;
    }

    public Object_CollectionVersion getFirstVersion() {
        if (getVersions().size() == 0) {
            return new Object_CollectionVersion(-System.currentTimeMillis());
        }

        return getVersions().get(0);
    }

    void addVersion(Object_CollectionVersion version) {
        if (versions == null) versions = new ArrayList<>();
        versions.add(version);
    }

    public Object_Collection(long id, long channel_id, long sort_channel_id, String name, String display_name, String description, String code, boolean published, String start_date, String end_date, String comment, String created_at, String updated_at, String traits, String image, String venue, int order, boolean auto_wili, String sort_channel_name, String badge_method, String currency, String timezone, boolean display_time, boolean display_price, boolean display_quantity, boolean display_bin, boolean is_blind, boolean display_group_headings, boolean has_predict_order, int product_count, boolean is_pinned, boolean is_browsable, boolean archived) {
        super(id);
        this.channel_id = channel_id;
        this.sort_channel_id = sort_channel_id;
        this.name = name;
        this.display_name = display_name;
        this.description = description;
        this.code = code;
        this.published = published;
        this.start_date = start_date;
        this.end_date = end_date;
        this.comment = comment;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.traits = Tools_Preferabli.convertJsonToObject(traits, new TypeToken<ArrayList<Object_CollectionTrait>>() {
        }.getType());
        this.venue = Tools_Preferabli.convertJsonToObject(venue, Object_Venue.class);
        this.sort_channel_name = sort_channel_name;
        this.currency = currency;
        this.timezone = timezone;
        this.badge_method = badge_method;
        this.is_blind = is_blind;
        this.display_time = display_time;
        this.display_bin = display_bin;
        this.display_quantity = display_quantity;
        this.display_price = display_price;
        this.display_group_headings = display_group_headings;
        this.has_predict_order = has_predict_order;
        this.product_count = product_count;
        this.is_browsable = is_browsable;
        this.archived = archived;
        this.primary_image = Tools_Preferabli.convertJsonToObject(image, Object_Media.class);
    }

    /**
     * Get the collection's image.
     * @param width returns an image with the specified width in pixels.
     * @param height returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getImage(int width, int height, int quality) {
        return Tools_Preferabli.getImageUrl(primary_image, width, height, quality);
    }

    public long getChannelId() {
        return channel_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public boolean isPublished() {
        return published;
    }

    public String getStartDate() {
        return start_date;
    }

    public String getEndDate() {
        return end_date;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public ArrayList<Object_CollectionTrait> getTraits() {
        if (traits == null) {
            traits = new ArrayList<>();
        }
        return traits;
    }

    public Object_Venue getVenue() {
        return venue;
    }

    @Override
    public boolean filter(Serializable serializable) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        for (String term : terms) {
            if (getName() != null && getName().toLowerCase().contains(term)) {
                continue;
            } else if (sort_channel_name != null && sort_channel_name.toLowerCase().contains(term)) {
                continue;
            } else if (venue != null && venue.filter(constraint)) {
                continue;
            }

            return false;
        }
        return true;
    }

    public boolean isDisplayGroupHeadings() {
        return display_group_headings;
    }

    /**
     * Lets us know if a collection is of the type {@link Other_CollectionType#INVENTORY}.
     * @return boolean
     */
    public boolean isInventory() {
        for (Object_CollectionTrait trait : getTraits()) {
            if (trait.getId() == 86)
                return true;
        }
        return false;
    }

    /**
     * Lets us know if a collection is of the type {@link Other_CollectionType#EVENT}.
     * @return boolean
     */
    public boolean isEvent() {
        for (Object_CollectionTrait trait : getTraits()) {
            if (trait.getId() == 84 || trait.getId() == 88 || trait.getId() == 90)
                return true;
        }
        return false;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.channel_id);
        dest.writeLong(this.sort_channel_id);
        dest.writeString(this.name);
        dest.writeString(this.display_name);
        dest.writeString(this.description);
        dest.writeString(this.code);
        dest.writeString(this.timezone);
        dest.writeByte(this.display_price ? (byte) 1 : (byte) 0);
        dest.writeByte(this.display_bin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.display_quantity ? (byte) 1 : (byte) 0);
        dest.writeByte(this.display_time ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_blind ? (byte) 1 : (byte) 0);
        dest.writeByte(this.display_group_headings ? (byte) 1 : (byte) 0);
        dest.writeString(this.currency);
        dest.writeString(this.badge_method);
        dest.writeByte(this.published ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_my_cellar ? (byte) 1 : (byte) 0);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeString(this.comment);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeTypedList(this.versions);
        dest.writeTypedList(this.traits);
        dest.writeParcelable(this.venue, flags);
        dest.writeParcelable(this.primary_image, flags);
        dest.writeString(this.sort_channel_name);
        dest.writeByte(this.has_predict_order ? (byte) 1 : (byte) 0);
        dest.writeInt(this.product_count);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_browsable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.archived ? (byte) 1 : (byte) 0);
    }

    protected Object_Collection(Parcel in) {
        super(in);
        this.channel_id = in.readLong();
        this.sort_channel_id = in.readLong();
        this.name = in.readString();
        this.display_name = in.readString();
        this.description = in.readString();
        this.code = in.readString();
        this.timezone = in.readString();
        this.display_price = in.readByte() != 0;
        this.display_bin = in.readByte() != 0;
        this.display_quantity = in.readByte() != 0;
        this.display_time = in.readByte() != 0;
        this.is_blind = in.readByte() != 0;
        this.display_group_headings = in.readByte() != 0;
        this.currency = in.readString();
        this.badge_method = in.readString();
        this.published = in.readByte() != 0;
        this.is_my_cellar = in.readByte() != 0;
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.comment = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.versions = in.createTypedArrayList(Object_CollectionVersion.CREATOR);
        this.traits = in.createTypedArrayList(Object_CollectionTrait.CREATOR);
        this.venue = in.readParcelable(Object_Venue.class.getClassLoader());
        this.primary_image = in.readParcelable(Object_Media.class.getClassLoader());
        this.sort_channel_name = in.readString();
        this.has_predict_order = in.readByte() != 0;
        this.product_count = in.readInt();
        this.isPublic = in.readByte() != 0;
        this.is_browsable = in.readByte() != 0;
        this.archived = in.readByte() != 0;
    }

    public static final Creator<Object_Collection> CREATOR = new Creator<Object_Collection>() {
        @Override
        public Object_Collection createFromParcel(Parcel source) {
            return new Object_Collection(source);
        }

        @Override
        public Object_Collection[] newArray(int size) {
            return new Object_Collection[size];
        }
    };

    /**
     * A version of a {@link Object_Collection}. For the most part, collections will only have one version.
     */
    public static class Object_CollectionVersion extends Object_BaseObject {
        private String name;
        private int order;
        private ArrayList<Object_CollectionGroup> groups;
        private String created_at;
        private String updated_at;

        public Object_CollectionVersion(long id){
            super(id);
        }

        public ArrayList<Object_CollectionGroup> getGroups() {
            if (groups == null) groups = new ArrayList<>();
            return groups;
        }

        public void addGroup(Object_CollectionGroup group) {
            if (groups == null) groups = new ArrayList<>();
            groups.add(group);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.name);
            dest.writeInt(this.order);
            dest.writeList(this.groups);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
        }

        protected Object_CollectionVersion(Parcel in) {
            super(in);
            this.name = in.readString();
            this.order = in.readInt();
            this.groups = new ArrayList<Object_CollectionGroup>();
            in.readList(this.groups, Object_CollectionGroup.class.getClassLoader());
            this.created_at = in.readString();
            this.updated_at = in.readString();
        }

        public static final Creator<Object_CollectionVersion> CREATOR = new Creator<Object_CollectionVersion>() {
            @Override
            public Object_CollectionVersion createFromParcel(Parcel source) {
                return new Object_CollectionVersion(source);
            }

            @Override
            public Object_CollectionVersion[] newArray(int size) {
                return new Object_CollectionVersion[size];
            }
        };
    }

    /**
     * Products in a Collection are organized into one or more groups. A CollectionGroup object sits within a {@link Object_CollectionVersion}. Each CollectionGroup has an {@link Object_CollectionGroup#order} representing its display order within a Collection. Products that are tagged as belonging to a Collection are ordered within the applicable CollectionGroup with {@link Object_CollectionGroup#orderings}.
     */
    public static class Object_CollectionGroup extends Object_BaseObject {
        private String name;
        private int order;
        private int orderings_count;
        private ArrayList<Object_CollectionOrder> orderings;
        private String created_at;
        private String updated_at;
        private long version_id;

        public Object_CollectionGroup(long id, String name, int orderings_count, int order, long version_id) {
            super(id);
            this.name = name;
            this.order = order;
            this.orderings_count = orderings_count;
            this.version_id = version_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getVersionId() {
            return version_id;
        }

        public int getOrderingsCount() {
            return orderings_count;
        }

        public int getOrder() {
            return order;
        }

        public ArrayList<Object_CollectionOrder> getOrderings() {
            if (this.orderings == null) {
                this.orderings = new ArrayList<>();
            }
            return orderings;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public String getUpdatedAt() {
            return updated_at;
        }

        void addOrdering(Object_CollectionOrder ordering) {
            if (this.orderings == null) {
                this.orderings = new ArrayList<>();
            }
            this.orderings.add(ordering);
        }

        private static class GroupComparator implements Comparator<Object_CollectionGroup> {
            @Override
            public int compare(Object_CollectionGroup group1, Object_CollectionGroup group2) {
                if (group1 == null && group2 == null) {
                    return 0;
                } else if (group1 == null) {
                    return 1;
                } else if (group2 == null) {
                    return -1;
                }

                return ((Integer) group1.getOrder()).compareTo(group2.getOrder());
            }
        }

        public static ArrayList<Object_CollectionGroup> sortGroups(ArrayList<Object_CollectionGroup> groups) {
            Collections.sort(groups, new GroupComparator());
            return groups;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.name);
            dest.writeInt(this.order);
            dest.writeInt(this.orderings_count);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeTypedList(this.orderings);
            dest.writeLong(this.version_id);
        }

        protected Object_CollectionGroup(Parcel in) {
            super(in);
            this.name = in.readString();
            this.order = in.readInt();
            this.orderings_count = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.orderings = in.createTypedArrayList(Object_CollectionOrder.CREATOR);
            this.version_id = in.readLong();
        }

        public static final Creator<Object_CollectionGroup> CREATOR = new Creator<Object_CollectionGroup>() {
            @Override
            public Object_CollectionGroup createFromParcel(Parcel source) {
                return new Object_CollectionGroup(source);
            }

            @Override
            public Object_CollectionGroup[] newArray(int size) {
                return new Object_CollectionGroup[size];
            }
        };
    }

    /**
     * The link between a {@link Object_Tag} (which in turn references a {@link Object_Product}) and a {@link Object_Collection}, including its ordering within the Collection.
     */
    public static class Object_CollectionOrder extends Object_BaseObject {
        private long tag_id;
        private int order;
        private String created_at;
        private String updated_at;
        private Object_Tag tag;
        private long group_id;

        public Object_CollectionOrder(long id, long tag_id, int order, long group_id) {
            super(id);
            this.tag_id = tag_id;
            this.order = order;
            this.group_id = group_id;
        }

        public long getGroupId() {
            return group_id;
        }

        void setTag(Object_Tag tag) {
            this.tag = tag;
        }

        void setGroupId(long group_id) {
            this.group_id = group_id;
        }

        public Object_Tag getTag() {
            return tag;
        }

        public long getTagId() {
            return tag_id;
        }

        public int getOrder() {
            return order;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public String getUpdatedAt() {
            return updated_at;
        }

        private static class OrderComparator implements Comparator<Object_CollectionOrder> {
            @Override
            public int compare(Object_CollectionOrder ordering1, Object_CollectionOrder ordering2) {
                if (ordering1 == null && ordering2 == null) {
                    return 0;
                } else if (ordering1 == null) {
                    return 1;
                } else if (ordering2 == null) {
                    return -1;
                }

                return ((Integer) ordering1.getOrder()).compareTo(ordering2.getOrder());
            }
        }

        public static ArrayList<Object_CollectionOrder> sortOrders(ArrayList<Object_CollectionOrder> orderings) {
            Collections.sort(orderings, new OrderComparator());
            return orderings;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.tag_id);
            dest.writeInt(this.order);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeLong(this.group_id);
        }

        protected Object_CollectionOrder(Parcel in) {
            super(in);
            this.tag_id = in.readLong();
            this.order = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.group_id = in.readLong();

        }

        public static final Creator<Object_CollectionOrder> CREATOR = new Creator<Object_CollectionOrder>() {
            @Override
            public Object_CollectionOrder createFromParcel(Parcel source) {
                return new Object_CollectionOrder(source);
            }

            @Override
            public Object_CollectionOrder[] newArray(int size) {
                return new Object_CollectionOrder[size];
            }
        };
    }

    /**
     * A trait descriptor for a {@link Object_Collection}.
     */
    public static class Object_CollectionTrait extends Object_BaseObject {
        private int order;
        private String name;
        private String created_at;
        private String updated_at;
        private boolean restrict_to_ring_it;

        public String getName() {
            return name;
        }

        public int getOrder() {
            return order;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public String getUpdatedAt() {
            return updated_at;
        }

        public boolean isRestrictToRingIT() {
            return restrict_to_ring_it;
        }

        private static class TraitComparator implements Comparator<Object_CollectionTrait> {
            @Override
            public int compare(Object_CollectionTrait trait1, Object_CollectionTrait trait2) {
                if (trait1 == null && trait2 == null) {
                    return 0;
                } else if (trait1 == null) {
                    return 1;
                } else if (trait2 == null) {
                    return -1;
                } else if (trait1.getOrder() == trait2.getOrder())
                    return trait1.getName().compareToIgnoreCase(trait2.getName());
                return ((Integer) trait1.getOrder()).compareTo(trait2.getOrder());
            }
        }

        public static ArrayList<Object_CollectionTrait> sortTraits(ArrayList<Object_CollectionTrait> traits) {
            Collections.sort(traits, new TraitComparator());
            return traits;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.order);
            dest.writeString(this.name);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeByte(this.restrict_to_ring_it ? (byte) 1 : (byte) 0);
        }

        protected Object_CollectionTrait(Parcel in) {
            super(in);
            this.order = in.readInt();
            this.name = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.restrict_to_ring_it = in.readByte() != 0;
        }

        public static final Creator<Object_CollectionTrait> CREATOR = new Creator<Object_CollectionTrait>() {
            @Override
            public Object_CollectionTrait createFromParcel(Parcel source) {return new Object_CollectionTrait(source);}

            @Override
            public Object_CollectionTrait[] newArray(int size) {return new Object_CollectionTrait[size];}
        };
    }

    /**
     * The type of a {@link Object_Collection}.
     */
    public enum Other_CollectionType {
        EVENT,
        INVENTORY,
        CELLAR,
        OTHER;

        public static Other_CollectionType getCollectionTypeBasedOffCollection(Object_Collection collection) {
            if (collection.isMyCellar()) {
                return CELLAR;
            } else if (collection.isEvent()) {
                return EVENT;
            } else if (collection.isInventory()) {
                return INVENTORY;
            }

            return OTHER;
        }
    }
}
