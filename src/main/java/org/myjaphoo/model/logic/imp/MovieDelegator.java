/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.ThumbType;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.myjaphoo.model.logic.MovieImport;
import org.myjaphoo.model.logic.ThumbnailJpaController;
import org.myjaphoo.model.logic.imp.movInfoProvider.MovieAttributeProvider;
import org.myjaphoo.model.logic.imp.thumbprovider.ThumbnailProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @author mla
 */
public class MovieDelegator implements ImportDelegator {

    public static final Logger LOGGER = LoggerFactory.getLogger(MovieImport.class.getName());
    private ThumbnailJpaController thumbController = new ThumbnailJpaController();
    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();
    /**
     * the thumbnailer to use. lazy created.
     */
    private ThumbnailProvider thumbnailProvider;


    /**
     * the movie attribute provider to use. could be null, if no one is available.
     */
    private MovieAttributeProvider movieAttributeProvider;

    private boolean movieAttributeProviderAlreadyTried = false;

    @Override
    public String getScanFilter() {
        return AppConfig.PRF_MOVIEFILTER.getVal();
    }

    public void addFrontCoverThumb(MovieEntry movieEntry, File picFile) throws Exception {
        MovieAdditionalFilesImporter addImporter = new MovieAdditionalFilesImporter();
        Thumbnail thumb = addImporter.createThumbnail(picFile);
        thumb.setType(ThumbType.COVER_FRONT);
        thumbController.create(thumb);
    }

    public void addBackCoverThumb(MovieEntry movieEntry, File picFile) throws Exception {
        MovieAdditionalFilesImporter addImporter = new MovieAdditionalFilesImporter();
        Thumbnail thumb = addImporter.createThumbnail(picFile);
        thumb.setType(ThumbType.COVER_BACK);
        thumbController.create(thumb);
    }

    public void addThumbListThumb(MovieEntry movieEntry, File picFile) throws Exception {
        MovieAdditionalFilesImporter addImporter = new MovieAdditionalFilesImporter();
        Thumbnail thumb = addImporter.createThumbnail(picFile);
        thumb.setType(ThumbType.THUMBLIST);
        thumbController.create(thumb);
    }

    @Override
    public List<Thumbnail> createAllThumbNails(MovieEntry movieEntry, File file) {
        try {
            List<Thumbnail> result = getOrCreateThumbnailProvider().createAllThumbNails(file);

            MovieAdditionalFilesImporter addImporter = new MovieAdditionalFilesImporter();
            List<Thumbnail> addThumbs = addImporter.scanAndCreateAdditionalFiles(movieEntry, file);
            result.addAll(addThumbs);

            return result;
        } catch (Exception e) {
            LOGGER.error("error creating thumb nails!", e);
        }
        return new ArrayList<>();
    }

    @Override
    public void getMediaInfos(MovieEntry entry) {
        MovieAttributeProvider provider = getOrCreateMovieAttributeProvider();
        if (provider != null) {
            final File substitude = new File(fileSubstitution.substitude(entry.getCanonicalPath()));
            entry.setMovieAttrs(provider.extract(substitude));
        }
    }

    public ThumbnailProvider getOrCreateThumbnailProvider() {
        if (thumbnailProvider == null) {
            thumbnailProvider = ProviderFactory.getBestThumbnailProvider();
        }
        return thumbnailProvider;
    }

    public MovieAttributeProvider getOrCreateMovieAttributeProvider() {
        if (movieAttributeProvider == null && !movieAttributeProviderAlreadyTried) {
            try {
                movieAttributeProvider = ProviderFactory.getBestMovieAttributeProvider();
            } catch (RuntimeException rte) {
                LOGGER.error("error creating movie attribute provider!", rte);
            }
            movieAttributeProviderAlreadyTried = true;
        }
        return movieAttributeProvider;
    }
}
