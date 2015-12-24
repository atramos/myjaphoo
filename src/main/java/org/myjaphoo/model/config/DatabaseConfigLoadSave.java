/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.config;

import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseConfigurations;
import org.myjaphoo.model.dbconfig.DatabaseDriver;
import org.myjaphoo.model.dbconfig.DriverParameter;
import org.myjaphoo.model.util.UserDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 *
 * @author lang
 */
public class DatabaseConfigLoadSave {

    private static Logger logger = LoggerFactory.getLogger(DatabaseConfigLoadSave.class);

    public static void save(DatabaseConfigurations configurations) {
        try {
            JAXBContext jaxb = JAXBContext.newInstance(DatabaseConfigurations.class, DatabaseConfiguration.class, DriverParameter.class, DatabaseDriver.class);
            File file = new File(UserDirectory.getDirectory() + "databases.xml");

            Marshaller m = jaxb.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(configurations, file);
        } catch (Exception e) {
            logger.error("error saving database configurations!", e);
            throw new RuntimeException("error saving database configurations!", e);
        }
    }

    public static DatabaseConfigurations load() {
        try {
            JAXBContext jaxb = JAXBContext.newInstance(DatabaseConfigurations.class, DatabaseConfiguration.class, DriverParameter.class, DatabaseDriver.class);
            Unmarshaller um = jaxb.createUnmarshaller();

            File file = new File(UserDirectory.getDirectory() + "databases.xml");
            DatabaseConfigurations dbconfigs = new DatabaseConfigurations();
            if (file.exists()) {
                dbconfigs = (DatabaseConfigurations) um.unmarshal(file);
            }
            return dbconfigs;
        } catch (Exception e) {
            logger.error("error loading database configurations!", e);
            throw new RuntimeException("error loading database configurations!", e);
        }
    }
}
