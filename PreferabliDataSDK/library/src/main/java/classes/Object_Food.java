//
//  Object_Food.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object_Food extends Object_BaseObject {
    private String name;
    private String description;
    private String created_at;
    private String updated_at;
    private String keywords;
    private String food_category_url;

    public String getKeywords() {
        return keywords;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean filter(Serializable serializable) {
        String constraint = ((String) serializable).toLowerCase();
        String[] terms = constraint.split("\\s+");
        for (String term : terms) {
            if (getName() != null && getName().toLowerCase().contains(term))
                continue;
            else if (getKeywords() != null && getKeywords().toLowerCase().contains(term))
                continue;
            else return false;
        }
        return true;
    }

    public String getFood_category_url() {
        return food_category_url;
    }

    public static class FoodComparator implements Comparator<Object_Food> {
        @Override
        public int compare(Object_Food food1, Object_Food food2) {
            if (food1 == null && food2 == null) {
                return 0;
            } else if (food1 == null) {
                return 1;
            } else if (food2 == null) {
                return -1;
            } else {
                return Tools_Preferabli.alphaSortIgnoreThe(food1.getName(), food2.getName());
            }
        }
    }

    public String getImage() {
        return food_category_url;
    }

    public static ArrayList<Object_Food> sortFoods(ArrayList<Object_Food> foods) {
        Collections.sort(foods, new FoodComparator());
        return foods;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.keywords);
        dest.writeString(this.food_category_url);
    }

    protected Object_Food(Parcel in) {
        super(in);
        this.name = in.readString();
        this.description = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.keywords = in.readString();
        this.food_category_url = in.readString();
    }

    public static final Creator<Object_Food> CREATOR = new Creator<Object_Food>() {
        @Override
        public Object_Food createFromParcel(Parcel source) {return new Object_Food(source);}

        @Override
        public Object_Food[] newArray(int size) {return new Object_Food[size];}
    };
}
