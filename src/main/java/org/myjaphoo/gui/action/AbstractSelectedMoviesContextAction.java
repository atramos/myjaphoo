/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.sellists.MovieSelectionConfirmationDialog;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.util.Filtering;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copies one or more files to a destination dir.
 * @author lang
 */
public abstract class AbstractSelectedMoviesContextAction extends AbstractWankmanAction {

    public AbstractSelectedMoviesContextAction(MyjaphooController controller, String title, ViewContext context) {
        super(controller, title, null, context);
    }

    public AbstractSelectedMoviesContextAction(MyjaphooController controller, String title, ViewContext context, Icon icon) {
        super(controller, title, icon, context);
    }

    protected ArrayList<MovieEntry> confirmSelection(String title, ViewContext context) {
        final List<MovieNode> selNodes = context.getSelMovies();
        ArrayList<MovieEntry> selMovies = Filtering.nodes2Entries(selNodes);
        return confirmSelection(title, selMovies);
    }

    protected ArrayList<MovieEntry> confirmSelection(String title, ArrayList<MovieEntry> moviesToConfirm) {
        MovieSelectionConfirmationDialog dlg =
                new MovieSelectionConfirmationDialog(title, getController().getView().getFrame(), moviesToConfirm);
        MyjaphooApp.getApplication().show(dlg);
        if (!dlg.isOk()) {
            return null;
        }
        return moviesToConfirm;
    }

    protected List<MovieNode> confirmSelectionOfNodes(String title, ViewContext context) {
        final List<MovieNode> selNodes = context.getSelMovies();
        //ArrayList<MovieEntry> selMovies = Filtering.nodes2Entries(selNodes);
        MovieSelectionConfirmationDialog dlg =
                new MovieSelectionConfirmationDialog(title, getController().getView().getFrame(), selNodes);
        MyjaphooApp.getApplication().show(dlg);
        if (!dlg.isOk()) {
            return null;
        }
        return selNodes;
    }

}
