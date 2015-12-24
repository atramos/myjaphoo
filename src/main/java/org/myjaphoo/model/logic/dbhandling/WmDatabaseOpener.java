/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.dbhandling;

import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.config.DatabaseConfigLoadSave;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseConfigurations;
import org.myjaphoo.model.dbconfig.DatabaseDriver;
import org.myjaphoo.model.util.UserDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Determines the database configuration for the myjaphoo database and
 * returns a DBConnection object for usage to access the database.
 * @author mla
 */
public class WmDatabaseOpener {

    /** after a bad try to access user defined database connections, we try the 
     next time to load the default database. */
    private static boolean fallbacktoDefaultDatabase = false;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WmDatabaseOpener.class.getName());

    /** the database definition to use. This is the user selection from the user interface. if null, we use the default database. */
    public static String databaseDefinition;

    public static DBConnection openDatabase() {
        DatabaseConfiguration configuration = determineConfiguration();
        return new DBConnection(configuration);
    }

    public static String getDatabaseDefinition() {
        return databaseDefinition;
    }

    public static void setDatabaseDefinition(String databaseDefinition) {
        WmDatabaseOpener.databaseDefinition = databaseDefinition;
        WmDatabaseOpener.fallbacktoDefaultDatabase = false;
    }

    /**
     * determines the database configuration for the application.
     * This could be the standard (default) one, or a special configured one.
     * @return 
     */
    private static DatabaseConfiguration determineConfiguration() {
        if (AppConfig.PRF_DB_USE_OTHERCONFIGURATION.getVal() && !fallbacktoDefaultDatabase) {
            String configName = AppConfig.PRF_DB_CONFIGURATIONNAME.getVal();
            DatabaseConfiguration configuration = tryFindConfiguration(configName);
            return configuration;
        } else if (databaseDefinition != null && !fallbacktoDefaultDatabase) {
            DatabaseConfiguration configuration = tryFindConfiguration(databaseDefinition);
            return configuration;

        } else {
            return getDefaultConfiguration();
        }
    }

    private static DatabaseConfiguration tryFindConfiguration(String configName) {
        DatabaseConfigurations configs = DatabaseConfigLoadSave.load();
        DatabaseConfiguration configuration = configs.findByName(configName);
        if (configuration == null) {
            fallbacktoDefaultDatabase = true;
            throw new RuntimeException("can not find database configuration " + configName);
        }
        return configuration;
    }

    /**
     * Returns the default database configuration. Either a derby embedded
     * or a derby server configuration.
     */
    private static DatabaseConfiguration getDefaultConfiguration() {

        String canonDBFileName = configuredDatabaseFileName();

        DatabaseConfiguration configuration = new DatabaseConfiguration();
        configuration.setUsername("app");
        configuration.setPassword("app");
        configuration.setCreateDb(true);
        if (AppConfig.PRF_DB_USE_DERBY_EMBEDDEDMODE.getVal()) {
            String dbLogFile = UserDirectory.getDirectory() + "derby.log";
            System.setProperty("derby.stream.error.file", dbLogFile);
            LOGGER.info("setting embedded database log file " + dbLogFile);

            configuration.setDatabaseDriver(DatabaseDriver.DERBY_EMBEDDED);

            configuration.setFilename(canonDBFileName);
        } else {
            String host = AppConfig.PRF_DB_DERBY_HOSTNAME.getVal();
            String port = AppConfig.PRF_DB_DERBY_PORT.getVal();
            configuration.setDatabaseDriver(DatabaseDriver.DERBY_SERVER);
            configuration.setServer(host);
            configuration.setPort(Integer.parseInt(port));
            configuration.setDatabasename(canonDBFileName);
        }
        return configuration;
    }

    public static String configuredDatabaseFileName() {
        String dbfile = AppConfig.PRF_DB_FILE.getVal();
        String canonDBFileName = getCanonicalFileName(dbfile);
        return canonDBFileName;
    }

    private static String getCanonicalFileName(String relFileName) {
        File file = new File(relFileName);
        if (!file.exists()) {
            // the folder does not exists, may be, because the db is not yet created:
            return relFileName;
        }
        String canonPath;
        try {
            canonPath = file.getCanonicalPath();
        } catch (IOException ex) {
            LOGGER.error("error", ex);
            throw new RuntimeException("can not find derby db folder " + file.getAbsolutePath());
        }
        LOGGER.info("loading movie database " + canonPath);
        return canonPath;
    }
}
