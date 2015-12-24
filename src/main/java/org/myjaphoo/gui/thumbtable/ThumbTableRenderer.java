/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import javax.swing.table.TableModel;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.swingx.renderer.JRendererLabel;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbNowLoadedMsg;

/**
 *
 * @author mla
 */
public class ThumbTableRenderer extends AbstractThumbTableRenderer {

    private boolean perColumnThumbs;
    /** should this renderer take care for the card thumb feature? */
    private boolean cardThumbAware;
    
    public ThumbTableRenderer(ThumbPanelController controller, boolean perColumnThumbs, boolean cardThumbAware) {
        super(controller);
        this.perColumnThumbs = perColumnThumbs;
        this.cardThumbAware = cardThumbAware;
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, Object value,
            boolean isSelected, boolean hasFocus, final int row, final int column) {
        JRendererLabel label = (JRendererLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        ThumbIsLoadedCallback callBack = new ThumbIsLoadedCallback() {

            @Override
            public void notifyIsLoaded(ThumbNowLoadedMsg msg) {

                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        TableModel tablemodel = table.getModel();
                        if (tablemodel instanceof AbstractTableModel) {
                            ((AbstractTableModel) tablemodel).fireTableCellUpdated(row, column);
                        }
                    }
                });
            }
        };
        int thumbNum = 0;
        if (perColumnThumbs) {
            thumbNum = column;
        }
        if (cardThumbAware && getController().isCardView()) {
            getThumbRendering().prepareCardViewJRendererLabel(label, value, isSelected, thumbNum, table.getBackground(), callBack);
        } else {
            getThumbRendering().prepareJRendererLabel(label, value, isSelected, thumbNum, table.getBackground(), callBack);
        }
        return label;

    }
}
