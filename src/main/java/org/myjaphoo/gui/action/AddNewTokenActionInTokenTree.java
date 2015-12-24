/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.newtagdlg.NewTagDialog;
import org.myjaphoo.gui.newtagdlg.NewTagDialog.NewTagDlgResult;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.logic.TokenJpaController;

/**
 *
 * @author lang
 */
public class AddNewTokenActionInTokenTree extends AbstractUndoAction {
    
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AddNewTokenActionInTokenTree");

    private TokenJpaController tokenjpa = new TokenJpaController();

    public AddNewTokenActionInTokenTree(MyjaphooController controller) {
        super(controller, localeBundle.getString("ADD NEW TAG"), Icons.IR_TAG_ADD.icon);
    }

    @Override
    public UndoableEdit runUndoAction(final MyjaphooController controller, ActionEvent e, ViewContext context) {
        final Token parentPreselected = controller.getFilter().getCurrentToken();
        List<Token> parensList = controller.getTokens();
        final NewTagDlgResult result = NewTagDialog.newTag(localeBundle.getString("ADD NAME FOR THE NEW TAG"), parensList, parentPreselected);

        //final String newTokenName = controller.getInputValue("Add name for the new tag");
        if (result != null) {
            final Token parent = result.parentToken;
            Token token = controller.createNewToken(result.name, result.descr, parent);
            controller.createChangeLog(ChangeLogType.NEWTOK, "new tag " + token.getName(), null); //NOI18N
            return new AbstractUndoableEdit() {

                @Override
                public void undo() throws CannotUndoException {
                    super.undo();
                    Token token = tokenjpa.findTokenByName(result.name);
                    controller.removeToken(token);
                }

                @Override
                public void redo() throws CannotRedoException {
                    super.redo();
                    Token token = controller.createNewToken(result.name, result.descr, parent);
                }

                @Override
                public String getPresentationName() {
                    return localeBundle.getString("CREATE TAG ") + result.name;
                }
            };

        }
        return null;
    }
}
