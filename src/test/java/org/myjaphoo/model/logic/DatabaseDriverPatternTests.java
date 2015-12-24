/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import junit.framework.TestCase;
import org.myjaphoo.model.dbconfig.DatabaseDriverUrlParameter;

/**
 *
 * @author mla
 */
public class DatabaseDriverPatternTests extends TestCase {

    public void testDatabaseDriverUrlParameterMatching() {

        assertTrue(DatabaseDriverUrlParameter.DATABASENAME.hasParameter("jdbc:derby://<server>:<port1527>/<databaseName>;create=<createDB>"));

        assertTrue(DatabaseDriverUrlParameter.CREATEDB.hasParameter("jdbc:derby://<server>:<port1527>/<databaseName>;create=<createDB>"));

        assertTrue(DatabaseDriverUrlParameter.FILENAME.hasParameter("jdbc:h2:<fileName>"));
        assertTrue(DatabaseDriverUrlParameter.PORT.hasParameter("jdbc:derby://<server>:<port(1547)>/<databaseName>;create=<createDB>"));
        assertTrue(DatabaseDriverUrlParameter.SERVER.hasParameter("jdbc:derby://<server>:<port1527>/<databaseName>;create=<createDB>"));

    }

    public void testDatabaseDriverUrlParameterReplacement() {

        String result = DatabaseDriverUrlParameter.DATABASENAME.replaceParameter("jdbc:derby://<server>:<port1527>/<databaseName>;create=<createDB>", "mydb");

        assertEquals("jdbc:derby://<server>:<port1527>/mydb;create=<createDB>", result);
        
        result = DatabaseDriverUrlParameter.SERVER.replaceParameter("jdbc:derby://<server>:<port1527>/<databaseName>;create=<createDB>", "localhost");
        
        assertEquals("jdbc:derby://localhost:<port1527>/<databaseName>;create=<createDB>", result);
        
        result = DatabaseDriverUrlParameter.PORT.replaceParameter("jdbc:derby://<server>:<port(1527)>/<databaseName>;create=<createDB>", "1234");
        
        assertEquals("jdbc:derby://<server>:1234/<databaseName>;create=<createDB>", result);
        
        
        String defaultPort = DatabaseDriverUrlParameter.PORT.extractDefaultParameterFromPattern("jdbc:derby://<server>:<port(1527)>/<databaseName>;create=<createDB>");
        
        assertEquals("1527", defaultPort);
        
    }
}
