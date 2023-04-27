//
//  Object_WhereToBuy.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.util.ArrayList;

/**
 * Container object returned by {@link Preferabli#whereToBuy(long, Other_FulfillSort, Boolean, Boolean, API_ResultHandler)}. This object will include an array of either {@link Object_MerchantProductLink}s (if sorted by price) or {@link Object_Venue}s (if sorted by distance).
 */
public class Object_WhereToBuy {

    private ArrayList<Object_MerchantProductLink> links;
    private ArrayList<Object_Venue> venues;

    public Object_WhereToBuy(ArrayList<Object_MerchantProductLink> links, ArrayList<Object_Venue> venues) {
        this.links = links;
        this.venues = venues;
    }

    public ArrayList<Object_MerchantProductLink> getLinks() {
        return links;
    }

    public ArrayList<Object_Venue> getVenues() {
        return venues;
    }
}
