/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseDriver;
import junit.framework.TestCase;

/**
 *
 * @author mla
 */
public class DatabaseConfigurationTest extends TestCase {

    public void testParameterSubstitution() {
        DatabaseConfiguration dc = new DatabaseConfiguration("TEST", DatabaseDriver.DERBY_SERVER);
        dc.setDatabasename("c:\\temp\\bla/blubb");
        dc.setServer("myserver");
        dc.setPort(8888);
        dc.setCreateDb(true);
        assertEquals("jdbc:derby://myserver:8888/c:\\temp\\bla/blubb;create=true", dc.getFilledConnectionUrl());
    }

    public void testSubstDefaultParameter() {
        DatabaseConfiguration dc = new DatabaseConfiguration("TEST", DatabaseDriver.POSTGRES);
        dc.setDatabasename("dbname");
        dc.setServer("myserver");

        dc.setCreateDb(true);
        assertEquals("jdbc:postgresql://myserver:5432/dbname", dc.getFilledConnectionUrl());
    }
}
