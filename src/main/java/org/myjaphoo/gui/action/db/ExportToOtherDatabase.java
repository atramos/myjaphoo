/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.db;

import java.awt.event.ActionEvent;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AbstractWankmanAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.logic.dbhandling.DatabaseExporter;

/**
 *
 * @author mla
 */
public class ExportToOtherDatabase extends AbstractWankmanAction {

    private DatabaseConfiguration targetDatabase;

    public ExportToOtherDatabase(MyjaphooController controller, DatabaseConfiguration targetDatabase) {
        super(controller, "Export Database", null);
        this.targetDatabase = targetDatabase;
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        DatabaseExporter exporter = new DatabaseExporter();
        exporter.exportToDatabase(targetDatabase);

    }
}
