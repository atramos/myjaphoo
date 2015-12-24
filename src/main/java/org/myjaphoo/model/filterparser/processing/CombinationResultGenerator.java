/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.processing;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbcompare.CombinationSet;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generates a set of combination results for a database.
 * <p/>
 * it delivers (db1.movie x db1.tag x db1.metatag) tuples
 * as result.
 * <p/>
 * This is the base for filtering & grouping and also database comparison.
 * The crossproduct of movie * tag * metatag is only generated, if
 * its necessary (this depends, if tag or metatag is part of the expressions
 * used for filtering or grouping).
 *
 * @author lang
 */
public class CombinationResultGenerator {

    public static final Token NULL_TOKEN = new Token();
    public static final MetaToken NULL_META_TOKEN = new MetaToken();
    public static final List<Token> EMPTY_TOKEN_LIST = new ArrayList<Token>();
    public static final List<MetaToken> EMPTY_META_TOKEN_LIST = new ArrayList<MetaToken>();

    static {
        EMPTY_TOKEN_LIST.add(NULL_TOKEN);
        EMPTY_META_TOKEN_LIST.add(NULL_META_TOKEN);
    }

    /**
     * same as getIterator, but only in a simple version, which just produces
     * simply all combinations in a result list and then creates an iterator
     * from this.
     * The code is much clearer, but a possible drawback would be that
     * all combination objects would be created and holded in a list.
     */
    public CombinationSet getSillyIterator(Collection<MovieEntry> allMovies, ProcessingRequirementInformation pri1, ProcessingRequirementInformation pri2) {


        ArrayList<JoinedDataRow> result = new ArrayList<JoinedDataRow>(allMovies.size() * 10);

        boolean needsTagRelation = determineNeedOfTagRelation(pri1, pri2);
        boolean needsMetaTagRelation = determineNeedOfMetaTagRelation(pri1, pri2);

        for (MovieEntry entry : allMovies) {

            Collection<Token> assignedTokens = EMPTY_TOKEN_LIST;
            if (needsTagRelation && !entry.getTokens().isEmpty()) {
                assignedTokens = entry.getTokens();
            }

            for (Token token : assignedTokens) {

                Collection<MetaToken> assignedMetaTokens = EMPTY_META_TOKEN_LIST;
                if (needsMetaTagRelation && !token.getAssignedMetaTokens().isEmpty()) {
                    assignedMetaTokens = token.getAssignedMetaTokens();
                }
                for (MetaToken metaToken : assignedMetaTokens) {

                    result.add(new JoinedDataRow(entry, token, metaToken));
                }
            }
        }
        CombinationSet set = new CombinationSet(result, allMovies, needsTagRelation, needsMetaTagRelation);
        return set;
    }

    private static boolean determineNeedOfTagRelation(ProcessingRequirementInformation pri1, ProcessingRequirementInformation pri2) {
        // we need the tag relation if a expression needs it, or (indirectly) if metatags are needed.
        return pri1.needsTagRelation() || pri2.needsTagRelation() || determineNeedOfMetaTagRelation(pri1, pri2);
    }

    private static boolean determineNeedOfMetaTagRelation(ProcessingRequirementInformation pri1, ProcessingRequirementInformation pri2) {
        return pri1.needsMetaTagRelation() || pri2.needsMetaTagRelation();
    }

}
