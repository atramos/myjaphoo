package org.myjaphoo.gui.movieprops;

import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for file nodes.
 */
public class PropertyCellRenderer extends DefaultTreeRenderer {

    private Color COLOR_HEADER_NODE = new Color(134, 157, 243);

    private Color COLOR_CHANGED_NODE = new Color(243, 230, 135);

    public PropertyCellRenderer() {
        super(createIconValue(), createStringValue());
    }

    public static StringValue createStringValue() {
        StringValue sv = new StringValue() {
            @Override
            public String getString(Object value) {
                return ((PropertyNode) value).getName();
            }
        };
        return sv;
    }

    public static IconValue createIconValue() {
        IconValue iv = new IconValue() {

            @Override
            public Icon getIcon(Object value) {

                PropertyNode node = (PropertyNode) value;
                return node.getIcon();

            }
        };
        return iv;

    }


    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        PropertyNode node  = (PropertyNode) value;
//        if (node instanceof AttributeHeaderNode) {
//            comp.setBackground(COLOR_HEADER_NODE);
//
//        }
//        if (node instanceof AttributeNode) {
//            if (((AttributeNode)node).isEdited()) {
//                comp.setBackground(COLOR_CHANGED_NODE);
//            }
//        }
        return comp;
    }
}
