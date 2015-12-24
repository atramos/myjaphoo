/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.structures;

import java.util.Collection;

/**
 * Basisinterface für Tree-Strukturen.
 * @author mla
 */
public interface TreeStructure<T extends TreeStructure<T>> {

    String getName();

    T getParent();

    Collection<T> getChildren();
}
