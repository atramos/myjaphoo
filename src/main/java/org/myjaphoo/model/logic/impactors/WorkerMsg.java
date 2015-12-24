/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.impactors;

import java.io.File;

/**
 *
 * @author mla
 */
public class WorkerMsg {

    private File next;
    private ImportQueue caller;

    WorkerMsg(File next, ImportQueue caller) {
        this.next=next;
        this.caller = caller;
    }

    /**
     * @return the next
     */
    public File getNext() {
        return next;
    }

    /**
     * @return the caller
     */
    public ImportQueue getCaller() {
        return caller;
    }

}
