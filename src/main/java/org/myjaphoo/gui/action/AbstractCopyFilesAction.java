/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.xml.bind.JAXBException;
import org.myjaphoo.FileCopying;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.model.db.ChangeLogType;

/**
 * Copies one or more files to a destination dir.
 * @author lang
 */
public class AbstractCopyFilesAction extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AbstractCopyFilesAction");
    private boolean createWmInfoFile;
    private FileCopying.PathOptionForCopying pathOption;

    public AbstractCopyFilesAction(MyjaphooController controller, ViewContext context, boolean createWmInfoFile, final FileCopying.PathOptionForCopying pathOption) {
        super(controller, createTitle(createWmInfoFile, pathOption), Icons.IR_COPY.icon, context);
        this.createWmInfoFile = createWmInfoFile;
        this.pathOption = pathOption;

    }

    private static String createTitle(boolean createWmInfoFile, final FileCopying.PathOptionForCopying pathOption) {
        switch (pathOption) {
            case NOADDITIONAL_PATH:
                return createWmInfoFile ? localeBundle.getString("COPY WITH WM INFOS") : localeBundle.getString("COPY FILE(S)");
            case PRESERVE_SOURCE_PATH:
                return createWmInfoFile ? localeBundle.getString("COPY WITH FULL PATH AND WM INFOS") : localeBundle.getString("COPY WITH FULL PATH");
            case USE_STRUCTURE_PATH:
                return createWmInfoFile ? localeBundle.getString("COPY USING STRUCTURE AS PATH AND WM INFOS") : localeBundle.getString("COPY USING STRUCTURE AS PATH");
            default:
                throw new RuntimeException("internal error: no case for pathoption");
        }
    }

    @Override
    public final void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        String name = (String) getValue(Action.NAME);
        File file = chooseDir(name);
        if (file != null) {
            controller.copyMovies(context.getSelMovies(), file.getAbsolutePath(), createWmInfoFile, pathOption);
            controller.createChangeLog(ChangeLogType.FILECOPY, name + localeBundle.getString(" TO ") + file.getAbsolutePath(), context.getSelMovies());
        }
    }
}
