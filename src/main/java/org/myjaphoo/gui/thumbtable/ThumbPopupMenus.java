/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.*;
import org.myjaphoo.gui.action.dbcompare.IntegrateChangesFromComparisonDatabase;
import org.myjaphoo.gui.action.scriptactions.*;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.util.TokenMenuCreation;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.registry.ComponentRegistry;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * @author mla
 */
public class ThumbPopupMenus {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/thumbtable/resources/ThumbPopupMenus");

    public static JPopupMenu createThumbPopupMenu(MyjaphooController controller, ArrayList<AbstractLeafNode> selectedNodes) {
        ViewContext context = new ViewContext(selectedNodes);
        return createThumbPopupMenu(controller, context);
    }

    public static JPopupMenu createThumbPopupMenu(MyjaphooController controller, ViewContext context) {
        JPopupMenu m = new JPopupMenu();

        MovieStructureNode structureNode = findAndCheckSameStructureNodeOfNodes(context.getSelNodes());
        if (structureNode != null) {
            for (JMenuItem menuItem : structureNode.getStructureCharacteristics().getMenuItems(controller, structureNode, context)) {
                m.add(menuItem);
            }
        }
        // add user defined actions from scripts:
        Collection<TagContextAction> entries = ComponentRegistry.registry.getEntryCollection(TagContextAction.class);
        if (entries.size() > 0) {
            m.addSeparator();

            for (TagContextAction entry : entries) {
                int counter = 0;
                for (Token token : context.getAssignedTokens()) {
                    counter++;
                    // max n entries, to not flood up on large context selection
                    if (counter < 5) {
                        TagScriptContextAction action = new TagScriptContextAction(controller, null, entry, Arrays.asList(token));
                        m.add(action);
                    }
                }
            }
        }

        Collection<EntryContextAction> entryEntries = ComponentRegistry.registry.getEntryCollection(EntryContextAction.class);
        if (entryEntries.size() > 0) {
            m.addSeparator();
            for( EntryContextAction entry : entryEntries) {
                MovieScriptContextAction action = new MovieScriptContextAction(controller, context, entry);
                m.add(action);
            }
            m.addSeparator();
        }

        if (context.hasMovies()) {
            m.add(new VLCPlayAction(controller, context));
            m.add(new MPlayerPlayAction(controller, context));
            m.add(new KMPlayerPlayAction(controller, context));
        }
        m.add(new OpenFileExplorer(controller, context));
        m.addSeparator();
        if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            m.add(new DesktopOpenAction(controller, context));
        }
        if (java.awt.Desktop.isDesktopSupported() && java.awt.Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
            m.add(new DesktopEditAction(controller, context));
        }
        m.addSeparator();
        m.add(new CopyFilesAction(controller, context));
        m.add(new CopyFilesWithFullPathAction(controller, context));
        m.add(new CopyFilesWithStructurePathAction(controller, context));
        m.add(new CopyFilesWithoutWmInfoFilesAction(controller, context));
        m.add(new CopyFilesWithFullPathWithoutWMInfoFilesAction(controller, context));
        m.add(new CopyFilesWithStructurePathWithoutWmInfoFilesAction(controller, context));
        if (context.hasDiffNodes()) {
            m.addSeparator();
            m.add(new IntegrateChangesFromComparisonDatabase(controller, context));
        }
        m.addSeparator();
        m.add(new CreateWmInfoFiles(controller, context));
        m.addSeparator();
        m.add(new AddNewTokenAction(controller, context));
        TokenMenuCreation.addLastUsedTokens(m, controller);

        m.add(new AssignTokensViaFilterDialog(controller, context));
        TokenMenuCreation.addTokenSubMenu(controller, m, context);
        m.addSeparator();

        m.add(new RemoveTokenRelations(controller, context));
        m.addSeparator();
        JMenu subMenuRating = new JMenu(localeBundle.getString("RATING"));
        m.add(subMenuRating);
        for (Rating rating : Rating.values()) {
            subMenuRating.add(new SetRatingAction(controller, rating, context));
        }
        m.addSeparator();
        JMenu subMenuNewView = new JMenu(localeBundle.getString("NEW VIEW"));
        m.add(subMenuNewView);
        subMenuNewView.add(new OpenViewAndSelectNode(controller, context));
        subMenuNewView.add(new FilterToSelection(controller, context));
        subMenuNewView.addSeparator();
        TokenMenuCreation.addMenusToFilterToTokens(controller, subMenuNewView, context);
        m.addSeparator();
        m.add(new RemoveEntryAction(controller, context));
        m.addSeparator();
        m.add(new DeleteFilesAction(controller, context));
        m.add(new DeleteCondensedDuplicates(controller, context));
        m.addSeparator();
        m.add(new RecreateThumbnailsAction(controller, context));
        m.add(new AddCoverFrontPicture(controller, context));
        m.add(new AddCoverBackPicture(controller, context));
        m.addSeparator();
        m.add(new GetMovInfos(controller, context));

        return m;
    }

    /**
     * check, if all the selected nodes have the same parent structure node.
     * If this is the case, then return this structure node, otherwise return null.
     * If it is the case, then it would mean, that special actions could be possible for this same kind of nodes.
     *
     * @param selectedNodes
     * @return
     */
    private static MovieStructureNode findAndCheckSameStructureNodeOfNodes(List<AbstractLeafNode> selectedNodes) {
        MovieStructureNode foundNode = null;
        for (AbstractLeafNode node : selectedNodes) {
            if (node.getParent() instanceof MovieStructureNode) {
                if (foundNode == null) {
                    foundNode = (MovieStructureNode) node.getParent();
                } else {
                    if (foundNode != node.getParent()) {
                        return null;
                    }
                }
            } else {
                return null;
            }
        }
        return foundNode;
    }

}
