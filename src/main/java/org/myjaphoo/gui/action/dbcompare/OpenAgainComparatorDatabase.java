/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.dbcompare;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.config.DatabaseConfigLoadSave;
import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;

/**
 *
 * @author 
 */
public class OpenAgainComparatorDatabase extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/dbcompare/resources/OpenAgainComparatorDatabase");
    private String databaseConfigurationName;

    public OpenAgainComparatorDatabase(MyjaphooController controller, String databaseConfigurationName) {
        super(controller, MessageFormat.format(localeBundle.getString("COMPARE WITH"), databaseConfigurationName), null);
        this.databaseConfigurationName = databaseConfigurationName;

    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        DatabaseConfiguration config = DatabaseConfigLoadSave.load().findByName(databaseConfigurationName);
        if (config != null) {
            DatabaseComparison.getInstance().openComparisonDatabase(config);
            controller.getView().getInfoPanel().updateDBComparisonValues(DatabaseComparison.getInstance());
        }
    }
}
