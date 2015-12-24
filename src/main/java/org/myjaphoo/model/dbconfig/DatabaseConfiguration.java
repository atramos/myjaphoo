/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import org.myjaphoo.model.config.AppConfig;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Stores a definition of a database configuration.
 * @author mla
 */
@XmlType(name = "databaseConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class DatabaseConfiguration implements Cloneable {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "databaseDriver")
    private DatabaseDriver databaseDriver = DatabaseDriver.DERBY_EMBEDDED;
    private String server;
    private Integer port;
    private String databasename;
    private String filename;
    private String sid;
    private boolean createDb;
    private String username;
    private String password;
    private Long id = new Random().nextLong();

    public DatabaseConfiguration() {
    }

    public DatabaseConfiguration(String name, DatabaseDriver databaseDriver) {
        this.name = name;
        this.databaseDriver = databaseDriver;

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the databaseDriver
     */
    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    /**
     * @param databaseDriver the databaseDriver to set
     */
    public void setDatabaseDriver(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    public String getFilledConnectionUrl() {
        String urlTemplate = databaseDriver.getUrlFormat();
        for (DatabaseDriverUrlParameter param : databaseDriver.getListOfUrlParameter()) {
            urlTemplate = param.replaceParameter(urlTemplate, this);
        }
        return urlTemplate;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the databasename
     */
    public String getDatabasename() {
        return databasename;
    }

    /**
     * @param databasename the databasename to set
     */
    public void setDatabasename(String databasename) {
        this.databasename = databasename;
    }

    /**
     * @return the sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * @return the createDb
     */
    public boolean isCreateDb() {
        return createDb;
    }

    /**
     * @param createDb the createDb to set
     */
    public void setCreateDb(boolean createDb) {
        this.createDb = createDb;
    }

    /**
     * makes a shallow copy of this object.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return new RuntimeException("internal error!");
        }
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**  
     * creates a property map with the connection parameter for
     * usage with hibernate.
     */
    public Map createPropetyMap() {
        Map properties = new HashMap();
        properties.put("hibernate.connection.username", getUsername());
        properties.put("hibernate.connection.password", getPassword());
        properties.put("hibernate.connection.driver_class", getDatabaseDriver().getJdbcDriverClass());
        properties.put("hibernate.connection.url", getFilledConnectionUrl());
        properties.put("hibernate.dialect", getDatabaseDriver().getHibernateDialect());
        if (AppConfig.PRF_HIBERNATE_SECOND_LEVEL_CACHE.getVal()) {
            properties.put("hibernate.cache.use_second_level_cache", "true");
            properties.put("hibernate.generate_statistics", "true");
        }

        return properties;
    }

    /**
     * @return the id
     */
    public Long getId() {
        // backward compatiblity: first version havend had a id,
        // generate one in that case:
        if (id == null) {
            // use a pseudo random value as id. 
            // this should be collision free enough for that view objects.
            id = new Random().nextLong();
        }
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DatabaseConfiguration) {
            DatabaseConfiguration other = (DatabaseConfiguration) obj;
            return getId().equals(other.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
