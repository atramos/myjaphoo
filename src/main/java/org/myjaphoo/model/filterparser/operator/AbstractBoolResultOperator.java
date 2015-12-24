/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.operator;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.syntaxtree.NullLiteral;
import org.myjaphoo.model.filterparser.values.BoolValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.values.ValueSet;

import java.util.Arrays;
import java.util.Collection;
/**
 * base class for all operators which have a boolean result.
 * @author mla
 */
public abstract class AbstractBoolResultOperator extends AbstractOperator {
    
    protected AbstractBoolResultOperator(int tok, char ch, String description, ExprType... types) {
        super(tok, ch, description, types);
    }

    protected AbstractBoolResultOperator(int tok, String str, String description, ExprType... types) {
        super(tok, str, description, types);
    }

    @Override
    public ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType) {
        return ExprType.BOOLEAN;
    }
    
    public final Value eval(Value v1, Value v2) {
        if (v1 instanceof ValueSet && v2 instanceof ValueSet) {
            return multiMultiEval((ValueSet) v1, (ValueSet) v2);
        } else if (v1 instanceof ValueSet) {
            return multiEval((ValueSet) v1, v2);
        } else if (v2 instanceof ValueSet) {
            return multiMultiEval(new ValueSet(Arrays.asList(v1)), (ValueSet) v2);
        } else {
            return BoolValue.forVal(internEval(v1, v2));
        }
    }

    protected abstract boolean internEval(Value v1, Value v2);

    public Value multiEval(ValueSet v1, Value v2) {

        Collection<? extends Value> values = v1.getValues();
        if (values == null || values.isEmpty()) {
            // leere liste bedeutet: operation mit null-wert:
            return eval(new NullLiteral(), v2);
        } else {

            if (v1.getCombining() == ValueSet.ValueCombining.OR) {
                // ein match reicht:
                for (Value value : values) {
                    if (internEval(value, v2)) {
                        return BoolValue.TRUE;
                    }
                }
                return BoolValue.FALSE;
            } else {
                // alles muss matchen:
                for (Value value : values) {
                    if (!internEval(value, v2)) {
                        return BoolValue.FALSE;
                    }
                }
                return BoolValue.TRUE;
            }
        }
    }

    public Value multiMultiEval(ValueSet v1, ValueSet v2) {
        Collection<? extends Value> values = v2.getValues();
        if (values == null || values.isEmpty()) {
            // leere liste bedeutet: operation mit null-wert:
            return multiEval(v1, new NullLiteral());
        } else {
            if (v2.getCombining() == ValueSet.ValueCombining.OR) {
                // ein match reicht:
                for (Value value : values) {
                    if (multiEval(v1, value).asBool()) {
                        return BoolValue.TRUE;
                    }
                }
                return BoolValue.FALSE;
            } else {
                // alles muss matchen:
                for (Value value : values) {
                    if (!multiEval(v1, value).asBool()) {
                        return BoolValue.FALSE;
                    }
                }
                return BoolValue.TRUE;
            }
        }
    }

}
