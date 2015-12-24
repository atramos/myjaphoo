/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * All possible database driver currently supported by myjaphoo.
 * 
 * 
 * @author lang
 */
public enum DatabaseDriver {

    DERBY_EMBEDDED("org.apache.derby.jdbc.EmbeddedDriver", "org.hibernate.dialect.DerbyDialect", "jdbc:derby:<fileName>;create=<createDB>"),
    DERBY_SERVER("org.apache.derby.jdbc.ClientDriver", "org.hibernate.dialect.DerbyDialect", "jdbc:derby://<server>:<port(1527)>/<databaseName>;create=<createDB>"),
    H2_EMBEDDED("org.h2.Driver", "org.hibernate.dialect.H2Dialect", "jdbc:h2:<fileName>"),
    H2_SERVER("org.h2.Driver", "org.hibernate.dialect.H2Dialect", "jdbc:h2:tcp:<server>:<port9092>/<databaseName>"),
    POSTGRES("org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect", "jdbc:postgresql://<server>:<port(5432)>/<databaseName>"),
    MYSQL("com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect", "jdbc:mysql://<server>:<port(3306)>/<databaseName>"),
    ORACLETHIN("oracle.jdbc.OracleDriver", "org.hibernate.dialect.Oracle10gDialect", "jdbc:oracle:thin:@<server>:<port(1521)>/<sid>");
    private String jdbcDriverClass;
    private String hibernateDialect;
    private String urlFormat;

    private DatabaseDriver(String jdbcDriverClass, String hibernateDialect, String urlFormat) {
        this.jdbcDriverClass = jdbcDriverClass;
        this.hibernateDialect = hibernateDialect;
        this.urlFormat = urlFormat;
    }

    /**
     * @return the jdbcDriverClass
     */
    public String getJdbcDriverClass() {
        return jdbcDriverClass;
    }

    public String getDriverName() {
        return name();
    }

    /**
     * @return the hibernateDialect
     */
    public String getHibernateDialect() {
        return hibernateDialect;
    }

    /**
     * @return the urlFormat
     */
    public String getUrlFormat() {

        return urlFormat;
    }
    
    public List<DatabaseDriverUrlParameter> getListOfUrlParameter() {
        ArrayList<DatabaseDriverUrlParameter> result = new ArrayList<DatabaseDriverUrlParameter>();
        for (DatabaseDriverUrlParameter param: DatabaseDriverUrlParameter.values()) {
            if (param.hasParameter(urlFormat)) {
                result.add(param);
            }
        }
        return result;
    }
    
    
    /**
     * Checks, if the needed jdbc driver is available (within the class path).
     * @return true, if the driver is available
     */
    public boolean isDriverAvailable() {
        try {
            // Classloader.getResource("Thingy.class")
            Class.forName(jdbcDriverClass,  false, this.getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
