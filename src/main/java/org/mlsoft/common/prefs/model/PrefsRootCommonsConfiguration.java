/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.mlsoft.common.prefs.model;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.mlsoft.common.prefs.model.editors.AbstractPrefVal;
import org.mlsoft.common.prefs.model.editors.BackingDelegator;
import org.mlsoft.common.prefs.model.editors.EditorRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * <p>Prefs root, using apache commons composite configuration.
 * This configuration class supports properties spreaded over 
 * two <code>PropertiesConfiguration</code> files. A property from the first 
 * configuration has priority over the value of a property by the same name 
 * located in a lower rank configuration.</p>
 * 
 * <p>The change to a property take precedence over all other properties, by
 * the same name, in the combined configuration. When the configuration is
 * saved, properties modified and others properties loaded from the first
 * <code>PropertiesConfiguration</code> file are written back to the 
 * user properties file.</p>
 * 
 * <p>Typically the first file, the user properties file, is in a directory 
 * located in the user file space; he has write access to the file. 
 * An application properties file, the second file, is located in an 
 * application controlled file space; the user has no write access to the 
 * application area. Both properties files should have the same 
 * base name to facilitate working with them.</p>
 * 
 * <p>The user properties file enables him to adapt application 
 * characteristics by changing some properties values; so these properties
 * need to have the highest priority. The application properties file sets
 * the base working characteristics of the application. An example of this
 * situation is when the user does not have it's own properties set such as 
 * in a new installation.</p>
 *
 * @author mla
 */
public class PrefsRootCommonsConfiguration extends EditorRoot {

    private CompositeConfiguration config = null;
    private final static Logger logger = LoggerFactory.getLogger(PrefsRootCommonsConfiguration.class);

    /**
     * Constructor using the file name passed in parameter to read 
     * user's properties. 
     * 
     * @param userPropFileName is the file name of the user supplied
     * property file.
     */
    public PrefsRootCommonsConfiguration(String userPropFileName) {
        this(userPropFileName, null);
    }

    /**
     * Constructor using the files names passed in parameter to read 
     * user's and application properties.
     * 
     * @param userPropFileName is the file name of the user supplied
     * property file.
     * @param appPropFileName is the file name of the application
     * supplied property file.
     */
    public PrefsRootCommonsConfiguration(String userPropFileName, String appPropFileName) {
        // First create a property configuration made by using the user 
        // properties file name, then create the composite configuration.
        // The composite configuration uses eventually that configuration 
        // for saving back modifications
        config = new CompositeConfiguration(openPropertiesConfiguration(userPropFileName));
        // Add configurations. The first configuration added is the same as 
        // the one in the previous constructor call, but it's a different 
        // instance. Adding that first property file another time ensures 
        // it's properties being prioritised over the properties of the 
        // others properties configurations.
        config.addConfiguration(openPropertiesConfiguration(userPropFileName));
        config.addConfiguration(openPropertiesConfiguration(appPropFileName));
        // Other configurations can be added like this:
        // logger.info("adding system properties");
        // config.addConfiguration(new SystemConfiguration());
    }

    public void save() {
        try {
            ((PropertiesConfiguration) config.getInMemoryConfiguration()).save();
        } catch (ConfigurationException ex) {
            throw new RuntimeException("could not save preferences!", ex);
        }

    }

    public String getDisplayClassName() {
        return "PrefsRoot";
    }

    public String info() {
        return "";
    }

    public BackingDelegator createBackingDelegator(AbstractPrefVal abstractPrefVal,
            Object defaultVal) {
        return new CommonsConfigurationBackingDelegator(this, abstractPrefVal, defaultVal);
    }

    /**
     * @return the config
     */
    public Configuration getConfig() {
        return config;
    }
    
    /**
     * Opens the PropertiesConfiguration identified by the file
     * name. The content of the property file is loaded in the configuration.
     * If the file name does not exist or is null, an empty configuration is 
     * created.
     * 
     * @param fileName the property file name to open and load.
     * @return the loaded configuration
     */
    private PropertiesConfiguration openPropertiesConfiguration(String fileName){
        PropertiesConfiguration aConfig = new PropertiesConfiguration();
        
        if(fileName != null) {
            File aFile = new File(fileName);
            String fileNamePath = aFile.getAbsolutePath();
            aConfig.setFile(aFile);
            try {
                aConfig.load();
                logger.info("Properties file '" + fileNamePath + "' exist");
            }
            catch (ConfigurationException ex) {
                logger.info("Properties file '" + fileNamePath + "' does not exist");
            }
        }
        return aConfig;
    }
}
