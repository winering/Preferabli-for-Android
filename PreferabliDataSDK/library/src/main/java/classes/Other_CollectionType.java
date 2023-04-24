//
//  Other_CollectionType.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/28/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

/**
 * The type of a {@link Object_Collection}.
 */
public enum Other_CollectionType {
    EVENT,
    INVENTORY,
    CELLAR,
    OTHER;

    public static Other_CollectionType getCollectionTypeBasedOffCollection(Object_Collection collection) {
        if (collection.isMyCellar()) {
            return CELLAR;
        } else if (collection.isEvent()) {
            return EVENT;
        } else if (collection.isInventory()) {
            return INVENTORY;
        }

        return OTHER;
    }
}
