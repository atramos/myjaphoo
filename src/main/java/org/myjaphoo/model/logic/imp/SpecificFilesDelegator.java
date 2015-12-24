package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.FileType;
import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * AllFilesDelegator
 *
 * @author mla
 * @version $Id$
 */
public class SpecificFilesDelegator implements ImportDelegator {

    public static final Logger LOGGER = LoggerFactory.getLogger(SpecificFilesDelegator.class.getName());

    private MovieDelegator movieDelegator = new MovieDelegator();

    private PicDelegator picDelegator = new PicDelegator();

    @Override
    public String getScanFilter() {
        return AppConfig.PRF_SPECIFICFILTER.getVal();
    }

    @Override
    public List<Thumbnail> createAllThumbNails(MovieEntry movieEntry, File file) {
        if (FileType.Pictures.is(file.getName())) {
            return picDelegator.createAllThumbNails(movieEntry, file);
        } else if (FileType.Movies.is(file.getName())) {
            return movieDelegator.createAllThumbNails(movieEntry, file);
        } else {
            List<Thumbnail> result = new ArrayList<>();
            try {
                MovieAdditionalFilesImporter addImporter = new MovieAdditionalFilesImporter();
                List<Thumbnail> addThumbs = addImporter.scanAndCreateAdditionalFiles(movieEntry, file);
                result.addAll(addThumbs);
            } catch (Exception e) {
                LOGGER.error("error creating thumb nails!", e);
            }
            return result;
        }
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
