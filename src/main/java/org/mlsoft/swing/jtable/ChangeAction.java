package org.mlsoft.swing.jtable;

/**
 * Change action handler for table or tree edit change events.
 */
public interface ChangeAction<T> {

    boolean onChangeNode(T row, Object oldValue, Object value, ColDescr<T> col);
}
