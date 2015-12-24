/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * Contains the result of  an advanced database comparison.
 * Contains a difference-combination between this and
 * the other database. The information can be interpreted as
 * a single instruction to merge the difference.
 * @author lang
 */
public class DBDiffCombinationResult extends JoinedDataRow {

    private JoinedDataRow t1;
    private JoinedDataRow t2;

    private CompareResult diffEntry;

    private CompareResult diffTag;

    private CompareResult diffMetaTag;

    private ComparisonContext context;

    public DBDiffCombinationResult(ComparisonContext context, JoinedDataRow t1, JoinedDataRow t2) {
        super(t1.getEntry(), t1.getToken(), t1.getMetaToken());
        this.context = context;
        this.t1 = t1;
        this.t2 = t2;
    }


    public MovieEntry getCDBEntry() {
        return getT2().getEntry();
    }

    public Token getCDBToken() {
        return getT2().getToken();
    }

    public MetaToken getCDBMetaToken() {
        return getT2().getMetaToken();
    }

    public CompareResult getDiffEntry() {
        if (diffEntry == null) {
            diffEntry = getContext().determineCompareResult(getEntry(), getCDBEntry());
        }
        return diffEntry;
    }

    public void setDiffEntry(CompareResult diffEntry) {
        this.diffEntry = diffEntry;
    }

    public CompareResult getDiffTag() {
        if (diffTag == null) {
            diffTag = getContext().determineCompareResult(getToken(), getCDBToken());
        }
        return diffTag;
    }

    public String getDiffInfoTag() {
        return getContext().determineCompareDiffInfo(getToken(), getCDBToken());
    }

    public String getDiffInfoMetaTag() {
        return getContext().determineCompareDiffInfo(getMetaToken(), getCDBMetaToken());
    }

    public String getDiffInfoEntry() {
        return getContext().determineCompareDiffInfo(getEntry(), getCDBEntry());
    }

    public void setDiffTag(CompareResult diffTag) {
        this.diffTag = diffTag;
    }

    public CompareResult getDiffMetaTag() {
        if (diffMetaTag == null) {
            diffMetaTag = getContext().determineCompareResult(getMetaToken(), getCDBMetaToken());
        }
        return diffMetaTag;
    }

    public void setDiffMetaTag(CompareResult diffMetaTag) {
        this.diffMetaTag = diffMetaTag;
    }

    public JoinedDataRow getT2() {
        return t2;
    }

    public ComparisonContext getContext() {
        return context;
    }
}
