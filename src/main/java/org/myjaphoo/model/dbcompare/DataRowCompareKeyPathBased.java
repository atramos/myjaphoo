package org.myjaphoo.model.dbcompare;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * data row compare key based on path.
 */
public class DataRowCompareKeyPathBased extends AbstractDataRowCompareKey {

    private int hash;
    private String path;
    private String tagname;
    private String mtagname;


    public DataRowCompareKeyPathBased(JoinedDataRow joinedDataRow) {
        super(joinedDataRow);
        this.path = joinedDataRow.getEntry().getCanonicalPath();
        this.tagname = joinedDataRow.getToken().getName();
        this.mtagname = joinedDataRow.getMetaToken().getName();

        hash = path != null ? path.hashCode() : 0;
        hash = 31 * hash + (tagname != null ? tagname.hashCode() : 0);
        hash = 31 * hash + (mtagname != null ? mtagname.hashCode() : 0);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRowCompareKeyPathBased that = (DataRowCompareKeyPathBased) o;

        if (mtagname != null ? !mtagname.equals(that.mtagname) : that.mtagname != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (tagname != null ? !tagname.equals(that.tagname) : that.tagname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
