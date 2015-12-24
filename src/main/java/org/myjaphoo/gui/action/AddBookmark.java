/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.db.ChangeLogType;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public class AddBookmark extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AddBookmark");
    
    public AddBookmark(MyjaphooController controller) {
        super(controller, localeBundle.getString("ADD THIS VIEW AS BOOKMARK"), null);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) {

        final String bookmarkname = controller.getInputValue(localeBundle.getString("ADD NAME FOR BOOKMARK"));
        if (bookmarkname != null) {
            controller.addNewBookMark(bookmarkname);
            controller.createChangeLog(ChangeLogType.NEWBOOKMARK, "new bookmark " + bookmarkname, null); //NOI18N
        }

    }
}
