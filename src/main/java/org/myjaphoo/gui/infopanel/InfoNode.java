/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.infopanel;

import org.mlsoft.structures.AbstractTreeNode;

/**
 *
 * @author mla
 */
public class InfoNode extends AbstractTreeNode<InfoNode> {

    private String name;

    private String value;

    public InfoNode() {
    }

    
    
    public InfoNode(String name, String value) {
        this.name = name;
        this.value = value;
    }

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(int val) {
        this.value = Integer.toString(val);
    }

    void removeAllChildren() {
        while (getChildCount() > 0) {
            removeChild(getChildAt(0));
        }
    }

}
