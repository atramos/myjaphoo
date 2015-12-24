/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;

import org.apache.commons.lang.StringUtils;
import org.mlsoft.common.prefs.model.editors.StringVal;

import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;

/**
 *
 * @author mla
 */
public enum FileType {

    Pictures(AppConfig.PRF_PICFILTER),
    Movies(AppConfig.PRF_MOVIEFILTER),
    CompressedFiles(AppConfig.PRF_COMPRESSEDFILESFILTER),
    Text(AppConfig.PRF_TEXTFILTER);
    private StringVal preference;

    private FileType(StringVal pref) {
        this.preference = pref;
    }

    public String getFileFilter() {
        return preference.getVal();
    }

    public boolean is(MovieEntry entry) {
        return is(entry.getName());
    }

    public boolean is(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if(dotIndex == -1 | dotIndex >= filename.length() - 1) {
            return false;
        }
        String extension = filename.substring(dotIndex).toLowerCase();
        String picSuffixes = getFileFilter().toLowerCase();
        return picSuffixes.contains(extension);
    }
}
