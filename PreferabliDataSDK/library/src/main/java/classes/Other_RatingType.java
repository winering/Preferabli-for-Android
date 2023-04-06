//
//  Other_RatingType.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/5/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public enum Other_RatingType {
    LOVE,
    LIKE,
    SOSO,
    DISLIKE,
    NONE;

    public static Other_RatingType getRatingTypeBasedOffTagValue(String value) {
        if (value != null) switch (value) {
            case "0":
                return Other_RatingType.NONE;
            case "1":
                return Other_RatingType.DISLIKE;
            case "2":
                return Other_RatingType.SOSO;
            case "3":
                return Other_RatingType.LIKE;
            case "4":
                return Other_RatingType.LOVE;
        }

        return Other_RatingType.NONE;
    }

    public String getValue() {
        switch (this) {
            case LOVE:
                return "4";
            case LIKE:
                return "3";
            case SOSO:
                return "2";
            case DISLIKE:
                return "1";
            case NONE:
                return "0";
        }

        return "0";
    }
}
