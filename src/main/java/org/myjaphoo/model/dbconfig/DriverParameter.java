/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * a driver parameter value.
 * @author mla
 */
@XmlType(name="driverparam")
@XmlAccessorType(XmlAccessType.FIELD)
public class DriverParameter {

    @XmlElement(name="name")
    private DatabaseDriverUrlParameter parameter;
    
    private String value;

    public DriverParameter(DatabaseDriverUrlParameter parameter, String value) {
        this.parameter = parameter;
        this.value = value;
    }

    public DriverParameter() {
    }

    /**
     * @return the parameter
     */
    public DatabaseDriverUrlParameter getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(DatabaseDriverUrlParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
