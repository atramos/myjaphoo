/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.db;

import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.util.tables.BaseTable;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public class DatabaseConnectionTable extends BaseTable {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/db/resources/DatabaseConnectionTableModel");

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTable.class);
    private DatabaseConnectionController controller;

    public DatabaseConnectionTable(DatabaseConnectionController controller) {
        this.controller = controller;
        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
    }

    public void refreshModel() {
        DatabaseConfigurations databaseConfigurations = controller.load();
        MappedTableModel<DatabaseConfiguration> model = MappedTableModel.configure(this, databaseConfigurations.getDatabaseConfigurations(), DatabaseConfiguration.class, localeBundle,
                ColDescr.col("name", "Name"),
                ColDescr.col("filledConnectionUrl", "Connection"),
                ColDescr.col("databaseDriver.driverName", "Type"),
                ColDescr.col("databaseDriver.driverAvailable", "DriverAvailable")
                ) ;



    }
}
