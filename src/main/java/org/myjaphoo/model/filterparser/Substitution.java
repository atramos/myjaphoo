/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

/**
 *
 * @author mla
 */
public class Substitution {

    private String name;
    private String expression;

    public Substitution(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return expression;
    }
}
