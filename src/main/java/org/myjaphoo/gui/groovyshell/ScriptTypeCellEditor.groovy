package org.myjaphoo.gui.groovyshell

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor
import org.jdesktop.swingx.combobox.EnumComboBoxModel
import org.myjaphoo.model.db.ScriptType

import javax.swing.*
import java.awt.*

/**
 * PropertyCellEditor 
 * @author lang
 * @version $Id$
 *
 */
class ScriptTypeCellEditor extends ComboBoxCellEditor {

    EnumComboBoxModel m = new EnumComboBoxModel(ScriptType.class);
    /**
     *  Creates a new ComboBoxCellEditor.
     * @param comboBox the comboBox that should be used as the cell editor.
     */
    ScriptTypeCellEditor() {
        super(new JComboBox())

    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        // change the model of the combobox according to the selected property name:
        JComboBox combo = (JComboBox) editorComponent;
        combo.setEditable(false);

        combo.setModel(m);
        return c;
    }
}
