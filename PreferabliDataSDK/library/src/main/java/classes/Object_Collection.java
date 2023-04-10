//
//  Object_Collection.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
    private int lbs_order;
    private String start_date;
    private boolean auto_wili;
    private boolean is_pinned;
    private String end_date;
    private String comment;
    private String created_at;
    private String updated_at;
    private ArrayList<Version> versions;
    private ArrayList<Object_CollectionTrait> traits;
    private ArrayList<Object_Media> images;
    private Object_Venue venue;
    private int order;
    private Object_Media primary_image;
    private String sort_channel_name;
    private int product_count;
    private boolean isCustomSort;
    private ArrayList<Object_UserCollection> userCollections;
    @SerializedName("public")
    public boolean isPublic;
    private boolean location_based_recs;
    private boolean is_browsable;
    private boolean archived;

    // transients
    private transient Object_UserCollection savedUserCollection;
    private transient Date saveDate;

    public Object_Collection(Object_Collection collection) {
        super(collection.getId());
        this.channel_id = collection.getChannelId();
        this.sort_channel_id = collection.getSortChannelId();
        this.name = collection.getName();
        this.description = collection.getDescription();
        this.code = collection.getCode();
        this.timezone = collection.getTimezone();
        this.display_price = collection.isDisplayPrice();
        this.display_bin = collection.isDisplayBin();
        this.display_quantity = collection.isDisplayQuantity();
        this.display_time = collection.isDisplayTime();
        this.display_group_headings = collection.isDisplayGroupHeadings();
        this.is_blind = collection.is_blind;
        this.has_predict_order = collection.isHasPredictOrder();
        this.currency = collection.getCurrency();
        this.badge_method = collection.getBadgeMethod();
        this.published = collection.isPublished();
        this.is_my_cellar = collection.isMyCellar();
        this.lbs_order = collection.getLbsOrder();
        this.start_date = collection.getStartDate();
        this.auto_wili = collection.isAutoWili();
        this.end_date = collection.getEndDate();
        this.comment = collection.getComment();
        this.created_at = collection.getCreatedAt();
        this.updated_at = collection.getUpdatedAt();
        this.traits = collection.getTraits();
        this.images = collection.getImages();
        this.venue = collection.getVenue();
        this.order = collection.getOrder();
        this.primary_image = collection.getPrimaryImage();
        this.sort_channel_name = collection.getSortChannelName();
        this.product_count = collection.getWineCount();
        this.isCustomSort = collection.isCustomSort();
        this.isPublic = collection.isPublic();
        this.location_based_recs = collection.isLocationBasedRecs();
        this.is_pinned = collection.isIs_pinned();
        this.is_browsable = collection.isIs_browsable();
        this.archived = collection.isArchived();

        // reset transient
        this.saveDate = null;

        // set so the set call is made
        setUserCollections(collection.getUserCollections());
    }

    public boolean isArchived() {
        return archived;
    }

    public boolean isIs_browsable() {
        return is_browsable;
    }

    public boolean isIs_pinned() {
        return is_pinned;
    }

    public int getWineCount() {
        return product_count;
    }

    public boolean isHasPredictOrder() {
        return has_predict_order;
    }

    public int getLbsOrder() {
        return lbs_order;
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

    public boolean isCustomSort() {
        return isCustomSort;
    }

    public String getSortChannelName() {
        return sort_channel_name;
    }

    public void setCustomSort(boolean customSort) {
        isCustomSort = customSort;
    }

    public boolean isDisplayQuantity() {
        return display_quantity;
    }

    public boolean isLocationBasedRecs() {
        return location_based_recs;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setChannelId(long channel_id) {
        this.channel_id = channel_id;
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

    public ArrayList<Version> getVersions() {
        if (versions == null) {
            versions = new ArrayList<>();
        }
        return versions;
    }

    public Version getFirstVersion() {
        if (getVersions().size() == 0) {
            return new Version();
        }

        return getVersions().get(0);
    }

    public void editUserCollection(Object_UserCollection userCollection) {
        if (userCollections == null) userCollections = new ArrayList<>();
        userCollections.remove(userCollection);
        userCollections.add(userCollection);
        setSavedUserCollection();
    }

    public void addUserCollection(Object_UserCollection userCollection) {
        if (userCollections == null) userCollections = new ArrayList<>();
        userCollections.add(userCollection);
        setSavedUserCollection();
    }

    public void addUserCollections(ArrayList<Object_UserCollection> userCollections) {
        if (userCollections == null) userCollections = new ArrayList<>();
        userCollections.addAll(userCollections);
        setSavedUserCollection();
    }

    public void removeUserCollection(Object_UserCollection userCollection) {
        if (userCollections == null) userCollections = new ArrayList<>();
        userCollections.remove(userCollection);
        setSavedUserCollection();
    }

    public void addVersion(Version version) {
        if (versions == null) versions = new ArrayList<>();
        versions.add(version);
    }

    public void setIs_pinned(boolean is_pinned) {
        this.is_pinned = is_pinned;
    }

    public void deleteOrdering(Version.Group.Ordering ordering) {
        for (Version version : getVersions()) {
            for (Version.Group group : version.getGroups()) {
                for (Version.Group.Ordering orderingInLoop : group.getOrderings()) {
                    if (ordering == orderingInLoop) {
                        group.removeOrdering(orderingInLoop);
                        return;
                    }
                }
            }
        }
    }

    public void updateOrdering(Version.Group.Ordering ordering) {
        for (Version version : getVersions()) {
            for (Version.Group group : version.getGroups()) {
                for (Version.Group.Ordering orderingInLoop : group.getOrderings()) {
                    if (ordering == orderingInLoop) {
                        orderingInLoop.updateWith(ordering);
                        return;
                    }
                }
            }
        }
    }

    public void addVersions(ArrayList<Version> versions) {
        if (this.versions == null) this.versions = new ArrayList<>();
        this.versions.addAll(versions);
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
        this.order = order;
        this.auto_wili = auto_wili;
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
        this.is_pinned = is_pinned;
        this.is_browsable = is_browsable;
        this.archived = archived;

        addImage(image);
    }

    public String getChannelName() {
        return sort_channel_name;
    }

    public Object_Product getWine(long variantId) {
        for (Version version : getVersions()) {
            for (Version.Group group : version.getGroups()) {
                for (Version.Group.Ordering ordering : group.getOrderings()) {
                    if (ordering.getTag().getVariant().getId() == variantId) {
                        return ordering.getTag().getVariant().getWine();
                    }
                }
            }
        }

        return null;
    }

    public void addImage(Object object) {
        if (images == null) images = new ArrayList<>();
        if (object != null) {
            Object_Media image;
            if (object instanceof Object_Media) image = (Object_Media) object;
            else if (object instanceof String) image = new Object_Media((String) object);
            else if (object instanceof Uri) image = new Object_Media((object).toString());
            else if (object instanceof File) image = new Object_Media(((File) object).getAbsolutePath());
            else return;
            images.add(image);
            primary_image = image;
        }
    }

    public void setVenue(Object_Venue venue) {
        this.venue = venue;
    }

    public String getImage() {
        if (primary_image != null) return primary_image.getPath();
        return null;
    }

    public Object_Media getPrimaryImage() {
        return primary_image;
    }

    public Object_Product.WineGroup getWineGroup(Object_Product product) {
        ArrayList<Version.Group> groups = versions.get(0).groups;
        for (Version.Group group : groups) {
            ArrayList<Version.Group.Ordering> orderings = group.getOrderings();
            for (Version.Group.Ordering ordering : orderings) {
                if (ordering.getCollectionVariantTagId() == product.getCollectionTag().getId()) {
                    return new Object_Product.WineGroup(group, ordering);
                }
            }
        }
        return null;
    }

    public boolean isAutoWili() {
        return auto_wili;
    }

    public boolean isSaved() {
        return getSavedUserCollection() != null;
    }

    public Object_UserCollection getSavedUserCollection() {
        return savedUserCollection;
    }

    public void setSavedUserCollection() {
        savedUserCollection = null;

        for (Object_UserCollection userCollection : getUserCollections()) {
            if (userCollection.getRelationship_type().equalsIgnoreCase("saved")) {
                savedUserCollection = userCollection;
            }
        }
    }

    public Object_UserCollection getMyCellarUserCollection() {
        for (Object_UserCollection userCollection : getUserCollections()) {
            if (userCollection.getRelationship_type().equalsIgnoreCase("mycellar")) {
                return userCollection;
            }
        }

        return null;
    }

    public void setUserCollections(ArrayList<Object_UserCollection> userCollections) {
        this.userCollections = userCollections;
        setSavedUserCollection();
    }

    public ArrayList<Object_UserCollection> getUserCollections() {
        if (userCollections == null) userCollections = new ArrayList<>();
        return userCollections;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public Date getSaveDate() {
        if (saveDate == null) {
            saveDate = Tools_Preferabli.convertAPITimeStampToDate(getSavedUserCollection().getCreated_at());
        }
        return saveDate;
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

    public ArrayList<Object_Media> getImages() {
        return images;
    }

    public Object_Venue getVenue() {
        return venue;
    }

    public String getVenueName() {
        if (venue != null) return venue.getDisplayName();
        return "";
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
            } else return false;
        }
        return true;
    }

    public static class Version implements Parcelable {
        private long id;
        private String name;
        private int order;
        private ArrayList<Group> groups;
        private String created_at;
        private String updated_at;

        public Version(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public ArrayList<Group> getGroups() {
            if (groups == null) groups = new ArrayList<>();
            return groups;
        }

        public void addGroup(Group group) {
            if (groups == null) groups = new ArrayList<>();
            groups.add(group);
        }

        public void addGroups(ArrayList<Group> groups) {
            if (this.groups == null) this.groups = new ArrayList<>();
            this.groups.addAll(groups);
        }

        public Group getGroupWithId(long id) {
            for (Group group : getGroups()) {
                if (group.getId() == id) {
                    return group;
                }
            }

            return null;
        }

        public static class Group implements Parcelable {
            private long id;
            private String name;
            private boolean display_name;
            private int order;
            private int orderings_count;
            private ArrayList<Ordering> orderings;
            private String created_at;
            private String updated_at;
            private long version_id;

            public Group(long id, String name, int orderings_count, int order, long version_id) {
                this.id = id;
                this.name = name;
                this.order = order;
                this.orderings_count = orderings_count;
                this.version_id = version_id;
            }

            public long getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public void setId(long id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getVersionId() {
                return version_id;
            }

            public boolean getDisplayName() {
                return display_name;
            }

            public int getOrderingsCount() {
                return orderings_count;
            }

            public int getOrderForNewEntry() {
                if (getOrderings().size() > 0) {
                    ArrayList<Object_Collection.Version.Group.Ordering> sortedOrders = getOrderings();
                    int currentHighestOrder = sortedOrders.get(sortedOrders.size() - 1).order;
                    return currentHighestOrder + 1;
                }

                return 1;
            }

            @Override
            public boolean equals(java.lang.Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Group group = (Group) o;

                return id == group.id;

            }

            @Override
            public int hashCode() {
                return (int) (id ^ (id >>> 32));
            }

            public int getOrder() {
                return order;
            }

            public ArrayList<Ordering> getOrderings() {
                if (this.orderings == null) {
                    this.orderings = new ArrayList<>();
                }
                return orderings;
            }

            public void addOrdering(Ordering ordering) {
                if (this.orderings == null) {
                    this.orderings = new ArrayList<>();
                }
                this.orderings.add(ordering);
            }

            public void removeOrdering(Ordering ordering) {
                if (this.orderings == null) {
                    this.orderings = new ArrayList<>();
                }
                this.orderings.remove(ordering);
            }

            public void addOrderings(ArrayList<Ordering> orderings) {
                if (this.orderings == null) {
                    this.orderings = new ArrayList<>();
                }
                this.orderings.addAll(orderings);
            }

            public void clearOrderings() {
                if (this.orderings == null) {
                    this.orderings = new ArrayList<>();
                }
                this.orderings.clear();
            }

            public String getCreatedAt() {
                return created_at;
            }

            public String getUpdatedAt() {
                return updated_at;
            }

            public static class GroupComparator implements Comparator<Group> {
                @Override
                public int compare(Group group1, Group group2) {
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

            public static ArrayList<Group> sortGroups(ArrayList<Group> groups) {
                Collections.sort(groups, new GroupComparator());
                return groups;
            }

            public static class Ordering extends Object_BaseObject {
                private long tag_id;
                private int order;
                private String created_at;
                private String updated_at;
                private Object_Tag tag;
                private long group_id;
                private boolean dirty;

                public Ordering() {
                    super(-System.currentTimeMillis());
                }

                public Ordering(long id, int order) {
                    super(id);
                    this.order = order;
                }

                public void setGroupId(long group_id) {
                    this.group_id = group_id;
                }

                public Ordering(long id, long tag_id, int order, boolean dirty, long group_id) {
                    super(id);
                    this.tag_id = tag_id;
                    this.order = order;
                    this.dirty = dirty;
                    this.group_id = group_id;
                }

                public void updateWith(Ordering ordering) {
                    setId(ordering.getId());
                    this.tag_id = ordering.getCollectionVariantTagId();
                    this.order = ordering.getOrder();
                    this.created_at = ordering.getCreatedAt();
                    this.updated_at = ordering.getUpdatedAt();
                    this.tag = ordering.getTag();
                    this.group_id = ordering.getGroupId();
                    this.dirty = ordering.isDirty();
                }

                public void setDirty(boolean dirty) {
                    this.dirty = dirty;
                }

                public void setCollectionVariantTagId(long collection_variant_tag_id) {
                    this.tag_id = collection_variant_tag_id;
                }

                public void setOrder(int order) {
                    this.order = order;
                }

                public long getGroupId() {
                    return group_id;
                }


                public Object_Tag getTag() {
                    return tag;
                }

                public void setTag(Object_Tag tag) {
                    this.tag = tag;
                }

                public long getCollectionVariantTagId() {
                    return tag_id;
                }

                public int getOrder() {
                    return order;
                }

                public boolean isDirty() {
                    return dirty || getId() < 0;
                }

                public String getCreatedAt() {
                    return created_at;
                }

                public String getUpdatedAt() {
                    return updated_at;
                }

                public static class OrderComparator implements Comparator<Ordering> {
                    @Override
                    public int compare(Ordering ordering1, Ordering ordering2) {
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

                public static ArrayList<Ordering> sortOrders(ArrayList<Ordering> orderings) {
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
                    dest.writeByte(this.dirty ? (byte) 1 : (byte) 0);
                }

                public Ordering(Object_Tag tag, long groupId, int order) {
                    super(-System.currentTimeMillis());
                    this.tag = tag;
                    this.dirty = true;
                    this.tag_id = tag.getId();
                    this.group_id = groupId;
                    this.order = order;
                }

                protected Ordering(Parcel in) {
                    super(in);
                    this.tag_id = in.readLong();
                    this.order = in.readInt();
                    this.created_at = in.readString();
                    this.updated_at = in.readString();
                    this.group_id = in.readLong();
                    this.dirty = in.readByte() != 0;

                }

                public static final Creator<Ordering> CREATOR = new Creator<Ordering>() {
                    @Override
                    public Ordering createFromParcel(Parcel source) {
                        return new Ordering(source);
                    }

                    @Override
                    public Ordering[] newArray(int size) {
                        return new Ordering[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.id);
                dest.writeString(this.name);
                dest.writeByte(this.display_name ? (byte) 1 : (byte) 0);
                dest.writeInt(this.order);
                dest.writeInt(this.orderings_count);
                dest.writeString(this.created_at);
                dest.writeString(this.updated_at);
                dest.writeTypedList(this.orderings);
                dest.writeLong(this.version_id);
            }

            public Group() {
            }

            protected Group(Parcel in) {
                this.id = in.readLong();
                this.name = in.readString();
                this.display_name = in.readByte() != 0;
                this.order = in.readInt();
                this.orderings_count = in.readInt();
                this.created_at = in.readString();
                this.updated_at = in.readString();
                this.orderings = in.createTypedArrayList(Ordering.CREATOR);
                this.version_id = in.readLong();
            }

            public static final Creator<Group> CREATOR = new Creator<Group>() {
                @Override
                public Group createFromParcel(Parcel source) {
                    return new Group(source);
                }

                @Override
                public Group[] newArray(int size) {
                    return new Group[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeString(this.name);
            dest.writeInt(this.order);
            dest.writeList(this.groups);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
        }

        public Version() {
        }

        protected Version(Parcel in) {
            this.id = in.readLong();
            this.name = in.readString();
            this.order = in.readInt();
            this.groups = new ArrayList<Group>();
            in.readList(this.groups, Group.class.getClassLoader());
            this.created_at = in.readString();
            this.updated_at = in.readString();
        }

        public static final Creator<Version> CREATOR = new Creator<Version>() {
            @Override
            public Version createFromParcel(Parcel source) {
                return new Version(source);
            }

            @Override
            public Version[] newArray(int size) {
                return new Version[size];
            }
        };
    }

    public static class EventDateComparator implements Comparator<Object_Collection> {

        @Override
        public int compare(Object_Collection collection1, Object_Collection collection2) {
            if (collection1 == null && collection2 == null) {
                return 0;
            } else if (collection1 == null) {
                return 1;
            } else if (collection2 == null) {
                return -1;
            } else if (collection2.getStartDate().compareTo(collection1.getStartDate()) == 0) {
                return Tools_Preferabli.alphaSortIgnoreThe(collection1.getName(), collection2.getName());
            } else if (new Date().before(Tools_Preferabli.convertAPITimeStampToDate(collection1.getStartDate())) && new Date().after(Tools_Preferabli.convertAPITimeStampToDate(collection2.getStartDate()))) {
                return -1;
            } else if (new Date().before(Tools_Preferabli.convertAPITimeStampToDate(collection2.getStartDate())) && new Date().after(Tools_Preferabli.convertAPITimeStampToDate(collection1.getStartDate()))) {
                return 1;
            } else if (new Date().after(Tools_Preferabli.convertAPITimeStampToDate(collection2.getStartDate())) && new Date().after(Tools_Preferabli.convertAPITimeStampToDate(collection1.getStartDate()))) {
                return collection2.getStartDate().compareTo(collection1.getStartDate());
            }

            return collection1.getStartDate().compareTo(collection2.getStartDate());
        }
    }

    public static class CollectionDateComparator implements Comparator<Object_Collection> {

        private boolean ascending;

        public CollectionDateComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Object_Collection collection1, Object_Collection collection2) {
            if (collection1 == null && collection2 == null) {
                return 0;
            } else if (collection1 == null) {
                return 1;
            } else if (collection2 == null) {
                return -1;
            } else if (collection2.getStartDate().compareTo(collection1.getStartDate()) == 0) {
                return Tools_Preferabli.alphaSortIgnoreThe(collection1.getName(), collection2.getName());
            }

            if (ascending) {
                return collection1.getStartDate().compareTo(collection2.getStartDate());
            } else {
                return collection2.getStartDate().compareTo(collection1.getStartDate());
            }

        }
    }

    public static class CollectionUpdatedDateComparator implements Comparator<Object_Collection> {

        private boolean ascending;

        public CollectionUpdatedDateComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Object_Collection collection1, Object_Collection collection2) {
            if (collection1 == null && collection2 == null) {
                return 0;
            } else if (collection1 == null) {
                return 1;
            } else if (collection2 == null) {
                return -1;
            }

            if (ascending) {
                return collection1.getUpdatedAt().compareTo(collection2.getUpdatedAt());
            } else {
                return collection2.getUpdatedAt().compareTo(collection1.getUpdatedAt());
            }
        }
    }

    public static class CollectionNameComparator implements Comparator<Object_Collection> {

        private boolean ascending;

        public CollectionNameComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(Object_Collection collection1, Object_Collection collection2) {
            if (collection1 == null && collection2 == null) {
                return 0;
            } else if (collection1 == null) {
                return 1;
            } else if (collection2 == null) {
                return -1;
            }

            if (ascending) {
                return Tools_Preferabli.alphaSortIgnoreThe(collection1.getName(), collection2.getName());
            } else {
                return Tools_Preferabli.alphaSortIgnoreThe(collection2.getName(), collection1.getName());
            }
        }
    }

    public static ArrayList<Object_Collection> sortCollectionsByName(ArrayList<Object_Collection> collections, boolean ascending) {
        Collections.sort(collections, new CollectionNameComparator(ascending));
        return collections;
    }

    public static ArrayList<Object_Collection> sortCollectionsByDate(ArrayList<Object_Collection> collections, boolean ascending) {
        Collections.sort(collections, new CollectionDateComparator(ascending));
        return collections;
    }

    public static ArrayList<Object_Collection> sortEventsByDate(ArrayList<Object_Collection> events) {
        Collections.sort(events, new EventDateComparator());
        return events;
    }

    public static ArrayList<Object_Collection> sortCollectionsByUpdatedDate(ArrayList<Object_Collection> collections, boolean ascending) {
        Collections.sort(collections, new CollectionUpdatedDateComparator(ascending));
        return collections;
    }


    public static class LBSOrderComparator implements Comparator<Object_Collection> {
        @Override
        public int compare(Object_Collection collection1, Object_Collection collection2) {
            if (collection1 == null && collection2 == null) {
                return 0;
            } else if (collection1 == null) {
                return 1;
            } else if (collection2 == null) {
                return -1;
            } else if (collection1.getLbsOrder() == collection2.getLbsOrder()) {
                return collection1.getName().compareToIgnoreCase(collection2.getName());
            } else {
                return ((Integer) collection1.getLbsOrder()).compareTo(collection2.getLbsOrder());
            }
        }
    }

    public static ArrayList<Object_Collection> sortCollectionsByLBSOrder(ArrayList<Object_Collection> collections) {
        Collections.sort(collections, new LBSOrderComparator());
        return collections;
    }

    public boolean isDisplayGroupHeadings() {
        return display_group_headings;
    }

    public boolean isInventory() {
        for (Object_CollectionTrait trait : getTraits()) {
            if (trait.getId() == 86)
                return true;
        }
        return false;
    }

    public boolean isEvent() {
        for (Object_CollectionTrait trait : getTraits()) {
            if (trait.getId() == 84 || trait.getId() == 88 || trait.getId() == 90)
                return true;
        }
        return false;
    }

    public boolean isFeatured() {
        for (Object_CollectionTrait trait : getTraits()) {
            if (trait.getId() == 102)
                return true;
        }
        return false;
    }

    public boolean shouldAutoSave() {
        if (isSaved()) {
            return false;
        }

        for (Object_CollectionTrait trait : getTraits()) {
            if (trait.getId() == 90)
                return true;
            else if (trait.getId() == 84)
                return true;
            else if (trait.getId() == 88)
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
        dest.writeInt(this.lbs_order);
        dest.writeString(this.start_date);
        dest.writeByte(this.auto_wili ? (byte) 1 : (byte) 0);
        dest.writeString(this.end_date);
        dest.writeString(this.comment);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeTypedList(this.versions);
        dest.writeTypedList(this.traits);
        dest.writeTypedList(this.images);
        dest.writeParcelable(this.venue, flags);
        dest.writeInt(this.order);
        dest.writeParcelable(this.primary_image, flags);
        dest.writeString(this.sort_channel_name);
        dest.writeByte(this.has_predict_order ? (byte) 1 : (byte) 0);
        dest.writeInt(this.product_count);
        dest.writeTypedList(this.userCollections);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeByte(this.location_based_recs ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_pinned ? (byte) 1 : (byte) 0);
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
        this.lbs_order = in.readInt();
        this.start_date = in.readString();
        this.auto_wili = in.readByte() != 0;
        this.end_date = in.readString();
        this.comment = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.versions = in.createTypedArrayList(Version.CREATOR);
        this.traits = in.createTypedArrayList(Object_CollectionTrait.CREATOR);
        this.images = in.createTypedArrayList(Object_Media.CREATOR);
        this.venue = in.readParcelable(Object_Venue.class.getClassLoader());
        this.order = in.readInt();
        this.primary_image = in.readParcelable(Object_Media.class.getClassLoader());
        this.sort_channel_name = in.readString();
        this.has_predict_order = in.readByte() != 0;
        this.product_count = in.readInt();
        this.userCollections = in.createTypedArrayList(Object_UserCollection.CREATOR);
        this.isPublic = in.readByte() != 0;
        this.location_based_recs = in.readByte() != 0;
        this.is_pinned = in.readByte() != 0;
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
}
