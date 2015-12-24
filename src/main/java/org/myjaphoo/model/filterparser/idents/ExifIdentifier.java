package org.myjaphoo.model.filterparser.idents;

import org.apache.commons.lang.StringUtils;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * Marker class for all exif identifier.
 */
public abstract class ExifIdentifier extends EntryContextIdentifier {

    private TagInfo tag;

    public ExifIdentifier(TagInfo tag, String descr, String exampleUsage, ExprType exprType) {
        super(createExifIdentName(tag.name), descr, exampleUsage, exprType);
        this.tag = tag;
    }

    /**
     * the exif name that we use in the filter language for that identifier.
     * @param name
     * @return
     */
    public static String createExifIdentName(String name) {
        return "exif_" + StringUtils.lowerCase(name).replace(' ', '_'); //NOI18N
    }

    public String getTagName() {
        return tag.name;
    }
}