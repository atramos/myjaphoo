/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.values.Value;

/**
 * An identifier.
 * An identifier itsself acts also always as a qualifier.
 * @see Qualifier
 *
 * @author lang
 */
public abstract class FixIdentifier<InputContext, OutputContext> extends Qualifier<InputContext, OutputContext>  {


    protected FixIdentifier(String name, String descr, String exampleUsage, ExprType type, Class<InputContext> inputContextClass, Class<OutputContext> outputContextClass,
                            boolean needsTagRelation, boolean needsMetaTagRelation) {
        super(name, descr, exampleUsage, type, inputContextClass, outputContextClass, needsTagRelation, needsMetaTagRelation);
    }

    /**
     * Extract the identifier value.
     * @param context the identifier context (produced by a qualifier chain, at least it is an output of this
     *         identifier when acting als (self)qualfier.
     * @return the value
     */
    abstract public Value extractIdentValue(OutputContext context);

}
