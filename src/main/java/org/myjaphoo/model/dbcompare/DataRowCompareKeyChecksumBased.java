package org.myjaphoo.model.dbcompare;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * key class for joined data rows where the checksum of a movie entry gets used.
 */
public class DataRowCompareKeyChecksumBased extends AbstractDataRowCompareKey {

    private int hash;
    private Long checksum;
    private String tagname;
    private String mtagname;


    public DataRowCompareKeyChecksumBased(JoinedDataRow joinedDataRow) {
        super(joinedDataRow);
        this.checksum = joinedDataRow.getEntry().getChecksumCRC32();
        this.tagname = joinedDataRow.getToken().getName();
        this.mtagname = joinedDataRow.getMetaToken().getName();

        hash = checksum != null ? checksum.hashCode() : 0;
        hash = 31 * hash + (tagname != null ? tagname.hashCode() : 0);
        hash = 31 * hash + (mtagname != null ? mtagname.hashCode() : 0);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRowCompareKeyChecksumBased that = (DataRowCompareKeyChecksumBased) o;

        if (checksum != null ? !checksum.equals(that.checksum) : that.checksum != null) return false;
        if (mtagname != null ? !mtagname.equals(that.mtagname) : that.mtagname != null) return false;
        if (tagname != null ? !tagname.equals(that.tagname) : that.tagname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}