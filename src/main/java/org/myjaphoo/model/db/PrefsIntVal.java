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
@DiscriminatorValue(value = "INTEGER")
public class PrefsIntVal extends Prefs {
    private Integer intValue;

    public PrefsIntVal() {
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }
}
