package org.myjaphoo.gui.movieprops

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor
import org.jdesktop.swingx.combobox.ListComboBoxModel
import org.myjaphoo.gui.editor.rsta.CachedHints

import javax.swing.*
import java.awt.*

/**
 * PropertyCellEditor 
 * @author lang
 * @version $Id$
 *
 */
class PropertyCellEditor extends ComboBoxCellEditor {


    /**
     *  Creates a new ComboBoxCellEditor.
     * @param comboBox the comboBox that should be used as the cell editor.
     */
    PropertyCellEditor() {
        super(new JComboBox())
    }

//    @Override
//    Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
//        Component c = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
//        // change the model of the combobox according to the selected property name:
//        JComboBox combo = (JComboBox) editorComponent;
//        combo.setEditable(true);
//
//
//        String propKeyName = tree.getValueAt(row, 0).toString();
//        java.util.List<String> hints = CachedHints.getHintsForAttributes(controller.model.entity, propKeyName);
//        if (hints == null) {
//            hints = new ArrayList<>();
//        }
//        def model = new ListComboBoxModel(hints);
//        model.setSelectedItem(value);
//        combo.setModel(model);
//        return c;
//    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        // change the model of the combobox according to the selected property name:
        JComboBox combo = (JComboBox) editorComponent;
        combo.setEditable(true);
        if (table.getValueAt(row, 0) instanceof AttributeNode) {
            AttributeNode node = (AttributeNode) table.getValueAt(row, 0);
            String propKeyName = node.getName();
            node.getEntity();
            java.util.List<String> hints = CachedHints.getHintsForAttributes(node.getEntity(), propKeyName);
            if (hints == null) {
                hints = new ArrayList<>();
            }
            def model = new ListComboBoxModel(hints);
            model.setSelectedItem(value);
            combo.setModel(model);
        }
        return c;
    }
}
