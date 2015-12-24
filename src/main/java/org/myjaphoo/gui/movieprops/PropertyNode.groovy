package org.myjaphoo.gui.movieprops

import org.mlsoft.structures.TreeStructure

import javax.swing.*

/**
 * Property node
 */
public class PropertyNode implements TreeStructure<PropertyNode> {

    def PropertyNode parent;

    def String name;

    def String value;

    def boolean editable;

    def boolean shouldBeExpanded;

    def Icon icon;

    def String filterExpression;

    def ArrayList<PropertyNode> children = new ArrayList<>();

    PropertyNode(PropertyNode parent) {
        this.parent = parent
    }


/**
     * helper for the tree model
     * @return
     */
    public PropertyNode getNode() {
        return this;
    }

    public String toString() {
        return name;
    }

    public boolean hasBeenChanged() { return false; }

}
