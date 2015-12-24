/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.metaToken;

import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.thumbtable.thumbcache.MetaTokenThumbCache;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.model.db.MetaToken;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author mla
 */
public class MetaTokenTreeCellRenderer extends DefaultTreeRenderer {

    private MetaTokenTree tokenTree;

    public MetaTokenTreeCellRenderer(MetaTokenTree tokenTree) {
        super(createIconValue(tokenTree), createStringValue(tokenTree));
        this.tokenTree = tokenTree;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        MetaToken token = getToken(value);


        return comp;
    }

    public static StringValue createStringValue(final MetaTokenTree tokenTree) {
        StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                return getToken(value).toString();
            }
        };
        return sv;
    }

    public static MetaToken getToken(Object value) {
        if (value instanceof WrappedNode) {
            return ((WrappedNode<MetaToken>) value).getRef();
        }
        throw new RuntimeException("Internal error"); //NOI18N
    }

    public static IconValue createIconValue(final MetaTokenTree tokenTree) {
        IconValue iv = new IconValue() {

            @Override
            public Icon getIcon(Object value) {

                if (tokenTree.isShowThumbs()) {
                    if (value instanceof MetaToken) {
                        MetaToken token = getToken(value);
                        return MetaTokenThumbCache.getInstance().loadImageForToken(token, tokenTree.getRowHeight(), null);
                    }
                }
                return Icons.IR_METATAG.icon;
            }
        };
        return iv;

    }
}
