package org.myjaphoo.model.filterparser.visitors;

import org.myjaphoo.model.filterparser.expr.*;
import org.myjaphoo.model.filterparser.idents.QualifierIdentExpression;
import org.myjaphoo.model.filterparser.syntaxtree.*;
import org.myjaphoo.model.groupbyparser.expr.AggregatedExpression;

/**
 * ExpressionVisitor
 * @author mla
 * @version $Id$
 */
public interface ExpressionVisitor<T> {

    T visit(LiteralList literalList);

    T visit(GroovyFunctionCall groovyFunctionCall);

    T visit(Term term);

    T visit(NullLiteral nullLiteral);

    T visit(NumberLiteral numberLiteral);

    T visit(Constant constant);

    T visit(DateLiteral dateLiteral);

    T visit(StringLiteral stringLiteral);

    T visit(QualifierIdentExpression qualifierIdentExpression);

    T visit(FunctionCall functionCall);

    T visit(BoolExpression boolExpression);

    T visit(WrappedBoolExpression wrappedBoolExpression);

    T visit(NegatedBoolExpression negatedBoolExpression);

    T visit(AggregatedExpression aggregatedExpression);

    T visit(GroovyBoolExpression groovyBoolExpression);

    T visit(SubstitutionExpression substitutionExpression);

    T visit(BoolTerm boolTerm);


    T visit(Expression expression);


}
