/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.editor.rsta;

import org.apache.commons.lang.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.myjaphoo.MyjaphooController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * @author lang
 */
public class MarkOccurencesRenderer extends DefaultTableRenderer {

    private RSyntaxTextArea textArea = new RSyntaxTextArea();
    private MyjaphooController controller;

    public MarkOccurencesRenderer(MyjaphooController controller) {
        this.controller = controller;
        textArea.setEnabled(false);
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, Object value,
            boolean isSelected, boolean hasFocus, final int row, final int column) {
        List<String> usedLiterals = controller.getFilter().getUsedLiterals();
        prepareTableCellRendererComponent(textArea, table, value, isSelected, hasFocus, row, column, usedLiterals);
        return textArea;
    }

    public static void prepareTableCellRendererComponent(RSyntaxTextArea textArea, JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column, List<String> usedLiterals) {
        RSTAHelper.getTableCellRendererComponent(textArea, table, value, isSelected, hasFocus, row, column);
        if (usedLiterals.size() > 0) {
            SearchContext searchContext = new SearchContext();
            String searchExpr = StringUtils.join(usedLiterals, "|");
            searchContext.setSearchFor(searchExpr);
            searchContext.setRegularExpression(true);
            SearchEngine.markAll(textArea, searchContext);
        }
    }

    public static void prepareTableCellRendererComponent(RSyntaxTextArea textArea, JTree table, Object value, boolean isSelected, boolean hasFocus, List<String> usedLiterals) {
        RSTAHelper.getTableCellRendererComponent(textArea, table, value, isSelected, hasFocus);
        if (usedLiterals.size() > 0) {
            SearchContext searchContext = new SearchContext();
            String searchExpr = StringUtils.join(usedLiterals, "|");
            searchContext.setSearchFor(searchExpr);
            searchContext.setRegularExpression(true);
            SearchEngine.markAll(textArea, searchContext);
        }
    }

    public static void main(String[] args) {
        JDialog dialog = new JDialog();
        RSyntaxTextArea textArea1 = new RSyntaxTextArea();
        dialog.add(textArea1);
        textArea1.setText("hallo dies ist ein test text!");
        SearchContext searchContext = new SearchContext();
        searchContext.setSearchFor("test");
        SearchEngine.markAll(textArea1, searchContext);
        dialog.pack();;
        dialog.setVisible(true);
    }
}
