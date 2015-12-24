/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.impactors;

import groovyx.gpars.actor.DynamicDispatchActor;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.model.logic.imp.ImportDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mla
 */
public class ImportQueue extends DynamicDispatchActor {

    private static Logger logger = LoggerFactory.getLogger(ImportQueue.class);
    private List<ImportWorkerActor> worker;
    private ImportTokenActor importTokenActor;
    /**
     * Actor from "outside" which could get informed when one import has finished.
     */
    private DynamicDispatchActor callBackActor = null;

    private ImportQueue(List<ImportWorkerActor> worker, ImportTokenActor importTokenActor) {
        this.worker = worker;
        this.importTokenActor = importTokenActor;
    }

    private LinkedList<File> queue = new LinkedList<File>();
    private int numOfImports = 0;
    private ArrayList<ImportMsg.FinishedMsg> results = new ArrayList<ImportMsg.FinishedMsg>();

    public void onMessage(StopMessage msg) {
        stop();
    }

    public void onMessage(ImportMsg.ImportFilesMsg msg) {
        logger.debug("handle import message");
        handleImportMsg(msg);
    }

    public void onMessage(ImportMsg.FinishedMsg msg) {
        logger.debug("handle finished import message");
        handleFinishedMsg(msg);
    }

    public void onMessage(ImportMsg.AbortImport msg) {
        logger.debug("aborting import... shutting down all actors");
        // dann sind wir fertig:
        // alle worker beenden:
        for (ImportWorkerActor w : worker) {
            w.stop();
        }

        // and the callback handler:
        if (callBackActor != null) {
            callBackActor.stop();
        }
        // and the import token actor:
        importTokenActor.stop();
        // shutdown ourself:
        this.stop();
    }

    private void handleFinishedMsg(ImportMsg msg) {
        ImportMsg.FinishedMsg finishedMsg = (ImportMsg.FinishedMsg) msg;
        getResults().add(finishedMsg);
        // ein worker hat seine arbeit beendet: mit nächsten file beschicken:
        if (queue.size() > 0) {
            File next = queue.poll();

            finishedMsg.getWorker().send(new WorkerMsg(next, this));
        } else  {
            finishedMsg.getWorker().send(new StopMessage());
        }
        if (callBackActor != null) {
            callBackActor.send(finishedMsg);
        }
        if (numOfImports == results.size()) {
            importTokenActor.send(new StopMessage());
            callBackActor.send(new StopMessage());
            this.send(new StopMessage());
        }
    }

    /**
     * Starte den import vorgang: gib jedem worker actor einen auftrag aus der queue.
     */
    private void handleImportMsg(ImportMsg msg) {
        ImportMsg.ImportFilesMsg impMsg = (ImportMsg.ImportFilesMsg) msg;
        queue.addAll(impMsg.getFilesToImport());
        numOfImports = impMsg.getFilesToImport().size();
        if (queue.size() > 0) {
            // gebe jeden worker einen job:
            int max = Math.min(queue.size(), worker.size());
            for (int i = 0; i < max; i++) {
                File next = queue.poll();
                worker.get(i).send(new WorkerMsg(next, this));
            }
        }
    }

    /**
     * @return the results
     */
    public ArrayList<ImportMsg.FinishedMsg> getResults() {
        return results;
    }

    public static ImportQueue createImportQueue(ImportDelegator importDelegator) {
        return createImportQueue(null, importDelegator);
    }

    public static ImportQueue createImportQueue(DynamicDispatchActor callBackActor, ImportDelegator importDelegator) {
        List<ImportWorkerActor> workers = new ArrayList<ImportWorkerActor>();
        ImportTokenActor importTokenActor = new ImportTokenActor();
        importTokenActor.start();

        for (int i = 0; i < MyjaphooAppPrefs.PRF_IMPORT_NUMWORKERTHREADS.getVal(); i++) {
            ImportWorkerActor worker = new ImportWorkerActor(importTokenActor, importDelegator);
            workers.add(worker);
            worker.start();
        }
        ImportQueue queue = new ImportQueue(workers, importTokenActor);

        queue.actorGroup.add(importTokenActor);
        queue.actorGroup.addAll(workers);
        queue.actorGroup.add(queue);

        if (callBackActor != null) {
            queue.setCallBackActor(callBackActor);
            queue.actorGroup.add(callBackActor);
            callBackActor.start();
        }
        queue.start();
        return queue;
    }

    private List<DynamicDispatchActor> actorGroup = new ArrayList<>();

    /**
     * @param callBackActor the callBackActor to set
     */
    public void setCallBackActor(DynamicDispatchActor callBackActor) {
        this.callBackActor = callBackActor;
    }

    public void waitTillAllFinished() throws InterruptedException {
        for (DynamicDispatchActor actor : actorGroup) {
            actor.join();
        }
    }


}
