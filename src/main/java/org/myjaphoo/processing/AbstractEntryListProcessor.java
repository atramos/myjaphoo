/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.processing;

import org.myjaphoo.model.db.MovieEntry;

/**
 * Abstract imlementation, welche keine implementierung für start- und ende hat.
 * @author mla
 */
public abstract class AbstractEntryListProcessor implements EntryListProcessor {

    @Override
    public void startProcess() {
    }


    @Override
    public void stopProcess() {
    }

    @Override
    public String shortName(MovieEntry t) {
        return t.getName();
    }

    @Override
    public String longName(MovieEntry t) {
        return t.getCanonicalPath();
    }


}
