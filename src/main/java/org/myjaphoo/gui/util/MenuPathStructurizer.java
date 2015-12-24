/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lang
 */
public class MenuPathStructurizer extends MenuStructurizer {

    private boolean withSeparator;

    private Map<String, JComponent> parentComponents = new HashMap<>();

    public MenuPathStructurizer(boolean withSeparator) {
        this.withSeparator = withSeparator;
    }

    @Override
    public void structurize(JComponent parent, int groupSize) {
        for (Entry entry : getEntryList()) {
            JComponent realparent = getParentComponent(parent, entry);
            realparent.add(entry.item);
        }
    }


    private JComponent getParentComponent(JComponent root, Entry entry) {
        if (StringUtils.isEmpty(entry.path)) {
            return root;
        }
        JComponent foundComponent = parentComponents.get(entry.path);
        if (foundComponent != null) {
            return foundComponent;
        }
        return createParentComponent(root, entry);
    }

    private JComponent createParentComponent(JComponent root, Entry entry) {
        // ran down the parts of the path and get/create/build up all components for this path:
        String[] pathNames = StringUtils.split(entry.path, "/");
        String partialPath = "";
        for (String pathname : pathNames) {
            if (partialPath.length() > 0) {
                partialPath += "/";
            }
            partialPath += pathname;
            JComponent comp = getOrCreateChildComponent(root, pathname, partialPath);
            root = comp;
        }
        return root;
    }

    private JComponent getOrCreateChildComponent(JComponent root, String pathname, String path) {
        JMenu menu = createMenu(pathname);
        parentComponents.put(path, menu);
        root.add(menu);
        return menu;
    }
}
