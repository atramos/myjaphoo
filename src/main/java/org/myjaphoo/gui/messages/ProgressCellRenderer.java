/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.messages;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

/**
 *
 * @author lang
 */
public class ProgressCellRenderer extends DefaultTableRenderer {

    private JProgressBar progressbar = new JProgressBar();

    public ProgressCellRenderer() {
        progressbar.setMinimum(0);
        progressbar.setMaximum(100);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, Object value,
            boolean isSelected, boolean hasFocus, final int row, final int column) {

        if (value instanceof Integer) {

            progressbar.setValue(((Integer) value).intValue());
        } else {
            progressbar.setValue(0);
        }

        if (isSelected) {
            progressbar.setForeground(table.getSelectionForeground());
            progressbar.setBackground(table.getSelectionBackground());
        } else {
            progressbar.setForeground(table.getForeground());
            progressbar.setBackground(table.getBackground());
        }
        if (hasFocus) {
            if (!isSelected /**&& context.isEditable()*/
                    ) {
                Color col = table.getForeground();
                if (col != null) {
                    progressbar.setForeground(col);
                }
                col = table.getBackground();
                if (col != null) {
                    progressbar.setBackground(col);
                }
            }
        }

        return progressbar;
    }
}
