package org.myjaphoo.gui.movimp;

import groovyx.gpars.actor.DynamicDispatchActor;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.myjaphoo.model.logic.impactors.ImportMsg;
import org.myjaphoo.model.logic.impactors.StopMessage;
import org.myjaphoo.model.util.FRessourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SwingCallBackActor
 *
 * @author mla
 * @version $Id$
 */
class SwingCallBackActor extends DynamicDispatchActor {
    public static Logger IMPLOGGER = LoggerFactory.getLogger(ImportDialog.WMIMPORT);
    private int currIndex = 0;
    private int allFiles;
    private ImportDialogFeedback dialog;
    private long start;
    private ImportWithActorsSwingWorker swingWorker;
    private FRessourceBundle bundle = new FRessourceBundle("org/myjaphoo/gui/movimp/resources/ImportWithActorsSwingWorker");

    public SwingCallBackActor(ImportDialogFeedback dialog, ImportWithActorsSwingWorker swingWorker, int allFiles) {
        this.dialog = dialog;
        this.allFiles = allFiles;
        start = System.currentTimeMillis();
        this.swingWorker = swingWorker;
    }

    public void onMessage(StopMessage msg) {
        stop();
    }

    public void onMessage(ImportMsg.FinishedMsg msg) {
        currIndex++;
        String filePath = msg.getMsg().getNext().getAbsolutePath();
        if (currIndex < 100) {
            // fÃ¼r kleine mengen von zu importierenden files, geben wir mehr infos aus:
            IMPLOGGER.info(bundle.getString("IMPORTING_DIR", filePath)); //NOI18N
        }
        // ab 100 nur noch wenig infos ausgeben:
        if (currIndex % 100 == 0) {
            IMPLOGGER.info(bundle.getString("IMPORT_N_OF_M_FILENAME", currIndex, allFiles, filePath)); //NOI18N
        }

        try {
            swingWorker.publishIt(filePath);
        } catch (Exception e) {
            IMPLOGGER.error("could not import file", e); //NOI18N
        }

        long currtime = System.currentTimeMillis();
        long timeSpend = currtime - start;
        long estimatedTimeToFinish = timeSpend / currIndex * (allFiles - currIndex);
        dialog.updatefileAndRemainingTime(filePath, DurationFormatUtils.formatDurationWords(estimatedTimeToFinish, true, true));
    }
}