/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Gruppiert nach dem exif create date hierarchisch nach jahr/monat/tag.
 * @author mla
 */
public class ExifCreateDatePartialGrouper extends AbstractPartialPathBuilder {

    /** no assignment path. */
    public static final Path[] NO_DATE = new Path[]{new Path(GroupingDim.ExifCreateDate, "-- no info --")};
    /** to build the structure year/month/day. */
    private DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        Date date = row.getEntry().getExifData().getExifCreateDate();
        if (date == null) {
            return NO_DATE;
        }
        Path[] result = new Path[1];

        String[] tokHierarchy = buildHierarchy(date);
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.ExifCreateDate, tokHierarchy);
        result[0] = new Path(attributes);

        return result;
    }

    private String[] buildHierarchy(Date date) {
        String formatted = df.format(date);
        String[] splitted = StringUtils.split(formatted, '/');
        return splitted;
    }

    @Override
    public boolean needsTagRelation() {
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return false;
    }
}
