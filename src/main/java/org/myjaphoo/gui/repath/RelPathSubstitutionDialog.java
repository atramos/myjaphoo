/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RelPathSubstitutionDialog.java
 *
 * Created on 08.01.2009, 17:28:59
 */

package org.myjaphoo.gui.repath;

import org.jdesktop.swingx.JXPanel;
import org.mlsoft.swing.annotation.ContextMenuAction;
import org.mlsoft.swing.annotation.ToolbarAction;
import org.mlsoft.swing.jtable.JXTableSupport;
import org.myjaphoo.MovieTreeModel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.model.db.PathMapping;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public class RelPathSubstitutionDialog extends javax.swing.JDialog {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/repath/resources/RelPathSubstitutionDialog");

    private javax.swing.JFileChooser jFileChooser1;
    private RelPathTable jTable1;
    private javax.swing.JTree jTree1;

    private MyjaphooController controller;

    /** Creates new form RelPathSubstitutionDialog */
    public RelPathSubstitutionDialog(java.awt.Frame parent, MyjaphooController controller) {
        super(parent, true);
        this.controller = controller;
        initComponents();
        
    }
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relative Path Substitutions");

        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser1.setControlButtonsAreShown(false);
        jFileChooser1.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        jFileChooser1.setName("jFileChooser1"); // NOI18N

        jTree1 = new javax.swing.JTree();
        jTree1.setModel(new MovieTreeModel(controller.getFilter().createMovieTreeModel()));
        JPanel mainPanel = new JPanel();
        JSplitPane selectorPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        selectorPanel.setLeftComponent(jFileChooser1);
        selectorPanel.setRightComponent(new JScrollPane(jTree1));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(selectorPanel, BorderLayout.NORTH);

        jTable1 = new RelPathTable();
        JXTableSupport tableSupport = new JXTableSupport<>(jTable1, jTable1.getRelPathModel());
        JXPanel tablePanel = tableSupport.createTableWithToolbar();
        tableSupport.setConfiguration(this);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        add(mainPanel);
        setSize(600, 350);
        pack();
    }

    @ToolbarAction(name = "add Relation Mapping", contextRelevant = false)
    @ContextMenuAction(name = "add Relation Mapping", contextRelevant = false)
    public void allRelAction() {
        File selectedFile = jFileChooser1.getSelectedFile();
        TreePath selectedPath = jTree1.getSelectionPath();
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, localeBundle.getString("NO DIRECTORY SELECTED!"));
            return;
        }
        if (selectedPath == null) {
            JOptionPane.showMessageDialog(this, localeBundle.getString("NO PREFIX PATH IN MOVIE TREE SELECTED!"));
            return;
        }
        PathMapping newMapping = controller.addMapping(selectedFile, (MovieStructureNode) selectedPath.getLastPathComponent());
        jTable1.addNewMapping(newMapping);
    }

    @ToolbarAction(name = "remove")
    @ContextMenuAction(name = "remove")
    public void delRelAction(PathMapping bm) {
        if (controller.confirm("Really delete mapping '" + bm.getSubstitution() + "'?")) {
            controller.removePathMapping(bm);
            jTable1.removeMapping(bm);
        }
    }


}
