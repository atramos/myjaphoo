/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.mlsoft.structures.AbstractTreeNode;

/**
 * A wrapped node, that simply contains a reference to a user object.
 * @author mla
 */
public class WrappedNode<T> extends AbstractTreeNode<WrappedNode<T>> {

    private T ref;

    public WrappedNode(T ref) {
        this.ref = ref;
    }

    /**
     * @return the token
     */
    public T getRef() {
        return ref;
    }

    /**
     * @param token the token to set
     */
    public void setRef(T token) {
        this.ref = token;
    }

    public String getName() {
        throw new IllegalArgumentException("not implemented!");
    }
}
