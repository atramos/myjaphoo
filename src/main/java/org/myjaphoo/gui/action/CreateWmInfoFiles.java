/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.xml.bind.JAXBException;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.util.Filtering;

/**
 * Creates Wminfo files for the selected movies.
 * @author lang
 */
public class CreateWmInfoFiles extends AbstractWankmanAction implements DisplayAsLastUsedActions {
    
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/CreateWmInfoFiles");

    public CreateWmInfoFiles(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("CREATE WMINFO FILE(S) FOR THE MEDIA FILES"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        controller.createWmInfoFiles(Filtering.nodes2Entries(context.getSelMovies()));
        controller.createChangeLog(ChangeLogType.CREATEWMINFOFILES, "create wm info files", context.getSelMovies()); //NOI18N
    }
}
