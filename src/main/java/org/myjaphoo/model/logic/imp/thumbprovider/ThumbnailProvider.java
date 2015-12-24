package org.myjaphoo.model.logic.imp.thumbprovider;

import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.logic.imp.ExternalProvider;

import java.io.File;
import java.util.List;

/**
 * interface for thumb nail provider, used to get thumbnails for movies.
 * There are several implementations all based on external dependencies (programs).
 *
 * @author mla
 * @version $Id$
 */
public interface ThumbnailProvider extends ExternalProvider{

    /**
     * creates thumbnail objects for a given movie file.
     * The thumbnails are not peristet. This is the responsibility of the caller
     *
     * @param file the movie file to generate thumbs from
     */
    List<Thumbnail> createAllThumbNails(File file);
}
