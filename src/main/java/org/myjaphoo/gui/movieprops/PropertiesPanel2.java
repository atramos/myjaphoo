package org.myjaphoo.gui.movieprops;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.mlsoft.swing.annotation.ContextMenuAction;
import org.mlsoft.swing.annotation.ToolbarAction;
import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jxtree.JXTreeTableSupport;
import org.mlsoft.swing.jxtree.MappedTreeTableModel;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.editor.rsta.CachedHints;
import org.myjaphoo.gui.editor.rsta.MarkOccurencesRenderer;
import org.myjaphoo.gui.util.Utils;
import org.myjaphoo.model.db.AttributedEntity;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * File (or better Folder) panel.
 * Shows the file system with options for displaying in myjaphoo.
 */
public class PropertiesPanel2 extends JPanel {


    private JXTreeTable jXTreeTable1 = new JXTreeTable();

    private JXTreeTableSupport<PropertyNode> treeSupport;

    private MappedTreeTableModel<PropertyNode> model;

    private MyjaphooController controller;

    private Color COLOR_NODE_HEADER_NODE = new Color(94, 55, 243);
    private Color COLOR_HEADER_NODE = new Color(136, 146, 243);
    private Color COLOR_INFO_NODE = new Color(140, 164, 255);
    private Color COLOR_EXIF_NODE = new Color(221, 158, 251);

    private Color COLOR_CHANGED_NODE = new Color(243, 230, 135);


    static abstract class PropHighlightPredicate implements HighlightPredicate {

        @Override
        public final boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
            return adapter.getValue(0) != null && isHighlighted((PropertyNode) adapter.getValue(0), renderer, adapter);
        }

