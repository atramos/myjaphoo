/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.errors;

import org.mlsoft.structures.AbstractTreeNode;

/**
 *
 * @author mla
 */
public class ErrorTreeNode extends AbstractTreeNode<ErrorTreeNode> implements Comparable<ErrorTreeNode> {


    private int id;
    private String message;
    private int row;
    private ErrorLevel level;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the level
     */
    public ErrorLevel getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(ErrorLevel level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public int compareTo(ErrorTreeNode o) {
        return this.row - o.row;
    }


    @Override
    public String getName() {
        return message;
    }
}
