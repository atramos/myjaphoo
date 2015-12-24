/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import org.jdesktop.swingx.JXTreeTable;

/**
 *
 * @author mla
 */
class PopupMovieTreeListener extends MouseAdapter {

    private MoviePanelController controller;
    private MovieTree tree;

    public PopupMovieTreeListener(MoviePanelController controller, MovieTree tree) {
        this.controller = controller;
        this.tree = tree;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        checkPopupTrigger(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        checkPopupTrigger(e);
    }

    public void checkPopupTrigger(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JXTreeTable source = (JXTreeTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());

            if (!source.isRowSelected(row)) {
                source.changeSelection(row, column, false, false);
            }

            JPopupMenu popup = controller.createPopUpMenu(controller.getCurrentSelectedDir());
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
