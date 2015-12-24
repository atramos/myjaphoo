/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.metatoken;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.myjaphoo.gui.action.AbstractUndoAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.logic.MetaTokenJpaController;

/**
 * Kommand fürs assignment von einem token zu einem metatoken.
 * @author lang
 */
public class MetaTokenAssignment extends AbstractUndoAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/metatoken/resources/MetaTokenAssignment");

    private MetaTokenJpaController tokenjpa = new MetaTokenJpaController();
    private Token token;
    private MetaToken metaToken;

    public MetaTokenAssignment(MyjaphooController controller, Token token, MetaToken metaToken) {
        super(controller, token.getName() + " <-> " + metaToken.getName(), null); //NOI18N
        this.token = token;
        this.metaToken = metaToken;
    }

    @Override
    public UndoableEdit runUndoAction(final MyjaphooController controller, ActionEvent e, ViewContext context) {

        controller.createChangeLog(ChangeLogType.ASSIGNMETATOK, "assignment " + token.getName() + " <-> " + metaToken.getName(), null); //NOI18N
        controller.assignMetaTokenToToken(metaToken, token);
        return new AbstractUndoableEdit() {

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                controller.unAssignMetaTokenFromToken(metaToken, Arrays.asList(token));
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                controller.assignMetaTokenToToken(metaToken, token);
            }

            @Override
            public String getPresentationName() {
                return MessageFormat.format(localeBundle.getString("ASSIGNMENT "), token.getName(), metaToken.getName());
            }
        };


    }
}
