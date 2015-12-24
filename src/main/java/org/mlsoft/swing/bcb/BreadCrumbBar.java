/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing.bcb;

import net.infonode.gui.icon.button.DropDownIcon;
import net.infonode.util.Direction;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.mlsoft.swing.ComponentForRestCreator;
import org.mlsoft.swing.JPopupMenuButton;
import org.mlsoft.swing.ResizeLayout;
import org.mlsoft.swing.TreePopupSelectionEvent;
import org.mlsoft.swing.popup.PopupWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * A bread crumb bar that uses a java swing tree model as internal model.
 *
 * @author lang
 */
public class BreadCrumbBar extends JToolBar {

    public static final Logger LOGGER = LoggerFactory.getLogger(BreadCrumbBar.class.getName());
    private TreeModel treeModel;
    private TreePath selectedPath;
    private boolean refreshing = false;
    private TreeCellRenderer renderer;


    private ComponentForRestCreator buttonComponentRestCreator = new ComponentForRestCreator() {

        @Override
        public JComponent createOtherComponent(Vector<Component> theRest) {
            DropDownIcon i = new DropDownIcon(12, Direction.DOWN);
            final JPopupMenuButton restComp = new JPopupMenuButton(i);

            JPopupMenu restm = new JPopupMenu();
            restComp.setPopupmenu(restm);

            for (Component c : theRest) {
                // we need to copy the items because we insert them into another component:
                final BreadCrumbButton item = (BreadCrumbButton) c;
                final JMenuItem restItem = new JMenuItem();
                restItem.setText(item.getText());
                restItem.setIcon(item.getIcon());
                restItem.addActionListener(new ActionListener() {

                    PopupWindow pw = new PopupWindow(null);

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int y = restItem.getHeight();
                        final JComponent comp = item.lpmc.createComponent(pw);
                        pw.show(restComp, comp, 0, y);
                    }
                });
                restm.add(restItem);
            }
            return restComp;
        }
    };

    public BreadCrumbBar() {
        GlobalBus.bus.register(this);
        setLayout(new ResizeLayout(buttonComponentRestCreator));
        //setLayout(new ResizeLayout1( getLayout(), buttonComponentRestCreator));
    }

    private void refreshbar() {
        refreshing = true;
        try {
            removeAll();
            if (treeModel != null && selectedPath != null) {
                //TreeModel tmodel = model.getTreeModel();

                Object[] currPath = selectedPath.getPath();
                LOGGER.debug("seting breadcrumb to path " + selectedPath); //NOI18N
                for (Object o : currPath) {
                    createBreadCrumbElement(o);
                }
            }
        } finally {
            refreshing = false;
        }
        repaint();
    }

    private void createBreadCrumbElement(final Object o) {

        LOGGER.debug("create bread combo for " + o); //NOI18N

        final BreadCrumbButton arrowButton = BreadCrumbButton.createArrowButton(o, renderer);
        add(arrowButton);

        final BreadCrumbButton textbutton = BreadCrumbButton.createTextButton(o, renderer);
        add(textbutton);
    }

    @Subscribe(onETD = true)
    public void popupSelected(TreePopupSelectionEvent e) {
        notifyClickEvent(e.evt.getNewLeadSelectionPath());
    }

    private void notifyClickEvent(TreePath path) {
        if (!refreshing) {
            notifyPathChanged(path);
        }
    }

    private void notifyPathChanged(TreePath treePath) {
        LOGGER.debug("notify bread crumb click event on path " + treePath); //NOI18N
        GlobalBus.bus.post(new BreadCrumbClickEvent(treePath));
    }

    /**
     * @return the treeModel
     */
    public TreeModel getTreeModel() {
        return treeModel;
    }

    /**
     * @param treeModel the treeModel to set
     */
    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
        refreshbar();
    }

    /**
     * @return the selectedPath
     */
    public TreePath getSelectedPath() {
        return selectedPath;
    }

    /**
     * @param selectedPath the selectedPath to set
     */
    public void setSelectedPath(TreePath selectedPath) {
        this.selectedPath = selectedPath;
        refreshbar();
    }

    /**
     * @return the renderer
     */
    public TreeCellRenderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(TreeCellRenderer renderer) {
        this.renderer = renderer;
    }
}
