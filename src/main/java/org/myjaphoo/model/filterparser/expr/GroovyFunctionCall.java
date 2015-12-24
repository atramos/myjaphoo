/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.syntaxtree.AbstractParsedPiece;
import org.myjaphoo.model.filterparser.values.ObjectValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;
import org.myjaphoo.model.groovyparser.GroovyFilterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * a groovy function(method) call within the regular myjaphoo filter/grouping language
 *
 * @author mla
 */
public class GroovyFunctionCall implements Expression, AbstractParsedPiece {

    private String functionName;
    private List<Expression> args;
    private ExprType type;

    public GroovyFunctionCall(String functionName, List<Expression> args) {
        this.functionName = functionName;
        this.args = args;
        determineType();
    }

    private void determineType() {

        // this does not work for dynamically defined methods defined via metaClass
        // on the other hand, such functions are defined via a closure and have only object as return type
        // as there is no possibility to define a return type for a closure.
        // therefore we have to deal with a object type within the filter language which gets dynamically converted
        // into a more specialized type if necessary by the other expression parts.



        // examine the meta method definitions on the script base class:
//        ExecutionContext executionContext = new ExecutionContext();
//        Script script = executionContext.getScriptWrapper().getScript();
//
//        for (MetaMethod mm : script.getMetaClass().getMetaMethods()) {
//            if (mm.getName().equals(functionName)) {
//                // found the metamethod, try to determine a type:
//                type = determineType(mm.getReturnType());
//                return;
//            }
//        }
//
//        List<MetaMethod> responseToResult = script.getMetaClass().respondsTo(script, functionName, createArgTypes(args));

        // TEST:
        type =ExprType.OBJECT;
        return;
        // this should not be the case: there is no such method:
        //throw new ParserException(null, "no groovy meta method " + functionName + " is defined!", 0, 0);
    }

//    private Object[] createArgTypes(ArrayList<Expression> args) {
//        if (args == null || args.size() == 0) {
//            return null;
//        }
//        Object[] result = new Object[args.size()];
//        for (int i=0; i<args.size(); i++) {
//            result[i] = args.get(i).getType().getJavaType();
//        }
//        return result;
//    }

//    private ExprType determineType(Class clazz) {
//        if (clazz == String.class) {
//            return ExprType.TEXT;
//        } else if (Number.class.isAssignableFrom(clazz)) {
//            return ExprType.NUMBER;
//        } else if (java.util.Date.class == clazz) {
//            return ExprType.DATE;
//        } else if (Boolean.class == clazz) {
//            return ExprType.BOOLEAN;
//        } else {
//            return ExprType.NULL;
//        }
//
//    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        GroovyFilterParser.GroovyScriptWrapper wrapper = context.getScriptWrapper();
        wrapper.setRowProperty(row);
        try {
            Object result = wrapper.getScript().invokeMethod(functionName, convertArgList(args, context, row));
            return new ObjectValue(result);
        } catch (Exception e) {
            throw new ParserException(functionName, e.getLocalizedMessage(), 0, 0);
        }

    }

//    private Value wrapValue(Object o) {
//        if (o == null) {
//            return new NullLiteral();
//        }
//        if (o instanceof String) {
//            return new StringValue(o.toString());
//        }
//        if (o instanceof Number) {
//            return new LongValue(((Number) o).longValue());
//        }
//        if (o instanceof java.util.Date) {
//            return new DateValue((java.util.Date) o);
//        }
//        if (o instanceof Boolean) {
//            return BoolValue.forVal((Boolean) o);
//        } else {
//            throw new ParserException(null, "unable to convert value from groovy method!" + o, 0, 0);
//        }
//    }

    private Object convertArgList(List<Expression> args, ExecutionContext context, JoinedDataRow row) {
        if (args == null || args.size() == 0) {
            return null;
        }
        if (args.size() == 1) {
            Expression e = args.get(0);
            return e.evaluate(context, row).getValue();
        }
        ArrayList result = new ArrayList();
        for (Expression e : args) {
            result.add(e.evaluate(context, row).getValue());
        }
        return result;
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.unmodifiableList(args);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }

    @Override
    public ExprType getType() {
        return type;
    }

    @Override
    public String getDisplayExprTxt() {
        return functionName;
    }

    @Override
    public boolean needsTagRelation() {
        return true;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return true;
    }

    public List<Expression> getArgs() {
        return args;
    }
}
