package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.Expressions;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A qualified identifier expression, in the form of qualA.qualB.ident.
 * The simplest form consists of just one identifier without any qualification.
 * @see Qualifier
 */
public class QualifierIdentExpression implements Expression {

    /** the qualifiers. */
    private List<Qualifier> qualifiers = new ArrayList<Qualifier>();

    /** the identifier of the qualifier expression. */
    private FixIdentifier ident;

    public QualifierIdentExpression(ArrayList<Qualifier> quals, FixIdentifier ident) {
        this.qualifiers = quals;
        this.ident = ident;
    }

    public QualifierIdentExpression(FixIdentifier ident) {
        this.ident = ident;
    }

    @Override
    public ExprType getType() {
        return ident.getType();
    }

    @Override
    public String getDisplayExprTxt() {
        StringBuilder b = new StringBuilder();
        for (Qualifier qual: qualifiers) {
            b.append(qual.getDisplayExprTxt());
            b.append(".");
        }
        b.append(ident.getDisplayExprTxt());
        return b.toString();
    }

    @Override
    public Value evaluate(ExecutionContext executionContext, JoinedDataRow row) {
        Object context = row;
        // build the context transformation by all the qualifiers starting with row as input context:
        for (Qualifier qual: qualifiers) {
            context = qual.extractQualifierContext(context);
        }
        // produce also a context via the identifer:
        // all identifiers can act as (self)qualifier to produce their needed context:
        context = ident.extractQualifierContext(context);

        return ident.extractIdentValue(context);
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean needsTagRelation() {
        return ident.needsTagRelation() || Expressions.needsTagRelation(qualifiers);
    }

    @Override
    public boolean needsMetaTagRelation() {
        return ident.needsMetaTagRelation() || Expressions.needsMetaTagRelation(qualifiers);
    }

    public FixIdentifier getIdent() {
        return ident;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
