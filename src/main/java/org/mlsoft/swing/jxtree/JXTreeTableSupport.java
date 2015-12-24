package org.mlsoft.swing.jxtree;

import org.jdesktop.swingx.JXTreeTable;
import org.mlsoft.swing.ComponentSupporter;
import org.mlsoft.swing.ConfigHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Helper functions for jxtreetables.
 * Goals:
 * - support easy popup menu actions
 * - support selection
 * - support actions on element T (which get placed on a jtoolbar)
 * - drag and drop within the tree.
 * <p/>
 * Uses configuration by convention. You can set a configuration object which might have some methods that
 * get used by their name for action callback handlers, etc.
 * see detailed documentation on setConfiguration.
 */
public class JXTreeTableSupport<T> implements ComponentSupporter<T> {

    private static final Logger logger = LoggerFactory.getLogger(JXTreeTableSupport.class);
    private JXTreeTable tree;
    private MappedTreeTableModel<T> model;

    private ConfigHandler configHandler = new ConfigHandler(this);

    private JPanel treeWithToolbar = new JPanel();
    private JToolBar toolBar = new JToolBar();


    public JXTreeTableSupport(JXTreeTable tree, MappedTreeTableModel<T> model) {
        this.tree = tree;
        this.model = model;

        registerPopupHandler();
        registerTreeSelectionListener();
        registerMouseListener();
    }

    public JPanel createTreeWithToolbar() {
        BorderLayout bl = new BorderLayout();
        treeWithToolbar.setLayout(bl);
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(tree);

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
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {// double click
                    T t = (T) tree.getTreeSelectionModel().getLeadSelectionPath().getLastPathComponent();
                    configHandler.configMethodCall("onDoubleClickAction", t);
                }
            }
        });
    }

    public void registerPopupHandler() {
        tree.addMouseListener(new MouseAdapter() {
            // we are checking for both mouseRelease and mousePressed, since
            // on different platforms, the popup triggers are different.
            @Override
            public void mouseReleased(MouseEvent e) {
                checkPopupTrigger(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                checkPopupTrigger(e);
            }

            public void checkPopupTrigger(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JXTreeTable source = (JXTreeTable) e.getSource();
                    int row = source.rowAtPoint(e.getPoint());
                    int column = source.columnAtPoint(e.getPoint());

                    if (!source.isRowSelected(row)) {
                        source.changeSelection(row, column, false, false);
                    }
                    if (source.getTreeSelectionModel().getLeadSelectionPath() != null) {
                        T t = (T) source.getTreeSelectionModel().getLeadSelectionPath().getLastPathComponent();
                        openPopupMenu(e, t);
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
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent evt) {
                T t = (T) evt.getPath().getLastPathComponent();
                configHandler.configMethodCall("onElementSelected", t);
                configHandler.updateActionsEnabledState(t);
            }
        });
    }

    /**
     * returns the first selected element within this component. Or null, if nothing is currently selected.
     *
     * @return
     */
    @Override
    public T getFirstSelectedElement() {
        T t = (T) tree.getTreeSelectionModel().getLeadSelectionPath().getLastPathComponent();
        return t;
    }

    @Override
    public List<T> getSelectedElements() {
        List<T> result = new ArrayList<>();
        TreePath[] allPaths = tree.getTreeSelectionModel().getSelectionPaths();
        if (allPaths != null) {
            for (TreePath path: allPaths) {
                result.add((T) path.getLastPathComponent());
            }
        }
        return result;
    }
}
