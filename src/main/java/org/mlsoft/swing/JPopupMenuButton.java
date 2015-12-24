/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.mlsoft.swing.popup.PopupWindow;

/**
 * Kombiniert einen Button mit einen Popupmenü.
 * Analog z.b. wie die Vor- und Zurückbuttons in Browsern (History-buttons).
 * @author mla
 */
public class JPopupMenuButton extends JButton {

    private JPopupMenu popupmenu;
    private PopUps.LazyPopupMenuCreator lpmc = new PopUps.LazyPopupMenuCreator() {

        @Override
        public JPopupMenu createPopup() {
            return getPopupmenu();
        }

        @Override
        public JComponent createComponent(PopupWindow pw) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    /**
     * Creates a button with no set text or icon.
     */
    public JPopupMenuButton() {
        super();
        init();
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon  the Icon image to display on the button
     */
    public JPopupMenuButton(Icon icon) {
        super(icon);
        init();
    }

    /**
     * Creates a button with text.
     *
     * @param text  the text of the button
     */
    public JPopupMenuButton(String text) {
        super(text);
        init();
    }

    /**
     * Creates a button with text.
     *
     * @param text  the text of the button
     */
    public JPopupMenuButton(String text, PopUps.LazyPopupMenuCreator lpmc) {
        super(text);
        this.lpmc = lpmc;
        init();
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     * @since 1.3
     */
    public JPopupMenuButton(Action a) {
        super(a);
        init();
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
    public JPopupMenuButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    private void init() {
        PopUps.makePopupForButton(this, lpmc);
    }

    /**
     * @return the popupmenu
     */
    public JPopupMenu getPopupmenu() {
        return popupmenu;
    }

    /**
     * @param popupmenu the popupmenu to set
     */
    public void setPopupmenu(JPopupMenu popupmenu) {
        this.popupmenu = popupmenu;

    }
}
