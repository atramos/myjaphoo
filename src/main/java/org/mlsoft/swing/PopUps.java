/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.mlsoft.swing.popup.PopupWindow;

/**
 * Helper functions for creating popup button stuff.
 * @author lang
 */
public class PopUps {

    /**
     * Adds an action to the button to popup the given popup menu on click.
     * @param button
     * @param menu 
     */
    public static void makePopupForButton(final JButton button, final LazyPopupMenuCreator lazyPopupCreator) {
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("event handler");
                int y = button.getHeight();
                final JPopupMenu popupmenu = lazyPopupCreator.createPopup();
                if (popupmenu != null) {
                    popupmenu.show(button, 0, y);
                }
            }
        });
    }

    public static void makePopupForButton2(final JButton button, final LazyPopupMenuCreator lazyPopupCreator) {
        button.addActionListener(new ActionListener() {

            PopupWindow pw = new PopupWindow(null);

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("event handler");
                int y = button.getHeight();
                final JComponent comp = lazyPopupCreator.createComponent(pw);
                pw.show(button, comp, 0, y);
            }
        });
    }

    /**
     * Useful for dynamic popup content, or to create popup menus lazy.
     */
    public static interface LazyPopupMenuCreator {

        public JPopupMenu createPopup();

        public JComponent createComponent(PopupWindow pw);
    }
}
