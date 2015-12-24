/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.db;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.CompleteRefresh;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.model.logic.MyjaphooDB;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 *
 * @author 
 */
public class SwitchToOtherDatabase extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/db/resources/SwitchToOtherDatabase");
    private String databaseConfigurationName;

    public SwitchToOtherDatabase(MyjaphooController controller, String databaseConfigurationName) {
        super(controller, MessageFormat.format(localeBundle.getString("switchToDb"), databaseConfigurationName), null);
        this.databaseConfigurationName = databaseConfigurationName;

    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        String msg = MessageFormat.format(localeBundle.getString("questionSwitchToDb"), databaseConfigurationName);
        if (this.getController().confirm(msg)) {

            // invalidate (clear) all caches:
            CompleteRefresh.invalidateAllCaches();

            String configName = databaseConfigurationName.equals("default") ? null : databaseConfigurationName;
            MyjaphooDB.changeToOtherDatabase(configName);

            ThreadedThumbCache.resetCache();

            controller.getView().updateAllViews();
        }
    }
}
