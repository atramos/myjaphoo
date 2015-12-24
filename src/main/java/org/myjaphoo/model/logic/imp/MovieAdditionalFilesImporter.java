/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

import groovy.text.SimpleTemplateEngine;
import org.apache.commons.lang.StringUtils;
import org.mlsoft.common.StringUtilities;
import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.ThumbType;
import org.myjaphoo.model.db.Thumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Imports additional files for movies: tries to find cover pictures in the
 * directory relative to the movie location.
 *
 * @author lang
 */
public class MovieAdditionalFilesImporter {

    private static final Logger logger = LoggerFactory.getLogger(MovieAdditionalFilesImporter.class);

    public List<Thumbnail> scanAndCreateAdditionalFiles(MovieEntry movieEntry, File file) throws Exception {

        ArrayList<Thumbnail> result = new ArrayList<Thumbnail>();
        if (AppConfig.PRF_MOVIE_SCANFORCOVERPICS.getVal()) {
            File dir = file.getParentFile();
            String[] extensions = StringUtils.split(StringUtils.remove(AppConfig.PRF_PICFILTER.getVal(), "."), ";");

            Collection<File> allRelevantPicFiles = org.apache.commons.io.FileUtils.listFiles(dir, extensions, true);
            // check, they match search patterns.
            allRelevantPicFiles = filter(movieEntry, allRelevantPicFiles);
            for (File picFile : allRelevantPicFiles) {
                logger.debug("adding additional cover picture " + picFile.getAbsolutePath());
                result.add(createThumbnail(picFile));
            }
        }
        return result;
    }

    public Thumbnail createThumbnail(File picFile) throws Exception {
        Thumbnail t = new Thumbnail();

        BufferedImage image = Picture.loadimage(picFile);
        if (image != null) {
            t.setThumbnail(Picture.toByte(image));
            t.setH(image.getWidth());
            t.setW(image.getHeight());
            t.setType(ThumbType.COVER_FRONT);
        }
        return t;
    }

    /**
     * max scan count. to prevent endless scanning of pictures.
     */
    private final int MAXSCAN = 5;


    private Collection<File> filter(MovieEntry movieEntry, Collection<File> allRelevantPicFiles) {
        String pattern = AppConfig.PRF_MOVIE_SCANPATTERN.getVal();
        pattern = applyVariableSubstitution(movieEntry, pattern);
        ArrayList<File> result = new ArrayList<File>();
        int i = 0;
        for (File file : allRelevantPicFiles) {
            logger.debug("checking possible cover file " + file.getAbsolutePath());
            Pattern p = Pattern.compile(pattern);

            Matcher m = p.matcher(file.getName());
            if (m.matches()) {
                result.add(file);
                i++;
                if (i >= MAXSCAN) {
                    logger.debug("reached max of scan count! ");
                    return result;
                }
            }

        }
        return result;
    }

    private String applyVariableSubstitution(MovieEntry movieEntry, String pattern) {
        SimpleTemplateEngine engine = new SimpleTemplateEngine();
        Map binding = new HashMap();
        binding.put("name", Pattern.quote(movieEntry.getName()));
        binding.put("namewithoutsuffix", Pattern.quote(StringUtilities.removeSuffix(movieEntry.getName())));
        try {
            pattern = engine.createTemplate(pattern).make(binding).toString();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("error substituting pattern " + pattern, e);
        }
        return pattern;
    }
}
