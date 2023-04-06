//
//  Object_GuidedRec.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object_GuidedRec extends Object_BaseObject {
    private String name;
    private String default_currency;
    private int default_price_min;
    private int default_price_max;
    private int max_results_per_type;
    private ArrayList<Object_GuidedRecQuestion> questions;

    public ArrayList<Object_GuidedRecQuestion> getQuestions() {
        return questions;
    }

    public int getMax_results_per_type() {
        return max_results_per_type;
    }

    public void addQuestion(Object_GuidedRecQuestion question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }

        questions.add(question);
    }

    public static class Object_GuidedRecQuestion extends Object_BaseObject {
        private int number;
        private String type;
        private ArrayList<Object_GuidedRecChoice> choices;
        private int minimum_selected;
        private int maximum_selected;
        private String text;

        public Object_GuidedRecQuestion() {
            super();
        }

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

        public void setChoices(ArrayList<Object_GuidedRecChoice> choices) {
            this.choices = choices;
        }

        public String getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public int getMinimum_selected() {
            return minimum_selected;
        }

        public int getMaximum_selected() {
            return maximum_selected;
        }
    }

    public static class Object_GuidedRecChoice extends Object_BaseObject {
        private long id;
        private int number;
        private ArrayList<Long> requires_choice_ids;
        private String text;
        private boolean selected;
        private boolean selectable;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
        }

        protected Object_GuidedRecChoice(Parcel in) {
            this.id = in.readLong();
        }


        public static final Parcelable.Creator<Object_GuidedRecChoice> CREATOR = new Parcelable.Creator<Object_GuidedRecChoice>() {
            @Override
            public Object_GuidedRecChoice createFromParcel(Parcel source) {
                return new Object_GuidedRecChoice(source);
            }

            @Override
            public Object_GuidedRecChoice[] newArray(int size) {
                return new Object_GuidedRecChoice[size];
            }
        };

        public Object_GuidedRecChoice(int number, String text) {
            this.number = number;
            this.text = text;
        }

        public ArrayList getRequires_choice_ids() {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Object_GuidedRecChoice quizChoice = (Object_GuidedRecChoice) o;
            return id == quizChoice.id;
        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }

        public long getId() {
            return id;
        }

        public boolean isSelected() {
            return selected;
        }

        public boolean isSelectable() {
            return selectable;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public static ArrayList<Object_GuidedRecChoice> sortQuizChoices(ArrayList<Object_GuidedRecChoice> quizChoices) {
            Collections.sort(quizChoices, new QuizChoiceComparator());
            return quizChoices;
        }

        public static class QuizChoiceComparator implements Comparator<Object_GuidedRecChoice> {

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
