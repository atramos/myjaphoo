/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.prefs.model.editors;

import org.mlsoft.common.prefs.model.AbstractMetadata;

/**
 *
 * @author mla
 */
public class EnumSelectionVal<T extends Enum> extends StringSelectionVal {

    private Class clazz;

    public EnumSelectionVal(AbstractMetadata parent, String name, String guiName, String description, T defaultVal, Class<T> clazz) {
        super(parent, name, guiName, description, defaultVal.name(), createStringAuswahl(clazz));
        this.clazz = clazz;
    }

    private static <T extends Enum> String[] createStringAuswahl(Class<T> clazz) {
        T[] allConsts = clazz.getEnumConstants();
        String[] strConsts = new String[allConsts.length];

        for (int i = 0; i < allConsts.length; i++) {
            strConsts[i] = allConsts[i].name();
        }
        return strConsts;
    }

    public T getSelectedEnum() {
        String name = getVal();
        return (T) Enum.valueOf(clazz, name);
    }
}
