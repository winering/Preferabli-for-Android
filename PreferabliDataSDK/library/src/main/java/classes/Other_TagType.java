//
//  Other_TagType.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public enum Other_TagType {
    RATING,
    WISHLIST,
    CELLAR,
    PURCHASE,
    OTHER;

    public static Other_TagType getTagTypeBasedOffDatabaseName(String type) {
        if (type != null) switch (type) {
            case "rating":
                return Other_TagType.RATING;
            case "wishlist":
                return Other_TagType.WISHLIST;
            case "collection":
                return Other_TagType.CELLAR;
            case "purchase":
                return Other_TagType.PURCHASE;
            default:
                return Other_TagType.OTHER;
        }

        return null;
    }

    public String getDatabaseName() {
        switch (this) {
            case RATING:
                return "rating";
            case WISHLIST:
                return "wishlist";
            case CELLAR:
                return "collection";
            case PURCHASE:
                return "purchase";
            case OTHER:
                return "other";
        }

        return null;
    }

}
