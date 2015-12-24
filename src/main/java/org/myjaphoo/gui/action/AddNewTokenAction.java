/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.myjaphoo.MovieNode;
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
public class AddNewTokenAction extends AbstractUndoAction implements DisplayAsLastUsedActions {
    
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AddNewTokenAction");

    private TokenJpaController tokenjpa = new TokenJpaController();

    public AddNewTokenAction(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("ADD NEW TAG"), Icons.IR_TAG_ADD.icon, context);
    }

    @Override
    public UndoableEdit runUndoAction(final MyjaphooController controller, ActionEvent e, ViewContext context) {
        // erst die selektierten holen, bevor sie durch den dialog evtl. wieder
        // deselektiert werden
        final List<MovieNode> selMovies = context.getSelMovies();
        final Token parentPreselected = controller.getFilter().getCurrentToken();
        List<Token> parensList = controller.getTokens();
        final NewTagDlgResult result = NewTagDialog.newTag(localeBundle.getString("ADD NAME FOR THE NEW TAG"), parensList, parentPreselected);

        if (result != null) {
            // there is not parent available here.
            final Token parent = null;
            Token token = controller.createNewToken(result.name, result.descr, result.parentToken);
            controller.assignTokenToMovieNodes(token, selMovies);

            controller.createChangeLog(ChangeLogType.ASSIGNTOK, "new tag " + token.getName(), selMovies); //NOI18N
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
                    controller.assignTokenToMovieNodes(token, selMovies);
                }

                @Override
                public String getPresentationName() {
                    return MessageFormat.format(localeBundle.getString("CREATE TAG AND ASSIGN"), result.name);
                }
            };

        }
        return null;
    }
}
