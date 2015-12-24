/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poc;

import org.jdesktop.swingx.JXSearchField;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author mla
 */
public class SearchField {

    public static void main(String[] args) throws Exception {
        new SearchField().testIt();
    }

    public void testIt() {
        final JXSearchField searchField = new JXSearchField();
        searchField.setPrompt("searchit");
        searchField.setInstantSearchDelay(500);
       
        searchField.setFindAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("action");

            }
        });
        JDialog dlg = new JDialog();
        dlg.add(searchField);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }
}
