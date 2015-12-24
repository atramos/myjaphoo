/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import java.util.ResourceBundle;

/**
 * Token types.
 * @author mla
 */
public enum TokenType {

    UNBESTIMMT("unbestimmt"),
    MOVIENAME("Movie"),
    DARSTELLER("Darsteller"),
    SERIE("Serie"),
    THEMA("Thema");

    private String name;

    TokenType(String name) {
        this.name=name;
    }

    @Override
    public String toString() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/db/resources/TokenType");
        return localeBundle.getString(name);
    }

    public String getGuiName() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/db/resources/TokenType");
        return localeBundle.getString(name);
    }


}
