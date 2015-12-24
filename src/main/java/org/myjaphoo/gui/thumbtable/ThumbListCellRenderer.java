/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import javax.swing.JList;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.ListModel;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.JRendererLabel;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbNowLoadedMsg;

/**
 * List cell renderer for the thumb grid view.
 * @author mla
 */
class ThumbListCellRenderer extends DefaultListRenderer {

    private ThumbPanelController controller;
    private ThumbRendering thumbRendering;

    public ThumbListCellRenderer(ThumbPanelController controller) {
        this.controller = controller;
        thumbRendering = new ThumbRendering(controller);
    }

    @Override
    public Component getListCellRendererComponent(final JList list, Object value, final int index, boolean isSelected, boolean cellHasFocus) {
        JRendererLabel jlabel = (JRendererLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        ThumbIsLoadedCallback callBack = new ThumbIsLoadedCallback() {

            @Override
            public void notifyIsLoaded(ThumbNowLoadedMsg msg) {

                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {

                        ListModel tablemodel = list.getModel();
                        if (tablemodel instanceof ThumbListModel) {
                            ((ThumbListModel) tablemodel).fireCellUpdated(index);
                        }
                    }
                });
            }
        };

        if (controller.isCardView()) {
            thumbRendering.prepareCardViewJRendererLabel(jlabel, value, isSelected, 0, list.getBackground(), callBack);
        } else {
            thumbRendering.prepareJRendererLabel(jlabel, value, isSelected, 0, list.getBackground(), callBack);
        }        

        return jlabel;
    }

}
