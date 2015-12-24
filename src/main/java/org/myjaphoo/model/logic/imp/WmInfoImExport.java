/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.imp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.myjaphoo.model.db.*;
import org.myjaphoo.model.logic.MyjaphooDB;

/**
 *
 * @author mla
 */
public class WmInfoImExport {

    public void export(File file, MovieEntry entry) throws JAXBException, FileNotFoundException, IOException {
       /**
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        Export list = createExportList();
        oos.writeObject(list.getTokens().toArray());
        oos.close();
        */

        entry = MyjaphooDB.singleInstance().ensureObjIsAttached(entry);

        JAXBContext jaxb = JAXBContext.newInstance(WmInfo.class);
        FileOutputStream fos = new FileOutputStream(file);
        Marshaller m = jaxb.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        WmInfo info = new WmInfo();
        if (entry.getChecksumCRC32() == null) {
            throw new RuntimeException("Fehler: checksumme ist null bei entry " + entry.getCanonicalPath());
        }
        info.checksum = entry.getChecksumCRC32();
        info.rating = entry.getRating();
        if (entry.getTokens().size() > 0) {
            String[] arr = new String[entry.getTokens().size()];
            int i = 0;
            for (Token token : entry.getTokens()) {
                arr[i] = token.getName(); i++;
            }
            info.tokens = arr;
        }
        info.thumb1 = entry.getThumbnail1();
        if (entry.getThumbnails().size() >1) {
            info.thumb2 = entry.getThumbnails().get(1).getThumbnail();
        }
        if (entry.getThumbnails().size() >2) {
            info.thumb3 = entry.getThumbnails().get(2).getThumbnail();
        }
        if (entry.getThumbnails().size() >3) {
            info.thumb4 = entry.getThumbnails().get(3).getThumbnail();
        }
        if (entry.getThumbnails().size() >4) {
            info.thumb5 = entry.getThumbnails().get(4).getThumbnail();
        }
        info.width = entry.getMovieAttrs().getWidth();
        info.height = entry.getMovieAttrs().getHeight();
        info.length = entry.getMovieAttrs().getLength();
        info.fps = entry.getMovieAttrs().getFps();
        info.bitrate = entry.getMovieAttrs().getBitrate();
        info.format = entry.getMovieAttrs().getFormat();
        m.marshal(info, fos);
        fos.close();
    }
}
