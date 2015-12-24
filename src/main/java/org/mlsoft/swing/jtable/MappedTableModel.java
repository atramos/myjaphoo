package org.mlsoft.swing.jtable;


import groovy.util.ObservableList;
import org.mlsoft.swing.util.MappingUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Table model based on property mapping.
 */
public class MappedTableModel<T> extends AbstractTableModel {

    private List<T> list;
    private Class<T> clazz;
    private ColDescr[] colDescrs;

    private ResourceBundle localeBundle = null;

    private ChangeAction<T> changeAction = null;

    private MappedTableModel(List<T> list, Class<T> clazz, ColDescr... colDescrs) {
        this.list = list;
        this.clazz = clazz;
        this.colDescrs = colDescrs;
        // if its a observable list, then send events to our table model:
        if (list instanceof ObservableList) {
            TableModelObservableListEventBridge tdelegate = new TableModelObservableListEventBridge(this, (ObservableList) list);
        }
    }


    public static <T> MappedTableModel<T> configure(JTable table, List<T> list, Class<T> clazz, ColDescr... colDescrs) {
        return configure(table, list, clazz, null, colDescrs);
    }

    public static <T> MappedTableModel<T> configure(JTable table, List<T> list, Class<T> clazz, ResourceBundle localeBundle, ColDescr... colDescrs) {
        MappedTableModel<T> model = new MappedTableModel<T>(list, clazz, colDescrs);
        model.localeBundle = localeBundle;
        table.setModel(model);
        model.configureTable(table);
        return model;
    }

    private void configureTable(JTable table) {
        for (int i = 0; i < colDescrs.length; i++) {
            ColDescr col = colDescrs[i];
            TableColumn tableCol = table.getColumnModel().getColumn(i);
            if (col.getCellRenderer() != null) {
                tableCol.setCellRenderer(col.getCellRenderer());
            }
            if (col.getCellEditor() != null) {
                tableCol.setCellEditor(col.getCellEditor());
            }
            if (col.getWidth() > 0) {
                tableCol.setWidth(col.getWidth());
                tableCol.setMinWidth(col.getWidth());
            }
        }
    }

    public T get(int index) {
        return list.get(index);
    }

    @Override
    public String getColumnName(int column) {
        return getLocaleString(colDescrs[column].getUiName());
    }

    private String getLocaleString(String string) {
        return localeBundle != null ? localeBundle.getString(string) : string;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> cl = colDescrs[columnIndex].getPropertyAccessor().getType(clazz);
        return MappingUtils.getCorrectType(cl);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return colDescrs[columnIndex].isEditable(list.get(rowIndex));
    }


    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return colDescrs.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T row = list.get(rowIndex);
        return colDescrs[columnIndex].getPropertyAccessor().getVal(row);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        T row = list.get(rowIndex);
        ColDescr col = colDescrs[columnIndex];
        Object oldVal = col.getPropertyAccessor().getVal(row);

        col.getPropertyAccessor().setVal(row, aValue);
        if (changeAction != null) {
            if (!changeAction.onChangeNode(row, oldVal, aValue, col)) {
                return;
            }
        }
    }


    public ChangeAction<T> getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(ChangeAction<T> changeAction) {
        this.changeAction = changeAction;
    }

    public void refresh(List<T> entries) {
        this.list = entries;
        fireTableDataChanged();
    }

    protected void setList(List<T> list) {
        this.list = list;
    }
}
