/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.model.db.DataView;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.List;


/**
 * Loader interface, um auf gefilterte movie entries zuzugreigen.
 * Hier giebt es eine konventionelle implementierung, die alles jeweils aus der DB mittels hibernate holt,
 * und eine, die Daten cached u. diese verwendet.
 * @author mla
 */
public interface MovieFilterInterface {

    List<JoinedDataRow> loadFilteredEntries(DataView dataView, List<? extends GroupAlgorithm> groupingAlgorithm) throws ParserException;


    public List<String> getUsedLiterals();
}
