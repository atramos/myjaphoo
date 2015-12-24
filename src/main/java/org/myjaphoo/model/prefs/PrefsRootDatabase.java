/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.prefs;

import org.mlsoft.common.prefs.model.editors.AbstractPrefVal;
import org.mlsoft.common.prefs.model.editors.BackingDelegator;
import org.mlsoft.common.prefs.model.editors.EditorRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mla
 */
public class PrefsRootDatabase extends EditorRoot {


    private final static Logger logger = LoggerFactory.getLogger(PrefsRootDatabase.class);


    /**
     *
     */
    public PrefsRootDatabase() {
    }



    public String getDisplayClassName() {
        return "PrefsRoot";
    }

    public String info() {
        return "";
    }

    public BackingDelegator createBackingDelegator(AbstractPrefVal abstractPrefVal,
            Object defaultVal) {
        return new DatabaseBackingDelegator(this, abstractPrefVal, defaultVal);
    }

}
