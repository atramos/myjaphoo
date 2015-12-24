/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.editor.rsta;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

/**
 *
 * @author lang
 */
public class EditorSyntaxCellRenderer extends DefaultTableRenderer {

    private RSyntaxTextArea textArea = new RSyntaxTextArea();

    public EditorSyntaxCellRenderer() {
        RSTAHelper.initAsFilterEditor(textArea);
        textArea.setEnabled(false);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, Object value,
            boolean isSelected, boolean hasFocus, final int row, final int column) {
        return RSTAHelper.getTableCellRendererComponent(textArea, table, value, isSelected, hasFocus, row, column);
    }
}
