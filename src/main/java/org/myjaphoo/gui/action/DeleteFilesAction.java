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
public class DeleteFilesAction extends AbstractSelectedMoviesContextAction implements DisplayAsLastUsedActions {
    
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/DeleteFilesAction");

    public DeleteFilesAction(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("DELETE FILE(S)"), context, Icons.IR_DELETE.icon);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        if (MyjaphooDBPreferences.PRF_FO_DELETION_ALLOWED.getVal()) {
            ArrayList<MovieEntry> movies = confirmSelection(localeBundle.getString("DO YOU REALLY WANT TO DELETE THE MEDIA FILES"), context);
            if (movies != null) {
                controller.deleteMovies(movies);
                controller.createChangeLog2(ChangeLogType.DELETEFILES, "delete files", movies); //NOI18N
            }
        } else {
            controller.message(localeBundle.getString("DELETION OF FILES IS NOT ALLOWED WITH THIS PREFERENCES"));
        }
    }
}
