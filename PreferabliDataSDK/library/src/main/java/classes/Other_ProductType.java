//
//  Other_ProductType.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public enum Other_ProductType {
    RED("red"),
    WHITE("white"),
    ROSE("rosé"),
    SPARKLING("sparkling"),
    FORTIFIED("fortified"),
    OTHER("other");

    private String name;

    Other_ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Other_ProductType getProductTypeFromString(String wineType) {
        if (Tools_PreferabliTools.isNullOrWhitespace(wineType)) {
            return OTHER;
        } else if (wineType.equalsIgnoreCase("red")) {
            return RED;
        } else if (wineType.equalsIgnoreCase("white")) {
            return WHITE;
        } else if (wineType.equalsIgnoreCase("rosé")) {
            return ROSE;
        } else if (wineType.equalsIgnoreCase("fortified")) {
            return FORTIFIED;
        } else if (wineType.equalsIgnoreCase("sparkling")) {
            return SPARKLING;
        }

        return OTHER;
    }
}
