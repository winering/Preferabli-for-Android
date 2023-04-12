//
//  Object_WhereToBuy.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.util.ArrayList;

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
