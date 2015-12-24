/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;
import org.myjaphoo.model.filterparser.syntaxtree.AbstractParsedPiece;
import org.myjaphoo.model.filterparser.syntaxtree.DescriptionFormatting;
import org.myjaphoo.model.filterparser.syntaxtree.SelfDescriptingElement;
import org.myjaphoo.model.filterparser.values.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * Eine Funktion, die n Argument annimmt, und einen Wert zurückliefert.
 *
 * @author mla
 */
public abstract class Function implements AbstractParsedPiece, SelfDescriptingElement, ProcessingRequirementInformation {

    private static HashMap<String, Function> map = new HashMap<String, Function>();

    public static Collection<Function> getList() {
        return map.values();
    }

    private String name;
    private String descr;

    private ExprType[] argTypes;

    public Function(String name, String descr, ExprType... argTypes) {
        this.name = name;
        this.descr = descr;
        this.argTypes = argTypes;
        map.put(name, this);
    }

    @Override
    public final String getSelfShortDescription() {
        return descr;
    }

    public static Function mapIdent(String name) {
        return map.get(name);
    }

    public boolean isBooleanFunction() {
        return ExprType.BOOLEAN == getType();
    }

    public boolean isStringFunction() {
        return ExprType.TEXT == getType();
    }

    public abstract Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args);

    public abstract ExprType getType();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean needsTagRelation() {
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return false;
    }

    /**
     * @return the argTypes
     */
    public ExprType[] getArgTypes() {
        return argTypes;
    }

    @Override
    public final String getSelfDescription() {
        return DescriptionFormatting.descFmt("Function", getName(), getType(), getSelfShortDescription(), getExampleUsage());
    }

    @Override
    public String getExampleUsage() {
        String example = getName() + "(";
        int i = 0;
        for (ExprType type : getArgTypes()) {
            if (i > 0) {
                example += ", ";
            }

            example += type.name();
        }
        example += ")";
        return example;
    }
}
