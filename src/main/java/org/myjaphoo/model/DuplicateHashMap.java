/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;

import org.myjaphoo.model.config.AppConfig;
import org.myjaphoo.model.db.MovieEntry;

import java.util.*;


/**
 *
 * Hashmap to build a map of duplicates of the movie entries.
 * It does this by the checksum.
 * The determination of duplicates could be suppressed by an internal option.
 * This is the case, if the calculation of checksums is disabled for the 
 * databse.
 * @author lang
 */
public class DuplicateHashMap {

    private Map<Long, ArrayList<MovieEntry>> chsumMap = null;
    private boolean suppressMode = false;

    private int numOfDups;
    private long wastedMem;

    private DuplicateHashMap(int initialSize) {
        chsumMap = new HashMap<Long, ArrayList<MovieEntry>>(initialSize);
    }

    public DuplicateHashMap(Collection<MovieEntry> entries) {


        this(entries.size() * 2);
        suppressMode = AppConfig.PRF_INTERNAL_DONOTBUILDCHECKSUM.getVal();
        if (!suppressMode) {
            for (MovieEntry entry : entries) {
                add(entry.getChecksumCRC32(), entry);
            }
        }
    }

    private void add(Long chSum, MovieEntry elem) {
        ArrayList<MovieEntry> listOfIdentical = chsumMap.get(chSum);
        if (listOfIdentical == null) {
            listOfIdentical = new ArrayList<MovieEntry>(3);
            chsumMap.put(chSum, listOfIdentical);
        } else {
            // a entry is already existing: count duplicate infos:
            numOfDups++;
            wastedMem += elem.getFileLength();
        }
        // add the movie node as child:
        listOfIdentical.add(elem);
    }

    public boolean hasDuplicates(Long checksum) {
        if (suppressMode) {
            return false;
        }
        ArrayList<MovieEntry> listOfIdentical = chsumMap.get(checksum);
        if (listOfIdentical == null) {
            return false;
        }
        return listOfIdentical.size() > 1; // because it contains itself
    }

    public boolean containsEntry(Long checksumCRC32) {
        return chsumMap.containsKey(checksumCRC32);
    }

    public ArrayList<MovieEntry> getDuplicatesForCheckSum(Long checksumCRC32) {
        return chsumMap.get(checksumCRC32);
    }

    public Collection<MovieEntry> getDuplicatesForMovie(MovieEntry entry) {
        ArrayList<MovieEntry> allDups = getDuplicatesForCheckSum(entry.getChecksumCRC32());
        if (allDups == null || allDups.size() <= 1) {
            return null;
        }
        ArrayList<MovieEntry> allDupsExceptMe = new ArrayList<MovieEntry>();
        for (MovieEntry dup : allDups) {
            if (!dup.equals(entry)) {
                allDupsExceptMe.add(dup);
            }
        }
        return allDupsExceptMe;
    }

    public long calcWastedMem() {
        return wastedMem;
    }

    public int calcDuplicationCount() {
        return numOfDups;
    }

    public void refreshEntry(MovieEntry refreshedEntry) {
        if (suppressMode) {
            return;
        }
        ArrayList<MovieEntry> entry = chsumMap.get(refreshedEntry.getChecksumCRC32());
        if (entry != null) {
            Iterator<MovieEntry> it = entry.iterator();
            while (it.hasNext()) {
                MovieEntry old = it.next();
                if (old.getId().equals(refreshedEntry.getId())) {
                    it.remove();
                    break;
                }
            }
            entry.add(refreshedEntry);
        }
    }
}
