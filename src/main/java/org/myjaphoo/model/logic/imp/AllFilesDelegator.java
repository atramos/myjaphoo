package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.FileType;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * AllFilesDelegator
 *
 * @author mla
 * @version $Id$
 */
public class AllFilesDelegator implements ImportDelegator {

    private MovieDelegator movieDelegator = new MovieDelegator();

    private PicDelegator picDelegator = new PicDelegator();

    @Override
    public String getScanFilter() {
        return null;
    }

    @Override
    public List<Thumbnail> createAllThumbNails(MovieEntry movieEntry, File file) {
        if (FileType.Pictures.is(file.getName())) {
            return picDelegator.createAllThumbNails(movieEntry, file);
        } else if (FileType.Movies.is(file.getName())) {
            return movieDelegator.createAllThumbNails(movieEntry, file);
        } else return Collections.emptyList();
    }

    @Override
    public void getMediaInfos(MovieEntry movieEntry) {
        if (FileType.Pictures.is(movieEntry)) {
            picDelegator.getMediaInfos(movieEntry);
        } else if (FileType.Movies.is(movieEntry)) {
            movieDelegator.getMediaInfos(movieEntry);
        }
    }
}
