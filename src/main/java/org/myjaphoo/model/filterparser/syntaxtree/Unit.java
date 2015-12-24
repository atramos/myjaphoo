/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import org.myjaphoo.model.grammars.FilterLanguageParser;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author mla
 */
public class Unit extends SpecialCharSymbol implements SelfDescriptingElement {

    private static HashMap<String, Unit> map = new HashMap<String, Unit>();
    private static HashMap<Integer, Unit> tokmap = new HashMap<>();
    private static final int KB_FACTOR = 1024;
    public static final Unit KB = new Unit(FilterLanguageParser.KB, "KB", KB_FACTOR);
    public static final Unit kb = new Unit(FilterLanguageParser.KB, "kb", KB_FACTOR);
    public static final Unit MB = new Unit(FilterLanguageParser.MB, "MB", KB_FACTOR * KB_FACTOR);
    public static final Unit mb = new Unit(FilterLanguageParser.MB, "mb", KB_FACTOR * KB_FACTOR);
    public static final Unit GB = new Unit(FilterLanguageParser.GB, "GB", KB_FACTOR * KB_FACTOR * KB_FACTOR);
    public static final Unit gb = new Unit(FilterLanguageParser.GB, "gb", KB_FACTOR * KB_FACTOR * KB_FACTOR);
    private long factor;

    public Unit(int tok, String name, long factor) {
        super(name);
        this.factor = factor;
        map.put(name, this);
        tokmap.put(tok, this);
    }

    public static Collection<Unit> getAllUnits() {
        return map.values();
    }

    public static Unit getUnitByTok(int tok) {
        return tokmap.get(tok);
    }

    public long getFactor() {
        return factor;
    }

    @Override
    public String getSelfDescription() {
        return getSymbol() + " : " + getSelfShortDescription();
    }

    @Override
    public String getSelfShortDescription() {
        return "Factor of " + getFactor();
    }

    @Override
    public String getExampleUsage() {
        return "";
    }

}
