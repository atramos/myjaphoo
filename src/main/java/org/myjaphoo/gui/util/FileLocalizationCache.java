/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import java.util.HashMap;
import org.myjaphoo.model.logic.FileSubstitutionImpl;

/**
 * Helfer klasse für die GUI:
 * Anzeige, ob eine Datei lokalisiert ist.
 * Das dauert rel. lange, v.a. wenn sehr viele Dateien NICHT lokalisierbar sind.
 * Abhilfe soll diese Klasse schaffen:
 * - cachen der ergebnise
 * - wenn ein parent nicht lokaliserbar ist, dann ist auch ein child nicht lokalisierbar.
 *   das sollte ausgenutzt werden.
 *
 * - invalidieren der Ergebnisse nach einer gewissen zeit.
 * @author mla
 */
public class FileLocalizationCache {

    private FileSubstitutionImpl fs = new FileSubstitutionImpl();
    private HashMap<String, Boolean> cache = new HashMap<String, Boolean>(400000);
    private static FileLocalizationCache instance = null;

    private FileLocalizationCache() {
    }

    public static FileLocalizationCache getInstance() {
        if (instance == null) {
            instance = new FileLocalizationCache();
        }
        return instance;
    }

    public boolean isLocateable(String path) {
        Boolean cacheHit = cache.get(path);
        if (cacheHit != null) {
            return cacheHit.booleanValue();
        } else {
            boolean located = fs.locateFileOnDrive(path) != null;
            cache.put(path, Boolean.valueOf(located));
            return located;
        }
    }
}
