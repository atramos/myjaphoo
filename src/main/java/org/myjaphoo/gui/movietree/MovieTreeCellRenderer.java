/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.editor.rsta.MarkOccurencesRenderer;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.gui.util.TextRepresentations;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 *
 * @author mla
 */
public class MovieTreeCellRenderer extends DefaultTreeRenderer {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/movietree/resources/MovieTreeCellRenderer");
    private MoviePanelController controller;
    private RSyntaxTextArea textArea = new RSyntaxTextArea();

    private JPanel renderComp = new JPanel();
    private JLabel rendIconLabel = new JLabel();
    private MovieTree movieTree;

    public MovieTreeCellRenderer(MovieTree movieTree, MoviePanelController controller) {
        super(createIconValue(movieTree, controller), null);
        this.controller = controller;
        this.movieTree = movieTree;

        renderComp.setLayout(new BorderLayout());
        renderComp.add(rendIconLabel, BorderLayout.LINE_START);
        renderComp.add(textArea, BorderLayout.CENTER);

    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            WrappingIconPanel jlabel = (WrappingIconPanel) comp;

            if (value instanceof MovieNode) {
                jlabel.setToolTipText(Helper.createThumbTipTextCompact((MovieNode) value));
            }

            MarkOccurencesRenderer.prepareTableCellRendererComponent(textArea, tree, value, selected, hasFocus, controller.getUsedLiterals());
            rendIconLabel.setText(nodeToString(value));
            jlabel.setComponent(renderComp);
            return comp;
    }

    public static String nodeToString(Object value) {
        if (value instanceof MovieNode) {
            return createLabelTextForMovieNode((MovieNode) value);
        } else if (value instanceof MovieStructureNode) {
            return createLabelTextForStructureNode((MovieStructureNode) value);
        } else {
            return value.toString();
        }
    }
    private static String createLabelTextForStructureNode(MovieStructureNode movieDirNode) {
        return TextRepresentations.createMovieTreeCellRendererIconLabelTextForStructureNode(movieDirNode);
    }

    private static String createLabelTextForMovieNode(MovieNode movieNode) {
        return TextRepresentations.createMovieTreeCellRendererIconLabelTextForMovieNode(movieNode);
    }


    public static IconValue createIconValue(final MovieTree movieTree, final MoviePanelController controller) {
        IconValue iv = new IconValue() {

            @Override
            public Icon getIcon(Object value) {
                return createIcon(value);
            }

            private Icon createIcon(Object value) {
                return MovieTreeIconCreator.createIcon(value, controller.isPreviewThumbsInMovieTree(), movieTree.getRowHeight(), movieTree.getTreeTableModel());
            }
        };
        return iv;


    }
}
