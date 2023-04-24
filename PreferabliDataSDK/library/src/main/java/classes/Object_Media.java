//
//  Object_Media.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import java.util.ArrayList;

/**
 * An image or video.
 */
public class Object_Media extends Object_BaseObject {
    private long user_id;
    private String path;
    private String updated_at;
    private String created_at;
    private String position;
    private String type;

    public String getType() {
        return type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getUserId() {
        return user_id;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public Object_Media(long id, String path, String position) {
        super(id);
        this.path = path;
        this.position = position;
    }

    public Object_Media(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Get the media's path for display as an image. Only use if the media is an image.
     * @param width returns an image with the specified width in pixels.
     * @param height returns an image with the specified height in pixels.
     * @param quality returns an image with the specified quality. Scales from 0 - 100.
     * @return the URL of the requested image.
     */
    public String getImage(int width, int height, int quality) {
        return Tools_Preferabli.getImageUrl(this, width, height, quality);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.user_id);
        dest.writeString(this.path);
        dest.writeString(this.updated_at);
        dest.writeString(this.created_at);
        dest.writeString(this.position);
        dest.writeString(this.type);
    }

    protected Object_Media(Parcel in) {
        super(in);
        this.user_id = in.readLong();
        this.path = in.readString();
        this.updated_at = in.readString();
        this.created_at = in.readString();
        this.position = in.readString();
        this.type = in.readString();
    }

    public static final Creator<Object_Media> CREATOR = new Creator<Object_Media>() {
        @Override
        public Object_Media createFromParcel(Parcel source) {
            return new Object_Media(source);
        }

        @Override
        public Object_Media[] newArray(int size) {
            return new Object_Media[size];
        }
    };
}
