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
import org.apache.commons.lang.Validate;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooView;

/**
 * Copies one or more files to a destination dir.
 * @author lang
 */
public class OpenViewAndSelectNode extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/OpenViewAndSelectNode");

    public OpenViewAndSelectNode(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("NEW VIEW AND SELECT THIS MEDIA FILE"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        final List<MovieNode> selMovies = context.getSelMovies();
        Validate.isTrue(selMovies.size()>0, localeBundle.getString("NO MEDIA FILE SELECTED!"));
        Validate.isTrue(selMovies.size()==1, localeBundle.getString("YOU NEED TO SELECT EXACTLY ONE MEDIA FILE!"));
        MyjaphooApp.getApplication().show(new MyjaphooView(MyjaphooApp.getApplication(), selMovies.get(0)));


    }
}
