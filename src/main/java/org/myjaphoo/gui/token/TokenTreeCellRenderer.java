/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;

import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.thumbtable.thumbcache.TokenThumbCache;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.model.db.Token;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author mla
 */
public class TokenTreeCellRenderer extends DefaultTreeRenderer {

    private TokenTree tokenTree;

    public TokenTreeCellRenderer(TokenTree tokenTree) {
        super(createIconValue(tokenTree), createStringValue(tokenTree));
        this.tokenTree = tokenTree;

    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        Token token = getToken(value);
        Color color = Helper.getColorForTokenType(token.getTokentype());
        comp.setForeground(color);

        return comp;
    }

    public static StringValue createStringValue(final TokenTree tokenTree) {
        StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                return getToken(value).toString();
            }
        };
        return sv;
    }

    public static Token getToken(Object value) {
        if (value instanceof WrappedNode) {
            return ((WrappedNode<Token>) value).getRef();
        }
        throw new RuntimeException("interner fehler"); //NOI18N
    }

    public static IconValue createIconValue(final TokenTree tokenTree) {
        IconValue iv = new IconValue() {

            @Override
            public Icon getIcon(Object value) {

                if (tokenTree.isShowThumbs()) {
                    if (value instanceof WrappedNode) {
                        Token token = getToken(value);
                        return TokenThumbCache.getInstance().loadImageForToken(token, tokenTree.getRowHeight(), null);
                    }
                } 
                return Icons.IR_TAG.icon;
            }
        };
        return iv;

    }
}
