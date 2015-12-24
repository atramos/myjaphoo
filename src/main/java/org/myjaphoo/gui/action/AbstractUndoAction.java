/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.undo.UndoableEdit;
import org.myjaphoo.MyjaphooController;

/**
 *
 * @author mla
 */
abstract public class AbstractUndoAction extends AbstractWankmanAction {

    public AbstractUndoAction(MyjaphooController controller, String name, Icon icon) {
        super(controller, name, icon);
    }

    public AbstractUndoAction(MyjaphooController controller, String name, Icon icon, ViewContext context) {
        super(controller, name, icon, context);
    }

    @Override
    public final void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        UndoableEdit edit = runUndoAction(controller, e, context);
        if (edit != null) {
            getController().getView().addUndoableEdit(edit);
        }
    }

    abstract public UndoableEdit runUndoAction(MyjaphooController controller, ActionEvent e, ViewContext context);
}
