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

/**
 * Internal class used for tracking searches.
 */
class Object_Search {

    private int count;
    private String last_searched;
    private String text;

    Object_Search(String text) {
        this.text = text;
    }

    Object_Search(int count, String last_searched, String text) {
        this.count = count;
        this.last_searched = last_searched;
        this.text = text;
    }

    String getLastSearched() {
        return last_searched;
    }

    String getText() {
        return text;
    }

    int getCount() {
        return count;
    }

    static ArrayList<Object_Search> sortSearchesByDate(ArrayList<Object_Search> searches) {
        Collections.sort(searches, new SearchDateComparator());
        return searches;
    }

    static class SearchDateComparator implements Comparator<Object_Search> {

        @Override
        public int compare(Object_Search search1, Object_Search search2) {
            if (search1 == null && search2 == null) {
                return 0;
            } else if (search1 == null) {
                return 1;
            } else if (search2 == null) {
                return -1;
            }

            return search2.getLastSearched().compareTo(search1.getLastSearched());
        }
    }

    static ArrayList<Object_Search> sortSearchesByCount(ArrayList<Object_Search> searches) {
        Collections.sort(searches, new SearchCountComparator());
        return searches;
    }

    static class SearchCountComparator implements Comparator<Object_Search> {

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
