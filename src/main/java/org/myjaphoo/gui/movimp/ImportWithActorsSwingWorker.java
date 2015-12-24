/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movimp;

import org.apache.commons.lang.time.StopWatch;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.logic.MovieImport;
import org.myjaphoo.model.logic.imp.ImportDelegator;
import org.myjaphoo.model.logic.imp.MovieDelegator;
import org.myjaphoo.model.logic.imp.ProviderFactory;
import org.myjaphoo.model.logic.imp.movInfoProvider.MovieAttributeProvider;
import org.myjaphoo.model.logic.imp.thumbprovider.ThumbnailProvider;
import org.myjaphoo.model.logic.impactors.ImportMsg;
import org.myjaphoo.model.logic.impactors.ImportQueue;
import org.myjaphoo.model.util.FRessourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mla
 */
public class ImportWithActorsSwingWorker extends SwingWorker<Object, String> {

    private static Logger logger = LoggerFactory.getLogger(ImportWithActorsSwingWorker.class);
    public static Logger IMPLOGGER = LoggerFactory.getLogger(ImportDialog.WMIMPORT);
    private FRessourceBundle bundle = new FRessourceBundle("org/myjaphoo/gui/movimp/resources/ImportWithActorsSwingWorker");
    private ImportQueue queue;

    void abortImport() {

        dialog.setStatusBarMessage(bundle.getString("CANCELLING IMPORT..."));
        IMPLOGGER.info(bundle.getString("CANCELLING IMPORT...")); //NOI18N
        if (queue != null) {
            queue.send(new ImportMsg.AbortImport());
        }
    }


    private ImportDelegator delegator;
    private File dir2Scan;
    private ImportDialogFeedback dialog;

    public ImportWithActorsSwingWorker(ImportDialogFeedback dialog, ImportDelegator delegator, File dir2Scan) {
        this.delegator = delegator;
        this.dir2Scan = dir2Scan;
        this.dialog = dialog;
    }
    int allFiles;
    int currFile;
    long start;

    @Override
    protected Object doInBackground() throws Exception {
        MovieImport i = new MovieImport();
        StopWatch watch = new StopWatch();
        watch.start();
        IMPLOGGER.info(bundle.getString("SCANNING_FILES"));
        start = System.currentTimeMillis();
        List<File> movieFiles = i.scanFiles(dir2Scan.getAbsolutePath(), delegator);
        IMPLOGGER.info(bundle.getString("FOUND_N_FILES", movieFiles.size()));
        IMPLOGGER.info(bundle.getString("NOW_IMPORTING_FILES"));
        allFiles = movieFiles.size();
        Set<String> allCanonicalPaths = MovieImport.getCanonicalUnifiedPathsOfAllEntries();
        i.flushDatabase(); // first level cache clearen; ansonsten kann es sehr langsam werden

        // prefilter files:
        ArrayList<File> filteredFiles = new ArrayList<File>();
        for (File file : movieFiles) {
            if (!allCanonicalPaths.contains(MovieImport.unifyPath(file.getCanonicalPath()))) {
                filteredFiles.add(file);
            } else {
                //logger.info("skipping " + file.getAbsolutePath() + "; already exists in db...");
            }
        }
        allFiles = filteredFiles.size();
        int diff = movieFiles.size() - filteredFiles.size();
        if (diff > 0) {
            IMPLOGGER.info(bundle.getString("SKIPPED_N_FILES_WHICHALREADY_EXIST", diff));
        }
        IMPLOGGER.info(bundle.getString("FOUND_N_NEWFILESTOIMPORT", filteredFiles.size()));

        if (allFiles > 0) {
            if (delegator instanceof MovieDelegator) {
                try {
                    // check for a properly configured thumb nail provider
                    ThumbnailProvider tn = ProviderFactory.getBestThumbnailProvider();
                    IMPLOGGER.info("using thumb nail provider " + tn.getDescr());

                } catch (Throwable rte) {
                   // no provider is configured:
                    IMPLOGGER.error(rte.getLocalizedMessage());
                    dialog.setStatusBarActionStopped();
                    return null;
                }
                // check for movie attribute extraction. but this is not mandatory; we do anyway start the import now:
                try {
                    MovieAttributeProvider ma = ProviderFactory.getBestMovieAttributeProvider();
                    IMPLOGGER.info("using movie attribute provider " + ma.getDescr());
                } catch (RuntimeException rte) {
                    IMPLOGGER.error(rte.getLocalizedMessage());
                }

            }

            SwingCallBackActor callBackActor = new SwingCallBackActor(dialog, this, allFiles);

            queue = ImportQueue.createImportQueue(callBackActor, delegator);
            queue.send(new ImportMsg.ImportFilesMsg(filteredFiles));
            queue.waitTillAllFinished();
        }
        watch.stop();
        IMPLOGGER.info(bundle.getString("FINISHED_WITH_DURATION", watch.toString()));

        CacheManager.getCacheActor().resetInternalCache();
        CacheManager.getCacheActor().resetImmutableCopy();
        dialog.setStatusBarActionStopped();
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        currFile += chunks.size();
        int perc = currFile * 100 / allFiles;
        if (perc > 100) {
            perc = 100;
        }
        dialog.setStatusBarProgress(perc);
    }

    public void publishIt(String filePath) {
        this.publish(filePath);
    }
}
