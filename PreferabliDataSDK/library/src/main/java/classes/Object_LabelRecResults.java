//
//  Object_LabelRecResults.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/25/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.util.ArrayList;

public class Object_LabelRecResults {
    private ArrayList<Object_LabelRecResult> results;
    private Object_Media media;

    public Object_LabelRecResults(Object_Media media, ArrayList<Object_LabelRecResult> results) {
        this.media = media;
        this.results = results;
    }

    public Object_Media getMedia() {
        return media;
    }

    public ArrayList<Object_LabelRecResult> getResults() {
        return results;
    }
}
