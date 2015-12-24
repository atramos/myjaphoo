package org.myjaphoo.gui.action.scriptactions;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.ViewContext;

import java.awt.event.ActionEvent;

/**
 * ScriptContextAction
 *
 * @author mla
 * @version $Id$
 */
public class MovieScriptContextAction extends AbstractWankmanAction {

    private ActionEntry actionEntry;

    public MovieScriptContextAction(MyjaphooController controller, ViewContext context, ActionEntry actionEntry) {
        super(controller, actionEntry.getName(), actionEntry.getIcon(), context);
        this.actionEntry = actionEntry;

    }


    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        actionEntry.getAction().call(controller, context.getSelNodes());
    }
}
