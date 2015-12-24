/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 *
 * @author mla
 */
public class DetailedPictureInfoExtractor {

    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();
    private static final Logger logger = LoggerFactory.getLogger(DetailedPictureInfoExtractor.class.getName());

    public MovieExtractionInfo extract(MovieEntry entry) {
        if (FileType.Pictures.is(entry)) {
            try {
                final File file = new File(fileSubstitution.substitude(entry.getCanonicalPath()));
                ImageInfo info = Sanselan.getImageInfo(file);

                String dump = Sanselan.dumpImageFile(file);
                //LoggerFactory.getLogger(DetailedPictureInfoExtractor.class.getName()).log(Level.INFO, dump);
                //info.dump();
                MovieExtractionInfo minfo = new MovieExtractionInfo(dump);
                minfo.put(MovieExtractionInfo.ID_VIDEO_FORMAT, info.getFormatName());
                minfo.put(MovieExtractionInfo.ID_VIDEO_WIDTH, Integer.toString(info.getWidth()));
                minfo.put(MovieExtractionInfo.ID_VIDEO_HEIGHT, Integer.toString(info.getHeight()));

                //extractExIf(file);
                return minfo;
            } catch (Exception ex) {
                logger.error("could not extract picture information!", ex);
                return new MovieExtractionInfo("");
            }
        } else {
            throw new RuntimeException("no picture!");
        }
    }

    private void extractExIf(File file) throws ImageReadException, IOException {
        IImageMetadata metadata = Sanselan.getMetadata(file);
        Hashtable<String, String> exifInfomationTable = new Hashtable<String, String>();
        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            
            TiffField field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_MAKE);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_MODEL);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_CREATE_DATE);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_FNUMBER);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_ISO);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_EXPOSURE_TIME);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_WIDTH);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
            field = jpegMetadata.findEXIFValue(TiffConstants.EXIF_TAG_EXIF_IMAGE_LENGTH);
            exifInfomationTable.put(field.getTagName(), field.getValueDescription());
        }
    }
}
