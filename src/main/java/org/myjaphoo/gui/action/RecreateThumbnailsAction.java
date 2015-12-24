/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.xml.bind.JAXBException;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.util.Filtering;

/**
 * Recreates the tumbs for  one or more files.
 * @author lang
 */
public class RecreateThumbnailsAction extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/RecreateThumbnailsAction");

    public RecreateThumbnailsAction(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("RECREATE THUMBS FOR FILE(S)"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {

        final List<MovieNode> selMovies = context.getSelMovies();
        controller.recreateThumbs(Filtering.nodes2Entries(selMovies));
        controller.createChangeLog(ChangeLogType.RECREATETHUMBS, "recreate thumbnails", selMovies); //NOI18N
    }
}
