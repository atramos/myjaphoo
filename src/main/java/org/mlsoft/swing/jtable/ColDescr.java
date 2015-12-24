package org.mlsoft.swing.jtable;

import org.mlsoft.common.DefaultPropertyAccessImpl;
import org.mlsoft.common.PropertyAccessor;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Column description.
 */
public class ColDescr<T> {

    private PropertyAccessor propertyAccessor;

    private String uiName;

    private boolean editable;

    private PropertyAccessor editableAccessor = null;

    private TableCellRenderer cellRenderer;

    private TableCellEditor cellEditor;

    private ChangeAction<T> changeAction;

    private int width;

    public ColDescr(String propExpr) {
        this.propertyAccessor = createPropertyAccessor(propExpr);
        this.setUiName(propExpr);
        this.setEditable(false);
    }

    public static PropertyAccessor createPropertyAccessor(String propExpr) {
        return new DefaultPropertyAccessImpl(propExpr);
    }

    public ColDescr(String propExpr, String uiName) {
        this.propertyAccessor = createPropertyAccessor(propExpr);
        this.setUiName(uiName);
        this.setEditable(false);
    }

    public ColDescr(String propExpr, String uiName, boolean editable) {
        this.propertyAccessor = createPropertyAccessor(propExpr);
        this.setUiName(uiName);
        this.setEditable(editable);
    }

    public ColDescr(String propExpr, String uiName, String editableExpression) {
        this.propertyAccessor = createPropertyAccessor(propExpr);
        this.setUiName(uiName);
        this.editableAccessor = new DefaultPropertyAccessImpl(editableExpression);
    }


    public static ColDescr col(String propExpr) {
        return new ColDescr(propExpr);
    }

    public static ColDescr col(String propExpr, String uiName) {
        return new ColDescr(propExpr, uiName);
    }

    public static ColDescr col(String propExpr, String uiName, boolean editable) {
        return new ColDescr(propExpr, uiName, editable);
    }

    public static ColDescr col(String propExpr, String uiName, String editableExpr) {
        return new ColDescr(propExpr, uiName, editableExpr);
    }


    public String getUiName() {
        return uiName;
    }

    public ColDescr setUiName(String uiName) {
        this.uiName = uiName;
        return this;
    }

    public boolean isEditable(Object o) {
        if (editableAccessor != null) {
            return (Boolean) editableAccessor.getVal(o);
        } else {
            return editable;
        }
    }

    public ColDescr setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }


    public TableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public ColDescr setCellRenderer(TableCellRenderer ceRenderer) {
        this.cellRenderer = ceRenderer;
        return this;
    }

    public TableCellEditor getCellEditor() {
        return cellEditor;
    }

    public ColDescr setCellEditor(TableCellEditor cellEditor) {
        this.cellEditor = cellEditor;
        return this;
    }

    public ChangeAction<T> getChangeAction() {
        return changeAction;
    }

    public ColDescr setChangeAction(ChangeAction<T> changeAction) {
        this.changeAction = changeAction;
        return this;
    }

    public ColDescr setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public PropertyAccessor getPropertyAccessor() {
        return propertyAccessor;
    }
}
