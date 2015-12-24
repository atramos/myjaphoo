/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.mlsoft.swing.EventDispatchTools;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.tokenselection.TokenSelectionDialog;
import org.myjaphoo.model.db.Token;

import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Copies one or more files to a destination dir.
 *
 * @author lang
 */
public class AssignTokensViaFilterDialog extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AssignTokensViaFilterDialog");


    public AssignTokensViaFilterDialog(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("ASSIGN TAGS VIA FILTERDIALOG"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, final ViewContext context) throws IOException, JAXBException {
        TokenSelectionDialog dlg = openDialog(controller);
        if (dlg.isOk()) {
            Token token = dlg.getSelectedToken();
            if (token != null) {
                AddTokenAction action = new AddTokenAction(controller, token, Icons.IR_TAG_ADD.icon, context);
                action.actionPerformed(new ActionEvent(this, 7, "cmd:add token")); //NOI18N
            }
        }
    }

    public TokenSelectionDialog openDialog(final MyjaphooController controller) throws IOException, JAXBException {
        final TokenSelectionDialog[] dlg = new TokenSelectionDialog[1];
        EventDispatchTools.onEDTWait(new Runnable() {
            @Override
            public void run() {
                // the dialog setup code needs to be run in EDT; insubstantial is very hard here...
                dlg[0] = new TokenSelectionDialog(controller, getController().getView().getFrame());
                MyjaphooApp.getApplication().show(dlg[0]);
            }
        });
        return dlg[0];
    }

}
