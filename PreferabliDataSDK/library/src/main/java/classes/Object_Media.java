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

public class Object_Media extends Object_BaseObject {
    private long user_id;
    private String path;
    private String updated_at;
    private String created_at;
    private String position;
    private String type;
    private ArrayList<Object_LabelRecResult> imageRecResults;
    private boolean needsLinking;
    private boolean selected;

    public void setImageRecResults(ArrayList<Object_LabelRecResult> imageRecResults) {
        if (imageRecResults == null) {
            this.imageRecResults = null;
            return;
        }
        this.imageRecResults = Object_LabelRecResult.sortLabelRecs(imageRecResults);
        Tools_Database.getInstance().openDatabase();
        for (Object_LabelRecResult imageRec : imageRecResults) {
            Object_Product product = imageRec.getProduct();
            product.setRateSourceLocation("label_rec");
            Tools_Database.getInstance().updateProductTable(product);
            for (Object_Variant variant : product.getVariants()) {
                variant.addTags(Tools_Database.getInstance().getVariantTags(variant));
            }
        }
        Tools_Database.getInstance().closeDatabase();
    }

    public ArrayList<Object_Product> getImageRecResults() {
        ArrayList<Object_Product> results = new ArrayList<>();
        for (Object_LabelRecResult rec : imageRecResults) {
            results.add(rec.getProduct());
        }
        return results;
    }

    public String getType() {
        return type;
    }

    public void setNeedsLinking(boolean needsLinking) {
        this.needsLinking = needsLinking;
    }

    public boolean isNeedsLinking() {
        return needsLinking;
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

    public Object_Media() {
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
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
        dest.writeTypedList(this.imageRecResults);
        dest.writeString(this.position);
        dest.writeByte(this.needsLinking ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
    }

    protected Object_Media(Parcel in) {
        super(in);
        this.user_id = in.readLong();
        this.path = in.readString();
        this.updated_at = in.readString();
        this.created_at = in.readString();
        this.imageRecResults = in.createTypedArrayList(Object_LabelRecResult.CREATOR);
        this.position = in.readString();
        this.needsLinking = in.readByte() != 0;
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
