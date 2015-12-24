/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.messages;

import javax.swing.Icon;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.myjaphoo.gui.icons.Icons;

/**
 *
 * @author lang
 */
public class MessageRenderer extends DefaultTreeRenderer {

    public MessageRenderer() {
        super(createIconValue(), createStringValue());
    }

    public static StringValue createStringValue() {
        StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                if (value instanceof MessageTreeNode) {
                    MessageTreeNode node = (MessageTreeNode) value;
                    return "<html>" + node.getMessage() + "</html>"; //NOI18N
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
                if (value instanceof MessageTreeNode) {
                    MessageTreeNode node = (MessageTreeNode) value;
                    if (node.signalsError()) {
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
