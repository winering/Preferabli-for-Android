//
//  Object_GuidedRec.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A Guided Rec questionnaire. Returned by {@link Preferabli#getGuidedRec(long, API_ResultHandler)}.
 */
public class Object_GuidedRec extends Object_BaseObject {

    /**
     * The default wine questionnaire.
     */
    public static long WINE_DEFAULT = 1;

    private String name;
    private String default_currency;
    private int default_price_min;
    private int default_price_max;
    private int max_results_per_type;
    private ArrayList<Object_GuidedRecQuestion> questions;

    public ArrayList<Object_GuidedRecQuestion> getQuestions() {
        return questions;
    }

    public int getMaxResultsPerType() {
        return max_results_per_type;
    }

    public void addQuestion(Object_GuidedRecQuestion question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }

        questions.add(question);
    }

    /**
     * A question within a {@link Object_GuidedRec} questionnaire.
     */
    public static class Object_GuidedRecQuestion extends Object_BaseObject {
        private int number;
        private String type;
        private ArrayList<Object_GuidedRecChoice> choices;
        private int minimum_selected;
        private int maximum_selected;
        private String text;

        public Object_GuidedRecQuestion(int number, String type, ArrayList<Object_GuidedRecChoice> choices, int minimum_selected, int maximum_selected, String text) {
            this.number = number;
            this.type = type;
            this.choices = choices;
            this.minimum_selected = minimum_selected;
            this.maximum_selected = maximum_selected;
            this.text = text;
        }

        public ArrayList<Object_GuidedRecChoice> getChoices() {
            return choices;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public int getMinimumSelected() {
            return minimum_selected;
        }

        public int getMaximumSelected() {
            return maximum_selected;
        }
    }

    /**
     * A choice within a {@link Object_GuidedRecQuestion}. Pass an array of these to get results from {@link Preferabli#getGuidedRecResults(long, ArrayList, Integer, Integer, Long, Boolean, API_ResultHandler)}.
     */
    public static class Object_GuidedRecChoice extends Object_BaseObject {
        private int number;
        private ArrayList<Long> requires_choice_ids;
        private String text;
        private boolean selectable;

        public ArrayList getRequiresChoiceIds() {
            if (requires_choice_ids == null) {
                requires_choice_ids = new ArrayList();
            }
            return requires_choice_ids;
        }

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }


        public boolean isSelectable() {
            return selectable;
        }

        public static ArrayList<Object_GuidedRecChoice> sortGuidedRecChoices(ArrayList<Object_GuidedRecChoice> guided_rec_choices) {
            Collections.sort(guided_rec_choices, new GuidedRecChoiceComparator());
            return guided_rec_choices;
        }

        private static class GuidedRecChoiceComparator implements Comparator<Object_GuidedRecChoice> {

            @Override
            public int compare(Object_GuidedRecChoice quizChoice1, Object_GuidedRecChoice quizChoice2) {
                if (quizChoice1 == null && quizChoice2 == null) {
                    return 0;
                } else if (quizChoice1 == null) {
                    return 1;
                } else if (quizChoice2 == null) {
                    return -1;
                } else if (quizChoice1.isSelectable() && !quizChoice2.isSelectable()) {
                    return -1;
                } else if (!quizChoice1.isSelectable() && quizChoice2.isSelectable()) {
                    return 1;
                }

                return ((Integer) quizChoice1.getNumber()).compareTo(quizChoice2.getNumber());
            }
        }
    }
}
