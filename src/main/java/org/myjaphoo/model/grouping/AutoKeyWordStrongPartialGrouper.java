/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;
/**
 * Gruppiert nach der auto-keywords, die aus dem pfad gebildet werden.
 * @author mla
 */
public class AutoKeyWordStrongPartialGrouper extends AbstractAutoKeyWordPartialGrouper {

    public AutoKeyWordStrongPartialGrouper() {
        super(2, "//:\\.0123456789&_[]?(){}%");
    }
}
