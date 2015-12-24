/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.Rating;

/**
 *
 * @author lang
 */
public class SetRatingAction extends AbstractUndoAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/SetRatingAction");

    private Rating rating;

    public SetRatingAction(MyjaphooController controller, Rating rating, ViewContext context) {
        super(controller, MessageFormat.format(localeBundle.getString("SET RATING"), rating.getName()), null, context);
        this.rating = rating;

    }

    @Override
    public UndoableEdit runUndoAction(final MyjaphooController controller, ActionEvent e, ViewContext context) {
        final List<MovieNode> nodes = context.getSelMovies();
        controller.setRating(rating, nodes);
        controller.createChangeLog(ChangeLogType.SETRATING, "set rating: " + rating.getName(), nodes); //NOI18N
        return new AbstractUndoableEdit() {

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                //controller.unassignTokenToMovieNodes(token, nodes);
                }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                controller.setRating(rating, nodes);
            }

            @Override
            public String getPresentationName() {
                return MessageFormat.format(localeBundle.getString("ASSIGN RATING"), rating.getName());
            }
        };
    }
}
