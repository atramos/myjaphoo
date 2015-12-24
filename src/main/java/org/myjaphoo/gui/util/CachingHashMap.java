package org.myjaphoo.gui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple linked hashmap implementation using soft references for the values.
 * Used for caches.
 */
public class CachingHashMap<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(CachingHashMap.class);

    private int max;
    private LinkedHashMap<K, SoftReference<V>> cacheStructure;

    public CachingHashMap(final int max) {
        // access ordered linked map erzeugen.
        //super(max * 3, 0.75f, true);
        this.max = max;

        cacheStructure = new LinkedHashMap<K, SoftReference<V>>(max * 3, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                if (logger.isDebugEnabled() && size() > max) {
                    logger.debug("freeing eldest; "); //NOI18N
                }
                // bedeuted hierbei, dass das am wenigsten zugegriffene element gelöscht wird,
                // wenn wir den maximal wert überschreiten.
                return size() > max;
            }
        };
    }

    public V get(K key) {
        SoftReference<V> softRef = cacheStructure.get(key);
        if (softRef == null) {
            return null;
        }
        V v = softRef.get();
        if (v == null) {
            // wenns schon freigegeben wurde, dann löschen wir auch den eintrag:
            cacheStructure.remove(key);
        }
        return v;
    }

    public void put(K key, V img) {
        cacheStructure.put(key, new SoftReference<V>(img));
    }

    public void clear() {
        cacheStructure.clear();
    }

    public boolean containsKey(K key) {
        return cacheStructure.containsKey(key);
    }
}
