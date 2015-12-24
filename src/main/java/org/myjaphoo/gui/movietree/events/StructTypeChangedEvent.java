/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree.events;

/**
 *
 * @author lang
 */
public class StructTypeChangedEvent {

    private String userDefinedStruct;
    
    public StructTypeChangedEvent(String userDefinedStruct) {
        this.userDefinedStruct = userDefinedStruct;
    }

    /**
     * @return the userDefinedStruct
     */
    public String getUserDefinedStruct() {
        return userDefinedStruct;
    }
    
}
