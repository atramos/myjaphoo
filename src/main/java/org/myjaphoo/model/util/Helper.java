/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.util;

import java.util.ResourceBundle;
import org.myjaphoo.model.db.MovieEntry;

/**
 *
 * @author mla
 */
public class Helper {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/util/resources/Helper");

    public static String[] splitPathName(String pathName) {
        return org.apache.commons.lang.StringUtils.split(pathName, "//:\\"); //NOI18N
    }
    
    public static String getRatingCategoryText(MovieEntry entry) {
        if (entry.getRating() == null) {
            return localeBundle.getString("-- OHNE RATING --");
        } else {
            return entry.getRating().getName();
        }
    }    
}
