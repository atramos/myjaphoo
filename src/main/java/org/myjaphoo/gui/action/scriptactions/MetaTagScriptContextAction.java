package org.myjaphoo.gui.action.scriptactions;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.model.db.MetaToken;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 * ScriptContextAction
 *
 * @author mla
 * @version $Id$
 */
public class MetaTagScriptContextAction extends AbstractWankmanAction {

    private ActionEntry actionEntry;

    private List<MetaToken> tags;

    public MetaTagScriptContextAction(MyjaphooController controller, ViewContext context, ActionEntry actionEntry, List<MetaToken> tags) {
        super(controller, actionEntry.getName(), actionEntry.getIcon(), context);
        this.actionEntry = actionEntry;
        this.tags = tags;
    }


    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        actionEntry.getAction().call(controller, tags);
    }
}
