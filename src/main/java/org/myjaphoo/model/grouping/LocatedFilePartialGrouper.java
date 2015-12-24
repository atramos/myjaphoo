/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.FileSubstitution;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Gruppiert nach lokalisierten Files. Also nach den Pfadnamen, wo die files wirklich auf dem Datenträger
 * gefunden werden.
 */
public class LocatedFilePartialGrouper extends AbstractPartialPathBuilder {

    private FileSubstitution fileSubstitution;

    @Override
    public void preProcess(GroupingExecutionContext context) {
        super.preProcess(context);
        fileSubstitution = context.getFileSubstitution();
    }

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        return getPaths(row.getEntry().getCanonicalDir());
    }

    private Path[] getPaths(String fullPathName) {

        String localizedPath = fileSubstitution.locateFileOnDrive(fullPathName);

        if (localizedPath != null) {
            String[] path = Helper.splitPathName(localizedPath);
            return new Path[]{new Path(GroupingDim.LocatedDir, path)};
        } else {

            String[] path = Helper.splitPathName(fullPathName);
            List<String> pathList = new ArrayList<String>(Arrays.asList(path));
            pathList.add(0, "unlocalizable");
            return new Path[]{new Path(GroupingDim.LocatedDir, pathList)};
        }
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
