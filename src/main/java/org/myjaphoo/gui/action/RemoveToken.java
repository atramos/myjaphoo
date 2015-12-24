/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooController;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.model.db.ChangeLogType;

/**
 *
 * @author mla
 */
public class RemoveToken extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/RemoveToken");

    public RemoveToken(MyjaphooController controller) {
        super(controller, localeBundle.getString("REMOVE TAG"), Icons.IR_TAG_DEL.icon);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        String tokenName = controller.getFilter().getCurrentToken().getName();
        if (controller.confirm(MessageFormat.format(localeBundle.getString("REALLY REMOVE TAG "), tokenName))) {
            controller.removeToken(controller.getFilter().getCurrentToken());
            controller.createChangeLog(ChangeLogType.REMOVETOKEN, "remove tag " + tokenName + " incl. all aissignments)", null); //NOI18N
        }
    }
}
