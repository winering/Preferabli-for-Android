//
//  Object_Recommendation.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.util.ArrayList;

/**
 * Container object returned by {@link Preferabli#getRecs(Object_Product.Other_ProductCategory, Object_Product.Other_ProductType, Long, Integer, Integer, ArrayList, ArrayList, Boolean, API_ResultHandler)}.
 */
public class Object_Recommendation {
    private ArrayList<Object_Product> products;
    private String message;

    public Object_Recommendation(String message, ArrayList<Object_Product> products) {
        this.message = message;
        this.products = products;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Object_Product> getProducts() {
        return products;
    }
}
