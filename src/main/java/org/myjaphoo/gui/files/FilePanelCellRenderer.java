package org.myjaphoo.gui.files;

import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.model.logic.FileSubstitutionImpl;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

/**
 * Renderer for file nodes.
 */
public class FilePanelCellRenderer extends DefaultTreeRenderer {

    private Color IN_DB_MARKER_COLOR = new Color(227, 255, 163);

    public FilePanelCellRenderer() {
        super(createIconValue(), createStringValue());
    }

    public static StringValue createStringValue() {
        StringValue sv = new StringValue() {

            private FileSubstitutionImpl fs = new FileSubstitutionImpl();

            @Override
            public String getString(Object value) {
                return ((FileNode) value).getName();
            }
        };
        return sv;
    }

    public static IconValue createIconValue() {
        IconValue iv = new IconValue() {

            @Override
            public Icon getIcon(Object value) {
                FileNode node = (FileNode) value;
                return FileSystemView.getFileSystemView().getSystemIcon(node.getFile());

            }
        };
        return iv;

    }


    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        FileNode node  = (FileNode) value;
        if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal() && node.isInDb()) {
            comp.setBackground(IN_DB_MARKER_COLOR);
        };
        return comp;
    }
}
