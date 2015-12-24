/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.prefs.model;

import org.mlsoft.common.prefs.model.editors.*;

import java.awt.*;

/**
 * @author mla
 */
public class CommonsConfigurationBackingDelegator extends BackingDelegator {

    PrefsRootCommonsConfiguration root;
    AbstractPrefVal abstractPrefVal;

    public CommonsConfigurationBackingDelegator(PrefsRootCommonsConfiguration root, AbstractPrefVal abstractPrefVal, Object defaultVal) {
        super(defaultVal);
        this.root = root;
        this.abstractPrefVal = abstractPrefVal;
    }

    public void setObjVal(Object newVal) {
        super.setObjVal(newVal);
        setPrefsObjVal(newVal);
    }

    public Object getObjVal() {
        try {
            if (abstractPrefVal instanceof BooleanVal) {
                return root.getConfig().getBoolean(abstractPrefVal.getName());
            } else if (abstractPrefVal instanceof StringVal) {
                return root.getConfig().getString(abstractPrefVal.getName());
            } else if (abstractPrefVal instanceof IntegerVal) {
                return new Integer(root.getConfig().getInt(abstractPrefVal.getName()));
            } else if (abstractPrefVal instanceof ColorVal) {
                String sval = root.getConfig().getString(abstractPrefVal.getName());
                if (sval != null) {
                    return Color.decode(sval);
                } else {
                    return null;
                }
            } else {
                throw new RuntimeException("nicht unterstützter Typ bei PrefsBackingDelegator");
            }
        } catch (java.util.NoSuchElementException nsee) {
            // there is no such element currently. return null;
            return null;
        }
    }

    public void commit() {
        super.commit();
    }

    public void rollback() {
        Object rollbackVal = getRollbackVal();
        if (rollbackVal != null) {
            setPrefsObjVal(rollbackVal);
        }
        super.rollback();

    }

    private void setPrefsObjVal(Object newVal) {
        if (newVal == null) {
            root.getConfig().setProperty(abstractPrefVal.getName(), null);
        } else if (newVal instanceof Boolean) {
            root.getConfig().setProperty(abstractPrefVal.getName(), ((Boolean) newVal).booleanValue());
        } else if (newVal instanceof String) {
            root.getConfig().setProperty(abstractPrefVal.getName(), (String) newVal);
        } else if (newVal instanceof Integer) {
            root.getConfig().setProperty(abstractPrefVal.getName(), ((Integer) newVal).intValue());
        } else if (newVal instanceof Color) {
            root.getConfig().setProperty(abstractPrefVal.getName(), toString((Color) newVal));
        } else {
            throw new RuntimeException("nicht unterstützter Typ bei PrefsBackingDelegator:" + newVal.getClass().getName());
        }
        root.save();
    }

    private String toString(Color newVal) {
        return "#" + Integer.toHexString(newVal.getRed()) + Integer.toHexString(newVal.getGreen()) + Integer.toHexString(newVal.getBlue());
    }
}