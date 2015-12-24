/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mla
 */
public class TextDelegator implements ImportDelegator {

    public TextDelegator() {
    }

    @Override
    public String getScanFilter() {
        return AppConfig.PRF_TEXTFILTER.getVal();
    }

    @Override
    public List<Thumbnail> createAllThumbNails(MovieEntry movieEntry, File file) {
        return new ArrayList<>();
    }

    @Override
    public void getMediaInfos(MovieEntry movieEntry) {

    }

}
