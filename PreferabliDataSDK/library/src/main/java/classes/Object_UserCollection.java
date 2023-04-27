//
//  Object_UserCollection.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;

/**
 * Internal class used for connecting collections to users.
 */
class Object_UserCollection extends Object_BaseObject {
    private String created_at;
    private String updated_at;
    private String archived_at;
    private long collection_id;
    private boolean is_admin;
    private boolean is_editor;
    private boolean is_pinned;
    private boolean is_viewer;
    private String relationship_type;
    private Object_Collection collection;

    Object_UserCollection(long id, String created_at, String updated_at, String archived_at, long collection_id, boolean is_admin, boolean is_editor, boolean is_pinned, boolean is_viewer, String relationship_type) {
        super(id);
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.archived_at = archived_at;
        this.collection_id = collection_id;
        this.is_admin = is_admin;
        this.is_editor = is_editor;
        this.is_pinned = is_pinned;
        this.is_viewer = is_viewer;
        this.relationship_type = relationship_type;
    }

    String getCreatedAt() {
        return created_at;
    }

    String getUpdatedAt() {
        return updated_at;
    }

    String getArchivedAt() {
        return archived_at;
    }

    long getCollectionId() {
        return collection_id;
    }

    boolean isAdmin() {
        return is_admin;
    }

    boolean isEditor() {
        return is_editor;
    }

    boolean isPinned() {
        return is_pinned;
    }

    boolean isViewer() {
        return is_viewer;
    }


    public String getRelationshipType() {
        return relationship_type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.archived_at);
        dest.writeLong(this.collection_id);
        dest.writeByte(this.is_admin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_editor ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_pinned ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_viewer ? (byte) 1 : (byte) 0);
        dest.writeString(this.relationship_type);
    }

    protected Object_UserCollection(Parcel in) {
        super(in);
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.archived_at = in.readString();
        this.collection_id = in.readLong();
        this.is_admin = in.readByte() != 0;
        this.is_editor = in.readByte() != 0;
        this.is_pinned = in.readByte() != 0;
        this.is_viewer = in.readByte() != 0;
        this.relationship_type = in.readString();
    }

    Object_Collection getCollection() {
        return collection;
    }

    void setCollection(Object_Collection collection) {
        this.collection = collection;
    }

    public static final Parcelable.Creator<Object_UserCollection> CREATOR = new Parcelable.Creator<Object_UserCollection>() {
        @Override
        public Object_UserCollection createFromParcel(Parcel source) {
            return new Object_UserCollection(source);
        }

        @Override
        public Object_UserCollection[] newArray(int size) {
            return new Object_UserCollection[size];
        }
    };
}