        abstract protected boolean isHighlighted(PropertyNode value, Component renderer, ComponentAdapter adapter);
    }

    static class ClassNodePredicate extends PropHighlightPredicate {

        private Class theClass;

        ClassNodePredicate(Class theClass) {
            this.theClass = theClass;
        }

        @Override
        protected boolean isHighlighted(PropertyNode value, Component renderer, ComponentAdapter adapter) {
            return theClass.isInstance(value);
        }
    }

    private HighlightPredicate movieNodePredicate = new ClassNodePredicate(MovieNodeInfoNode.class);

    private HighlightPredicate headerPredicate = new ClassNodePredicate(AttributeHeaderNode.class);

    private HighlightPredicate infoPredicate = new ClassNodePredicate(InfoNode.class);

    private HighlightPredicate exifPredicate = new ClassNodePredicate(ExifNode.class);

    private HighlightPredicate changedPredicate = new PropHighlightPredicate() {
        @Override
        public boolean isHighlighted(PropertyNode value, Component renderer, ComponentAdapter adapter) {
            return (value instanceof EditableNode &&
                    ((EditableNode) value).isEdited());
        }
    };


    private AttributedEntity attributedEntity;
    private MovieNode movieNode;
    private List<MovieNode> currSelectedMovies;

    public PropertiesPanel2(MyjaphooController controller) {
        this.controller = controller;

        model = MappedTreeTableModel.configure(jXTreeTable1,
                new PropertyNode(null),
                PropertyNode.class, "parent", "children",
                ColDescr.col("node", "Name").setWidth(100),
                ColDescr.col("value", "Value", "editable").setCellEditor(new PropertyCellEditor()).setWidth(180));

        treeSupport = new JXTreeTableSupport<>(jXTreeTable1, model);
        // set this class as config by convention object to use action handler direct defined as methods here
        treeSupport.setConfiguration(this);
        jXTreeTable1.setEditable(true);
        // jXTreeTable1.getColumn(1).setCellEditor(new PropertyCellEditor());

        jXTreeTable1.setDefaultEditor(String.class, new PropertyCellEditor());
        jXTreeTable1.setDefaultRenderer(String.class, new MarkOccurencesRenderer(controller));
        jXTreeTable1.setTreeCellRenderer(new PropertyCellRenderer());

        jXTreeTable1.setExpandsSelectedPaths(true);
        jXTreeTable1.addHighlighter(new ColorHighlighter(movieNodePredicate, COLOR_NODE_HEADER_NODE, Color.white));
        jXTreeTable1.addHighlighter(new ColorHighlighter(headerPredicate, COLOR_HEADER_NODE, Color.white));
        jXTreeTable1.addHighlighter(new ColorHighlighter(infoPredicate, COLOR_INFO_NODE, jXTreeTable1.getForeground()));

        jXTreeTable1.addHighlighter(new ColorHighlighter(changedPredicate, COLOR_CHANGED_NODE, jXTreeTable1.getForeground()));
        jXTreeTable1.addHighlighter(new ColorHighlighter(exifPredicate, COLOR_EXIF_NODE, jXTreeTable1.getForeground()));
        initComponents();
    }

    public void currSelectionChanged(AttributedEntity attributedEntity, MovieNode movieNode) {
        currSelectionChanged(null, attributedEntity, movieNode);
    }

    public void currSelectionChanged(List<MovieNode> currSelectedMovies, AttributedEntity attributedEntity, MovieNode movieNode) {
        this.attributedEntity = attributedEntity;
        this.movieNode = movieNode;
        this.currSelectedMovies = currSelectedMovies;

        rebuildTree(currSelectedMovies, attributedEntity, movieNode);
    }

    private void rebuildTree(List<MovieNode> currSelectedMovies, AttributedEntity attributedEntity, MovieNode movieNode) {
        PropertyNode newRoot = new PropertyNode(null);
        addSelectionInfo(newRoot, currSelectedMovies);
        if (movieNode != null) {
            newRoot.getChildren().add(new MovieNodeInfoNode(newRoot, movieNode));

        }

        if (attributedEntity != null) {
            List<AttributedEntity> entityList = findAllConnectedAttributes(attributedEntity);
            for (AttributedEntity entity : entityList) {
                buildAttributes(newRoot, entity);
            }
        }

        model.setRoot(newRoot);
        expandRelevantNodes(newRoot);
    }

    private void addSelectionInfo(PropertyNode newRoot, List<MovieNode> currSelectedMovies) {
        if (currSelectedMovies != null) {
            long size = 0;
            for (MovieNode node : currSelectedMovies) {
                if (node != null && node.getMovieEntry() != null) {
                    size += node.getMovieEntry().getFileLength();
                }
            }
            InfoNode selNr = new InfoNode(newRoot, "Files", Integer.toString(currSelectedMovies.size()));
            newRoot.getChildren().add(selNr);
            InfoNode selSize = new InfoNode(newRoot, "Size", Utils.humanReadableByteCount(size));
            newRoot.getChildren().add(selSize);
        }
    }

    private void expandRelevantNodes(PropertyNode newRoot) {
        for (PropertyNode node : newRoot.getChildren()) {
            if (node.isShouldBeExpanded()) {
                jXTreeTable1.expandPath(new TreePath(model.getPathToRoot(node)));
            }
            expandRelevantNodes(node);
        }
    }

    private void buildAttributes(PropertyNode root, AttributedEntity attributedEntity) {
        PropertyNode entityRoot = new AttributeHeaderNode(root, attributedEntity);
        root.getChildren().add(entityRoot);
    }

    private List<AttributedEntity> findAllConnectedAttributes(AttributedEntity attributedEntity) {
        LinkedHashSet<AttributedEntity> set = new LinkedHashSet<>();
        set.add(attributedEntity);
        addReferencedAttributes(set, attributedEntity);
        return new ArrayList<>(set);
    }

    private void addReferencedAttributes(Collection<AttributedEntity> list, AttributedEntity attributedEntity) {
        for (AttributedEntity ref : attributedEntity.getReferences()) {
            list.add(ref);
            addReferencedAttributes(list, ref);
        }
    }


    @ToolbarAction(name = "Save", order = 5, contextRelevant = false)
    public void saveAction() {
        PropertyNode root = (PropertyNode) model.getRoot();
        for (PropertyNode node : root.getChildren()) {
            if (node instanceof AttributeHeaderNode) {
                save((AttributeHeaderNode) node);
            }
        }
    }

    @ToolbarAction(name = "Undo", order = 6, contextRelevant = false)
    public void undoAction() {
        // simply reload the tree:
        if (JOptionPane.showConfirmDialog(controller.getView().getFrame(), "Undo Changes?", "Undo editing of Attributes?", JOptionPane.YES_NO_OPTION) == 0) {
            rebuildTree(currSelectedMovies, attributedEntity, movieNode);
        }
    }

    @ContextMenuAction(name = "Open View filtered by Item")
    public void openNewView(PropertyNode attr) {
        String filterExpression = attr.getFilterExpression();
        if (filterExpression != null) {
            MyjaphooApp.getApplication().startNewView(filterExpression);
        }
    }

    @ToolbarAction(name = "New", enableExpr = "config.findHeaderNode(selObj) != null")
    @ContextMenuAction(name = "new Attribute", enableExpr = "config.findHeaderNode(selObj) != null")
    public void newButtonAction(PropertyNode attr) {
        AttributeHeaderNode headerNode = findHeaderNode(attr);

        if (headerNode != null) {
            String name = JOptionPane.showInputDialog(controller.getView().getFrame(),
                    "Choose Attribute Name", "New Attribute", JOptionPane.QUESTION_MESSAGE);
            if (name != null) {
                addAttribute(headerNode, name);
            }
        }
    }

    private AttributeHeaderNode findHeaderNode(PropertyNode attr) {
        if (attr instanceof AttributeNode) {
            return (AttributeHeaderNode) attr.getParent();
        } else if (attr instanceof AttributeHeaderNode) {
            return (AttributeHeaderNode) attr;
        }
        return null;
    }


    @ToolbarAction(name = "add Existing", enableExpr = "config.findHeaderNode(selObj) != null")
    @ContextMenuAction(name = "add Existing Attribute", enableExpr = "config.findHeaderNode(selObj) != null")
    public void newExistingButtonAction(PropertyNode attr) {
        AttributeHeaderNode headerNode = findHeaderNode(attr);

        if (headerNode != null) {
            List<String> allExisting = CachedHints.getHintsForAttributeKeys(headerNode.getAttributedEntity());
            List<String> allNewOnes = filterOutExistingOnesOfEntity(allExisting, headerNode.getAttributedEntity());
            Object[] valList = allNewOnes.toArray();

            String name = (String) JOptionPane.showInputDialog(controller.getView().getFrame(),
                    "Choose Attribute Name", "Add Existing Attribute", JOptionPane.QUESTION_MESSAGE, null, valList, null);
            if (name != null) {
                addAttribute(headerNode, name);
            }
        }
    }

    private void addAttribute(AttributeHeaderNode headerNode, String name) {
        AttributeNode newNode = new AttributeNode(headerNode, name, "");
        newNode.setEdited(true);
        headerNode.getChildren().add(newNode);
        model.nodeAdded(headerNode, headerNode.getChildren().indexOf(newNode), newNode);
    }

    private List<String> filterOutExistingOnesOfEntity(List<String> allExisting, AttributedEntity attributedEntity) {
        ArrayList<String> copy = new ArrayList<>(allExisting);
        copy.removeAll(attributedEntity.getAttributes().keySet());
        return copy;
    }

    @ToolbarAction(name = "Del", enableExpr = "selObj instanceof org.myjaphoo.gui.movieprops.AttributeNode")
    @ContextMenuAction(name = "delete Attribute", enableExpr = "selObj instanceof org.myjaphoo.gui.movieprops.AttributeNode")
    public void deleteButtonAction(PropertyNode attr) {
        if (attr instanceof AttributeNode) {
            AttributeHeaderNode parent = (AttributeHeaderNode) attr.getParent();
            parent.setAttributesHaveBeenDeleted(true);
            int index = parent.getChildren().indexOf(attr);
            parent.getChildren().remove(attr);
            model.nodeRemoved(parent, index, attr);
        }
    }


    private void save(AttributeHeaderNode node) {
        if (node.needsSaving()) {
            node.save();
        }

    }

    private void initComponents() {
        setLayout(new BorderLayout());
        add(treeSupport.createTreeWithToolbar(), BorderLayout.CENTER);
    }
}
