/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser;

import org.myjaphoo.gui.movietree.grouping.GroupingExecutionContextImpl;
import org.myjaphoo.gui.movietree.grouping.PartialGrouper;
import org.myjaphoo.model.filterparser.AbstractParserTest;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;
import org.myjaphoo.model.filterparser.processing.FilterAndGroupingProcessor;
import org.myjaphoo.model.groupbyparser.expr.GroupingExpression;
import org.myjaphoo.model.grouping.GroupingExecutionContext;
import org.myjaphoo.model.grouping.Path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author mla
 */
public class GrouperTest extends AbstractParserTest {

    public void test01Simple() throws ParserException {
        tstOnEntry("(if path like bla {a} else {b})", "a");
    }

    public void test02Simple() throws ParserException {
        tstOnEntry("(if path like bla {a})", "a");
    }

    public void test02() throws ParserException {
        tstOnEntry("(if path like \".mpg\" {'film'} elseif path like \".jpg\" {pic }else {'not specified'})", "film");
        tstOnEntry("(if ismov() {film} elseif ispic() {pic })", "film");
        tstOnEntry("(if ismov() {'film'} elseif ispic() {pic }else {'not specified'})", "film");
    }

    public void test03Elseifpart() throws ParserException {
        tstOnEntry("(if path like ggll {'film'} elseif path like \".mpg\" {datiset }else {'not specified'})", "datiset");

    }

    public void test03Elsepart() throws ParserException {
        tstOnEntry("(if path like ggll {'film'} elseif path like tttt {datiset }else {'not specified'})", "not specified");
    }

    public void test04Elsepart() throws ParserException {
        tstOnEntry("(if path like ggll {'film'} else {'not specified'})", "not specified");
    }

    public void test04Functionconcat() throws ParserException {
        tstOnEntry("(if path like bla {concat(film, fff)})", "filmfff");
    }

    public void test06BugProblemFS117() throws ParserException {
        String grouping = "(if tag.parents is d and not(metatag.parents is h) {'H fehlt'} elseif tag.parents is d and not(metatag.parents like 'B') {'B Fehlt'}), Token ";
        GroupByParser p = new GroupByParser(null);
        List<GroupingExpression> groupExpr = p.parseGroupByExpression(grouping);
        FilterAndGroupingProcessor.filterEntries(Arrays.asList(testEntry), null, null, new PartialGrouper(groupExpr.get(0).createGrouper(), null, null));

    }

    protected void tstOnEntry(String strexpr, String... groups) throws ParserException {
        GroupByParser p = new GroupByParser(null);
        List<GroupingExpression> result = p.parseGroupByExpression(strexpr);
        final PartialGrouper partialGrouper = new PartialGrouper(result.get(0).createGrouper(), null, null);
        GroupingExecutionContext context = new GroupingExecutionContextImpl(null, new ExecutionContext());

        partialGrouper.preProcess(context);
        
        JoinedDataRow row = new JoinedDataRow();
        row.setEntry(testEntry);
        row.setToken(token);
        row.setMetaToken(CombinationResultGenerator.NULL_META_TOKEN);
        
        Path[] paths = partialGrouper.getPaths(row);

        Set<String> allExpected = new HashSet<String>(Arrays.asList(groups));
        for (Path path : paths) {
            if (!allExpected.remove(path.getLastPathName())) {
                fail("found " + path.getLastPathName() + " which is not expected! expected: " + allExpected);
            }
        }
        if (allExpected.size() > 0) {
            fail("still expected :" + allExpected);
        }
    }

    public void testGroupingByAFunction_DirWithLettersOnly() throws ParserException {
        tstOnEntry("(retainletters(dir))", "aaabbbccc");
    }

    public void testGroupingByIfAndFunctionConsequence() throws ParserException {
        tstOnEntry("(if ismov() {retainletters(dir)} else {'nomov'})", "aaabbbccc");
    }
}
