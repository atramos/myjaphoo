/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooDBPreferences;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.MovieEntry;

import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Copies one or more files to a destination dir.
 * @author lang
 */
public class DeleteCondensedDuplicates extends AbstractSelectedMoviesContextAction implements DisplayAsLastUsedActions {
    
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/DeleteCondensedDuplicates");

    public DeleteCondensedDuplicates(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("DELETE CONDENSED DUPLICATE(S)"), context, Icons.IR_DELETE.icon);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        if (MyjaphooDBPreferences.PRF_FO_DELETION_ALLOWED.getVal()) {
            final List<MovieNode> selMovies = context.getSelMovies();
            ArrayList<MovieEntry> duplicates = findDuplicates(selMovies);
            ArrayList<MovieEntry> duplicates2Delete = confirmSelection(localeBundle.getString("DO YOU REALLY WANT TO DELETE THE FOLLOWING DUPLICATES"), duplicates);
            if (duplicates2Delete != null) {
                controller.deleteMovies(duplicates2Delete);
                controller.createChangeLog(ChangeLogType.DELETEFILES, "delete condensed dups", selMovies); //NOI18N
            }
        } else {
            controller.message(localeBundle.getString("DELETION OF FILES IS NOT ALLOWED WITH THIS PREFERENCES"));
        }
    }

    private ArrayList<MovieEntry> findDuplicates(List<MovieNode> selMovies) {
        ArrayList<MovieEntry> duplicates = new ArrayList<MovieEntry>();
        for (MovieNode node: selMovies) {
            if (node != null) {
                duplicates.addAll(node.getCondensedDuplicates());
            }
        }
        return duplicates;
    }
}
