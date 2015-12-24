/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.db.MovieAttrs;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mla
 */
public class MovieExtractionInfo {

    private String unparsedResult;
    private HashMap<String, String> idMap = new HashMap<String, String>();
    public static final String ID_VIDEO_FORMAT = "ID_VIDEO_FORMAT";
    public static final String ID_VIDEO_BITRATE = "ID_VIDEO_BITRATE";
    public static final String ID_VIDEO_WIDTH = "ID_VIDEO_WIDTH";
    public static final String ID_VIDEO_HEIGHT = "ID_VIDEO_HEIGHT";
    public static final String ID_VIDEO_FPS = "ID_VIDEO_FPS";
    public static final String ID_LENGTH = "ID_LENGTH";
    /*

    ID_VIDEO_FORMAT=H264
    ID_VIDEO_BITRATE=668424
    ID_VIDEO_WIDTH=480
    ID_VIDEO_HEIGHT=360
    ID_VIDEO_FPS=25.000
    ID_LENGTH=125.62
     */

    public MovieExtractionInfo(String unparsedResult) {
        this.unparsedResult = unparsedResult;
    }

    /**
     * @return the unparsedResult
     */
    public String getUnparsedResult() {
        return unparsedResult;
    }

    public void put(String key, String value) {
        idMap.put(key, value);
    }

    public Map<String, String> getMap() {
        return idMap;
    }

    private int getInt(String id) {
        String v = idMap.get(id);
        if (v != null) {
            return Integer.parseInt(v);
        } else {
            return 0;
        }
    }

    private double getDbl(String id) {
        String v = idMap.get(id);
        if (v != null) {
            return Double.parseDouble(v);
        } else {
            return 0.0;
        }
    }

    public MovieAttrs genMovieAttrs() {
        MovieAttrs attrs = new MovieAttrs();
        String format = idMap.get(ID_VIDEO_FORMAT);
        double bitRate = getDbl(ID_VIDEO_BITRATE);
        int width = getInt(ID_VIDEO_WIDTH);
        int height = getInt(ID_VIDEO_HEIGHT);
        double fps = getDbl(ID_VIDEO_FPS);
        double length = getDbl(ID_LENGTH);
        attrs.setFormat(format);
        attrs.setFps((int) fps);
        attrs.setWidth(width);
        attrs.setHeight(height);
        attrs.setLength((int) length);
        attrs.setBitrate((int) bitRate);
        return attrs;
    }

}
