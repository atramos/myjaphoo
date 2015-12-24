/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Gruppiert nach Vorschlägen für weitere Assignements für existierende Tokens.
 *
 * @author mla
 */
public class TokenAssignemntProposolerPartialGrouper extends CachingPartialPathBuilder<ProposalEntry> {

    /**
     * no assignment path.
     */
    public static final Path[] NO_PROPOSAL = new Path[]{new Path(GroupingDim.Token, "-- no proposal --")};
    private List<TagToCheck> tagList;

    public static String PATHMARKER_TAGPROPOSALGROUP = "TAG_PROPSALGROUP";

    static class TagToCheck {
        Token token;
        Pattern LenientPattern;
        Pattern wordMatchPattern;
        public String[] parts;

        /**
         * precheck the search string by simply checking the occurence of all "words".
         * This speeds up this grouping enormous for data where most of
         * the movies do not match a tag proposal.
         * This is because a first simpler part match is much faster than a regex pattern match.
         * With this check we can easily filter out all movies that do not match in any case.
         * All other need the regex pattern checking to check for exact word matches, etc.
         *
         * @param searchStr
         * @return
         */
        public boolean preCheck(String searchStr) {
            for (String part : parts) {
                if (!searchStr.contains(part)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void preProcess(GroupingExecutionContext context) {
        super.preProcess(context);
        this.tagList = buildList(context.getTagList());
    }

    private List<TagToCheck> buildList(List<Token> tagList) {
        ArrayList<TagToCheck> result = new ArrayList(tagList.size());
        for (Token token : tagList) {
            result.add(createTagToCheck(token));
        }
        return result;
    }

    private TagToCheck createTagToCheck(Token token) {
        TagToCheck ttc = new TagToCheck();
        ttc.token = token;
        String tokenName = token.getName().toLowerCase();
        ttc.parts = tokenName.split(" ");
        String regexLenient = createLenientPattern(tokenName);
        ttc.LenientPattern = Pattern.compile(regexLenient, Pattern.CASE_INSENSITIVE);

        String regexWordMatch = createWordmatchPattern(tokenName);
        ttc.wordMatchPattern = Pattern.compile(regexWordMatch, Pattern.CASE_INSENSITIVE);
        return ttc;
    }

    public static String createLenientPattern(String str) {
        String regex = "";
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        while (tokenizer.hasMoreTokens()) {
            String fragment = tokenizer.nextToken();
            regex += ".*" + Pattern.quote(fragment);
        }
        regex += ".*";
        return regex;
    }

    public static String createWordmatchPattern(String str) {
        String regex = "";
        boolean first = true;
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        while (tokenizer.hasMoreTokens()) {
            String fragment = tokenizer.nextToken();
            if (first) {
                first = false;
                regex += ".*[^a-zA-Z0-9]";
            } else {
                regex += "[^a-zA-Z0-9]";

            }
            regex += Pattern.quote(fragment);
        }
        regex += "[^a-zA-Z0-9].*";
        return regex;
    }

    public TokenAssignemntProposolerPartialGrouper() {
    }

    @Override
    public final Path[] getPaths(JoinedDataRow row) {

        ArrayList<ProposalEntry> proposals = new ArrayList<>();
        final Set<Token> tokenset = row.getEntry().getTokens();
        String searchStr = row.getEntry().getCanonicalPath().toLowerCase();
        // check for all tags that are not already assigned to that entry
        // if it is a proposal for that movie:
        for (TagToCheck tagCheck : tagList) {

            if (tokenset.isEmpty() || !tokenset.contains(tagCheck.token)) {
                if (tagCheck.preCheck(searchStr)) {
                    if (matchesPattern(tagCheck.wordMatchPattern, searchStr)) {
                        proposals.add(new ProposalEntry("full word phrase match", tagCheck.token));
                    } else if (matchesPattern(tagCheck.LenientPattern, searchStr)) {
                        proposals.add(new ProposalEntry("match", tagCheck.token));
                    }
                }
            }
        }

        if (proposals.isEmpty()) {
            return NO_PROPOSAL;
        }

        Path[] result = new Path[proposals.size()];
        for (int i = 0; i < proposals.size(); i++) {
            result[i] = getPath(proposals.get(i));
        }

        return result;
    }

    @Override
    protected final Path createPath(ProposalEntry proposalGroup) {
        PathAttributes[] attributes = new PathAttributes[2];
        attributes[0] = new PathAttributes(GroupingDim.Token.name(), proposalGroup.name, null, PathAttributes.PATHMARKER_NOTHING);
        attributes[1] = new PathAttributes(GroupingDim.Token.name(), proposalGroup.tag.getName(), null, PATHMARKER_TAGPROPOSALGROUP);
        return new Path(attributes);
    }

    private boolean matchesPattern(Pattern pattern, String searchStr) {
        Matcher matcher = pattern.matcher(searchStr);
        return matcher.matches();
    }

    @Override
    public boolean needsTagRelation() {
        return true;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return false;
    }
}
