/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mla
 */
public class MenuABCStructurizer extends MenuStructurizer {

    private boolean withSeparator;

    public MenuABCStructurizer(boolean withSeparator) {
        this.withSeparator = withSeparator;
    }

    @Override
    public void structurize(JComponent parent, int groupSize) {
        ArrayList<Entry> entryList = getEntryList();
        if (entryList.size() > groupSize) {
            int count = 0;
            List<Entry> subList = new ArrayList<Entry>();
            for (Entry entry : entryList) {
                subList.add(entry);
                count++;
                if (count >= groupSize) {
                    createSubGroup(parent, subList);
                    subList.clear();
                    count = 0;
                }
            }
            if (subList.size() > 0) {
                createSubGroup(parent, subList);
            }
        } else {
            for (Entry entry : entryList) {
                add(parent, entry.item);
            }
        }
    }

    private void createSubGroup(JComponent parent, List<Entry> subList) {
        String firstName = subList.get(0).orderCriteria;
        String lastName = subList.get(subList.size() - 1).orderCriteria;
        String first = StringUtils.upperCase(StringUtils.left(firstName, 3));
        String last = StringUtils.upperCase(StringUtils.left(lastName, 3));
        JMenu subMenu = createMenu("\'" + first + "\' - \'" + last + "\'");

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
