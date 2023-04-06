//
//  Object_FoodCategory.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by nluss on 7/25/2016.
 */
public class Object_FoodCategory extends Object_BaseObject {
    private String name;
    private String icon_url;

    public String getName() {
        return name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public static class FoodCategoryComparator implements Comparator<Object_FoodCategory> {
        @Override
        public int compare(Object_FoodCategory food1, Object_FoodCategory food2) {
            if (food1 == null && food2 == null) {
                return 0;
            } else if (food1 == null) {
                return 1;
            } else if (food2 == null) {
                return -1;
            } else {
                return Tools_PreferabliTools.alphaSortIgnoreThe(food1.getName(), food2.getName());
            }
        }
    }

    public static ArrayList<Object_FoodCategory> sortFoods(ArrayList<Object_FoodCategory> foods) {
        Collections.sort(foods, new FoodCategoryComparator());
        return foods;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.icon_url);
    }

    protected Object_FoodCategory(Parcel in) {
        super(in);
        this.name = in.readString();
        this.icon_url = in.readString();
    }
}
