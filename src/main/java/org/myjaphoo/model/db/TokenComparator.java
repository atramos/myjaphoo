/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import java.util.Comparator;

/**
 *
 * @author mla
 */
public class TokenComparator implements Comparator<Token> {

    @Override
    public int compare(Token o1, Token o2) {
        return o1.getName().compareTo(o2.getName());
    }

}
