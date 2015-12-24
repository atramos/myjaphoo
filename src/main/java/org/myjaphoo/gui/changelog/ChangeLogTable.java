/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.changelog;

import java.util.List;
import java.util.ResourceBundle;

import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.util.tables.BaseTable;
import org.myjaphoo.model.db.ChangeLog;

/**
 *
 * @author mla
 */
public class ChangeLogTable extends BaseTable {

    private MyjaphooController controller;

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/changelog/resources/ChangeLogTableModel");

    public ChangeLogTable(MyjaphooController controller) {
        this.controller = controller;
        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
    }

    public void refreshModel() {
        List<ChangeLog> entries = controller.getAllChangeLogs();

        MappedTableModel<ChangeLog> model = MappedTableModel.configure(this, entries, ChangeLog.class, localeBundle, ColDescr.col("created", "Created"),
                ColDescr.col("cltype.descr", "type"),
                ColDescr.col("msg", "message"),
                ColDescr.col("objDescription", "targets"));

    }

}
