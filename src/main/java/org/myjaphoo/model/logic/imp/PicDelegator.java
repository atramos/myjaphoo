/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.db.exif.ExifData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mla
 */
public class PicDelegator implements ImportDelegator {

    private static final Logger logger = LoggerFactory.getLogger(PicDelegator.class);
    DetailedPictureInfoExtractor extractor = new DetailedPictureInfoExtractor();

    @Override
    public String getScanFilter() {
        return AppConfig.PRF_PICFILTER.getVal();
    }


    private Thumbnail createThumbNail(MovieEntry movieEntry, File file) throws IOException, Exception {
        BufferedImage image = Picture.loadimage(file);
        if (image != null) {
            movieEntry.getMovieAttrs().setWidth(image.getWidth());
            movieEntry.getMovieAttrs().setHeight(image.getHeight());
        }
        BufferedImage thumb = Picture.scaleToStdThumbSize(image);
        Thumbnail tn = new Thumbnail();
        tn.setThumbnail(Picture.toByte(thumb));
        return tn;
    }

    @Override
    public List<Thumbnail> createAllThumbNails(MovieEntry movieEntry, File file) {
        ArrayList<Thumbnail> result = new ArrayList<Thumbnail>();
        try {
            result.add(createThumbNail(movieEntry, file));
        } catch (IOException ex) {
            LoggerFactory.getLogger(PicDelegator.class.getName()).error("error", ex);
        } catch (Exception ex) {
            LoggerFactory.getLogger(PicDelegator.class.getName()).error("error", ex);
        }
        return result;
    }

    @Override
    public void getMediaInfos(MovieEntry entry) {

        MovieExtractionInfo info = extractor.extract(entry);
        entry.setMovieAttrs(info.genMovieAttrs());
        entry.setExifData(extractExif(entry));
    }

    private ExifData extractExif(MovieEntry entry) {
        ExifData exifData = null;

        try {

            String path = entry.getCanonicalPath();
            IImageMetadata metadata = Sanselan.getMetadata(new File(path));
            if (metadata instanceof JpegImageMetadata) {
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                ArrayList<TiffField> foundFields = new ArrayList<TiffField>();

                //allFields = jpegMetadata.getExif().getAllFields();
                //logger.info("all fields:" + allFields);
                for (TagInfo tag : ExifTagConstants.ALL_EXIF_TAGS) {
                    TiffField field = jpegMetadata.findEXIFValue(tag);
                    if (field != null) {
                        foundFields.add(field);
                    }
                }
                Double longitude = null;
                Double latitude = null;
                
                                 // simple interface to GPS data
	            TiffImageMetadata exifMetadata = jpegMetadata.getExif();
	            if (exifMetadata != null) {
	                try {
	                    TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
	                    if (null != gpsInfo) {
	                        longitude = gpsInfo.getLongitudeAsDegreesEast();
	                        latitude = gpsInfo.getLatitudeAsDegreesNorth();
	                    }
	                } catch (ImageReadException e) {
	                    logger.debug("error extraction gps data", e);
	                }
	            }
                
                exifData = new ExifData(foundFields, longitude, latitude);
            }
            return exifData;
        } catch (Exception ex) {
            logger.debug("failed to extract exif", ex);
            return null;
        }
    }
}
