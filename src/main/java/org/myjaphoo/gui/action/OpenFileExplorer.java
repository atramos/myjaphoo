/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.logic.FileSubstitutionImpl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author lang
 */
public class OpenFileExplorer extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/OpenFileExplorer");
    private static FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();

    public OpenFileExplorer(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("OPEN DIR IN FILE EXPLORER"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException {
        // erst die selektierten holen, bevor sie durch den dialog evtl. wieder
        // deselektiert werden
        final List<MovieNode> selMovies = context.getSelMovies();
        if (selMovies.size() == 1) {
            openEntryPathInExplorer(selMovies.get(0).getCanonicalDir());
        }
    }

    public static void openEntryPathInExplorer(String path) throws IOException {
        String sourcepath = fileSubstitution.locateFileOnDrive(path);
        if (sourcepath == null) {
            throw new RuntimeException("can not localize directory " + path);
        }
        File dir = new File(sourcepath);
        Desktop.getDesktop().open(dir);
    }
}
