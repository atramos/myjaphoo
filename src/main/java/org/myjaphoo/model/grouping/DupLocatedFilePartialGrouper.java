/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.FileSubstitution;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Gruppiert nach files die auf das gleiche file zeigen.
 * Heisst, dass hier doppelte links in der db sind, die man entfernen sollte
 * gefunden werden.
 */
public class DupLocatedFilePartialGrouper extends AbstractPartialPathBuilder {

    private FileSubstitution fileSubstitution;
    private HashMap<String, ArrayList<MovieEntry>> localizedEntries = new HashMap<String, ArrayList<MovieEntry>>();

    @Override
    public void preProcess(GroupingExecutionContext context) {
        super.preProcess(context);
        fileSubstitution = context.getFileSubstitution();
        localizedEntries = new HashMap<String, ArrayList<MovieEntry>>(context.getAllEntriesToGroup().size() * 2);
        for (MovieEntry entry : context.getAllEntriesToGroup()) {
            String localizedPath = fileSubstitution.locateFileOnDrive(entry.getCanonicalPath());
            ArrayList<MovieEntry> entryList = localizedEntries.get(localizedPath);
            if (entryList == null) {
                entryList = new ArrayList<MovieEntry>();
                localizedEntries.put(localizedPath, entryList);
            }
            entryList.add(entry);
        }
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {
        String localizedPath = fileSubstitution.locateFileOnDrive(row.getEntry().getCanonicalPath());
        ArrayList<MovieEntry> entryList = localizedEntries.get(localizedPath);
        if (entryList != null && entryList.size() > 1) {
            String[] path = Helper.splitPathName(row.getEntry().getCanonicalDir());
            List<String> pathList = new ArrayList<String>(Arrays.asList(path));
            pathList.add(0, "--duplicated localized link");
            return new Path[]{new Path(GroupingDim.Dup_Links_ByLocating, pathList)};
        } else {
            String[] path = Helper.splitPathName(row.getEntry().getCanonicalDir());
            return new Path[]{new Path(GroupingDim.Dup_Links_ByLocating, path)};
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
