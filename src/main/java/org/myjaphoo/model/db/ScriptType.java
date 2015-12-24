package org.myjaphoo.model.db;

/**
 * ScriptType
 *
 * @author lang
 * @version $Id$
 */
public enum ScriptType {
    /** a script.*/
    SCRIPT,

    /** a script of this type gets executed at application startup. Could be used for special initialisations, e.g. user
     * defined functions for the filtering language. */
    INITSCRIPT,
    /** not really a script, but merely a text file. useful e.g. as payload for other scripts, e.g. save data as json, etc. */
    TEXT
}
