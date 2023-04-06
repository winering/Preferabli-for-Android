//
//  Other_ProductCategory.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public enum Other_ProductCategory {
    WHISKEY("whiskey"),
    MEZCAL("tequila"),
    BEER("beer"),
    WINE("wine"),
    NONE("");

    private String name;

    Other_ProductCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Other_ProductCategory getProductCategoryFromString(String category) {
        if (Tools_PreferabliTools.isNullOrWhitespace(category)) {
            return NONE;
        } else if (category.equalsIgnoreCase("whiskey")) {
            return WHISKEY;
        } else if (category.equalsIgnoreCase("tequila")) {
            return MEZCAL;
        } else if (category.equalsIgnoreCase("beer")) {
            return BEER;
        } else if (category.equalsIgnoreCase("wine")) {
            return WINE;
        }

        return NONE;
    }
}
