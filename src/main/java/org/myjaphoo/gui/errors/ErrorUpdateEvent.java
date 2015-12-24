/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.errors;

import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.myjaphoo.model.filterparser.ParserException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lang
 */
public class ErrorUpdateEvent {
    private String groupName;

    private Collection<ErrorTreeNode> nodes;

    public ErrorUpdateEvent(String groupName, List<ParserNotice> notices) {
        this.groupName = groupName;
        this.nodes = convert(notices);
    }

    public ErrorUpdateEvent(String groupName, ParserException pe) {
        this.groupName = groupName;
        nodes = new ArrayList<>();
        ErrorTreeNode node = new ErrorTreeNode();
        node.setLevel(ErrorLevel.ERROR);
        node.setMessage(pe.getBlankErrMsg());
        nodes.add(node);
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    public Collection<ErrorTreeNode> getNodes() {
        return nodes;
    }

    private Collection<ErrorTreeNode> convert(List<ParserNotice> notices) {
        ArrayList<ErrorTreeNode> result = new ArrayList<ErrorTreeNode>();
        for (ParserNotice pn : notices) {
            result.add(convert(pn));
        }
        return result;
    }

    private ErrorTreeNode convert(ParserNotice pn) {
        ErrorTreeNode node = new ErrorTreeNode();
        node.setLevel(convertLevel(pn.getLevel()));
        node.setMessage(pn.getMessage());
        node.setRow(pn.getLine());

        return node;
    }

    private ErrorLevel convertLevel(ParserNotice.Level level) {
        switch (level) {
            case ERROR:
                return ErrorLevel.ERROR;
            case INFO:
                return ErrorLevel.INFO;
            case WARNING:
                return ErrorLevel.WARNING;
        }
        throw new RuntimeException("conversion not possible");
    }
}
