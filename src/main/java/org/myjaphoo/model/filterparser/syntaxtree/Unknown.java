/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

/**
 *
 * @author mla
 */
public class Unknown implements AbstractParsedPiece {

    private int tt;
    private String sval;

    public Unknown(int tt, String sval) {
        this.tt = tt;
        this.sval = sval;
    }

    @Override
    public String toString() {
        return "unknown:" + tt + "," + sval;
    }


}
