/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.impactors;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.myjaphoo.LoggingConfiguration;
import org.myjaphoo.model.logic.imp.TextDelegator;
import org.myjaphoo.model.logic.impactors.ImportMsg;
import org.myjaphoo.model.logic.impactors.ImportQueue;

/**
 *
 * @author mla
 */
public class ActorsImportTest extends TestCase {

    public static final int NUMFILES = 10;

    public void testImport() throws InterruptedException {
        LoggingConfiguration.configurate();
        
        ImportQueue impQueue = ImportQueue.createImportQueue(new TextDelegator());
        Collection<File> testFiles = new ArrayList<File>();
        for (int i = 0; i < NUMFILES; i++) {
            testFiles.add(new File("testtxtfile.txt"));
        }
        impQueue.send(new ImportMsg.ImportFilesMsg(testFiles));

        impQueue.waitTillAllFinished();

        assertEquals(NUMFILES, impQueue.getResults().size());
    }
}
