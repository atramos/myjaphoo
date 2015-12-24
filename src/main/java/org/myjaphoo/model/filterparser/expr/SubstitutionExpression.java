package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.List;

/**
 * A expression, that was build by a substitution. Therefore it only contains the inner substituted expression
 * and the name of the substitution (only for information purpose for later scanning the ast tree).
 *
 * @author lang
 * @version $Id$
 */
public class SubstitutionExpression extends AbstractBoolExpression {

    private AbstractBoolExpression substitution;
    private String name;

    public SubstitutionExpression(String name, AbstractBoolExpression substitution) {
        this.substitution = substitution;
        this.name = name;
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return substitution.evaluate(context, row);
    }

    @Override
    public List<Expression> getChildren() {
        return substitution.getChildren();
    }

    @Override
    public String getDisplayExprTxt() {
        return "$'" + name + "'";
        //return substitution.getDisplayExprTxt();
    }

    @Override
    public boolean needsTagRelation() {
        return substitution.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return substitution.needsMetaTagRelation();
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}