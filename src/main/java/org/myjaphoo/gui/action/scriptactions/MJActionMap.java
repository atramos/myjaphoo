package org.myjaphoo.gui.action.scriptactions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * MJActionMap
 *
 * @author lang
 * @version $Id$
 */
public class MJActionMap {

    private LinkedHashMap<String, ActionEntry> actionMap = new LinkedHashMap<>();


    public void put(String key, ActionEntry entry) {
        actionMap.put(key, entry);
    }

    public Set<Map.Entry<String, ActionEntry>> getEntries() {
        return actionMap.entrySet();
    }
}
