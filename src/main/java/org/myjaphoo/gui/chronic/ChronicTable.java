/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.chronic;

import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.editor.rsta.EditorSyntaxCellRenderer;
import org.myjaphoo.gui.util.tables.BaseTable;
import org.myjaphoo.model.db.ChronicEntry;

import java.util.List;
import java.util.ResourceBundle;

import static org.mlsoft.swing.jtable.ColDescr.col;

/**
 * @author mla
 */
public class ChronicTable extends BaseTable {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/chronic/resources/ChronicTableModel");

    private MyjaphooController controller;

    private MappedTableModel<ChronicEntry> model;

    public ChronicTable(MyjaphooController controller) {
        this.controller = controller;
        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
        List<ChronicEntry> entries = MainApplicationController.getInstance().getChronicList();

        model = MappedTableModel.configure(this, entries, ChronicEntry.class, localeBundle,
                col("view.created", "Created", false),
                col("view.filterExpression", "Filter", false).setCellRenderer(new EditorSyntaxCellRenderer()),
                col("view.currentSelectedDir", "Dir", false),
                col("view.userDefinedStruct", "Grouping structure", false).setCellRenderer(new EditorSyntaxCellRenderer()),
                col("view.thumbmode", "Thumb Mode", false)
        );
    }


    public void refreshModel() {
        MainApplicationController.getInstance().reloadChronicList();
    }

    public MappedTableModel<ChronicEntry> getChronicModel() {
        return model;
    }
}
