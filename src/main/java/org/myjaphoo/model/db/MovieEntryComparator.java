/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import java.util.Comparator;

/**
 *
 * @author mla
 */
public class MovieEntryComparator implements Comparator<MovieEntry> {

    @Override
    public int compare(MovieEntry o1, MovieEntry o2) {
        return o1.getCanonicalPath().compareTo(o2.getCanonicalPath());
    }

}
