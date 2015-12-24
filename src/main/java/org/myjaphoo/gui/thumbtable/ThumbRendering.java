/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.JRendererLabel;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.gui.util.TextRepresentations;
import org.myjaphoo.model.FileSubstitution;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.logic.FasterFileSubstitution;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class with helper methods for cell renderers which display thumb nails.
 * Has helper methods to create thumbs with additional overlay information,
 * as well as functions to prepare jLabels with content to use for rendering.
 *
 * @author mla
 */
public class ThumbRendering {

    private Border border = new org.jdesktop.swingx.border.DropShadowBorder();
    public static final Painter SELECTIONPAINTER = new PinstripePainter(null, 45.0, 1.0, 6.0);
    private static FileSubstitution substitution = new FasterFileSubstitution();
    private ThumbPanelController controller;
    private BufferedImage biNotUnique;
    private BufferedImage biRating;
    private BufferedImage biNotLocated;
    private BufferedImage biDuplicates;
    private BufferedImage biIntCompDb;
    private BufferedImage biCondensed;

    public ThumbRendering(ThumbPanelController controller) {
        this.controller = controller;
        try {
            biNotUnique = Icons.IR_NOTUNIQUE.createBufferedImage();
            biRating = Icons.IR_RATING.createBufferedImage();
            biNotLocated = Icons.IR_NOTLOCATED.createBufferedImage();
            biDuplicates = Icons.IR_DUPLICATES.createBufferedImage();
            biIntCompDb = Icons.IR_INCOMPDATABASE.createBufferedImage();
            biCondensed = Icons.IR_CONDENSED.createBufferedImage();
        } catch (IOException ex) {
            LoggerFactory.getLogger(ThumbRendering.class.getName()).error("error", ex); //NOI18N
        }
    }

    private void setLabelText(JRendererLabel jlabel, String name) {
        FontMetrics fm = jlabel.getFontMetrics(jlabel.getFont());
        int height = fm.getHeight();

        if (fm.stringWidth(name) > jlabel.getWidth()) {
            int charWidth = fm.charWidth('w');
            int estimatedChars = jlabel.getWidth() / charWidth;
            estimatedChars = estimatedChars + (jlabel.getWidth() - fm.stringWidth(name.substring(0, estimatedChars - 1))) / charWidth;
            name = StringUtils.abbreviate(name, estimatedChars);
        }
        jlabel.setText(name);
    }

    private void setBackgroundColor(JRendererLabel jlabel) {
//        if (MyjaphooAppPrefs.PRF_BACKGROUNDCOLORTHUMBNAILSACTIVATED.getVal()) {
//            jlabel.setBackground(MyjaphooAppPrefs.PRF_BACKGROUNDCOLORTHUMBNAILS.getVal());
//        }
    }

    /**
     * Hilfsklasse, um meherer Icons, die als Overlay im Thumb angezeigt werden,
     * in einer Reihe darzustellen.
     * Nützlich, da die Anzahl der Icons sich dynamisch ergibt (und damit auch der Platz in der Reihe).
     */
    class OverlayImageSequencer {

        private int i = 0;
        private HorizontalAlignment horizontal;
        private VerticalAlignment vertical;

        public OverlayImageSequencer(HorizontalAlignment horizontal, VerticalAlignment vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        public ImagePainter createNextImagePainter(BufferedImage img) {
            ImagePainter imagePainter = new ImagePainter(img,
                    horizontal, vertical);
            imagePainter.setInsets(new Insets(0, i * 16, 0, 0));
            i++;
            return imagePainter;
        }
    }

    public Icon createIcon(MovieNode node, boolean isSelected, int column, ThumbIsLoadedCallback loadedCallBack) {

        ImageIcon ii = controller.getZoom().getThumbImage(node, column, controller.getThumbTypeDisplayMode(), loadedCallBack);
        if (ii == null) {
            if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal()) {
                String located = substitution.locateFileOnDrive(node.getCanonicalPath());
                if (located != null) {
                    return FileSystemView.getFileSystemView().getSystemIcon(new File(located));
                }
            }
            return Icons.IR_EMPTY_PIC.icon;

            // ty to get system icon.
            //ii = (ImageIcon) tryToGetAFileSystemIcon(located);
            // we do not add any additional infos via image painter (as we do not have any infos, so just return here:
            //return ii;
        }

