//
//  Object_PreferenceData.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/13/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public class Object_PreferenceData {
    private String title;
    private String details;
    private int confidence_code;
    private int formatted_predict_rating;

    public Object_PreferenceData(String title, String details, int confidence_code, int formatted_predict_rating) {
        this.title = title;
        this.details = details;
        this.confidence_code = confidence_code;
        this.formatted_predict_rating = formatted_predict_rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public int getConfidence_code() {
        return confidence_code;
    }

    public int getFormatted_predict_rating() {
        return formatted_predict_rating;
    }

    @Override
    public String toString() {
        return title + " - " + details;
    }
}
