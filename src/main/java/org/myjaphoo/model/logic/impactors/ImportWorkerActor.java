/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.impactors;


import groovyx.gpars.actor.DynamicDispatchActor;
import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.dbhandling.ThreadLocalTransactionBoundaryHandler;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator.CommitBlock;
import org.myjaphoo.model.logic.imp.ImportDelegator;
import org.myjaphoo.model.logic.imp.WmInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author lang
 */

public class ImportWorkerActor extends DynamicDispatchActor {

    private final Logger logger = LoggerFactory.getLogger(ImportWorkerActor.class.getName());
    /**
     * Eigenen default transaction handler einrichten, der einen eigenen
     * EntitManager hat.
     */
    private ThreadLocalTransactionBoundaryHandler tr =
            new ThreadLocalTransactionBoundaryHandler(MyjaphooDB.singleInstance().getConnection());
    private ImportDelegator importDelegator;
    private ImportTokenActor importTokenActor;

    public ImportWorkerActor(ImportTokenActor importTokenActor, ImportDelegator importDelegator) {
        this.importDelegator = importDelegator;
        this.importTokenActor = importTokenActor;
    }

    public void onMessage(StopMessage msg) {
        stop();
    }

    public void onMessage(WorkerMsg msg) {
        try {
            importMovie(msg);
        } catch (Exception ex) {
            // alle Fehler catchen, damit auf jeden Fall eine messgage zurückgeschickt wird.
            // ansonsten würde evtl. der Aufrufer ewig warten...
            logger.error("import failed", ex);
        }
        // work on this: import it usw...
        // send back to caller that it is finished.
        msg.getCaller().send(new ImportMsg.FinishedMsg(msg, this));
    }

    private void importMovie(WorkerMsg msg) throws IOException {
        File file = msg.getNext();
        File dir = file.getParentFile();
        String path = file.getCanonicalPath();
        logger.debug(path + "\t" + file.length());
        final MovieEntry movieEntry = new MovieEntry();
        movieEntry.setName(file.getName());
        movieEntry.setCanonicalDir(dir.getCanonicalPath());
        movieEntry.setFileLength(file.length());

        WmInfo wmInfo = findWmInfo(file);
        if (wmInfo != null) {
            List<Thumbnail> thumbs = fillFromWmFile(movieEntry, wmInfo);
            saveMovieAndThumbs(movieEntry, thumbs);
        } else {
            if (!AppConfig.PRF_INTERNAL_DONOTBUILDCHECKSUM.getVal()) {
                logger.debug("calculating checksum for " + path);
                long checksum = org.apache.commons.io.FileUtils.checksumCRC32(file);
                movieEntry.setChecksumCRC32(checksum);
            }
            logger.debug("creating thumbs");
            List<Thumbnail> thumbs = importDelegator.createAllThumbNails(movieEntry, file);
            saveMovieAndThumbs(movieEntry, thumbs);

            // save:
            tr.doInNewTransaction(new CommitBlock() {

                @Override
                public void runCommitBlock(EntityManager em) throws Exception {
                    // using the merged entry, because the thumbs save method 
                    // works internally on a merged instance
                    MovieEntry mergedEntry = em.merge(movieEntry);
                    logger.debug("extracting media information");
                    importDelegator.getMediaInfos(mergedEntry);
                }
            });

        }

        // Tokens importieren und assignen wir über einen eigenen Actor,
        // damit es sequenziell abläuft. Ansonsten
        // müssten wir hier wieder synchronisieren,
        // da mehrere actor-threads parallel tokens+assignements anlegen würden.
        if (wmInfo != null && wmInfo.tokens != null) {
            importTokenActor.send(new AssignTokenMsg(movieEntry.getId(), Arrays.asList(wmInfo.tokens)));

        }

    }

    private ArrayList<Thumbnail> fillFromWmFile(MovieEntry movieEntry, WmInfo wmInfo) {
        ArrayList<Thumbnail> result = new ArrayList<Thumbnail>();
        movieEntry.setChecksumCRC32(wmInfo.checksum);
        if (wmInfo.thumb1 != null) {
            result.add(createThumbFromByteArray(wmInfo.thumb1));
        }
        if (wmInfo.thumb2 != null) {
            result.add(createThumbFromByteArray(wmInfo.thumb2));
        }
        if (wmInfo.thumb3 != null) {
            result.add(createThumbFromByteArray(wmInfo.thumb3));
        }
        if (wmInfo.thumb4 != null) {
            result.add(createThumbFromByteArray(wmInfo.thumb4));
        }
        if (wmInfo.thumb5 != null) {
            result.add(createThumbFromByteArray(wmInfo.thumb5));
        }
        if (wmInfo.rating != null) {
            movieEntry.setRating(wmInfo.rating);
        }

        movieEntry.getMovieAttrs().setWidth(save(wmInfo.width));
        movieEntry.getMovieAttrs().setHeight(save(wmInfo.height));
        movieEntry.getMovieAttrs().setLength(save(wmInfo.length));
        movieEntry.getMovieAttrs().setFps(save(wmInfo.fps));
        movieEntry.getMovieAttrs().setBitrate(save(wmInfo.bitrate));
        movieEntry.getMovieAttrs().setFormat(wmInfo.format);
        return result;
    }

    private Thumbnail createThumbFromByteArray(byte[] bytes) {
        Thumbnail t = new Thumbnail();
        t.setThumbnail(bytes);
        return t;
    }

    private WmInfo findWmInfo(File file) {
        try {
            // construct filename:

            String filename = file.getCanonicalPath() + ".wminfo";
            File filetoread = new File(filename);
            if (!filetoread.exists()) {
                return null;
            }
            JAXBContext jaxb = JAXBContext.newInstance(WmInfo.class);
            Unmarshaller um = jaxb.createUnmarshaller();

            // input stream selbst anlegen: dies verhindert fehler, wenn filename sonderzeichen, z.b. # entählt
            // u. man einfach das file objekt an unmarshal geben würde.
            // Scheinbar hat unmarschal seine eigene mehtode, den filenamen nochmals zu interpretieren...
            FileInputStream fis = new FileInputStream(filetoread);

            WmInfo info = (WmInfo) um.unmarshal(fis);
            return info;
        } catch (Exception ex) {
            throw new RuntimeException("parsing info file failed!", ex);
        }
    }

    private int save(Integer i) {
        if (i == null) {
            return 0;
        } else {
            return i.intValue();
        }
    }

    private void saveMovieAndThumbs(final MovieEntry movieEntry, final List<Thumbnail> thumbs) {
        tr.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                em.persist(movieEntry);
                for (Thumbnail t : thumbs) {
                    t.setMovieEntry(movieEntry);
                    movieEntry.getThumbnails().add(t);
                    em.persist(t);
                }
            }
        });
    }
}
