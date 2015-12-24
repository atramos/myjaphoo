package org.mlsoft.swing

/**
 * ComponentSupporter 
 * @author lang
 * @version $Id$
 *
 */
public interface ComponentSupporter<T> {

    /**
     * returns the first selected element within this component. Or null, if nothing is currently selected.
     * @return
     */
    T getFirstSelectedElement();

    List<T> getSelectedElements();
}