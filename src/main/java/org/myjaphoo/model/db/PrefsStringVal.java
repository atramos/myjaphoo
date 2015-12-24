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
@DiscriminatorValue(value = "STRING")
public class PrefsStringVal extends Prefs {
    private String stringValue;

    public PrefsStringVal() {
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
