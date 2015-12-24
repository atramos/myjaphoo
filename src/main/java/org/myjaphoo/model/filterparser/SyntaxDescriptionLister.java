/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.idents.FixIdentifier;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.syntaxtree.AbstractParsedPiece;
import org.myjaphoo.model.filterparser.syntaxtree.Constant;
import org.myjaphoo.model.filterparser.syntaxtree.SelfDescriptingElement;
import org.myjaphoo.model.filterparser.syntaxtree.Unit;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author mla
 */
public class SyntaxDescriptionLister {

    public static String getIdentList() {
        return getSelfDescription(FixIdentifier.getList());
    }

    public static String getSelfDescription(Collection<? extends SelfDescriptingElement> elements) {
        StringBuilder b = new StringBuilder();
        for (SelfDescriptingElement element : elements) {
            if (b.length() > 0) {
                b.append("\n");
            }
            b.append(element.getSelfDescription());
        }
        return b.toString();
    }

    public static Collection<AbstractParsedPiece> getAllElements() {
        // ensure, that all static objects are initialized:
        FilterParser.initialize();
        ArrayList<AbstractParsedPiece> result = new ArrayList<AbstractParsedPiece>();
        result.addAll(AbstractOperator.getList());
        result.addAll(Function.getList());
        result.addAll(Constant.getAllConstants());
        result.addAll(Unit.getAllUnits());
        result.addAll(FixIdentifier.getList());
        return result;
    }

    public static String getOperatorList() {
        return getSelfDescription(AbstractOperator.getList());
    }

    public static String getFunctionList() {
        return getSelfDescription(Function.getList());
    }

    public static String getConstantsList() {
        return getSelfDescription(Constant.getAllConstants());
    }

    public static String getUnitsList() {
        return getSelfDescription(Unit.getAllUnits());
    }

    public static String getFullSyntaxDescr() {
        // ensure, that all static objects are initialized:
        FilterParser.initialize();

        StringBuilder b = new StringBuilder();
        b.append("Syntax: \n");
        b.append("boolexpr := boolfunc | boolterm | boolcomp | NOT(boolexpr) | (boolexpr) \n");
        b.append("boolfunc := funcCall  # mit return type boolean \n");
        b.append("boolterm := ident operator literallist \n");
        b.append("boolcomp := boolexpr [AND|OR] boolexpr  \n");
        b.append("literallist := literal[&literal*][|literal*] \n");
        b.append("Erlaubte Identifier: \n");
        b.append(getIdentList());
        b.append("\nErlaubte Operatoren: \n");
        b.append(getOperatorList());
        b.append("\nErlaubte Funktionen: \n");
        b.append(getFunctionList());
        b.append("\nErlaubte Konstanten: \n");
        b.append(getConstantsList());
        b.append("\nErlaubte Einheitenpräfixe: \n");
        b.append(getUnitsList());
        return b.toString();
    }
}
