/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

/**
 *
 * @author mla
 */
public class Braces extends SpecialCharSymbol {

    public static final Braces OPENBRACE = new Braces('(');
    public static final Braces CLOSEBRACE = new Braces(')');

    private Braces(char ch) {
        super(ch);
    }
}
