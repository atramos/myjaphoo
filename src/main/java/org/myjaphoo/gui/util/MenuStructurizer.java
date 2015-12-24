/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author lang
 */
public abstract class MenuStructurizer {

    private ArrayList<Entry> entryList = new ArrayList<Entry>();

    private static final Comparator<Entry> COMPARATOR = new Comparator<Entry>() {

        @Override
        public int compare(Entry o1, Entry o2) {
            return o1.orderCriteria.compareTo(o2.orderCriteria);
        }
    };
    private Icon folderIcon;

    public void setFolderIcon(Icon folderIcon) {
        this.folderIcon = folderIcon;
    }

    protected JMenu createMenu(String txt) {
        JMenu m = new JMenu(txt);
        if (folderIcon != null) {
            m.setIcon(folderIcon);
        }
        return m;
    }

    protected static class Entry {

        JMenuItem item;
        String orderCriteria;
        String path;
    }

    public void add(JMenuItem item, String name, String path) {
        Entry entry = new Entry();
        entry.item = item;
        entry.orderCriteria = name;
        entry.path = StringUtils.strip(path, "/");
        entryList.add(entry);
    }

    public void add(Action action, String name, String path) {
        Entry entry = new Entry();
        entry.item = new JMenuItem(action);
        entry.orderCriteria = name;
        entry.path = path;
        entryList.add(entry);
    }

    public int entrySize() {
        return entryList.size();
    }

    protected ArrayList<Entry> getEntryList() {
        Collections.sort(entryList, COMPARATOR);
        return entryList;
    }

    abstract public void structurize(JComponent parent, int groupSize);
}
