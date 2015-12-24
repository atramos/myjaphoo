/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db.exif;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author mla
 */
@Embeddable
public class ExifData implements Serializable, Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(ExifData.class);
    // die gebräuchlichsten exif daten als attribute direkt aufnehmen:
    private String exifMake;
    private String exifModel;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date exifCreateDate;
    private String exifFnumber;
    private String exifIso;
    private String exifExposureTime;
    private Double longitude;
    private Double latitude;
    /** alle anderen werden als key/value pairs gehalten u. als string serialisiert gespeichert:
     *  diese map wird lazy initialisiert, da sie meist sowiso nicht gebraucht wird.
     */
    private transient Map<String, String> exifValues = null;
    /** die serialisierten Daten aller exifdaten (bis auf die häufigsten). */
    @Column(length = 4000)
    private String serExifData;

    public ExifData() {
    }

    public ExifData(Collection<TiffField> exifFields, Double longitude, Double latitude) throws ImageReadException, ParseException {
        this.longitude = longitude;
        this.latitude = latitude;
        exifValues = new HashMap<String, String>();
        ArrayList<TiffField> fieldstoSerialize = new ArrayList<TiffField>();
        for (TiffField field : exifFields) {
            if (field.tagInfo == TiffConstants.EXIF_TAG_MAKE) {
                exifMake = field.getValueDescription();
            } else if (field.tagInfo == TiffConstants.EXIF_TAG_MODEL) {
                exifModel = field.getValueDescription();
            } else if (field.tagInfo == TiffConstants.EXIF_TAG_CREATE_DATE) {
                exifCreateDate = parseDate(field.getValue());
            } else if (field.tagInfo == TiffConstants.EXIF_TAG_FNUMBER) {
                exifFnumber = field.getValueDescription();
            } else if (field.tagInfo == TiffConstants.EXIF_TAG_ISO) {
                exifIso = field.getValueDescription();
            } else if (field.tagInfo == TiffConstants.EXIF_TAG_EXPOSURE_TIME) {
                exifExposureTime = field.getValueDescription();
            } else {
                exifValues.put(field.getTagName(), field.getValueDescription());
                fieldstoSerialize.add(field);
            }
        }
        serExifData = ExifSerializer.serialize(fieldstoSerialize);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExifData exifData = (ExifData) o;

        if (exifCreateDate != null ? !exifCreateDate.equals(exifData.exifCreateDate) : exifData.exifCreateDate != null)
            return false;
        if (exifExposureTime != null ? !exifExposureTime.equals(exifData.exifExposureTime) : exifData.exifExposureTime != null)
            return false;
        if (exifFnumber != null ? !exifFnumber.equals(exifData.exifFnumber) : exifData.exifFnumber != null)
            return false;
        if (exifIso != null ? !exifIso.equals(exifData.exifIso) : exifData.exifIso != null) return false;
        if (exifMake != null ? !exifMake.equals(exifData.exifMake) : exifData.exifMake != null) return false;
        if (exifModel != null ? !exifModel.equals(exifData.exifModel) : exifData.exifModel != null) return false;
        if (latitude != null ? !latitude.equals(exifData.latitude) : exifData.latitude != null) return false;
        if (longitude != null ? !longitude.equals(exifData.longitude) : exifData.longitude != null) return false;
        if (serExifData != null ? !serExifData.equals(exifData.serExifData) : exifData.serExifData != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = exifMake != null ? exifMake.hashCode() : 0;
        result = 31 * result + (exifModel != null ? exifModel.hashCode() : 0);
        result = 31 * result + (exifCreateDate != null ? exifCreateDate.hashCode() : 0);
        result = 31 * result + (exifFnumber != null ? exifFnumber.hashCode() : 0);
        result = 31 * result + (exifIso != null ? exifIso.hashCode() : 0);
        result = 31 * result + (exifExposureTime != null ? exifExposureTime.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (serExifData != null ? serExifData.hashCode() : 0);
        return result;
    }

    public Object getExifValue(String tagName) {
        if (tagName.equals(TiffConstants.EXIF_TAG_MAKE.name)) {
            return exifMake;
        } else if (tagName.equals(TiffConstants.EXIF_TAG_MODEL.name)) {
            return exifModel;
        } else if (tagName.equals(TiffConstants.EXIF_TAG_CREATE_DATE.name)) {
            return exifCreateDate;
        } else if (tagName.equals(TiffConstants.EXIF_TAG_FNUMBER.name)) {
            return exifFnumber;
        } else if (tagName.equals(TiffConstants.EXIF_TAG_ISO.name)) {
            return exifIso;
        } else if (tagName.equals(TiffConstants.EXIF_TAG_EXPOSURE_TIME.name)) {
            return exifExposureTime;
        } else {
            return getExifValues().get(tagName);
        }
    }

    private void initSerializedValues() {
        try {
            exifValues = ExifSerializer.deserialize(getSerExifData());
        } catch (org.json.simple.parser.ParseException ex) {
            logger.error("error parsing exif json serialized data!", ex);
        }
    }

    private Date parseDate(Object value) throws ParseException {
        // mit etwas glück hat sanselan das schon als date gespeichert,
        // jedenfalls sieht der code so danach aus:
        if (value instanceof Date) {
            return (Date) value;
        } else {
            // try to parse the date:
            DateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            return df.parse(value.toString());
        }
    }

    /**
     * @return the exifMake
     */
    public String getExifMake() {
        return exifMake;
    }

    /**
     * @return the exifModel
     */
    public String getExifModel() {
        return exifModel;
    }

    /**
     * @return the exifCreateDate
     */
    public Date getExifCreateDate() {
        return exifCreateDate;
    }

    /**
     * @return the exifFnumber
     */
    public String getExifFnumber() {
        return exifFnumber;
    }

    /**
     * @return the exifIso
     */
    public String getExifIso() {
        return exifIso;
    }

    /**
     * @return the exifExposureTime
     */
    public String getExifExposureTime() {
        return exifExposureTime;
    }

    /**
     * @return the exifValues
     */
    public Map<String, String> getExifValues() {
        if (exifValues == null) {
            initSerializedValues();
        }
        return exifValues;
    }

    /**
     * @return the serExifData
     */
    public String getSerExifData() {
        return serExifData;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object theClone = super.clone();
        ((ExifData) theClone).exifValues = null; //reset the transient object
        return theClone;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }
}
