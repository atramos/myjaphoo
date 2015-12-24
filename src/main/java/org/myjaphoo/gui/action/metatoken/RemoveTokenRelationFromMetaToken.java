/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.metatoken;

import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import java.util.List;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.RemoveTokenRelations;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.removeElementsDialog.RemoveElementsDialog;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;

/**
 *
 * @author mla
 */
public class RemoveTokenRelationFromMetaToken extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/metatoken/resources/RemoveTokenRelationFromMetaToken");

    public RemoveTokenRelationFromMetaToken(MyjaphooController controller) {
        super(controller, localeBundle.getString("REMOVE TAG RELATION FROM META TAG"), null);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {

        MetaToken currMetaToken = controller.getFilter().getCurrentMetaToken();

        ArrayList<Token> orderedList = new ArrayList<Token>(currMetaToken.getAssignedTokens());
        Collections.sort(orderedList);
        RemoveElementsDialog<Token> dlg = new RemoveElementsDialog<Token>(controller.getView().getFrame(),
                MessageFormat.format(localeBundle.getString("SELECT WHICH TAG RELATIONS SHOULD BE REMOVED FROM METATAG "), currMetaToken.getName()),
                localeBundle.getString("CHOOSE TAG RELATIONS TO REMOVE"), orderedList);
        MyjaphooApp.getApplication().show(dlg);
        if (dlg.isOk()) {
            List<Token> toks2Remove = dlg.getCheckedElements();

            controller.unAssignMetaTokenFromToken(currMetaToken, toks2Remove);
            controller.createChangeLog(ChangeLogType.REMOVEMETATOKEN, "remove tag relations from metatag " + currMetaToken.getName() + ": " + RemoveTokenRelations.tokList(toks2Remove), null); //NOI18N
        }
    }
}
