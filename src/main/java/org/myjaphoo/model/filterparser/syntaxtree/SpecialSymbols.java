/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

/**
 * @author mla
 */
public class SpecialSymbols extends SpecialCharSymbol {

    public static final SpecialSymbols POINT = new SpecialSymbols('.');
    public static final SpecialSymbols COMMA = new SpecialSymbols(',');
    public static final SpecialSymbols SEMICOLON = new SpecialSymbols(';');
    public static final SpecialSymbols DOLLAR = new SpecialSymbols('$');
    public static final SpecialSymbols HASH = new SpecialSymbols('#');

    public static final SpecialSymbols GROOVY = new SpecialSymbols("groovy");

    private SpecialSymbols(char ch) {
        super(ch);
    }

    private SpecialSymbols(String str) {
        super(str);
    }
}
