/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.errors;

import javax.swing.Icon;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.util.WrappedNode;

/**
 *
 * @author lang
 */
public class ErrorRenderer extends DefaultTreeRenderer {

    public ErrorRenderer() {
        super(createIconValue(), createStringValue());
    }

    public static StringValue createStringValue() {
        StringValue sv = new StringValue() {
            @Override
            public String getString(Object value) {
                if (value instanceof WrappedNode) {
                    ErrorTreeNode node = (ErrorTreeNode) ((WrappedNode)value).getRef();
                    return  node.getMessage(); //NOI18N
                } else {
                    return value.toString();
                }
            }
        };
        return sv;
    }

    public static IconValue createIconValue() {
        IconValue iv = new IconValue() {
            @Override
            public Icon getIcon(Object value) {
                if (value instanceof WrappedNode) {
                    ErrorTreeNode node = (ErrorTreeNode) ((WrappedNode)value).getRef();
                    if (node.getLevel() == ErrorLevel.ERROR) {
                        return Icons.IR_ERROR.icon;
                    } else {
                        return Icons.IR_INFO.icon;
                    }
                } else {
                    return null;
                }
            }
        };
        return iv;

    }
}
