package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MovieEntry;

/**
 * Key class for entries.
 */
public class EntryKeyPathBased extends AbstractEntryKey {
    private String path;

    public EntryKeyPathBased(MovieEntry entry) {
        this.path = entry.getCanonicalPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryKeyPathBased entryKey = (EntryKeyPathBased) o;

        if (path != null ? !path.equals(entryKey.path) : entryKey.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
