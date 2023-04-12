//
//  Other_Sort.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public class Other_Sort {

    private SortType type;
    private boolean ascending;

    public Other_Sort(SortType type, boolean ascending) {
        this.type = type;
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public void setType(SortType type) {
        this.type = type;
    }

    public SortType getType() {
        return type;
    }

    public enum SortType {
        PRICE,
        DATE,
        ALPHABETICAL,
        REGION,
        GRAPE,
        RATING,
        TYPE,
        LAST_UPDATED,
        DISTANCE,
        DEFAULT;
    }
}