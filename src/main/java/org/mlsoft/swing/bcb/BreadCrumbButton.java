/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing.bcb;

import net.infonode.gui.icon.button.ArrowIcon;
import net.infonode.util.Direction;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.swing.PopUps;
import org.mlsoft.swing.TreePopupSelectionEvent;
import org.mlsoft.swing.popup.PopupWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * A bread crumb button showing one piece ("a bread crumb) within the bread crumb bar.
 * @author lang
 */
public class BreadCrumbButton extends JButton {

    /** the tree to pop up. */
    private JTree tree = new JTree();
    private TreeCellRenderer renderer;
    private Object o;
    PopUps.LazyPopupMenuCreator lpmc = new PopUps.LazyPopupMenuCreator() {

        @Override
        public JPopupMenu createPopup() {
            throw new IllegalArgumentException();
            //return createTreePopup(o, tree, renderer);
        }

        public JComponent createComponent(PopupWindow pw) {
            return createTree(o, tree, renderer, pw);
        }
    };

    public BreadCrumbButton(final Object o, TreeCellRenderer renderer) {
        this.renderer = renderer;
        this.o = o;
        PopUps.makePopupForButton2(this, lpmc);

    }
    public static final Logger LOGGER = LoggerFactory.getLogger(BreadCrumbButton.class.getName());

    public static BreadCrumbButton createArrowButton(final Object o, final TreeCellRenderer renderer) {
        LOGGER.debug("create bread combo for " + o); //NOI18N

        final BreadCrumbButton arrowButton = new BreadCrumbButton(o, renderer);
        ArrowIcon icon = new ArrowIcon(Direction.RIGHT);
        arrowButton.setIcon(icon);

        return arrowButton;
    }

    public static BreadCrumbButton createTextButton(final Object o, final TreeCellRenderer renderer) {
        LOGGER.debug("create bread combo for " + o); //NOI18N

        final BreadCrumbButton button = new BreadCrumbButton(o, renderer);
        button.setText(o.toString());

        if (renderer != null) {
            button.addRendererInfoToTextButton();
        }
        return button;
    }

    private static JTree createTree(Object o, final JTree tree, TreeCellRenderer renderer, final PopupWindow pw) {

        tree.setModel(createPartialModel(o));
        if (renderer != null) {
            tree.setCellRenderer(renderer);
        }
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                GlobalBus.bus.post(new TreePopupSelectionEvent(tree, evt));

                pw.setVisible(false);
            }
        });
        return tree;
    }

    private static TreeModel createPartialModel(Object o) {
        if (o instanceof TreeNode) {
            DefaultTreeModel m = new DefaultTreeModel((TreeNode) o);
            return m;
        }
        return null;
    }

    /**
     * use the renderer to render text and icons (and probably other information)
     * to the button.
     */
    private void addRendererInfoToTextButton() {
        // no good idea how to realize that. here a silly one:
        // call the renderer which will return a JLabel component.
        // take all relevant properties from that component and set it to the button:
        JLabel rendLabel = (JLabel) renderer.getTreeCellRendererComponent(tree, o, false, false, false, 0, false);
        if (rendLabel.getIcon() != null) {
            setIcon(rendLabel.getIcon());
        }
        if (rendLabel.getText() != null) {
            setText(rendLabel.getText());
        }
    }
}
