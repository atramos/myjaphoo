package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MovieEntry;

/**
 * Entry key based on checksum.
 */
public class EntryKeyChecksumBased extends AbstractEntryKey {


    private Long checksum;

    public EntryKeyChecksumBased(MovieEntry entry) {
        this.checksum = entry.getChecksumCRC32();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryKeyChecksumBased that = (EntryKeyChecksumBased) o;

        if (checksum != null ? !checksum.equals(that.checksum) : that.checksum != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return checksum != null ? checksum.hashCode() : 0;
    }
}
