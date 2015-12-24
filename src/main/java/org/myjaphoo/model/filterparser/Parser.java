/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.filterparser;

/**
 *
 * @author mla
 */
public interface Parser {

    /**
     * Prüft, ob der String-ausdruck syntaktisch korrekt ist.
     * Falls nicht, so wird eine ParserException geworfen.
     */
    public void checkCorrectness(String txt) throws ParserException;
}
