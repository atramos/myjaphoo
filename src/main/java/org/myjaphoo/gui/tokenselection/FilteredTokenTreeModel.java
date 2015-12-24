/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.tokenselection;

import org.myjaphoo.gui.token.AbstractTokenTreeModel;
import org.myjaphoo.model.db.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Token Tree modell, welches auf einen Teilausdruck des Namens filtern kann.
 * @author lang
 */
public class FilteredTokenTreeModel extends AbstractTokenTreeModel {

    private static Logger logger = LoggerFactory.getLogger(FilteredTokenTreeModel.class);

    FilteredTokenTreeModel(Token newroot, boolean isFlat, String typedText) {
        super(newroot, true, typedText, isFlat, false);
    }
}
