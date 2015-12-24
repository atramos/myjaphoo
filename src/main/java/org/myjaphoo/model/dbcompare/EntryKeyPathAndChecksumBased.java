package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MovieEntry;

/**
 * Key class for entries.
 */
public class EntryKeyPathAndChecksumBased extends AbstractEntryKey {

    private String path;
    private Long checksum;

    public EntryKeyPathAndChecksumBased(MovieEntry entry) {
        this.path = entry.getCanonicalPath();
        this.checksum = entry.getChecksumCRC32();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryKeyPathAndChecksumBased that = (EntryKeyPathAndChecksumBased) o;

        if (checksum != null ? !checksum.equals(that.checksum) : that.checksum != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (checksum != null ? checksum.hashCode() : 0);
        return result;
    }
}
