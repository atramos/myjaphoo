/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author mla
 */
public class PopupMouseListener extends MouseAdapter {
    
    private ThumbPanelController controller;
    private ThumbDisplayingComponent component;
    
    public PopupMouseListener(ThumbPanelController controller, ThumbDisplayingComponent component) {
        this.controller = controller;
        this.component = component;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        checkPopupTrigger(e);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        checkPopupTrigger(e);
    }
    
    private void checkPopupTrigger(MouseEvent e) {
        if (e.isPopupTrigger()) {
            ensureClickPointIsSelected(e);
            
            JPopupMenu popup = controller.createPopUpMenu(component.getAllSelectedNodes());
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Selects the element under the click point, if it is not yet selected.
     * 
     * @param e 
     */
    private void ensureClickPointIsSelected(MouseEvent e) {
        if (e.getSource() instanceof JTable) {
            JTable source = (JTable) e.getSource();
            int row = source.rowAtPoint(e.getPoint());
            int column = source.columnAtPoint(e.getPoint());
            
            if (!source.isRowSelected(row)) {
                source.changeSelection(row, column, false, false);
            }
        } else if (e.getSource() instanceof JList) {
            JList list = (JList) e.getSource();
            int index = list.locationToIndex(e.getPoint());
            if (!list.isSelectedIndex(index)) {
                list.addSelectionInterval(index, index);
            }
        }
    }
}
