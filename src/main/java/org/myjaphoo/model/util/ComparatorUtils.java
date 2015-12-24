/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.util;

import java.util.Date;

/**
 *
 * @author mla
 */
public class ComparatorUtils {

    public static boolean equalsTo(Date d1, Date d2) {
        return compareTo(d1, d2) == 0;
    }

    public static int compareTo(String txt1, String txt2) {
        if (txt1 == null && txt2 == null) {
            return 0;
        }
        if (txt1 == null) {
            return -1;
        }
        if (txt2 == null) {
            return 1;
        }
        return txt1.compareTo(txt2);
    }

    public static int compareTo(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return 0;
        }
        if (d1 == null) {
            return -1;
        }
        if (d2 == null) {
            return 1;
        }
        return d1.compareTo(d2);
    }
}
