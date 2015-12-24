package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.values.BoolValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;
import org.myjaphoo.model.groovyparser.GroovyFilterParser;

import java.util.Collections;
import java.util.List;

/**
 * GroovyBoolExpression
 *
 * @author mla
 * @version $Id$
 */
public class GroovyBoolExpression extends AbstractBoolExpression {

    private GroovyFilterParser.GroovyScriptWrapper script;

    public GroovyBoolExpression(GroovyFilterParser.GroovyScriptWrapper script) {
        this.script = script;
    }

    @Override
    public String getDisplayExprTxt() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        script.setRowProperty(row);

        Object returnVal = script.runScript();
        if (returnVal instanceof Boolean) {
            return BoolValue.forVal((Boolean) returnVal);
        } else {
            throw new ParserException(null, "error evaluating groovy filter script, not returning boolean!", 0, 0);
        }
    }


    @Override
    public List<Expression> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean needsTagRelation() {
        return true;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return true;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
