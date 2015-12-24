/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.messages;

import org.mlsoft.structures.AbstractTreeNode;

/**
 *
 * @author mla
 */
public class MessageTreeNode extends AbstractTreeNode<MessageTreeNode> {

    public static enum Status {

        INFO, ERROR
    }
    private int id;
    private String message;
    private int progress;
    private String duration;
    private Status status = Status.INFO;

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
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * return true, if this message describes an error (of if any of the
     * children of this message contains an error)
     * @return 
     */
    boolean signalsError() {
        if (status == Status.ERROR) {
            return true;
        }
        for (MessageTreeNode child : getChildren()) {
            if (child.signalsError()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return message;
    }
}
