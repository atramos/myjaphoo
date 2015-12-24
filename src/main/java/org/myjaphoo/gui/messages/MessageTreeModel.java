/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.messages;

import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.events.*;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.myjaphoo.gui.WmTreeTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreePath;

/**
 * @author mla
 */
public class MessageTreeModel extends WmTreeTableModel {

    private static final java.util.ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("org/myjaphoo/gui/messages/resources/MessagePanel");

    private static String[] COLUMNS = new String[]{BUNDLE.getString("MESSAGE"), BUNDLE.getString("PROGRESS"), BUNDLE.getString("DURATION")};

    private static final Logger logger = LoggerFactory.getLogger(MessageTreeModel.class);

    public MessageTreeModel(MessageTreeNode root) {
        super(root, COLUMNS);

        GlobalBus.bus.register(this);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        MessageTreeNode mtn = (MessageTreeNode) node;
        switch (column) {
            case 0:
                return mtn;
            case 1:
                return mtn.getProgress();
            case 2:
                return mtn.getDuration();
        }
        return null;
    }

    @Subscribe(onETD = true, sequential = true)
    public void activityStarted(ActivityStartedEvent event) {

        try {
            MessageTreeNode channelNode = getOrCreateChannelNode(event.getChannel());
            MessageTreeNode root = (MessageTreeNode) getRoot();
            modelSupport.fireChildAdded(new TreePath(root), root.getIndex(channelNode), channelNode);
        } catch (Exception e) {
            logger.warn("error consuming channel event!", e);
        }
    }

    @Subscribe(onETD = true, sequential = true)
    public void progress(ProgressEvent event) {
        try {
            MessageTreeNode channelNode = getOrCreateChannelNode(event.getChannel());
            channelNode.setProgress(event.getPercentage());
            nodeChanged(channelNode);
        } catch (Exception e) {
            logger.warn("error consuming channel event!", e);
        }

    }

    @Subscribe(onETD = true, sequential = true)
    public void message(MessageEvent event) {
        try {
            MessageTreeNode channelNode = getOrCreateChannelNode(event.getChannel());
            addMsgNode(channelNode, event.getMessage());
        } catch (Exception e) {
            logger.warn("error consuming channel event!", e);
        }
    }

    @Subscribe(onETD = true, sequential = true)
    public void errormessage(ErrorMessageEvent event) {
        try {
            MessageTreeNode channelNode = getOrCreateChannelNode(event.getChannel());
            MessageTreeNode newNode = addErrorNode(channelNode, event.getErrorMessage());
            if (event.getT() != null) {
                String[] frames = org.apache.commons.lang.exception.ExceptionUtils.getStackFrames(event.getT());
                for (String frame : frames) {
                    addErrorNode(newNode, frame);
                }
            }
        } catch (Exception e) {
            logger.warn("error consuming channel event!", e);
        }
    }

    @Subscribe(onETD = true, sequential = true)
    public void activityStopped(ActivityFinishedEvent event) {
        try {
            MessageTreeNode channelNode = getOrCreateChannelNode(event.getChannel());
            channelNode.setDuration(event.getChannel().getDuration());
            nodeChanged(channelNode);
        } catch (Exception e) {
            logger.warn("error consuming channel event!", e);
        }
    }

    private MessageTreeNode getOrCreateChannelNode(Channel channel) {
        MessageTreeNode msgRoot = (MessageTreeNode) root;
        // search in the list of children of the root;
        // from back to front, as its most common that changes happen to the latest channels
        for (int i = msgRoot.getChildCount() - 1; i >= 0; i--) {
            MessageTreeNode childNode = msgRoot.getChildren().get(i);
            if (childNode.getId() == channel.getId()) {
                return childNode;
            }
        }
        // no one found: create one:
        MessageTreeNode newNode = new MessageTreeNode();
        newNode.setId(channel.getId());
        newNode.setMessage(channel.getActivityTitle());
        msgRoot.addChild(newNode);
        return newNode;
    }

    private MessageTreeNode addMsgNode(MessageTreeNode channelNode, String message) {
        MessageTreeNode newNode = new MessageTreeNode();
        newNode.setMessage(message);
        return addNode(channelNode, newNode);
    }

    private MessageTreeNode addErrorNode(MessageTreeNode channelNode, String message) {
        MessageTreeNode newNode = new MessageTreeNode();
        newNode.setStatus(MessageTreeNode.Status.ERROR);
        newNode.setMessage(message);
        return addNode(channelNode, newNode);
    }

    private MessageTreeNode addNode(MessageTreeNode parent, MessageTreeNode newNode) {
        parent.addChild(newNode);
        modelSupport.fireChildAdded(new TreePath(parent), parent.getIndex(newNode), newNode);
        return newNode;
    }

}
