/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import org.myjaphoo.model.db.Rating;

/**
 *
 * @author mla
 */
@XmlRootElement(name = "Export")
@XmlAccessorType(XmlAccessType.FIELD)
public class WmInfo {

    public long checksum;
    public byte[] thumb1;
    public byte[] thumb2;
    public byte[] thumb3;
    public byte[] thumb4;
    public byte[] thumb5;

    public String[] tokens;
    public Rating rating;

    public Integer width;
    public Integer height;
    /** länge in sekunden. */
    public Integer length;
    public Integer fps;
    public Integer bitrate;
    public String format;
    
}
