/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.util;

import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public enum ColorizationType {
    NONE("no colors"),
    SAME_DIR("colorize by dir"),
    SAME_CHECKSUM("colorize by checksum"),
    SAME_TOKEN("colorize by tags"),
    SAME_STRUCT_PATH("colorize by structure path"),
    SAME_RATING("colorize by rating"),
    BY_DB_COMPARISON("colorize by db comparison");

    private String guiName;
    private ColorizationType(String guiName) {
        this.guiName = guiName;
    }

    @Override
    public String toString() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/util/resources/ColorizationType");
        return localeBundle.getString(guiName);
    }
}
