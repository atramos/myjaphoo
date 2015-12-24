/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import java.util.ResourceBundle;
/**
 *
 * @author 
 */
public enum Rating {

    NONE("6. none"),
    VERY_BAD("5. very bad"),
    BAD("4. bad"),
    MIDDLE("3. middle"),
    GOOD("2. good"),
    VERY_GOOD("1. very good");
    
    private transient String name;

    private Rating(String name) {
        this.name = name;
    }

    public String getName() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/db/resources/Rating");
        return localeBundle.getString(name);
    }
}
