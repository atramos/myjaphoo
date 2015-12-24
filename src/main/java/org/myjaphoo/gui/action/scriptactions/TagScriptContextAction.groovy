package org.myjaphoo.gui.action.scriptactions

import org.myjaphoo.MyjaphooController
import org.myjaphoo.gui.action.AbstractWankmanAction
import org.myjaphoo.gui.action.ViewContext
import org.myjaphoo.model.db.Token

import javax.swing.*
import java.awt.event.ActionEvent

/**
 * ScriptContextAction
 *
 * @author mla
 * @version $Id$
 */
public class TagScriptContextAction extends AbstractWankmanAction {

    private ActionEntry actionEntry;

    private List<Token> tags;

    public TagScriptContextAction(MyjaphooController controller, ViewContext context, ActionEntry actionEntry, List<Token> tags) {
        super(controller, (String) "$actionEntry.name: $tags", (Icon) actionEntry.getIcon(), context);
        this.actionEntry = actionEntry;
        this.tags = tags;
    }


    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        actionEntry.getAction().call(controller, tags);
    }
}
