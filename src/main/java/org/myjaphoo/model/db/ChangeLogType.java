/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import java.util.ResourceBundle;

/**
 *
 * @author mla
 */
public enum ChangeLogType {

    NEWTOK,
    ASSIGNTOK,
    IMPORTMOVIES,
    FILECOPY,
    CREATEWMINFOFILES,
    DELETEFILES,
    IMPORTTOKENS,
    RECREATETHUMBS,
    REMOVEENTRIES,
    REMOVETOKEN,
    REMOVETOKENRELATION,
    SETRATING,
    NEWBOOKMARK,
    NEWMETATOK,
    REMOVEMETATOKEN,
    REMOVEMETATOKENRELATION,
    ASSIGNMETATOK;

    private String descr;

    private ChangeLogType() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/db/resources/ChangeLogType");
        descr = localeBundle.getString(this.name());
    }

    public String getDescr() {
        return descr;
    }
}
