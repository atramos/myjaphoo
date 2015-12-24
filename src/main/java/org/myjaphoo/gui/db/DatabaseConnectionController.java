/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.db;

import org.mlsoft.eventbus.EventBus;
import org.mlsoft.eventbus.GlobalBus;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.db.ExportToOtherDatabase;
import org.myjaphoo.model.config.DatabaseConfigLoadSave;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;

/**
 *
 * @author lang
 */
public class DatabaseConnectionController {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionController.class);
    private DatabaseConfigurations dbconfigs;
    private MyjaphooController controller;

    private EventBus eventBus = GlobalBus.bus;
    
    public DatabaseConnectionController(MyjaphooController controller) {
        this.controller = controller;
        dbconfigs = load();
    }

    public DatabaseConfigurations load() {
        try {
            DatabaseConfigurations databaseConfigurations = DatabaseConfigLoadSave.load();

            return databaseConfigurations;
        } catch (Exception ex) {
            logger.error("error loading db config!", ex);

        }
        return new DatabaseConfigurations();
    }

    DatabaseConfiguration newDatabaseConnection() {
        DatabaseConfiguration dbconfig = new DatabaseConfiguration();
        dbconfigs.getDatabaseConfigurations().add(dbconfig);
        save();
        eventBus.post(new DBConfigChangeEvent());
        return dbconfig;
    }

    private void save() {
        try {
            DatabaseConfigLoadSave.save(dbconfigs);
        } catch (Exception ex) {
            logger.error("error saving db config!", ex);
        }
    }

    public DatabaseConfigurations getConfig() {
        return dbconfigs;
    }

    void delete(DatabaseConfiguration dbconfig) {
        dbconfigs.getDatabaseConfigurations().remove(dbconfig);
        save();
        eventBus.post(new DBConfigChangeEvent());
    }

    void edit(DatabaseConfiguration dbconfig) {
        DatabaseConfiguration editClone = (DatabaseConfiguration) dbconfig.clone();
        EditDatabaseConnectionDialog dlg = new EditDatabaseConnectionDialog(this, editClone, true);
        dlg.pack();
        MyjaphooApp.getApplication().show(dlg);
        if (dlg.isOk()) {
            dbconfigs.getDatabaseConfigurations().remove(dbconfig);
            dbconfigs.getDatabaseConfigurations().add(editClone);
            save();
            eventBus.post(new DBConfigChangeEvent());
        }

    }

    /**
     * @return the controller
     */
    public MyjaphooController getController() {
        return controller;
    }

    void exportToDatabase(DatabaseConfiguration dbconfig) {
        String msg = "Do you really want to export all data of this database to the database " + dbconfig.getName() + ": " + dbconfig.getFilledConnectionUrl()
                + "?\n Please note, that the export can take a long time for a big database!\nReally start exporting now?";
        if (controller.confirm(msg)) {
            ExportToOtherDatabase action = new ExportToOtherDatabase(controller, dbconfig);
            action.actionPerformed(new ActionEvent(this, 0, "exportdatabase"));
        }
    }
}
