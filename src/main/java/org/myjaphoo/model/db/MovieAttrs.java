/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author mla
 */
@Embeddable
public class MovieAttrs implements Serializable, Cloneable {

    private Integer width;
    private Integer height;
    /** länge in sekunden. */
    private Integer length;
    private Integer fps;
    private Integer bitrate;
    private String format;

    /**
     * @return the width
     */
    public int getWidth() {
        return getInt(width);
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return getInt(height);
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return getInt(length);
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the fps
     */
    public int getFps() {
        return getInt(fps);
    }

    /**
     * @param fps the fps to set
     */
    public void setFps(int fps) {
        this.fps = fps;
    }

    /**
     * @return the bitrate
     */
    public int getBitrate() {
        return getInt(bitrate);
    }

    /**
     * @param bitrate the bitrate to set
     */
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    private int getInt(Integer i) {
        if (i == null) {
            return 0;
        } else {
            return i.intValue();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
