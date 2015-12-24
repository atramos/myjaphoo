package org.mlsoft.swing.jtable

import groovy.transform.TypeChecked
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.swing.*
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * maps events from an observable list into table model change events.
 * @author lang
 * @version $Id$
 *
 */
@TypeChecked
class TableModelObservableListEventBridge {

    MappedTableModel tableModel;
    ObservableList list;
    public static final Logger LOGGER = LoggerFactory.getLogger(TableModelObservableListEventBridge.class.getName());

    public TableModelObservableListEventBridge(MappedTableModel tableModel, ObservableList list) {
        this.tableModel = tableModel;
        this.list = list;

        // set a copy of the observable list to the model.
        // from now on, we present always (immutable) list states to the model that changes
        // on every event.
        tableModel.setList(new ArrayList(list));

        list.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            void propertyChange(PropertyChangeEvent evt) {
                try {
                    if (evt instanceof ObservableList.ElementEvent) {
                        // for each event we create a cloned list of the observable list and
                        // use it to reflect the table model at that point when the event occured
                        // therefore we need to clone the list when the event occured
                        // and present it to the table model later at that time when we fire the event to the model.

                        ObservableList.ElementEvent ev = (ObservableList.ElementEvent) evt;
                        if (SwingUtilities.isEventDispatchThread()) {
                            final List clonedList = new ArrayList<>(list);
                            distributeEvent(ev, clonedList)
                        } else {
                            // clone the list (to be in the same state as when the event occured
                            // and re-set this state in the table model when firing the table model event later,
                            // to properly reflect the event/list-state order
                            final List clonedList = new ArrayList<>(list);
                            SwingUtilities.invokeAndWait {
                                distributeEvent(ev, clonedList)
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("error in event bridge distribution!", e);
                }
            }

            private void distributeEvent(ObservableList.ElementEvent ev, List clonedList) {
                // if we have a cloned list which represents the state on which this event occured,
                // then set the table model to this state before firing the table model event.
                // we do not need to take care about threading here,
                // since we are executed on the event dispatch thread:
                if (clonedList != null) {
                    tableModel.setList(clonedList);
                }

                switch (ev.changeType) {
                    case ObservableList.ChangeType.ADDED:
                        tableModel.fireTableRowsInserted(ev.index, ev.index);
                        break
                    case ObservableList.ChangeType.UPDATED:
                        tableModel.fireTableRowsUpdated(ev.index, ev.index);
                        break
                    case ObservableList.ChangeType.REMOVED:
                        tableModel.fireTableRowsDeleted(ev.index, ev.index);
                        break
                    case ObservableList.ChangeType.CLEARED:
                        tableModel.fireTableDataChanged();
                        break
                    case ObservableList.ChangeType.MULTI_ADD:
                        tableModel.fireTableDataChanged();
                        break
                    case ObservableList.ChangeType.MULTI_REMOVE:
                        tableModel.fireTableDataChanged();
                        break
                    case ObservableList.ChangeType.NONE:
                        break
                }
            }
        })
    }
}
