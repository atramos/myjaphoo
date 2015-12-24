/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

/**
 * Ein syntax-element, das die Fähigkeit hat, sich selbst zu beschreiben.
 * @author mla
 */
public interface SelfDescriptingElement {

    /** the name of the element. */
    public String getName();

    /** returns a self description of this syntactical element. */
    public String getSelfDescription();

    /** returns a short self description of this syntactical element. */
    public String getSelfShortDescription();

    /** returns an example usage of this syntactical element. */
    public String getExampleUsage();
}
