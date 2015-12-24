package org.myjaphoo.gui.action.historychange;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * HistoryBackAction
 * @author mla
 * @version $Id$
 */
public class HistoryBackAction extends AbstractAction {

    private MyjaphooController controller;

    public HistoryBackAction(MyjaphooController controller) {
        super("Back", Icons.IR_ARROW_LEFT.icon);
        this.controller = controller;
    }

    /**
     * Invoked when an action occurs.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.historyBack();
    }
}
