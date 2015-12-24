package org.myjaphoo.model.dbcompare;

import org.apache.commons.lang.ObjectUtils;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;

/**
 * Context holder object.
 */
public class ComparisonContext {

    private ComparisonMethod comparisonMethod = DatabaseComparison.getInstance().getComparisonMethod();

    public ComparisonContext() {}

    /**
     * copy the aspect value from one object to another . Static method here, as enums could not have baseclasses...
     * @param o1
     * @param o2
     */
    private static <T> void copy(ComparisonAspect<T> a, T o1, T o2) {
        a.setAspect(o2, a.getAspect(o1));
    }

    /**
     * copy the aspect value from one object to another
     * @param t1
     * @param t2
     */
    private static <T>  boolean compare(ComparisonAspect<T> a, T t1, T t2) {
        return ObjectUtils.equals(a.getAspect(t1), a.getAspect(t2));
    }
    private <T> String determineCompareDiffInfo(T o1, T o2, ComparisonAspect<T> compareAspects[], Object nullMarker) {

        if (determineCompareResult(o1, o2, compareAspects, nullMarker) == CompareResult.CHANGED) {
            StringBuilder b = new StringBuilder();
            for (ComparisonAspect aspect : compareAspects) {
                if (!compare(aspect, o1, o2)) {
                    b.append(aspect.getName() + " changed:" + aspect.getAspect(o1) + " <-> " + aspect.getAspect(o2));
                    b.append("<br>");
                }
            }
            return b.toString();
        }
        return "";
    }

    private <T> CompareResult determineCompareResult(T o1, T o2, ComparisonAspect<T> compareAspects[], Object nullMarker) {
        if (o1 == o2) {
            return CompareResult.EQUAL;
        } else if (o1 == nullMarker) {
            return CompareResult.NEW;
        } else if (o2 == nullMarker) {
            return CompareResult.REMOVED;
        } else {
            return compare(o1, o2, compareAspects);
        }
    }

    private <T> CompareResult compare(T o1, T o2, ComparisonAspect<T>[] compareAspects) {
        for (ComparisonAspect aspect : compareAspects) {
            if (!compare(aspect, o1, o2)) {
                return CompareResult.CHANGED;
            }
        }
        return CompareResult.EQUAL;
    }


    public CompareResult determineCompareResult(MovieEntry entry, MovieEntry cdbentry) {
        if (entry.getId() < 0) {
            return CompareResult.NEW;
        } else if (cdbentry.getId() < 0) {
            return CompareResult.REMOVED;
        }

        return determineCompareResult(entry, cdbentry, MovieEntryCompareAspect.values(), ComparisonSetGenerator.NULL_ENTRY);
    }

    public CompareResult determineCompareResult(Token token, Token cdbtoken) {
        return determineCompareResult(token, cdbtoken, TokenCompareAspect.values(), CombinationResultGenerator.NULL_TOKEN);
    }

    public CompareResult determineCompareResult(MetaToken metaToken, MetaToken cdbmetatoken) {
        return determineCompareResult(metaToken, cdbmetatoken, MetaTokenCompareAspect.values(), CombinationResultGenerator.NULL_META_TOKEN);
    }

    public String determineCompareDiffInfo(MetaToken metaToken, MetaToken cdbmetatoken) {
        return determineCompareDiffInfo(metaToken, cdbmetatoken, MetaTokenCompareAspect.values(), CombinationResultGenerator.NULL_META_TOKEN);
    }

    public String determineCompareDiffInfo(Token token, Token cdbtoken) {
        return determineCompareDiffInfo(token, cdbtoken, TokenCompareAspect.values(), CombinationResultGenerator.NULL_TOKEN);
    }

    public String determineCompareDiffInfo(MovieEntry entry, MovieEntry cdbentry) {
        return determineCompareDiffInfo(entry, cdbentry, MovieEntryCompareAspect.values(), ComparisonSetGenerator.NULL_ENTRY);
    }

    public AbstractDataRowCompareKey createKey(JoinedDataRow cr) {
        return comparisonMethod.createKey(cr);
    }

    public AbstractEntryKey createEntryKey(MovieEntry entry) {
        return comparisonMethod.createEntryKey(entry);
    }

    public void copyAttr(MovieEntry source, MovieEntry dest) {
        copy(source, dest, MovieEntryCompareAspect.values());
    }


    private <T> void copy(T o1, T o2, ComparisonAspect<T> compareAspects[]) {
        for (ComparisonAspect aspect : compareAspects) {
            copy(aspect, o1, o2);
        }
    }

    public void copyAttr(Token source, Token dest) {
        copy(source, dest, TokenCompareAspect.values());
    }

    public void copyAttr(MetaToken source, MetaToken dest) {
        copy(source, dest, MetaTokenCompareAspect.values());
    }
}
