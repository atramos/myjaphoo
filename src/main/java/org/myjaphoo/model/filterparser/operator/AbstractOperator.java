/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.operator;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.syntaxtree.DescriptionFormatting;
import org.myjaphoo.model.filterparser.syntaxtree.SelfDescriptingElement;
import org.myjaphoo.model.filterparser.syntaxtree.SpecialCharSymbol;
import org.myjaphoo.model.filterparser.values.Value;

import java.util.*;

/**
 * Abstract base class for operators.
 * An operator in myjaphoo is a function which takes exactly two expressions and can calculate
 * one result value.
 *
 * @author mla
 */
public abstract class AbstractOperator extends SpecialCharSymbol implements SelfDescriptingElement {

    private static HashMap<Integer, AbstractOperator> tokMap = new HashMap<>();

    private static HashMap<String, AbstractOperator> map = new HashMap<String, AbstractOperator>();
    private EnumSet<ExprType> types;
    private String description;

    protected AbstractOperator(int tok, char ch, String description, ExprType... types) {
        super(ch);
        if (types != null && types.length > 0) {
            this.types = EnumSet.copyOf(Arrays.asList(types));
        } else {
            this.types = EnumSet.noneOf(ExprType.class);
        }
        this.description = description;
        map.put(new String(new char[]{ch}), this);
        tokMap.put(tok, this);
    }

    protected AbstractOperator(int tok, String str, String description, ExprType... types) {
        super(str);
        if (types != null && types.length > 0) {
            this.types = EnumSet.copyOf(Arrays.asList(types));
        } else {
            this.types = EnumSet.noneOf(ExprType.class);
        }

        this.description = description;
        map.put(str, this);
        tokMap.put(tok, this);
    }

    /**
     * Calculates a result value for two input values.
     *
     * @param v1
     * @param v2
     * @return
     */
    public abstract Value eval(Value v1, Value v2);

    public Set<ExprType> worksWithTypes() {
        return types;
    }

    /**
     * Returns the resulting expression type for this operator. Since there
     * are operators, where the result depends on the input, this method
     * expects two expression types (for the operator) to evaluate the output
     * type.
     *
     * @param rightExpressionType
     * @param leftExpressionType
     * @return
     */
    public abstract ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType);

    public static Collection<AbstractOperator> getList() {
        return map.values();
    }

    @Override
    public final String getSelfDescription() {
        return DescriptionFormatting.descFmt("Operator", getName(), null, getSelfShortDescription(), getExampleUsage());
    }

    @Override
    public String getSelfShortDescription() {
        return description;
    }

    @Override
    public String getExampleUsage() {
        String example = "";
        for (ExprType type : worksWithTypes()) {
            if (example.length() > 0) {
                example += " or <br>";
            }

            example += type.name() + " " + getSymbol() + " " + type.name();
        }
        return example;
    }

    public static AbstractOperator getOperatorByToken(int tok) {
        return tokMap.get(tok);
    }
}
