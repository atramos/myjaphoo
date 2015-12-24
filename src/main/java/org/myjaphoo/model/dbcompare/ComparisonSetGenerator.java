/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Generates the set of tuples for comparison.
 * Actually in an experimental stage.
 *
 * @author lang
 */
public class ComparisonSetGenerator {

    public static final MovieEntry NULL_ENTRY = new MovieEntry();

    static {
        NULL_ENTRY.setId(-1L);
        NULL_ENTRY.setName("not existent");
        NULL_ENTRY.setCanonicalDir("not existent");
        NULL_ENTRY.setChecksumCRC32(0L);

    }

    ComparisonContext context = new ComparisonContext();

    private boolean needTagRelation;

    public Iterable<DBDiffCombinationResult> createComparisonSetIterator(final CombinationSet leftSet, final CombinationSet rightSet) {

        this.needTagRelation = leftSet.isNeedsTagRelation();

        // build map for right side:
        HashMap<AbstractDataRowCompareKey, JoinedDataRow> mapForRightSide = new HashMap<AbstractDataRowCompareKey, JoinedDataRow>(rightSet.getMovies().size());
        for (JoinedDataRow cr : rightSet.getResult()) {
            mapForRightSide.put(context.createKey(cr), cr);
        }

        // build entry keymap for left side:
        HashMap<AbstractEntryKey, MovieEntry> leftSideEntrySet = buildEntryMap(leftSet);

        // build entry keymap for right side:
        HashMap<AbstractEntryKey, MovieEntry> rightSideEntrySet = buildEntryMap(rightSet);

        ArrayList<DBDiffCombinationResult> result = new ArrayList<DBDiffCombinationResult>(leftSet.getMovies().size() + rightSet.getMovies().size());
        // build results for all elements of the left side:
        for (JoinedDataRow leftElem : leftSet.getResult()) {
            AbstractDataRowCompareKey key = context.createKey(leftElem);
            JoinedDataRow rightElem = mapForRightSide.remove(key);
            if (rightElem != null) {
                result.add(new DBDiffCombinationResult(context, leftElem, rightElem));
                //mapForRightSide.remove(key);
            } else {
                JoinedDataRow nearCombination = findNearestCombination(leftElem, rightSideEntrySet);
                if (nearCombination != null) {
                    result.add(new DBDiffCombinationResult(context, leftElem, nearCombination));
                }
            }
        }
        // build results for all the left elements in the left side:
        for (JoinedDataRow rightElem : mapForRightSide.values()) {
            JoinedDataRow nearCombination = findNearestCombination(rightElem, leftSideEntrySet);
            if (nearCombination != null) {
                result.add(new DBDiffCombinationResult(context, nearCombination, rightElem));
            }
        }

        return result;
    }

    private HashMap<AbstractEntryKey, MovieEntry> buildEntryMap(CombinationSet combinations) {
        HashMap<AbstractEntryKey, MovieEntry> map = new HashMap<AbstractEntryKey, MovieEntry>(combinations.getMovies().size());
        for (MovieEntry leftEntry : combinations.getMovies()) {
            map.put(context.createEntryKey(leftEntry), leftEntry);
        }
        return map;
    }

    private static long counter = -1L;

    /**
     * Constructs a combination that is as similar as possible to the given combination element, but with elements
     * from the other compare set.
     * This is necessary, as the user wants to get a view, where the diffs are linked to "near" elements of the other set.
     * For us this means, find the appropriate entry, check, if the tag could also be found. We need to take care for
     * the metatag, as if this would be also present, whe would have a "equal" match, rather than a diff match.
     *
     * @param elem
     * @param entrySetOfOtherSet
     * @return a "near" combination or null, if no diff row at all should be constructed.
     */
    private JoinedDataRow findNearestCombination(JoinedDataRow elem, HashMap<AbstractEntryKey, MovieEntry> entrySetOfOtherSet) {
        MovieEntry other = entrySetOfOtherSet.get(context.createEntryKey(elem.getEntry()));
        Token token = CombinationResultGenerator.NULL_TOKEN;
        MetaToken metatok = CombinationResultGenerator.NULL_META_TOKEN;
        if (other == null) {
            // there is not even a partial match:
            return createNonExistentResultEntry(elem.getEntry());
        } else {
            // check if we need tag relation data:
            if (needTagRelation) {
                // check if tag matches also:
                if (elem.getToken() != null && elem.getToken() != CombinationResultGenerator.NULL_TOKEN) {
                    // try to find tag:
                    // TODO may be bottle neck: linear search:
                    for (Token otherTok : other.getTokens()) {
                        if (otherTok.getName().equals(elem.getToken().getName())) {
                            token = otherTok;
                            break;
                        }
                    }
                } else {
                    // we have no tag in the given elem; if we have now tags on the other side,
                    // then we suppress creating a diff element here, as it would be unnecessary information:
                    // the other side would create by the revert lookup later own diff elements with the tag information.
                    if (other.getTokens().size() > 0) {
                        return null;
                    }
                }
            }
            return new JoinedDataRow(other, token, metatok);
        }

    }

    /**
     * Creates a "pseudo" combination for cases where we do not match even a movie entry.
     * We create a pseudoe movie entry, otherwise the result would not be viewable, groupable and
     * may be not filterable in the ui.
     *
     * @param entry
     * @return
     */
    private JoinedDataRow createNonExistentResultEntry(MovieEntry entry) {
        // there is no partial match at all:

        // todo: intermediate test: simply a null entry.
        // you need therefore apropriate groupings that use values of
        // both left and right side.

        return new JoinedDataRow(NULL_ENTRY, CombinationResultGenerator.NULL_TOKEN, CombinationResultGenerator.NULL_META_TOKEN);
        // TEST: make individual "empty" result:
        /*
        MovieEntry nullEntry = new MovieEntry();
        nullEntry.setId(counter--);
        nullEntry.setName(entry.getName());
        nullEntry.setCanonicalDir(entry.getCanonicalDir());
        nullEntry.setChecksumCRC32(entry.getChecksumCRC32());
        return new JoinedDataRow(nullEntry, CombinationResultGenerator.NULL_TOKEN, CombinationResultGenerator.NULL_META_TOKEN);
        */
    }

}
