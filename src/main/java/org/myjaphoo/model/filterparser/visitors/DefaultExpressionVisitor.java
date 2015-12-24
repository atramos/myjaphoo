package org.myjaphoo.model.filterparser.visitors;

import org.myjaphoo.model.filterparser.expr.*;
import org.myjaphoo.model.filterparser.idents.QualifierIdentExpression;
import org.myjaphoo.model.filterparser.syntaxtree.*;
import org.myjaphoo.model.groupbyparser.expr.AggregatedExpression;

/**
 * DefaultExpressionVisitor
 * @author mla
 * @version $Id$
 */
public class DefaultExpressionVisitor<T> implements ExpressionVisitor<T> {

    @Override
    public T visit(LiteralList literalList) {
        return doIteration(literalList);
    }

    @Override
    public T visit(GroovyFunctionCall groovyFunctionCall) {
        return doIteration(groovyFunctionCall);
    }

    @Override
    public T visit(Term term) {
        return doIteration(term);
    }

    @Override
    public T visit(NullLiteral nullLiteral) {
        return doIteration(nullLiteral);
    }

    @Override
    public T visit(NumberLiteral numberLiteral) {
        return doIteration(numberLiteral);
    }

    @Override
    public T visit(Constant constant) {
        return doIteration(constant);
    }

    @Override
    public T visit(DateLiteral dateLiteral) {
        return doIteration(dateLiteral);
    }

    @Override
    public T visit(StringLiteral stringLiteral) {
        return doIteration(stringLiteral);
    }

    @Override
    public T visit(QualifierIdentExpression qualifierIdentExpression) {
        return doIteration(qualifierIdentExpression);
    }

    @Override
    public T visit(FunctionCall functionCall) {
        return doIteration(functionCall);
    }

    @Override
    public T visit(BoolExpression boolExpression) {
        return doIteration(boolExpression);
    }

    @Override
    public T visit(WrappedBoolExpression wrappedBoolExpression) {
        return doIteration(wrappedBoolExpression);
    }

    @Override
    public T visit(NegatedBoolExpression negatedBoolExpression) {
        return doIteration(negatedBoolExpression);
    }

    @Override
    public T visit(AggregatedExpression aggregatedExpression) {
        return doIteration(aggregatedExpression);
    }

    @Override
    public T visit(GroovyBoolExpression groovyBoolExpression) {
        return doIteration(groovyBoolExpression);
    }

    @Override
    public T visit(SubstitutionExpression substitutionExpression) {
        return doIteration(substitutionExpression);
    }

    @Override
    public T visit(BoolTerm boolTerm) {
        return doIteration(boolTerm);
    }

    @Override
    public T visit(Expression expression) {
        return doIteration(expression);
    }

    public T doIteration(Expression expression) {
        for (Expression child : expression.getChildren()) {
            child.accept(this);
        }
        // return simply null for default implementation
        return null;
    }
}
