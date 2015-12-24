/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.metaToken;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import org.jdesktop.swingx.JXTreeTable;

/**
 *
 * @author mla
 */
class MetaTokenTreeMouseListener extends MouseAdapter {

    private MetaTokenPanelController controller;
    private MetaTokenTree tree;

    public MetaTokenTreeMouseListener(MetaTokenPanelController controller, MetaTokenTree tree) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            selectTokenAtThisPoint(e);
            controller.doubleClicked();
        }

    }

    private void selectTokenAtThisPoint(MouseEvent e) {
        JXTreeTable source = (JXTreeTable) e.getSource();
        int row = source.rowAtPoint(e.getPoint());
        int column = source.columnAtPoint(e.getPoint());
        if (!source.isRowSelected(row)) {
            source.changeSelection(row, column, false, false);
        }
    }

    public void checkPopupTrigger(MouseEvent e) {
        if (e.isPopupTrigger()) {
            selectTokenAtThisPoint(e);

            JPopupMenu popup = controller.createTokenTreePopupMenu();
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
