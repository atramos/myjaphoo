/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

/**
 *
 * @author mla
 */
@XmlRootElement(name = "DatabaseConfigurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class DatabaseConfigurations {

    @XmlElement(name = "DatabaseConfiguration", required = false)
    private List<DatabaseConfiguration> databaseConfigurations = new ArrayList<DatabaseConfiguration>();

    /**
     * @return the databaseConfigurations
     */
    public List<DatabaseConfiguration> getDatabaseConfigurations() {
        if (databaseConfigurations == null) {
            databaseConfigurations = new ArrayList<DatabaseConfiguration>();
        }
        return databaseConfigurations;
    }

    /**
     * @param databaseConfigurations the databaseConfigurations to set
     */
    public void setDatabaseConfigurations(List<DatabaseConfiguration> databaseConfigurations) {
        this.databaseConfigurations = databaseConfigurations;
    }

    /**
     * tries to find a database configuration by name.
     * returns null, if no config with the given name exists.
     */
    public DatabaseConfiguration findByName(String configName) {
        // linear search, since there might not too much database configs, hopefully:
        for (DatabaseConfiguration dbconfig : getDatabaseConfigurations()) {
            if (configName.equals(dbconfig.getName())) {
                return dbconfig;
            }
        }
        return null;
    }
}
