/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author mla
 */
public abstract class SpecialCharSymbol implements AbstractParsedPiece {

    private static HashMap<String, SpecialCharSymbol> map = new HashMap<String, SpecialCharSymbol>();
    private String symbol;

    public static Collection<SpecialCharSymbol> getSymbols() {
        return map.values();
    }

    protected SpecialCharSymbol(char chsymbol) {
        this.symbol = new String(new char[]{chsymbol});
        map.put(this.symbol, this);
    }

    protected SpecialCharSymbol(String symbol) {
        this.symbol = symbol;
        map.put(this.symbol, this);
    }

    /**
     * @return the operatorSymbol
     */
    public String getSymbol() {
        return symbol;
    }
    
    public String getName() {
        return getSymbol();
    }

    public static SpecialCharSymbol mapIdent(char chsymbol) {
        return map.get(new String(new char[]{chsymbol}));
    }

    public static SpecialCharSymbol mapIdent(String symbol) {
        return map.get(symbol);
    }

    @Override
    public String toString() {
        return "sym:" + symbol;
    }


}
