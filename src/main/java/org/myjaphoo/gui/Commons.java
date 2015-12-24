/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import javax.swing.border.Border;

import org.jdesktop.swingx.decorator.*;
import org.jdesktop.swingx.rollover.RolloverProducer;
import org.myjaphoo.model.dbcompare.DatabaseComparison;

/**
 *
 * @author mla
 */
public class Commons {
  public static ColorHighlighter ROLLOVER_ROW_HIGHLIGHTER =
        new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.ORANGE, Color.BLACK);

  private static HighlightPredicate ROLLOVER_CELL = new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
            if (!adapter.getComponent().isEnabled()) return false;
            Point p = (Point) adapter.getComponent().getClientProperty(
                    RolloverProducer.ROLLOVER_KEY);
            return p != null &&  p.y == adapter.row && p.x == adapter.column;
        }
    };

   public static ColorHighlighter ROLLOVER_CELL_HIGHLIGHTER =
        new ColorHighlighter(ROLLOVER_CELL, Color.ORANGE, Color.WHITE);

    private static final HighlightPredicate DBCOMPARE_MODE_PREDICATE = new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
            return DatabaseComparison.getInstance().isActive();
        }
    };
    /**
     * a highlighter to show that we do a comparison.
     */
    public static final Highlighter DBCOMPARE_HIGHLIGHTER = new ColorHighlighter(DBCOMPARE_MODE_PREDICATE, new Color(184, 245, 239), Color.black);



    public static Border createBorder() {
       return new org.jdesktop.swingx.border.DropShadowBorder();
   }
}
