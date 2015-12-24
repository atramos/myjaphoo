/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;

import java.io.File;
import java.util.List;

/**
 *
 * @author mla
 */
public interface ImportDelegator {

    String getScanFilter();

    /**
     * erzeugt die thumbnails, aber speichert sie nicht in der db ab.
     * Benötigt deshalb auch keine transaktion bzw. db-verbindung.
     * Kann deshalb gut parallelisiert werden.
     */
    List<Thumbnail> createAllThumbNails(MovieEntry movieEntry, File file);

    /**
     * befüllt alle medien infos.
     */ 
    public void getMediaInfos(MovieEntry movieEntry);
}
