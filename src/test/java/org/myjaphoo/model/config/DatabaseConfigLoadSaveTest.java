/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.config;

import org.myjaphoo.model.config.DatabaseConfigLoadSave;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseConfigurations;
import org.myjaphoo.model.dbconfig.DatabaseDriver;
import org.myjaphoo.model.util.UserDirectory;

/**
 *
 * @author mla
 */
public class DatabaseConfigLoadSaveTest extends TestCase {
    
    
    public void testSaving() throws JAXBException, FileNotFoundException, IOException {
        
        System.out.println("userdir = " + UserDirectory.getDirectory());
        //System.setProperty("wankman.model.util.UserDirectory", ".");
        
        DatabaseConfigurations configs = new DatabaseConfigurations();
        DatabaseConfiguration dbconfig = new DatabaseConfiguration("mytest", DatabaseDriver.DERBY_EMBEDDED);
        dbconfig.setDatabasename("blabla");
        dbconfig.setFilename("blublubu");
        dbconfig.setServer("serverblblablab");
        dbconfig.setPort(1234);
        dbconfig.setName("testdb connection");
   
        configs.getDatabaseConfigurations().add(dbconfig);
        
        DatabaseConfigLoadSave.save(configs);
    }
}
