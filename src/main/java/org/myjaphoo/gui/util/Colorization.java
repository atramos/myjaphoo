/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.movietree.DiffNode;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbcompare.CompareResult;

import java.awt.*;

/**
 *
 * @author lang
 */
public class Colorization {

    public static final Color noColor = new Color(210, 210, 200);

    private static final Color[] colorArray = new Color[]{
        // ein paar pastellfarben:
        new Color(235, 147, 150),
        new Color(246, 178, 128),
        new Color(255, 250, 135),
        new Color(165, 220, 165),
        new Color(156, 183, 228),
        new Color(176, 156, 221),
        new Color(214, 170, 191),
        new Color(214, 217, 228),
        new Color(107, 121, 147),
        new Color(192, 192, 192),
        new Color(109, 109, 109),
        new Color(199, 33, 44),
        new Color(209, 92, 14),
        new Color(193, 46, 34),
        new Color(177, 172, 0),
        new Color(60, 143, 50),
        new Color(51, 146, 116),
        new Color(117, 135, 70),
        new Color(48, 94, 176),
        new Color(94, 64, 164),
        new Color(92, 62, 192),
        new Color(150, 70, 110)
    /*
    Color.DARK_GRAY.brighter().brighter(), Color.LIGHT_GRAY,
    Color.MAGENTA.brighter().brighter(), Color.ORANGE,
    Color.PINK, Color.yellow.brighter().brighter()
     * */
    };
    private ColorizationType type = ColorizationType.NONE;

    /**
     * @return the type
     */
    public ColorizationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ColorizationType type) {
        this.type = type;
    }

    /**
     * Liefert eine farbe für den movie zur benutzung als background.
     * sollte pro verzeichnis eine eindeutige farbe liefern.
     * @param node
     * @return
     */
    public Color getColorForMovie(MovieNode node, boolean isSelected) {
        if (node == null || node.getMovieEntry() == null) {
            return null;
        }
        MovieEntry nodeItem = node.getMovieEntry();
        switch (type) {
            case SAME_DIR:
                int hc = nodeItem.getCanonicalDir().hashCode();
                return scaleOnValue(hc, isSelected);
            case SAME_CHECKSUM:
                long checksum = 0;
                if (nodeItem.getChecksumCRC32() != null) {
                    checksum = nodeItem.getChecksumCRC32();
                }
                return scaleOnValue(checksum, isSelected);
            case NONE:
                return null;
            case SAME_TOKEN:
                int hash = 0;
                for (Token token : nodeItem.getTokens()) {
                    hash += token.getName().hashCode();
                }
                return scaleOnValue(hash, isSelected);
            case SAME_STRUCT_PATH:
                return scaleOnValue(path(node), isSelected);
            case SAME_RATING:
                int ratingOrdinal = nodeItem.getRating() != null ? nodeItem.getRating().ordinal() : 0;
                return scaleOnValue(ratingOrdinal, isSelected);
            case BY_DB_COMPARISON:
                return getDiffColor(node, isSelected);
            default:
                throw new RuntimeException("error in getcolorformovie"); //NOI18N
        }
    }

    private Color getDiffColor(MovieNode node, boolean selected) {
        if (node instanceof DiffNode) {
           DiffNode dn = (DiffNode) node;
            CompareResult maxDiff = CompareResult.max(CompareResult.max(dn.getDbdiff().getDiffEntry(), dn.getDbdiff().getDiffTag()), dn.getDbdiff().getDiffMetaTag());
            Color color = maxDiff.getDiffColor();
            return getColor(color, selected);
        } else {
            return  getColor(CompareResult.EQUAL.getDiffColor(), selected);
        }
    }

    private Color scaleOnValue(long hc, boolean isSelected) {
        int index = (int) (hc % colorArray.length);
        if (index < 0) {
            index = -index;
        }
        Color color = colorArray[index];
        return getColor(color, isSelected);
    }

    private Color getColor(Color color, boolean isSelected) {
        if (isSelected) {
            color = color.darker();
        }
        return color;
    }

    private long path(MovieNode node) {
        HashCodeBuilder b = new HashCodeBuilder();
        AbstractTreeNode parent = node.getParent();
        while (parent != null) {

            b.append(parent.toString());
            parent = parent.getParent();
        }
        return b.toHashCode();
    }
}
