/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;
import org.myjaphoo.model.filterparser.syntaxtree.StringLiteral;
import org.myjaphoo.model.grouping.PartialPathBuilder;

import java.util.ArrayList;


/**
 * @author mla
 */
public class IfElseGrouping extends Grouping implements ProcessingRequirementInformation {

    /**
     * listet alle "if then elseif thens ... "
     * jedes if, then ist als boolean expression u. eine konsequence expression gepaart.
     * jedes elseif ist ebenfalls als boolean expression u. eine konsequence expression gepaart.
     * Faktisch ist also kein unterschied zw. dem IF teil, un den ELSEIF teilen.
     */
    private ArrayList<IfThen> ifThens = new ArrayList<IfThen>();
    /**
     * "else" konsequenz: ist eine expression, die auf einene string evaluiert. (i.d.r. ein string-literal.)
     */
    private Expression elseConsequence = new StringLiteral("EMPTY ELSE");

    public void addElseConsequence(Expression elseConsequence) {
        this.elseConsequence = elseConsequence;
    }

    @Override
    public boolean needsTagRelation() {
        if (elseConsequence.needsTagRelation()) {
            return true;
        }
        for (IfThen ifthen : ifThens) {
            if (ifthen.expr.needsTagRelation()) {
                return true;
            }
            if (ifthen.consequence.needsTagRelation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        if (elseConsequence.needsMetaTagRelation()) {
            return true;
        }
        for (IfThen ifthen : ifThens) {
            if (ifthen.expr.needsMetaTagRelation()) {
                return true;
            }
            if (ifthen.consequence.needsMetaTagRelation()) {
                return true;
            }
        }
        return false;
    }

    private static class IfThen {

        private AbstractBoolExpression expr;
        private Expression consequence;

        public IfThen(AbstractBoolExpression expr, Expression consequence) {
            this.expr = expr;
            this.consequence = consequence;
        }

        public void addDisplayExpr(StringBuilder b) {
            b.append("IF ");
            b.append(expr.getDisplayExprTxt());
            b.append("{");
            b.append(consequence.getDisplayExprTxt());
            b.append("}");
        }
    }

    public void addIfThen(AbstractBoolExpression expr, Expression consequence) {
        ifThens.add(new IfThen(expr, consequence));
    }

    @Override
    public PartialPathBuilder createPartialPathBuilder() {
        return new IfThenElsePartialPathBuilder(this);
    }

    @Override
    public String getDisplayExprTxt() {
        StringBuilder b = new StringBuilder();
        for (IfThen cond : ifThens) {
            cond.addDisplayExpr(b);
        }
        if (elseConsequence != null) {
            addDisplayExprForElse(b, elseConsequence);
        }
        return b.toString();
    }

    private static void addDisplayExprForElse(StringBuilder b, Expression elseConsequence) {
        b.append(" ELSE ");
        b.append("{");
        b.append(elseConsequence.getDisplayExprTxt());
        b.append("}");
    }

    public static class ConsequenceReturnValue {
        private String consequenceLiteral;

        private IfThen matchedIfThen;
        private Expression elseConsequence;

        public ConsequenceReturnValue(String consequenceLiteral, IfThen matchedIfThen) {
            this.consequenceLiteral = consequenceLiteral;
            this.matchedIfThen = matchedIfThen;
        }

        public ConsequenceReturnValue(String consequenceLiteral, Expression elseConsequence) {
            this.consequenceLiteral = consequenceLiteral;
            this.elseConsequence = elseConsequence;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ConsequenceReturnValue that = (ConsequenceReturnValue) o;

            if (!consequenceLiteral.equals(that.consequenceLiteral)) return false;
            if (elseConsequence != null ? !elseConsequence.equals(that.elseConsequence) : that.elseConsequence != null)
                return false;
            if (matchedIfThen != null ? !matchedIfThen.equals(that.matchedIfThen) : that.matchedIfThen != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = consequenceLiteral.hashCode();
            result = 31 * result + (matchedIfThen != null ? matchedIfThen.hashCode() : 0);
            result = 31 * result + (elseConsequence != null ? elseConsequence.hashCode() : 0);
            return result;
        }

        public String getConsequenceLiteral() {
            return consequenceLiteral;
        }

        public String getDisplayExprTxt() {
            StringBuilder b = new StringBuilder();
            if (matchedIfThen != null) {
                matchedIfThen.addDisplayExpr(b);
            } else if (elseConsequence != null) {
                addDisplayExprForElse(b, elseConsequence);
            }
            return b.toString();
        }
    }

    public ConsequenceReturnValue determineConsequenceLiteral(ExecutionContext context, JoinedDataRow row) {
        for (IfThen cond : ifThens) {
            if (cond.expr.evaluate(context, row).asBool()) {
                return new ConsequenceReturnValue(cond.consequence.evaluate(context, row).asString(), cond);
            }
        }
        if (elseConsequence != null) {
            return new ConsequenceReturnValue(elseConsequence.evaluate(context, row).asString(), elseConsequence);
        }
        return new ConsequenceReturnValue("nicht festgelegt", (Expression) null);
    }
}
