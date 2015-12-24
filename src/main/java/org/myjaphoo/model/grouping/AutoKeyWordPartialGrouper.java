/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

/**
 * Gruppiert nach der auto-keywords, die aus dem pfad gebildet werden.
 * @author mla
 */
public class AutoKeyWordPartialGrouper extends AbstractAutoKeyWordPartialGrouper {

    public AutoKeyWordPartialGrouper(){
        super(2, "//:\\.0123456789");
    }
}
