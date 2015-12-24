/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

/**
 * Gruppiert nach der auto-keywords, die aus dem pfad gebildet werden.
 * @author mla
 */
public class AutoKeyWordVeryStrongPartialGrouper extends AbstractAutoKeyWordPartialGrouper {

    public static final String SEPARATORS = "//:\\.0123456789&_[]?(){}%, !;=-";

    public AutoKeyWordVeryStrongPartialGrouper() {
        super(2, SEPARATORS);
    }

}
