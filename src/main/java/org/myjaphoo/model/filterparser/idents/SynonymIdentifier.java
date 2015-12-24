/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.idents;


import org.myjaphoo.model.filterparser.values.Value;


/**
 * Synonym definition für einen identifier.
 * @author mla
 */
public class SynonymIdentifier<I, O> extends FixIdentifier<I, O> {

    private FixIdentifier<I, O> fixIdentifier;

    protected SynonymIdentifier(String name, FixIdentifier<I, O> fixIdentifier) {
        super(name, fixIdentifier.getSelfShortDescription(), fixIdentifier.getExampleUsage(), fixIdentifier.getType(),
                fixIdentifier.getInputContextClass(), fixIdentifier.getOutputContextClass(),
                fixIdentifier.needsTagRelation(), fixIdentifier.needsMetaTagRelation());
        this.fixIdentifier = fixIdentifier;
    }

    @Override
    public Value extractIdentValue(O context) {
        return fixIdentifier.extractIdentValue(context);
    }

    @Override
    public O extractQualifierContext(I row) {
        return fixIdentifier.extractQualifierContext(row);
    }
}
