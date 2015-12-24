/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.useCases;

import junit.framework.TestCase;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.events.MoviesAddedEvent;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.myjaphoo.model.logic.MovieImport;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.imp.PicDelegator;
import org.myjaphoo.model.logic.impactors.ImportMsg;
import org.myjaphoo.model.logic.impactors.ImportQueue;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author mla
 */
public class ImportInDbTest extends TestCase {

    public void testImport() throws InterruptedException {
        // reset mock of previous tests
        CacheManager.setCacheActor(null);

        String picDir = System.getProperty("test.pics");
        if (picDir == null) {
            // run within the IDE:
            picDir = "testcaserunressources/pics";
        }

        // simple test to check test configuration of database
        MyjaphooDB.singleInstance().getEmf();
        final PicDelegator picDelegator = new PicDelegator();
        MovieImport i = new MovieImport();
        List<File> files = i.scanFiles(picDir, picDelegator);

        ImportQueue queue = ImportQueue.createImportQueue(picDelegator);
        queue.send(new ImportMsg.ImportFilesMsg(files));

        queue.waitTillAllFinished();
    }

    public class MyHandler {

        public int eventCounter = 0;

        @Subscribe
        public void dataCacheChanged(MoviesAddedEvent e) {
            eventCounter++;
        }
    }

    public void testUpdateInfos() throws IOException, JAXBException {
        // reset mock of previous tests
        CacheManager.setCacheActor(null);

        MyjaphooController controller = new MyjaphooController(null);
        MovieEntryJpaController jpa = new MovieEntryJpaController();

        MyHandler handler = new MyHandler();
        GlobalBus.bus.register(handler);

        controller.extractMovieInfos(jpa.fetchAll());
    }
}
