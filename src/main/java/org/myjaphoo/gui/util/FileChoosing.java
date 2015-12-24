/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooDBPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 * @author mla
 */
public class FileChoosing {

    private static final Logger logger = LoggerFactory.getLogger(FileChoosing.class.getName());

    /**
     * Methode, um directories auszuwählen.
     * Das letzte gewählte directory wird in den prefs gespeichert u. beim nächsten mal vorgewählt.
     * @param title dialog title
     * @return die ausgewählte datei oder null bei abbruch
     */
    public static File chooseFile(MyjaphooController controller, String title) {
        return chooser(controller, title, JFileChooser.FILES_ONLY);
    }

    /**
     * Methode, um directories auszuwählen.
     * Das letzte gewählte directory wird in den prefs gespeichert u. beim nächsten mal vorgewählt.
     * @param title dialog title
     * @return die ausgewählte datei oder null bei abbruch
     */
    public static File chooseDir(MyjaphooController controller, String title) {
        return chooser(controller, title, JFileChooser.DIRECTORIES_ONLY);
    }

    private static File chooser(final MyjaphooController controller, final String title, final int whatType) {
        try {
            final File[] returnFile = new File[1];
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    String preselectedDir = MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_FILEDIALOG.getVal();
                    if (preselectedDir == null) {
                        preselectedDir = "."; // programm verzeichnis auswählen. //NOI18N
                    }
                    JFileChooser dlg = new JFileChooser(new File(preselectedDir));
                    dlg.putClientProperty("FileChooser.useShellFolder", Boolean.FALSE); //NOI18N
                    dlg.setFileSelectionMode(whatType);
                    dlg.setDialogTitle(title);
                    int returnVal = dlg.showOpenDialog(controller.getView().getFrame());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        returnFile[0] = dlg.getSelectedFile();
                        // im filemodus das übergeordnete dir speichern; im dir modus das dir selbst:
                        if (JFileChooser.FILES_ONLY == whatType) {
                            MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_FILEDIALOG.setVal(dlg.getCurrentDirectory().getAbsolutePath());
                        } else {
                            MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_FILEDIALOG.setVal(dlg.getSelectedFile().getAbsolutePath());
                        }
                    }
                }
            });
            return returnFile[0];

        } catch (Exception ex) {
            logger.error("error calling file chooser!", ex); //NOI18N
            return null;
        }
    }
}
