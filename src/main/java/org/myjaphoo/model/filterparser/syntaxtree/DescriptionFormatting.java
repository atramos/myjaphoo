/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 *
 * Formatter functions for syntactical descriptions which gets presented
 * in the autocompletion summary.
 * @author lang
 */
public class DescriptionFormatting {

    public static String descFmt(String elementType, String name, ExprType returnType, String explanation, String exampleUsage) {

        String retTypeStr = returnType != null ? " Return type " + returnType + "<p>" : "";
        name = org.apache.commons.lang.StringEscapeUtils.escapeHtml(name);
        explanation = org.apache.commons.lang.StringEscapeUtils.escapeHtml(explanation);
        

        return descFormatting(elementType + " \"" + name + "\"", retTypeStr, explanation, exampleUsage);
    }

    public static String descFmtWithHtml(String elementType, String name, ExprType returnType, String explanation, String exampleUsage) {

        String retTypeStr = returnType != null ? " Return type " + returnType + "<p>" : "";
        return descFormatting(elementType + " \"" + name + "\"", retTypeStr, explanation, exampleUsage);
    }

    private static String descFormatting(String header, String paragraph1, String paragraph2, String paragraph3) {
        return "<h2>" + header + "</h2><hr /><p>" + paragraph1 + "<i>" + paragraph2 
                + "</i><p>Example:<br><div style=\"background-color:silver; width: 100%;\"><code>" + paragraph3 + "</code></div>";
    }
}
