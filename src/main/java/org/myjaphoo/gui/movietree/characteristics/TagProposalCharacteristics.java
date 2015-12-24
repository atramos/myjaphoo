package org.myjaphoo.gui.movietree.characteristics;

import org.apache.commons.lang.StringUtils;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.StructureCharacteristics;
import org.myjaphoo.gui.util.TokenMenuCreation;
import org.myjaphoo.model.db.Token;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Characteristics for tag proposal movie structure nodes
 *
 * @author mla
 * @version $Id$
 */
public class TagProposalCharacteristics implements StructureCharacteristics {
    @Override
    public List<JMenuItem> getMenuItems(MyjaphooController controller, MovieStructureNode structureNode, ViewContext context) {
        ArrayList<JMenuItem> result = new ArrayList<>();
        Token token =   findToken(controller, structureNode.getName());
        if (token != null) {
            result.add(TokenMenuCreation.createAddTokenMenu(controller, token, context));
        }
        return result;
    }

    private Token findToken(MyjaphooController controller, String name) {
        for (Token token: controller.getTokens()) {
            if (StringUtils.equals(token.getName(), name)) {
                return token;
            }
        }
        return null;
    }
}
