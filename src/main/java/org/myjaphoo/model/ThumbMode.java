/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;

import org.myjaphoo.ThumbDisplayFilterResult;

/**
 * possible selectable thumb view modes.
 *
 * @author lang
 */
public enum ThumbMode {

    EXTENDED_TABLEVIEW(ThumbDisplayFilterResult.ThumbDisplayFilterResultMode.PlAINLIST),
    ALTTHUMB(ThumbDisplayFilterResult.ThumbDisplayFilterResultMode.PlAINLIST),
    STRIPES(ThumbDisplayFilterResult.ThumbDisplayFilterResultMode.SLIDESGROUPED);
    private ThumbDisplayFilterResult.ThumbDisplayFilterResultMode mode;

    private ThumbMode(ThumbDisplayFilterResult.ThumbDisplayFilterResultMode mode) {
        this.mode = mode;
    }

    /**
     * @return the mode
     */
    public ThumbDisplayFilterResult.ThumbDisplayFilterResultMode getMode() {
        return mode;
    }
}
