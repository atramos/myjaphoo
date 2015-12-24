package org.myjaphoo.gui.movietree;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.movietree.characteristics.TagProposalCharacteristics;
import org.myjaphoo.model.grouping.TokenAssignemntProposolerPartialGrouper;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StructureCharacteristicsFactory
 *
 * @author mla
 * @version $Id$
 */
public class StructureCharacteristicsFactory {

    public static final StructureCharacteristics NO_CHARACTERISTICS = new StructureCharacteristics() {
        @Override
        public List<JMenuItem> getMenuItems(MyjaphooController controller, MovieStructureNode structureNode, ViewContext context) {
            return Collections.EMPTY_LIST;
        }
    };

    private static Map<String, StructureCharacteristics> map = new HashMap();

    static {
        addStructureCharacteristics(TokenAssignemntProposolerPartialGrouper.PATHMARKER_TAGPROPOSALGROUP, new TagProposalCharacteristics());
    }

    public static void addStructureCharacteristics(String marker, StructureCharacteristics structureCharacteristics) {
        map.put(marker, structureCharacteristics);
    }

    public static StructureCharacteristics getCharacteristics(String pathMarker) {
        StructureCharacteristics sc = map.get(pathMarker);
        if (sc == null) {
            return NO_CHARACTERISTICS;
        } else {
            return sc;
        }
    }
}
