package org.mlsoft.common.prefs.model.editors;

import org.mlsoft.common.prefs.model.AbstractMetadata;
import org.mlsoft.common.prefs.model.edit.EditableVal;
import org.mlsoft.eventbus.GlobalBus;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */
public abstract class AbstractPrefVal extends AbstractPrefItem implements EditableVal {

    private BackingDelegator delegator;

    public AbstractPrefVal(AbstractMetadata parent, String name, String guiName, String description, Object defaultVal) {
        super(parent, name, guiName, description);


        delegator = getRoot().createBackingDelegator(this, defaultVal);

    }

    @Override
    public Object getDefaultVal() {
        return delegator.getDefaultVal();
    }

    public void setObjVal(Object newVal) {
        delegator.setObjVal(newVal);
        GlobalBus.bus.post(this);
    }

    public Object getObjVal() {
        Object o = delegator.getObjVal();
        if (o == null) {
            return getDefaultVal();
        } else {
            return o;
        }
    }

    public boolean isValueDefined() {
        return delegator.getObjVal() != null;
    }

    public void commit() {
        delegator.commit();
    }

    public void rollback() {
        delegator.rollback();
    }
}