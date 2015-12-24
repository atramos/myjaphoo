package org.myjaphoo.gui.files;

import org.jdesktop.swingx.JXTreeTable;
import org.mlsoft.swing.annotation.ToolbarAction;
import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jxtree.JXTreeTableSupport;
import org.mlsoft.swing.jxtree.MappedTreeTableModel;
import org.myjaphoo.MyjaphooController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * File (or better Folder) panel.
 * Shows the file system with options for displaying in myjaphoo.
 */
public class FilePanel extends JPanel {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/OpenFileExplorer");

    private org.jdesktop.swingx.JXTreeTable jXTreeTable1 = new JXTreeTable();
    private MyjaphooController controller;
    private JXTreeTableSupport<FileNode> treeSupport;

    private MappedTreeTableModel<FileNode> model;

    public FilePanel(final MyjaphooController controller) {
        this.controller = controller;

        model = MappedTreeTableModel.configure(jXTreeTable1,
                FileNode.createRoot(controller),
                FileNode.class, "parent", "children",
                ColDescr.col("name", "Name").setWidth(300),
                ColDescr.col("numOfContainingMovies", "Num Entries in DB").setWidth(80),
                ColDescr.col("fileSizeInDB", "Size in DB").setWidth(80),
                ColDescr.col("foundPathInDb", "Path in DB"));

        treeSupport = new JXTreeTableSupport<FileNode>(jXTreeTable1, model);
        // set this class as config by convention object to use action handler direct defined as methods here
        treeSupport.setConfiguration(this);
        jXTreeTable1.setTreeCellRenderer(new FilePanelCellRenderer());
        initComponents();
    }

    /**
     * conf.by.convention handler for popup trigger events for the tree.
     * @param fileNode
     * @return
     */
    public JPopupMenu getPopupFor(final FileNode fileNode) {
        JPopupMenu m = new JPopupMenu("FileMenu"); //NOI18N
         if (fileNode.getFile().isDirectory()) {
             m.add(new AbstractAction("import from this directory...") {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     controller.showImportDialog(fileNode.getFile().getAbsolutePath());
                 }
             });
             String openDirInFileExplorerName = localeBundle.getString("OPEN DIR IN FILE EXPLORER");
             m.add(new AbstractAction(openDirInFileExplorerName) {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     try {
                         Desktop.getDesktop().open(fileNode.getFile());
                     } catch (IOException e1) {
                         e1.printStackTrace();
                     }
                 }
             });

         }
         return m;
    }


    @ToolbarAction(name = "Import", order = 10)
    public void importDirAction(FileNode fileNode) {
        if (fileNode.getFile().isDirectory()) {
            controller.showImportDialog(fileNode.getFile().getAbsolutePath());
        }
    }

    @ToolbarAction(name = "Refresh", order = 5, contextRelevant = false)
    public void refreshAction() {
        model.setRoot(FileNode.createRoot(controller));
    }

    private List<FileNode> collectCheckedNodes(MappedTreeTableModel<FileNode> model) {
        FileNode root = (FileNode) model.getRoot();
        return root.collectSelectedNodes();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        add(treeSupport.createTreeWithToolbar(), java.awt.BorderLayout.CENTER);
    }
}
