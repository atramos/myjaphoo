package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * Defines the method for comparison.
 */
public enum ComparisonMethod {

    COMPARE_PATH{
        @Override
        public AbstractDataRowCompareKey createKey(JoinedDataRow cr) {
            return new DataRowCompareKeyPathBased(cr);
        }

        @Override
        public AbstractEntryKey createEntryKey(MovieEntry entry) {
            return new EntryKeyPathBased(entry);
        }
    },
    COMPARE_CHECKSUM{
        @Override
        public AbstractDataRowCompareKey createKey(JoinedDataRow cr) {
            return new DataRowCompareKeyChecksumBased(cr);
        }

        @Override
        public AbstractEntryKey createEntryKey(MovieEntry entry) {
            return new EntryKeyChecksumBased(entry);
        }
    },
    COMPARE_PATH_AND_CHECKSUM{
        @Override
        public AbstractDataRowCompareKey createKey(JoinedDataRow cr) {
            return new DataRowCompareKeyPathAndChecksumBased(cr);
        }

        @Override
        public AbstractEntryKey createEntryKey(MovieEntry entry) {
            return new EntryKeyPathAndChecksumBased(entry);
        }
    };

    public abstract  AbstractEntryKey createEntryKey(MovieEntry entry);

    public abstract AbstractDataRowCompareKey createKey(JoinedDataRow cr);

}
