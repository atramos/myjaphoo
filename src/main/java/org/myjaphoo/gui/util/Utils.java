package org.myjaphoo.gui.util;

import javax.swing.*;

/**
 * Utils
 * @author lang
 * @version $Id$
 */
public class Utils {

    /**
     * converts byte size into a human readable string.
     * Alternative to commons io´s FileUtils.byteCountToDisplaySize which rounds the values
     * found in http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
     * @param bytes
     * @param si
     * @return
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, false);
    }

    /**
     * Registers a key action on this component and all its inner components.
     * @param panel
     * @param keystroke
     * @param actionName
     * @param action
     */
    public static void registerKeyAction(JPanel panel, KeyStroke keystroke, String actionName, Action action) {
        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keystroke, actionName);
        panel.getActionMap().put(actionName, action);
    }
}
