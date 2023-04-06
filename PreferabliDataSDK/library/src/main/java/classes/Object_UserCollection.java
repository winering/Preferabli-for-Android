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

public class Object_UserCollection extends Object_BaseObject {
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

    public Object_UserCollection(long id, String created_at, String updated_at, String archived_at, long collection_id, boolean is_admin, boolean is_editor, boolean is_pinned, boolean is_viewer, String relationship_type) {
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

    public void setCollection(Object_Collection collection) {
        this.collection = collection;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getArchived_at() {
        return archived_at;
    }

    public long getCollection_id() {
        return collection_id;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public boolean isIs_editor() {
        return is_editor;
    }

    public boolean isIs_pinned() {
        return is_pinned;
    }

    public boolean isIs_viewer() {
        return is_viewer;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public void setIs_editor(boolean is_editor) {
        this.is_editor = is_editor;
    }

    public void setIs_viewer(boolean is_viewer) {
        this.is_viewer = is_viewer;
    }

    public String getRelationship_type() {
        return relationship_type;
    }

    public Object_Collection getCollection() {
        return collection;
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Object_UserCollection() {
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
