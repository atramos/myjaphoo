/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooDBPreferences;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.MovieEntry;

import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Copies one or more files to a destination dir.
 * @author lang
 */
public class RemoveEntryAction extends AbstractSelectedMoviesContextAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/RemoveEntryAction");

    public RemoveEntryAction(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("REMOVE ENTRY(S) FROM DATABASE"), context, Icons.IR_DB_REMOVE.icon);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        if (MyjaphooDBPreferences.PRF_FO_REMOVING_ALLOWED.getVal()) {
            ArrayList<MovieEntry> movies = confirmSelection(localeBundle.getString("DO YOU REALLY WANT TO"), context);
            if (movies != null) {
                controller.removeMovieEntriesFromDatabase(movies);
                controller.createChangeLog2(ChangeLogType.REMOVEENTRIES, "remove entries from db", movies); //NOI18N
            }
        } else {
            controller.message(localeBundle.getString("REMOVING OF ENTRIES"));
        }
    }
}
