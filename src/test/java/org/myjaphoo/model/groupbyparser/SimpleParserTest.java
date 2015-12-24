/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser;

import junit.framework.TestCase;
import org.myjaphoo.model.filterparser.ParserException;

/**
 *
 * @author mla
 */
public class SimpleParserTest extends TestCase {

    public void testParsing1() throws ParserException {
        GroupByParser p = new GroupByParser(null);
        p.parseGroupByExpression("group by token");
        p.parseGroupByExpression("groupby token");
        p.parseGroupByExpression("by token");
        p.parseGroupByExpression("token");
    }

    public void testParsing2() throws ParserException {
        GroupByParser p = new GroupByParser(null);
        p.parseGroupByExpression("group by tokenhierarchy, directory");
        p.parseGroupByExpression("group by tokenhierarchy; directory");
        p.parseGroupByExpression("tokenhierarchy; directory");
    }

    public void testIfThen1() throws ParserException {
        GroupByParser p = new GroupByParser(null);
        p.parseGroupByExpression("group by (if path like blubber {blubber} else {blabber})");
        p.parseGroupByExpression("group by tokenhierarchy, (if path like blubber {blubber} else {blabber})");

    }

    public void testIfThen2() throws ParserException {
        GroupByParser p = new GroupByParser(null);
        p.parseGroupByExpression("(if path like blubber {blubber} elseif x like 'bab'  {blabber} elseif size > 400 {'bigsize'} else {'5'})");

    }
}
