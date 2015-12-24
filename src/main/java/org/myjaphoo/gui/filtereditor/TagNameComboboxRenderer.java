package org.myjaphoo.gui.filtereditor;

import org.myjaphoo.gui.thumbtable.thumbcache.TokenThumbCache;

import javax.swing.*;
import java.awt.*;

/**
 * TagNameComboboxRenderer
 *
 * @author mla
 * @version $Id$
 */
public class TagNameComboboxRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list,
                value, index, isSelected, cellHasFocus);
        String tagName = (String) value;
        label.setIcon(TokenThumbCache.getInstance().loadImageForToken(tagName, 22, null));
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        return label;
    }
}
