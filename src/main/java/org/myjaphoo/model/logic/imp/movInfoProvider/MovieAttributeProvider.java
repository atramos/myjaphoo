package org.myjaphoo.model.logic.imp.movInfoProvider;

import org.myjaphoo.model.db.MovieAttrs;
import org.myjaphoo.model.logic.imp.ExternalProvider;

import java.io.File;

/**
 * A movie info extractor that provides information about a movie.
 */
public interface MovieAttributeProvider extends ExternalProvider {

    /**
     * extracts information about a movie.
     *
     * @param file the movie file
     * @return the extracted attributes.
     */
    MovieAttrs extract(File file);
}
