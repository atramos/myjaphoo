/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.groupedthumbs;

import org.myjaphoo.gui.movietree.AbstractLeafNode;

/**
 *
 * @author lang
 */
public class BreakDownChild {

    private static final int COLMAX = 10;
    public ThumbStripe stripe;
    public int startIndex;

    @Override
    public String toString() {
        return "<html>"
                + stripe.getStructureNode().getName() + "<br>" //NOI18N
                + startIndex + " - " + (startIndex + COLMAX) + "</html>"; //NOI18N
    }

    public AbstractLeafNode getCol(int column) {
        int index = startIndex + column;
        if (index < stripe.getMovieNodes().size()) {
            return stripe.getMovieNodes().get(index);
        } else {
            return null;
        }
    }
}
