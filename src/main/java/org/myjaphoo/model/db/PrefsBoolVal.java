package org.myjaphoo.model.db;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: lang
 * Date: 01.04.13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@DiscriminatorValue(value = "BOOLEAN")
public class PrefsBoolVal extends Prefs {
    /***
     * boolean saved as int: some dbs and versions have problems with the hibernate mapping
     * here. e.g. derby when using an older db created with 10.5 and using under 10.7.
     * therefore we just use an int value internally.
     */
    private int boolValue;

    public PrefsBoolVal() {
    }

    public Boolean getBoolValue() {
        return boolValue == 1;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue ? 1 : 0;
    }
}
