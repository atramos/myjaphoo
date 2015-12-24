/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.logic.FileSubstitutionImpl;

/**
 *
 * @author lang
 */
public class DesktopEditAction extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/resources/GuiResources");
    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();

    public DesktopEditAction(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("DesktopOpenAction.EDIT FILE"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException {
        // erst die selektierten holen, bevor sie durch den dialog evtl. wieder
        // deselektiert werden
        final List<MovieNode> selMovies = context.getSelMovies();
        if (selMovies.size() == 1) {

            String sourcepath = fileSubstitution.locateFileOnDrive(selMovies.get(0).getCanonicalPath());
            if (sourcepath == null) {
                throw new RuntimeException("can not localize file " + selMovies.get(0).getCanonicalPath());
            }
            File path = new File(sourcepath);
            Desktop.getDesktop().edit(path);
        }

    }
}
