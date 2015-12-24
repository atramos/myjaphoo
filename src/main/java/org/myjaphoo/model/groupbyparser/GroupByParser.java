package org.myjaphoo.model.groupbyparser;

import groovy.lang.Closure;
import org.antlr.v4.runtime.CommonTokenStream;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.filterparser.ParserErrorListener;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.Substitution;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.syntaxtree.SpecialCharSymbol;
import org.myjaphoo.model.filterparser.visitors.AggregationTesterVisitor;
import org.myjaphoo.model.grammars.FilterLanguageParser;
import org.myjaphoo.model.groovyparser.GroovyClosureGrouping;
import org.myjaphoo.model.groovyparser.GroovyFilterParser;
import org.myjaphoo.model.groupbyparser.expr.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GroupByParserAntlrVersion
 * @author mla
 * @version $Id$
 */
public class GroupByParser extends FilterParser {

    private AggregationTesterVisitor aggTester = new AggregationTesterVisitor();

    public GroupByParser(Map<String, Substitution> substitutions) {
        super(substitutions);
    }

    protected GroupByParser(Map<String, Substitution> substitutions, int callLevel) throws ParserException {
        super(substitutions, callLevel);
    }


    public List<GroupingExpression> parseGroupByExpression(String txt) throws ParserException {
        CommonTokenStream tokens = FilterParser.createTokenStream(txt);
        FilterLanguageParser parser = new FilterLanguageParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        FilterLanguageParser.GroupContext tree = parser.group();
        return parse(tree, txt);
    }

    private List<GroupingExpression> parse(FilterLanguageParser.GroupContext tree, String txt) {
        List<GroupingExpression> result = new ArrayList<>();
        if (tree.GroovyCode() != null) {
            // remove "groovy" from the txt:
            String textWithoutGroovyKeyWord = txt.substring(6);
            result.add(doGroovyCode(textWithoutGroovyKeyWord));
        } else {
            for (FilterLanguageParser.GroupInstructionContext context : tree.groupInstruction()) {
                result.add(parseGroupInstruction(context));
            }
        }
        return result;
    }

    private GroupingExpression parseGroupInstruction(FilterLanguageParser.GroupInstructionContext groupInstructionContext) {
        GroupingExpression expr = new GroupingExpression();
        expr.setText(groupInstructionContext.getText());
        for (FilterLanguageParser.GroupExprPartContext groupExprPartContext : groupInstructionContext.groupExprList().groupExprPart()) {
            parseGrouping(expr, groupExprPartContext);
        }
        if (groupInstructionContext.havingClause() != null) {
            expr.setHavingClause(new AggregatedExpression(parse(groupInstructionContext.havingClause().boolexpr())));
            expr.getHavingClause().semanticCheckAggregationExpression();
        }

        return expr;
    }

    private void parseGrouping(GroupingExpression groupingExpression, FilterLanguageParser.GroupExprPartContext groupExprPartContext) {
        if (groupExprPartContext.qualifier() != null) {
            // try if it is a standard grouping:
            if (groupExprPartContext.qualifier().ID().size() == 1) {
                String idName = groupExprPartContext.qualifier().ID(0).getText();
                // todo make nicer.
                SpecialCharSymbol standGroup = StandardGroupingSymbols.mapIdent(idName);
                if (standGroup != null && standGroup instanceof StandardGroupingSymbols) {
                    groupingExpression.addGrouping(new StandardGrouping((StandardGroupingSymbols) standGroup));
                    return;
                }
            }
            // ok, so it must be an identifier (evtl. qualified):
            Expression expr = super.parseQualifier(groupExprPartContext.qualifier());
            groupingExpression.addGrouping(new StringExpressionGrouping(expr));
        } else {
            parseGroupExpr(groupingExpression, groupExprPartContext.groupExpr());
        }
    }

    private void parseGroupExpr(GroupingExpression groupingExpression, FilterLanguageParser.GroupExprContext groupExprContext) {
        if (groupExprContext.groupTerm() != null) {
            Expression expr = super.parse(groupExprContext.groupTerm().expr());
            boolean isAggregationExpr = aggTester.visit(expr);
            if (!isAggregationExpr) {
                groupingExpression.addGrouping(new StringExpressionGrouping(expr));
            } else {
                groupingExpression.addAggregation(new Aggregation(new AggregatedExpression(expr)));
            }
        } else {
            groupingExpression.addGrouping(parseIfStmt(groupExprContext.ifStmt()));
        }
    }

    private Grouping parseIfStmt(FilterLanguageParser.IfStmtContext ifStmtContext) {
        IfElseGrouping g = new IfElseGrouping();
        AbstractBoolExpression condition = super.parse(ifStmtContext.boolexpr());
        Expression thenExpr = super.parse(ifStmtContext.thenStmt().expr());
        g.addIfThen(condition, thenExpr);

        for (FilterLanguageParser.ElseIfStmtContext elseIfStmtContext : ifStmtContext.elseIfStmt()) {
            AbstractBoolExpression elseCond = super.parse(elseIfStmtContext.boolexpr());
            Expression elseThenExpr = super.parse(elseIfStmtContext.thenStmt().expr());
            g.addIfThen(elseCond, elseThenExpr);
        }
        if (ifStmtContext.elseStmt() != null) {
            Expression elseExpr = super.parse(ifStmtContext.elseStmt().thenStmt().expr());
            g.addElseConsequence(elseExpr);
        }
        return g;
    }

    private GroupingExpression doGroovyCode(String txt) {
        GroovyFilterParser gfp = new GroovyFilterParser();
        GroovyFilterParser.GroovyScriptWrapper script = gfp.createScript(txt);
        script.runScript();
        // get the grouper definition:
        Closure[] grouper = (Closure[]) script.getGrouper();
        // and produce a grouping expression from it:
        GroupingExpression expr = new GroupingExpression();
        for (Closure closure : grouper) {
            expr.addGrouping(new GroovyClosureGrouping(closure));
        }
        return expr;
    }


    public void checkCorrectness(String txt) throws ParserException {
        parseGroupByExpression(txt);
    }

}
