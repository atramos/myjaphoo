/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author mla
 */
public class StringPool {

    /** the pool to map identical strings to the same reference. */
    private static Map<String, WeakReference<String>> pool = new WeakHashMap<String, WeakReference<String>>();

    /**
     * Constructor.
     */
    private StringPool() {
    }

    /**
     * Returns a pooled string for the given string.
     * @param text the string to get a pooled reference for it.
     * @return the pooled reference
     */
    synchronized public static String pooledString(String text) {
        if (text == null) {
            return null;
        }
        WeakReference<String> reference = pool.get(text);
        if (reference != null) {
            String result = reference.get();
            // Another null check, since the GC may have kicked in between the
            // two lines above.
            if (result != null) {
                return result;
            }
        }
        // If we got here it is because the map doesn't have the key, add it.
        pool.put(text, new WeakReference<String>(text));
        return text;
    }
}
