package org.mlsoft.swing.jtable;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.mlsoft.swing.ComponentSupporter;
import org.mlsoft.swing.ConfigHandler;
import org.myjaphoo.gui.util.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Table support functions for JXTables.
 * Goals:
 * - support easy popup menu actions
 * - support selection
 * - support actions on element T (which get placed on a jtoolbar)
 * <p/>
 * Uses configuration by convention. You can set a configuration object which might have some methods that
 * get used by their name for action callback handlers, etc.
 * see detailed documentation on setConfiguration.
 */
public class JXTableSupport<T> implements ComponentSupporter<T> {
    private static final Logger logger = LoggerFactory.getLogger(JXTableSupport.class);
    private JXTable table;
    private MappedTableModel<T> model;

    private ConfigHandler configHandler = new ConfigHandler(this);

    private JXPanel treeWithToolbar = new JXPanel();
    private JToolBar toolBar = new JToolBar();

    public JXTableSupport(JXTable table, MappedTableModel<T> model) {
        this.table = table;
        this.model = model;

        registerPopupHandler();
        registerTreeSelectionListener();
        registerMouseListener();
    }

    public JXPanel createTableWithToolbar() {
        BorderLayout bl = new BorderLayout();
        treeWithToolbar.setLayout(bl);
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(table);

        treeWithToolbar.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        treeWithToolbar.add(toolBar, java.awt.BorderLayout.NORTH);
        return treeWithToolbar;
    }

    /**
     * sets the configuration object. Methods defined on that object with special reserved names and signature get
     * used for callback handlers for this tree. This is the "configuration by convention" part of this implementation.
     * A implementor needs not to define all of them if he does not
     * want to react on all events. The following methods get used:
     * <p/>
     * - JPopupMenu getPopupFor(T selectedEleemnt) : creates a popup if a popup trigger has been recognized on a given item.
     * - void onDoubleClickAction(T selElement): action handler when a double click happens on a given item.
     * - void onElementSelected(T selElement): action handler when a element got selected in the tree.
     *
     * @param configByConventionHandler
     */
    public void setConfiguration(Object configByConventionHandler) {
        setConfiguration(configByConventionHandler, null);
    }

    public void setConfiguration(Object configByConventionHandler, ResourceBundle localeBundle) {
        configHandler.setConfiguration(configByConventionHandler, localeBundle);
        configHandler.updateToolbarActions(toolBar);
    }


    private void registerMouseListener() {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {// double click
                    T t = getFirstSelectedElement();
                    if (t != null) {
                        configHandler.configMethodCall("onDoubleClickAction", t);
                    }
                }
            }
        });
    }

    public T getFirstSelectedElement() {
        int[] selRows = Tables.getModelSelectedRowsForJXTable(table);
        if (selRows.length > 0) {
            int row = selRows[0];
            T t = model.get(row);
            return t;
        }
        return null;
    }

    @Override
    public List<T> getSelectedElements() {
        List<T> result = new ArrayList<>();
        int[] selRows = Tables.getModelSelectedRowsForJXTable(table);
        if (selRows.length > 0) {
            for (int row: selRows) {
                T t = model.get(row);
                result.add(t);
            }
        }
        return result;
    }

    public void registerPopupHandler() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                checkPopupTrigger(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                checkPopupTrigger(e);
            }

            private void checkPopupTrigger(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    ensureClickPointIsSelected(e);
                    T t = getFirstSelectedElement();
                    if (t != null) {
                        openPopupMenu(e, t);
                    }
                }
            }

            /**
             * Selects the element under the click point, if it is not yet selected.
             *
             * @param e
             */
            private void ensureClickPointIsSelected(MouseEvent e) {
                if (e.getSource() instanceof JTable) {
                    JTable source = (JTable) e.getSource();
                    int row = source.rowAtPoint(e.getPoint());
                    int column = source.columnAtPoint(e.getPoint());

                    if (!source.isRowSelected(row)) {
                        source.changeSelection(row, column, false, false);
                    }
                } else if (e.getSource() instanceof JList) {
                    JList list = (JList) e.getSource();
                    int index = list.locationToIndex(e.getPoint());
                    if (!list.isSelectedIndex(index)) {
                        list.addSelectionInterval(index, index);
                    }
                }
            }
        });
    }

    private void openPopupMenu(MouseEvent e, T t) {
        try {
            JPopupMenu popup = configHandler.createPopup();
            if (popup != null) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        } catch (Exception ex) {
            logger.warn("config by convention: getPopupTrigger failed!", ex);
        }
    }


    public void registerTreeSelectionListener() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                T t = getFirstSelectedElement();
                if (t != null) {
                    configHandler.configMethodCall("onElementSelected", t);
                    configHandler.updateActionsEnabledState(t);
                }
            }
        });

    }

    public JToolBar getToolBar() {
        return toolBar;
    }
}
