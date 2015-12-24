/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.db.MovieEntry;
/**
 *
 * @author lang
 */
public class AddCoverBackPicture extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AddCoverFrontPicture");

    public AddCoverBackPicture(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("ADD COVER PIC FOR FILE(S)"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        final List<MovieNode> selMovies = context.getSelMovies();
        if (selMovies.size() !=1) {
            getController().message("You must select exactly one movie entry for this function!");
            return;
        }
        MovieEntry entry = context.getSelMovies().get(0).getMovieEntry();
        if (!FileType.Movies.is(entry)) {
            getController().message("You must select exactly one movie entry for this function!");
            return;
        }        
        File file = super.chooseFile("Select the cover back picture file");

        controller.addCoverBackPicture(file, entry);
        //controller.createChangeLog(ChangeLogType.RECREATETHUMBS, "recreate thumbnails", selMovies); //NOI18N
    }
}
