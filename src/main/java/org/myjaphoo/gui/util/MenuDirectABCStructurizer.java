/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mla
 */
public class MenuDirectABCStructurizer extends MenuStructurizer {

    private boolean withSeparator;

    public MenuDirectABCStructurizer(boolean withSeparator) {
        this.withSeparator = withSeparator;
    }

    @Override
    public void structurize(JComponent parent, int groupSize) {
        List<Entry> subList = new ArrayList<Entry>();
        char lastChar = 0;
        ArrayList<Entry> entryList = getEntryList();
        for (Entry entry : entryList) {
            if (lastChar == 0) {
                // init first:
                lastChar = entry.orderCriteria.toUpperCase().charAt(0);
                subList.add(entry);
            } else {
                char currentChar = entry.orderCriteria.toUpperCase().charAt(0);
                if (currentChar != lastChar) {
                    createSubGroup(parent, subList, lastChar);
                    subList.clear();
                    lastChar = currentChar;
                    subList.add(entry);
                } else {
                    subList.add(entry);
                }
            }
        }
        if (subList.size() > 0) {
            createSubGroup(parent, subList, lastChar);
        }

    }

    private void createSubGroup(JComponent parent, List<Entry> subList, char ch) {
        JMenu subMenu = createMenu(new String(new char[]{ch}));
        for (Entry entry : subList) {
            add(subMenu, entry.item);
        }
        parent.add(subMenu);
    }

    private void add(JComponent parent, JMenuItem child) {
        parent.add(child);
        if (withSeparator) {
            if (parent instanceof JMenu) {
                ((JMenu) parent).addSeparator();
            }
        }
    }
}
