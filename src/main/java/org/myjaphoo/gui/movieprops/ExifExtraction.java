package org.myjaphoo.gui.movieprops;

import org.apache.commons.lang.StringUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.myjaphoo.MovieNode;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.db.exif.ExifData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ResourceBundle;

/**
 * ExifExtraction
 * @author mla
 * @version $Id$
 */
public class ExifExtraction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/exifpanel/resources/ExifPanel");
    private static final Logger logger = LoggerFactory.getLogger(ExifExtraction.class);

    public static ExifHeaderNode createExifNodes(PropertyNode parent, MovieNode node) {
        // live info:
        ExifHeaderNode exifRoot = tryToExtractExIf(parent, node);

        ExifData exifdata = node.getMovieEntry().getExifData();
        for (TagInfo tag : ExifTagConstants.ALL_EXIF_TAGS) {
            Object value = exifdata.getExifValue(tag.name);
            if (value != null) {
                mergeexif(exifRoot, tag.name, value.toString());
            }
        }
        if (exifdata.getLatitude() != null && exifdata.getLongitude() != null) {
            InfoNode gps = new InfoNode(exifRoot, localeBundle.getString("GPS"), ""); //NOI18N
            exifRoot.getChildren().add(gps);
            gps.getChildren().add(new InfoNode(gps, localeBundle.getString("LONGITUDE"), Double.toString(exifdata.getLongitude())));
            gps.getChildren().add(new InfoNode(gps, localeBundle.getString("LATITUDE"), Double.toString(exifdata.getLatitude())));
        }
        return exifRoot;
    }

    private static ExifHeaderNode tryToExtractExIf(PropertyNode parent, MovieNode node) {
        ExifHeaderNode exif = new ExifHeaderNode(parent, "Exif");
        if (parent != null) {
            parent.getChildren().add(exif);
        }
        try {

            String path = node.getCanonicalPath();
            File file = new File(path);
            if (FileType.Pictures.is(file.getName()) && file.exists()) {
                IImageMetadata metadata = Sanselan.getMetadata(file);
                if (metadata instanceof JpegImageMetadata) {
                    JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                    //allFields = jpegMetadata.getExif().getAllFields();
                    //logger.info("all fields:" + allFields);
                    for (TagInfo tag : ExifTagConstants.ALL_EXIF_TAGS) {
                        tryAddField(exif, jpegMetadata, tag);
                    }


                    // simple interface to GPS data
                    TiffImageMetadata exifMetadata = jpegMetadata.getExif();
                    if (exifMetadata != null) {
                        try {
                            TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();
                            if (null != gpsInfo) {
                                double longitude = gpsInfo.getLongitudeAsDegreesEast();
                                double latitude = gpsInfo.getLatitudeAsDegreesNorth();
                                exif.getChildren().add(new ExifNode(exif, localeBundle.getString("GPS LONGITUDE (DEGREES EAST)"), Double.toString(longitude)));
                                exif.getChildren().add(new ExifNode(exif, localeBundle.getString("GPS LATITUDE (DEGREES NORTH)"), Double.toString(latitude)));
                            }
                        } catch (ImageReadException e) {
                            logger.debug("error extraction gps data", e); //NOI18N
                        }
                    }


                }
            }

        } catch (Exception ex) {
            logger.debug("failed to extract exif", ex); //NOI18N
        }
        return exif;
    }

    private static void tryAddField(ExifHeaderNode exif, JpegImageMetadata jpegMetadata, TagInfo tag) {
        TiffField field = jpegMetadata.findEXIFValue(tag);
        if (field != null) {
            exif.getChildren().add(new ExifNode(exif, field.getTagName(), field.getValueDescription()));
        }
    }

    private static void mergeexif(ExifHeaderNode exifRoot, String name, String toString) {
        InfoNode foundNode = find(exifRoot, name);
        if (foundNode == null) {
            exifRoot.getChildren().add(new InfoNode(exifRoot, name, toString));
        } else {
            if (StringUtils.equals(foundNode.getValue(), toString)) {
                return; // both equal, nothing to do
            } else {
                // display both values:
                foundNode.setValue(foundNode.getValue() + " <-> " + toString); //NOI18N
            }
        }
    }

    private static InfoNode find(PropertyNode node, String name) {
        if (StringUtils.equals(node.getName(), name)) {
            return (InfoNode) node;
        } else {
            for (PropertyNode child : node.getChildren()) {
                InfoNode subSearch = find(child, name);
                if (subSearch != null) {
                    return subSearch;
                }
            }
        }
        return null;
    }


}
