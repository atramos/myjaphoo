/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.groovyshell;

import org.mlsoft.swing.jtable.ChangeAction;
import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.util.tables.BaseTable;
import org.myjaphoo.model.db.SavedGroovyScript;
import org.myjaphoo.model.logic.ScriptJpaController;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.mlsoft.swing.jtable.ColDescr.col;

/**
 * @author mla
 */
public class ScriptTable extends BaseTable {

    private ScriptJpaController jpa = new ScriptJpaController();

    private MyjaphooController controller;

    private MappedTableModel<SavedGroovyScript> model;

    public ScriptTable(MyjaphooController controller) {
        this.controller = controller;
        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
        init();
    }

    private void init() {
        List<SavedGroovyScript> entries = MainApplicationController.getInstance().getScriptList();

        model = MappedTableModel.configure(this, entries, SavedGroovyScript.class,
                col("name", "Script", false),
                col("menuPath", "Menu Path", true),
                col("scriptType", "Type", true).setCellEditor(new ScriptTypeCellEditor()),
                col("name", "Name", true),
                col("descr", "Description", true)
        );

        model.setChangeAction(new ChangeAction<SavedGroovyScript>() {
            @Override

            public boolean onChangeNode(SavedGroovyScript script, Object oldVal, Object value, ColDescr<SavedGroovyScript> col) {

                try {
                    jpa.edit(script);
                    MainApplicationController.getInstance().getScriptList().fireListElementChanged(script);
                } catch (Exception ex) {
                    LoggerFactory.getLogger(ScriptTable.class.getName()).error("error", ex); //NOI18N
                    throw new RuntimeException(ex);
                }
                return true;
            }
        });
    }

    public void refreshModel() {
        MainApplicationController.getInstance().reloadScriptList();
    }

    public MappedTableModel<SavedGroovyScript> getScriptModel() {
        return model;
    }

}
