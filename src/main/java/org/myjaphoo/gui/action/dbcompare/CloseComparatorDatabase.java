/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.dbcompare;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.dbcompare.DatabaseComparison;

/**
 *
 * @author 
 */
public class CloseComparatorDatabase extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/dbcompare/resources/CloseComparatorDatabase");

    public CloseComparatorDatabase(MyjaphooController controller) {
        super(controller, localeBundle.getString("CLOSE COMPARISON DB"), null);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        DatabaseComparison.getInstance().closeComparisonDatabase();
        controller.getView().getInfoPanel().updateDBComparisonValues(DatabaseComparison.getInstance());
    }
}