        if (ii != null) {
            ImagePainter ip = new ImagePainter((BufferedImage) ii.getImage());
            PainterIcon painterIcon = new PainterIcon(new Dimension(ii.getIconWidth(), ii.getIconHeight()));
            painterIcon.setPainter(ip);
            ArrayList<Painter> pipeline = new ArrayList<Painter>();

            if (isSelected) {
                AlphaPainter selPainter = new AlphaPainter();
                selPainter.setAlpha(0.7F);
                pipeline.add(selPainter);
                selPainter.setPainters(ip);
            } else {
                pipeline.add(ip);
            }
            if (node.getMovieEntry().getRating() != null) {
                addRatingPainter(node.getMovieEntry().getRating(), pipeline);
            }
            OverlayImageSequencer seq = new OverlayImageSequencer(HorizontalAlignment.LEFT, VerticalAlignment.TOP);
            if (!node.isUnique()) {
                pipeline.add(seq.createNextImagePainter(biNotUnique));
            }
            if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal()) {
                String located = substitution.locateFileOnDrive(node.getCanonicalPath());
                if (located == null) {
                    pipeline.add(seq.createNextImagePainter(biNotLocated));
                }
            }
            if (node.isHasDups()) {
                pipeline.add(seq.createNextImagePainter(biDuplicates));
            }
            if (DatabaseComparison.getInstance().hasSameEntry(node.getMovieEntry())) {
                pipeline.add(seq.createNextImagePainter(biIntCompDb));
            }
            if (node.getCondensedDuplicatesSize() > 0) {
                pipeline.add(seq.createNextImagePainter(biCondensed));
            }

            if (pipeline.size() > 0) {
                org.jdesktop.swingx.painter.CompoundPainter cp = new CompoundPainter(pipeline.toArray(new Painter[pipeline.size()]));
                painterIcon.setPainter(cp);
            }
            return painterIcon;
        } else {
            return null;
        }

    }

    private void addRatingPainter(Rating rating, ArrayList<Painter> pipeline) {
        OverlayImageSequencer seq = new OverlayImageSequencer(HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        for (int i = 0; i < rating.ordinal(); i++) {
            pipeline.add(seq.createNextImagePainter(biRating));
        }
    }

    public void prepareJRendererLabel(JRendererLabel jlabel, Object value, boolean isSelected, int thumbNum, Color background, ThumbIsLoadedCallback callBack) {

        final Dimension labeldimension = new Dimension(controller.getZoom().getEffectiveWidth() + 2, controller.getHeightForThumbLabelComponent());
        jlabel.setPreferredSize(labeldimension);
        jlabel.setSize(labeldimension);
        jlabel.setBorder(border);
        if (value != null) {
            if (value instanceof MovieNode) {
                MovieNode node = (MovieNode) value;
                jlabel.setIcon(createIcon(node, isSelected, thumbNum, callBack));

                String name = node.getMovieEntry().getName();

                setLabelText(jlabel, name);

                jlabel.setToolTipText(Helper.createThumbTipTextCompact(node));
                Color colorForMovie = controller.getColorization().getColorForMovie(node, isSelected);
                if (colorForMovie != null) {
                    jlabel.setBackground(colorForMovie);
                } else {
                    setBackgroundColor(jlabel);
                }
            } else {
                jlabel.setText(value.toString());
                setBackgroundColor(jlabel);
            }
        } else {
            // ansonsten gezielt standardbackground setzen:
            setBackgroundColor(jlabel);
            jlabel.setToolTipText(null);
        }

        jlabel.setHorizontalAlignment(SwingConstants.CENTER);
        jlabel.setHorizontalTextPosition(SwingConstants.CENTER);
        jlabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        // set special painter to visualize selected items:
        if (isSelected) {
            jlabel.setPainter(SELECTIONPAINTER);

        } else {
            jlabel.setPainter(null);
        }
    }

    public void prepareCardViewJRendererLabel(JRendererLabel jlabel, Object value, boolean isSelected, int thumbNum, Color background, ThumbIsLoadedCallback callBack) {

        int thumbWidth = controller.getZoom().getEffectiveWidth() + 2;
        int thumbHeight = controller.getZoom().getEffectiveHeight() + 2;

        int labelWidth = thumbWidth * 5 / 2;
        int labelHeight = thumbHeight;

        final Dimension labeldimension = new Dimension(labelWidth, labelHeight);
        jlabel.setPreferredSize(labeldimension);
        jlabel.setSize(labeldimension);
        jlabel.setBorder(border);


        if (value != null) {
            if (value instanceof MovieNode) {
                MovieNode node = (MovieNode) value;
                jlabel.setIcon(createIcon(node, isSelected, thumbNum, callBack));

                String name = node.getMovieEntry().getName();

                if (node.getCondensedDuplicatesSize() > 0) {
                    name += " " + (node.getCondensedDuplicatesSize() + 1) + "x"; //NOI18N
                }

                String labelText = TextRepresentations.createCardLabelText(node, name); //NOI18N

                jlabel.setText(labelText);
                jlabel.setToolTipText(Helper.createThumbTipTextCompact(node));
                Color colorForMovie = controller.getColorization().getColorForMovie(node, isSelected);
                if (colorForMovie != null) {
                    jlabel.setBackground(colorForMovie);
                } else {
                    setBackgroundColor(jlabel);
                }
            } else {
                jlabel.setText(value.toString());
                setBackgroundColor(jlabel);
            }
        } else {
            // ansonsten gezielt standardbackground setzen:
            setBackgroundColor(jlabel);
            jlabel.setToolTipText(null);
        }

        jlabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        jlabel.setVerticalTextPosition(SwingConstants.TOP);

        // set special painter to visualize selected items:
        if (isSelected) {
            jlabel.setPainter(SELECTIONPAINTER);

        } else {
            jlabel.setPainter(null);
        }
    }

}
