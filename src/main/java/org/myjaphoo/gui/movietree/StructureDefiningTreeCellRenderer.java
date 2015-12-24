/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.model.logic.FileSubstitutionImpl;

/**
 *
 * @author mla
 */
class StructureDefiningTreeCellRenderer extends DefaultTreeRenderer {

    public StructureDefiningTreeCellRenderer() {
        super(createIconValue(), createStringValue());
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        return comp;
    }

    public static StringValue createStringValue() {
        StringValue sv = new StringValue() {

            private FileSubstitutionImpl fs = new FileSubstitutionImpl();

            @Override
            public String getString(Object value) {
                if (value instanceof StructureDefiningPanel.DefNode) {
                    return createLabelTextForDefNode((StructureDefiningPanel.DefNode) value);
                } else {
                    return value.toString();
                }
            }

            private String createLabelTextForDefNode(StructureDefiningPanel.DefNode defNode) {
                Color color = Helper.getColorForDim(defNode.getDim());
                if (color != null) {
                    return "<html>" + Helper.wrapColored(color, defNode.toString()) + "</html>"; //NOI18N
                } else {
                    return defNode.toString();
                }
            }
        };
        return sv;
    }

    public static IconValue createIconValue() {
        IconValue iv = new IconValue() {

            @Override
            public Icon getIcon(Object value) {
                return null;
            }
        };
        return iv;

    }
}
