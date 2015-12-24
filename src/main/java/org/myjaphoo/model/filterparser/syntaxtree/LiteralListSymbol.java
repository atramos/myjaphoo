/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

/**
 *
 * @author mla
 */
public class LiteralListSymbol extends SpecialCharSymbol {

    public static final LiteralListSymbol OR_SEPARATOR = new LiteralListSymbol('|');
    public static final LiteralListSymbol AND_SEPARATOR = new LiteralListSymbol('&');

    private LiteralListSymbol(char ch) {
        super(ch);
    }
}
