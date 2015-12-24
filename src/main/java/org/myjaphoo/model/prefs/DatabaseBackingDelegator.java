/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.prefs;

import org.mlsoft.common.prefs.model.editors.*;
import org.myjaphoo.model.db.PrefsBoolVal;
import org.myjaphoo.model.db.PrefsIntVal;
import org.myjaphoo.model.db.PrefsStringVal;
import org.myjaphoo.model.logic.PreferencesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 *
 * @author mla
 */
public class DatabaseBackingDelegator extends BackingDelegator {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseBackingDelegator.class);
    PrefsRootDatabase root;
    AbstractPrefVal abstractPrefVal;

    public DatabaseBackingDelegator(PrefsRootDatabase root, AbstractPrefVal abstractPrefVal, Object defaultVal) {
        super(defaultVal);
        this.root = root;
        this.abstractPrefVal = abstractPrefVal;
    }

    public void setObjVal(Object newVal) {
        super.setObjVal(newVal);
        setPrefsObjVal(newVal);
    }

    public Object getObjVal() {
        PreferencesDao dao = new PreferencesDao();
        if (abstractPrefVal instanceof BooleanVal) {
            PrefsBoolVal prefsBoolVal = dao.find(abstractPrefVal.getName(), PrefsBoolVal.class);
            Object value = prefsBoolVal != null ? prefsBoolVal.getBoolValue() : null;
            return value;
        } else if (abstractPrefVal instanceof StringVal) {
            PrefsStringVal prefsStringVal = dao.find(abstractPrefVal.getName(), PrefsStringVal.class);
            Object value = prefsStringVal != null ? prefsStringVal.getStringValue() : null;
            return value;
        } else if (abstractPrefVal instanceof IntegerVal) {
            PrefsIntVal prefsIntVal = dao.find(abstractPrefVal.getName(), PrefsIntVal.class);
            Object value = prefsIntVal != null ? prefsIntVal.getIntValue() : null;
            return value;
        } else if (abstractPrefVal instanceof ColorVal) {
            PrefsStringVal prefsStringVal = dao.find(abstractPrefVal.getName(), PrefsStringVal.class);
            Object value = prefsStringVal != null ? prefsStringVal.getStringValue() : null;
            if (value != null) {
                return Color.decode((String) value);
            } else {
                return null;
            }

        } else {
            throw new RuntimeException("nicht unterstützter Typ bei PrefsBackingDelegator");
        }
    }

    private Object defaultOrValue(Object value, Object defaultValue) {
        return value != null ? value : defaultValue;
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
        PreferencesDao dao = new PreferencesDao();
        try{
        if (newVal == null) {
            dao.remove(abstractPrefVal.getName());
        } else if (newVal instanceof Boolean) {
            PrefsBoolVal value = new PrefsBoolVal();
            value.setId(abstractPrefVal.getName());
            value.setBoolValue(((Boolean) newVal).booleanValue());
            dao.edit(value);
        } else if (newVal instanceof String) {
            PrefsStringVal value = new PrefsStringVal();
            value.setId(abstractPrefVal.getName());
            value.setStringValue((String) newVal);
            dao.edit(value);
        } else if (newVal instanceof Integer) {
            PrefsIntVal value = new PrefsIntVal();
            value.setId(abstractPrefVal.getName());
            value.setIntValue(((Integer) newVal).intValue());
            dao.edit(value);
        } else if (newVal instanceof Color) {
            PrefsStringVal value = new PrefsStringVal();
            value.setId(abstractPrefVal.getName());
            value.setStringValue(toString((Color) newVal));
            dao.edit(value);
        } else {
            throw new RuntimeException("nicht unterstützter Typ bei PrefsBackingDelegator:" + newVal.getClass().getName());
        }
        } catch(Exception e) {
              logger.error("saving failed!", e);
        }

    }

    private String toString(Color newVal) {
        return "#" + Integer.toHexString(newVal.getRed()) + Integer.toHexString(newVal.getGreen()) + Integer.toHexString(newVal.getBlue());
    }
}