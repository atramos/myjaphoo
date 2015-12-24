/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.metatoken;

import org.myjaphoo.MyjaphooController;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.model.db.ChangeLogType;

/**
 *
 * @author mla
 */
public class RemoveMetaToken extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/metatoken/resources/RemoveMetaToken");

    public RemoveMetaToken(MyjaphooController controller) {
        super(controller,localeBundle.getString("REMOVE META TAG"), null);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        if (controller.confirm(localeBundle.getString("REALLY REMOVE META TAG WITH ALL ASSIGNMENTS?"))) {
            String tokenName = controller.getFilter().getCurrentMetaToken().getName();
            controller.removeMetaToken(controller.getFilter().getCurrentMetaToken());
            controller.createChangeLog(ChangeLogType.REMOVEMETATOKEN, "remove meta tag " + tokenName + " incl. all assignments)", null); //NOI18N
        }
    }
}
