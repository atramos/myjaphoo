/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.impactors;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author mla
 */
public class ImportMsg {

    
    public static class AbortImport extends ImportMsg {}
    
    public static class ImportFilesMsg extends ImportMsg {

        private Collection<File> filesToImport;

        public ImportFilesMsg(Collection<File> filesToImport) {
            this.filesToImport = filesToImport;
        }

        public Collection<File> getFilesToImport() {
            return filesToImport;
        }
    }

    public static class FinishedMsg extends ImportMsg {

        private WorkerMsg msg;
        private ImportWorkerActor worker;

        public FinishedMsg(WorkerMsg msg, ImportWorkerActor worker) {
            this.msg = msg;
            this.worker = worker;
        }

        /**
         * @return the msg
         */
        public WorkerMsg getMsg() {
            return msg;
        }

        /**
         * @return the worker
         */
        public ImportWorkerActor getWorker() {
            return worker;
        }
    }

}
