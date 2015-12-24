/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui;
import java.util.List;
import javax.swing.table.*;
/**
 *
 * @author mla
 */
public abstract class WmTableModel<T>  extends AbstractTableModel {

    protected List<T> nodes;

    private String[] colnames;

    public WmTableModel(List<T> nodes, String[] colnames) {
        this.nodes = nodes;
        this.colnames = colnames;
    }

    @Override
    public final int getRowCount() {
        return nodes.size();
    }

    @Override
    public final int getColumnCount() {
        return colnames.length;
    }

    /**
     * Returns the column name.
     *
     * @return a name for this column using the string value of the
     * appropriate member in <code>columnIdentifiers</code>.
     * If <code>columnIdentifiers</code> does not have an entry
     * for this index, returns the default
     * name provided by the superclass.
     */
    @Override
    public final String getColumnName(int columnIndex) {
        return colnames[columnIndex];
    }

    public final void refreshModelData(List<T> nodes) {
        this.nodes = nodes;
        super.fireTableDataChanged();
        //super.fireTableStructureChanged();
    }
}
