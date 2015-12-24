/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieTreeIconCreator;
import org.myjaphoo.gui.util.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 *
 * @author mla
 */
public class BreadCrumbTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel,
            boolean expanded,
            boolean leaf, int row,
            boolean hasFocus) {

        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);


        Icon theIcon = createIconForANode(value, label.getHeight());

        if (theIcon != null) {
            label.setIcon(theIcon);
        }
        String txt = ""; //NOI18N

        if (value != null) {
            txt += value.toString();
            txt += " (" + ((AbstractMovieTreeNode) value).getNumOfContainingMovies() + ")"; //NOI18N
            txt += " ," + Utils.humanReadableByteCount(((AbstractMovieTreeNode) value).getSizeOfContainingMovies()); //NOI18N

        }
        label.setText(txt);
        return this;

    }

    public static Icon createIconForANode(Object value, int height) {
        if (height ==0) {
            height =16;
        }
        return MovieTreeIconCreator.createIcon(value, true, height, null);
    }
}
