package org.myjaphoo.model.filterparser;

import org.myjaphoo.MovieInMemoryFilter;
import org.myjaphoo.model.filterparser.expr.*;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.idents.FixIdentifier;
import org.myjaphoo.model.filterparser.idents.QualifierIdentExpression;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.syntaxtree.Literal;

import java.util.ArrayList;
import java.util.List;

/**
 * FilterBrickParser
 * Parses a filter expression back into bricks used in the filter user interface.
 *
 * @author lang
 * @version $Id$
 */
public class FilterBrickParser {

    public static class IdentFilterBrick {

        public FixIdentifier ident;
        public String literalStr;
        public AbstractOperator operator;
    }

    public static class AttrFilterBrick {
        public Function attrFunction;
        public String literalStr;
        public AbstractOperator operator;
        public String attribute;
    }


    public static class ParsedFilterBricks {

        public ArrayList<IdentFilterBrick> bricks = new ArrayList<>();

        public ArrayList<AttrFilterBrick> attrbricks = new ArrayList<>();

        public ArrayList<String> bookmarkBricks = new ArrayList<>();
    }

    /**
     * parses a expression that denotes saved filter bricks into the respective filter brick information.
     * This is then used to display into the filter UI.
     *
     * @param preFilterExpr
     * @return
     */
    public ParsedFilterBricks parseFilterBrickExpression(String preFilterExpr) {
        Expression expr = MovieInMemoryFilter.parseExpr(preFilterExpr);
        return parseFilterBrickExpression(expr);
    }

    public ParsedFilterBricks parseFilterBrickExpression(Expression expr) {
        ParsedFilterBricks result = new ParsedFilterBricks();

        List<BoolExpression> allTerms = Expressions.findAllExprParts(expr, BoolExpression.class);
        for (BoolExpression term : allTerms) {
            if (Expressions.findAllExprParts(term, FunctionCall.class).size() > 0) {
                AttrFilterBrick brick = createAttrFilterBrick(term);
                if (brick != null) {
                    result.attrbricks.add(brick);
                }
            } else {
                IdentFilterBrick brick = createBrick(term);
                if (brick != null) {
                    result.bricks.add(brick);
                }
            }
        }
        // special bricks: bookmarks:
        List<SubstitutionExpression> substs = Expressions.findAllExprParts(expr, SubstitutionExpression.class, new Expressions.Predicate() {
            @Override
            public boolean eval(Expression expr) {
                // stop on first level of subst expr. to not scan into nested substitutions
                return !(expr instanceof SubstitutionExpression);
            }
        });
        for (SubstitutionExpression subst : substs) {
            result.bookmarkBricks.add(subst.getName());
        }
        return result;
    }

    private IdentFilterBrick createBrick(BoolExpression term) {
        QualifierIdentExpression qualifierIdentExpression = Expressions.findFirstDeepSearch(term.getExpr1(), QualifierIdentExpression.class);
        Literal literal = Expressions.findFirstDeepSearch(term.getExpr2(), Literal.class);
        if (qualifierIdentExpression != null && literal != null) {
            FixIdentifier ident = qualifierIdentExpression.getIdent();
            String literalStr = literal.convertToString();
            IdentFilterBrick brick = new IdentFilterBrick();
            brick.ident = ident;
            brick.literalStr = literalStr;
            brick.operator = term.getComparison();
            return brick;
        } else {
            return null;
        }
    }

    private AttrFilterBrick createAttrFilterBrick(BoolExpression term) {
        FunctionCall funcCall = Expressions.findFirstDeepSearch(term.getExpr1(), FunctionCall.class);
        Function function = funcCall.getFunction();

        Literal literal = Expressions.findFirstDeepSearch(term.getExpr2(), Literal.class);

        Literal attribute = Expressions.findFirstDeepSearch(funcCall, Literal.class);
        if (funcCall != null && function != null && attribute != null) {
            String literalStr = literal.convertToString();
            AttrFilterBrick brick = new AttrFilterBrick();
            brick.attrFunction = function;
            brick.literalStr = literalStr;
            brick.attribute = attribute.convertToString();
            brick.operator = term.getComparison();
            return brick;
        } else {
            return null;
        }
    }
}
