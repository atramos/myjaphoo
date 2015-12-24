/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db.exif;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author mla
 */
public class ExifSerializer {

    private static HashMap<String,String> hexid2TagName = new HashMap<String, String>(); 
    static {
        for (TagInfo exiftag: ExifTagConstants.ALL_EXIF_TAGS) {
            hexid2TagName.put(Integer.toHexString(exiftag.tag), exiftag.name);
        }
    }
    
    
    static Map<String, String> deserialize(String serExifData) throws ParseException {
        if (serExifData == null) {
            return Collections.EMPTY_MAP;
        }
        HashMap<String,String> result = new HashMap<String, String>();
        
        JSONParser parser=new JSONParser();
        Map<String,String> map = (Map<String,String>) parser.parse(serExifData);
        for (Map.Entry<String,String> tag: hexid2TagName.entrySet()) {
            String value = map.get(tag.getKey());
            if (value != null) {
                result.put(tag.getValue(), value);
            }
        }
        return result;
    }

    static String serialize(Collection<TiffField> exifFields) {
        // prepare map which maps id to value:
        // (use id to reduce mem)
        HashMap<String,String> map = new HashMap<String, String>();
        for (TiffField field: exifFields) {
            map.put(Integer.toHexString(field.tag), field.getValueDescription());
        }
        String jsonText = JSONValue.toJSONString(map);
        return jsonText;
    }
    
}
