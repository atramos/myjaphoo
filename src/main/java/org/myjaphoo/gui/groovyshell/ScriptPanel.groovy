/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BookMarkPanel.java
 *
 * Created on 05.03.2010, 12:42:25
 */
package org.myjaphoo.gui.groovyshell

import groovy.transform.TypeChecked
import org.mlsoft.swing.annotation.ContextMenuAction
import org.mlsoft.swing.annotation.ToolbarAction
import org.mlsoft.swing.jtable.JXTableSupport
import org.myjaphoo.MyjaphooController
import org.myjaphoo.gui.MainApplicationController
import org.myjaphoo.gui.panel.AbstractEmbeddablePanel
import org.myjaphoo.gui.scripting.Scripting
import org.myjaphoo.model.db.SavedGroovyScript

import javax.swing.*

/**
 * @author mla
 */
@TypeChecked
public class ScriptPanel extends AbstractEmbeddablePanel {

    private MyjaphooController controller;
    private ScriptTable bookmarkTable;
    private JXTableSupport<SavedGroovyScript> tableSupport;


    /**
     * Creates new form BookMarkPanel
     */
    public ScriptPanel(MyjaphooController controller) {
        this.controller = controller;
        bookmarkTable = new ScriptTable(controller);
        bookmarkTable.refreshModel();
        tableSupport = new JXTableSupport<>(bookmarkTable, bookmarkTable.getScriptModel());
        tableSupport.setConfiguration(this, null);
        setLayout(new java.awt.BorderLayout());
        JPanel panel = tableSupport.createTableWithToolbar();
        add(panel, java.awt.BorderLayout.CENTER);
        setMinimumSize(new java.awt.Dimension(99, 25));
        setName("Form"); // NOI18N
    }

    @ToolbarAction(name = "refresh", contextRelevant = false)
    @ContextMenuAction(name = "refresh", contextRelevant = false)
    public void refreshButtonAction() {
        MainApplicationController.getInstance().reloadScriptList();
    }

    @ToolbarAction(name = "delete")
    @ContextMenuAction(name = "delete")
    public void deleteButtonAction(List<SavedGroovyScript> bms) {
        def names = bms.collect{ SavedGroovyScript it -> it.name }

        if (controller.confirm("Really delete script '$names'?")) {
            bms.each {   SavedGroovyScript it ->
                controller.deleteScript(it);
            }
        }
    }

    @ToolbarAction(name = "new", contextRelevant = false)
    @ContextMenuAction(name = "new", contextRelevant = false)
    public void newAction() {
        String name = controller.getInputValue("New Script name?");
        if (name != null) {
            SavedGroovyScript script = new SavedGroovyScript();
            script.setName(name);
            controller.addScript(script);
        }
    }

    @ToolbarAction(name = "edit")
    @ContextMenuAction(name = "edit")
    public void editButtonAction(SavedGroovyScript bm) {
        Scripting.editScript(controller, bm);
    }

    public void onDoubleClickAction(SavedGroovyScript node) {
        Scripting.editScript(controller, node);
    }


    @Override
    public String getTitle() {
        return "Groovy Scripts";
    }

    @Override
    public void refreshView() {
    }

    @Override
    public void clearView() {
    }
}
