//
//  Object_Search.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 6/30/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object_Search {

    private int count;
    private String last_searched;
    private String text;

    public Object_Search(String text) {
        this.text = text;
    }

    public Object_Search(int count, String last_searched, String text) {
        this.count = count;
        this.last_searched = last_searched;
        this.text = text;
    }

    public String getLast_searched() {
        return last_searched;
    }

    public String getText() {
        return text;
    }

    public int getCount() {
        return count;
    }

    public static ArrayList<Object_Search> sortSearchesByDate(ArrayList<Object_Search> searches) {
        Collections.sort(searches, new SearchDateComparator());
        return searches;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLast_searched(String last_searched) {
        this.last_searched = last_searched;
    }

    public static class SearchDateComparator implements Comparator<Object_Search> {

        @Override
        public int compare(Object_Search search1, Object_Search search2) {
            if (search1 == null && search2 == null) {
                return 0;
            } else if (search1 == null) {
                return 1;
            } else if (search2 == null) {
                return -1;
            }

            return search2.getLast_searched().compareTo(search1.getLast_searched());
        }
    }

    public static ArrayList<Object_Search> sortSearchesByCount(ArrayList<Object_Search> searches) {
        Collections.sort(searches, new SearchCountComparator());
        return searches;
    }

    public static class SearchCountComparator implements Comparator<Object_Search> {

        @Override
        public int compare(Object_Search search1, Object_Search search2) {
            if (search1 == null && search2 == null) {
                return 0;
            } else if (search1 == null) {
                return 1;
            } else if (search2 == null) {
                return -1;
            }

            return ((Integer) search2.getCount()).compareTo(search1.getCount());
        }
    }
}
