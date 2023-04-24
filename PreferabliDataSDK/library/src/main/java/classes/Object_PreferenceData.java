//
//  Object_PreferenceData.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

/**
 * Indicates a user's level of preference for a specific {@link Object_Product}.
 */
public class Object_PreferenceData {
    private String title;
    private String details;
    /**
     * How confident we are in our rating.
     */
    private int confidence_code;
    /**
     * A score from 85 - 100 which informs us how likely a user is to enjoy a product. Null if the user will not like the product.
     */
    private Integer formatted_predict_rating;

    public Object_PreferenceData(String title, String details, int confidence_code, int formatted_predict_rating) {
        this.title = title;
        this.details = details;
        this.confidence_code = confidence_code;
        this.formatted_predict_rating = formatted_predict_rating;
    }

    public Object_PreferenceData(int confidence_code, int formatted_predict_rating) {
        this.confidence_code = confidence_code;
        this.formatted_predict_rating = formatted_predict_rating;
    }

    public Object_PreferenceData(int formatted_predict_rating) {
        this.formatted_predict_rating = formatted_predict_rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    int getConfidenceCode() {
        return confidence_code;
    }

    public int getFormattedPredictRating() {
        return formatted_predict_rating;
    }

    @Override
    public String toString() {
        return title + " - " + details;
    }
}
