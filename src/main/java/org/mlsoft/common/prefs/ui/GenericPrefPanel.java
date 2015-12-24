package org.mlsoft.common.prefs.ui;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */
import org.mlsoft.common.swing.TreeNodeModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.mlsoft.common.prefs.model.*;
import org.mlsoft.common.prefs.model.edit.*;
import org.mlsoft.common.prefs.model.editors.EditorGroup;
import org.mlsoft.common.prefs.model.editors.EditorRoot;
import org.mlsoft.structures.AbstractTreeNode;


public class GenericPrefPanel extends JPanel implements TreeSelectionListener {


    // create some components
    private JScrollPane scroll = new JScrollPane();

    private JTree scrollTree = new JTree();
    private JSplitPane splitPane = null;

    /**
     * The PreferencesPanel constructor creates the Preferences window and
     * delegates the creation of each Preferences component.
     */
    public GenericPrefPanel(EditorRoot structure, JPanel additionalPanel) {


        this.setLayout(new BorderLayout());

        if (additionalPanel != null) {
          Marker marker = new Marker(structure, additionalPanel.toString());
          itemMap.put(marker, additionalPanel);
        }
        //Create a tree that allows one selection at a time.
        scrollTree.setModel( new PrefPanelTreeModel(structure));
        scrollTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        scrollTree.addTreeSelectionListener(this);
        scrollTree.setMinimumSize(new Dimension(100, 450));
        scrollTree.setPreferredSize(new Dimension(100, 450));
        scrollTree.setRootVisible(false);
        scrollTree.setShowsRootHandles(true);

        scroll.setViewportView(scrollTree);
        //Create a splitpane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(scroll);
        splitPane.setRightComponent( getComponent((EditorGroup)structure.getChildAt (0)));
        splitPane.setDividerLocation(100);
        splitPane.setOneTouchExpandable(true);
        splitPane.setPreferredSize(new Dimension(740, 450));
        splitPane.setMaximumSize(new Dimension(740, 450));
        this.add(splitPane, BorderLayout.CENTER);
    }

    static class Marker extends EditorGroup
    {
      public Marker(AbstractMetadata parent, String name)
      {
        super(parent,name,name,name);
      }

    }

    public void valueChanged(TreeSelectionEvent event) {
        AbstractTreeNode node = (AbstractTreeNode) scrollTree.getLastSelectedPathComponent();

        if (node instanceof EditableGroup)
        {
          JPanel page = getComponent( (EditorGroup) node);
          splitPane.setRightComponent(page);
        }
    }

    private TreeMap<EditorGroup, JPanel> itemMap = new TreeMap<EditorGroup, JPanel>();


    private JPanel getComponent(EditorGroup item)
    {
      Object found = itemMap.get(item);
      if (found!=null)
        return (JPanel)found;

      GenericPrefPage page= new GenericPrefPage( item);
      itemMap.put(item,page);
      return page;
    }


}
