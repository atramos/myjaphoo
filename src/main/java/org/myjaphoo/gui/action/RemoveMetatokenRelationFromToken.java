/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import java.util.List;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.removeElementsDialog.RemoveElementsDialog;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;

/**
 *
 * @author mla
 */
public class RemoveMetatokenRelationFromToken extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/RemoveMetatokenRelationFromToken");

    public RemoveMetatokenRelationFromToken(MyjaphooController controller) {
        super(controller, localeBundle.getString("REMOVE METATAG RELATION FROM TAG"), Icons.IR_TAG_DEL.icon);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {

        Token currToken = controller.getFilter().getCurrentToken();

        ArrayList<MetaToken> orderedList = new ArrayList<MetaToken>(currToken.getAssignedMetaTokens());
        Collections.sort(orderedList);
        RemoveElementsDialog<MetaToken> dlg = new RemoveElementsDialog<MetaToken>(controller.getView().getFrame(),
                MessageFormat.format(localeBundle.getString("SELECT WHICH METATAG"), currToken.getName()),
                localeBundle.getString("CHOOSE METATAG RELATIONS TO REMOVE"), orderedList);
        MyjaphooApp.getApplication().show(dlg);
        if (dlg.isOk()) {
            List<MetaToken> toks2Remove = dlg.getCheckedElements();
            List<Token> tokenList = Arrays.asList(currToken);
            for (MetaToken mt : toks2Remove) {
                controller.unAssignMetaTokenFromToken(mt, tokenList);
            }
            controller.createChangeLog(ChangeLogType.REMOVEMETATOKEN, "remove metatag relations from tag " + currToken.getName() + ": " + tokList(toks2Remove), null); //NOI18N
        }
    }

    public static String tokList(List<MetaToken> tokens2RemoveRelations) {
        StringBuilder b = new StringBuilder();
        for (MetaToken token : tokens2RemoveRelations) {
            b.append(token.getName());
            b.append(";"); //NOI18N
        }
        return b.toString();
    }
}
