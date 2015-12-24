/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.jdesktop.swingx.JXTable;

/**
 * Helper methods for jdk6 tables.
 * @author mla
 */
public class Tables {

    public static int getModelSelectedCol(JTable table) {
        int[] cols = table.getColumnModel().getSelectedColumns();
        return cols[0];
    }

    public static int[] getModelSelectedCols(JTable table) {
        //ListSelectionModel selModel = table.getColumnModel().getSelectionModel();
        //int[] cols = getSelectedRows(selModel);
        int[] cols = table.getColumnModel().getSelectedColumns();
        return cols;
    }

    public static int getModelSelectedRow(JTable table) {
        int row = table.getSelectedRow();
        if (table.getRowSorter() != null) {
            return table.getRowSorter().convertRowIndexToModel(row);
        } else {
            return row;
        }
    }

    public static int[] getModelSelectedRows(JTable table) {
        int[] rows = table.getSelectedRows();
        if (table.getRowSorter() == null) {
            return rows;
        } else {
            int[] modelRows = new int[rows.length];
            for (int i = 0; i < rows.length; i++) {
                modelRows[i] = table.getRowSorter().convertRowIndexToModel(rows[i]);
            }
            return modelRows;
        }
    }

    public static int[] getModelSelectedRowsForJXTable(JXTable table) {

        int[] rows = table.getSelectedRows();

        int[] modelRows = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            modelRows[i] = table.convertRowIndexToModel(rows[i]);
        }
        return modelRows;

    }

}
