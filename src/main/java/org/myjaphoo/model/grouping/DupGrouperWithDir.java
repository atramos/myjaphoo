/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.DuplicateHashMap;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.util.Helper;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author mla
 */
public class DupGrouperWithDir extends AbstractPartialPathBuilder {

    private DuplicateHashMap duplicates;

    @Override
    public void preProcess(GroupingExecutionContext context) {
        duplicates = new DuplicateHashMap(context.getAllEntriesToGroup());
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {

        // check, if this is a node, where duplicates exists:
        if (!duplicates.hasDuplicates(row.getEntry().getChecksumCRC32())) {
            return null;
        }
        ArrayList<MovieEntry> dups =
                duplicates.getDuplicatesForCheckSum(row.getEntry().getChecksumCRC32());

        // alle hinzufügen mit ihrem jeweiligen verzeichnispfad u. all die duplikate
        // bei jedem unterhalb ihres pfades als duplikate hinzufügen:
        int resultindex = 0;
        Path[] result = new Path[dups.size()];
        ArrayList<String> dirPath = createDirPath(row.getEntry());
        dirPath.add("dup of '" + row.getEntry().getName() + "'"); // name als weiteres "dir" element hinzufügen
        result[resultindex++] = new Path(GroupingDim.Duplicates, dirPath);

        // an alle "anderen" identischen als identical unterhalb in deren verzeichnis pfad hinzufügen:
        for (MovieEntry duplicMovie : dups) {
            if (!duplicMovie.getId().equals(row.getEntry().getId())) {
                dirPath = createDirPath(duplicMovie);
                dirPath.add("dup of '" + duplicMovie.getName() + "'"); // name als weiteres "dir" element hinzufügen
                dirPath.add("Duplikate"); // u. unter dem namen eine gruppierung aller duplikate
                result[resultindex++] = new Path(GroupingDim.Duplicates, dirPath);
            }
        }

        return result;
    }

    private ArrayList<String> createDirPath(MovieEntry entry) {
        String[] dirpath = Helper.splitPathName(entry.getCanonicalDir());
        ArrayList<String> dirPath = new ArrayList<String>(Arrays.asList(dirpath));
        return dirPath;
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
