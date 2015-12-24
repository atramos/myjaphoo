/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.action.AddTokenAction;
import org.myjaphoo.gui.action.LastActionContextActionWrapper;
import org.myjaphoo.gui.action.OpenViewWithFilter;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.action.metatoken.MetaTokenAssignment;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.db.TokenType;
import org.myjaphoo.model.filterparser.idents.Identifiers;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.swing.*;
import java.util.*;


/**
 *
 * @author mla
 */
public class TokenMenuCreation {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/util/resources/TokenMenuCreation");

    public static void addLastUsedTokens(JPopupMenu m, MyjaphooController controller) {
        JMenu lastTokens = new JMenu(localeBundle.getString("LAST USED TAGS"));
        m.add(lastTokens);
        for (Token token : controller.getLastUsedTokens()) {
            lastTokens.add(new LastActionContextActionWrapper(controller, new AddTokenAction(controller, token, createIconForToken(token), ViewContext.EMPTYCONTEXT)));
        }
    }

    public static void addTokenSubMenu(MyjaphooController controller, JPopupMenu m, ViewContext context) {
        Set<Token> assignedTokensOfSelectedNodes = context.getAssignedTokens();
        // to not overload with lots of menus/submenus, disply this only if at max 7 tags are assigned to the
        // current context at all:
        if (assignedTokensOfSelectedNodes.size() < 7) {
            addMetaTokenAssignments(controller, m, assignedTokensOfSelectedNodes);
        }
    }

    private static void addTokensABC(JPopupMenu m, List<Token> tokens, MyjaphooController controller, ViewContext context) {
        JMenu subMenuAddExistingToken = new JMenu(localeBundle.getString("ADD TAG (BY ABC)"));
        m.add(subMenuAddExistingToken);
        MenuABCStructurizer structurizer = new MenuABCStructurizer(true);
        for (Token token : tokens) {
            createTokenMenuItem(structurizer, controller, token, context);
        }
        structurizer.structurize(subMenuAddExistingToken, 30);
    }

    private static void createTokenMenuItem(MenuABCStructurizer structurizer, MyjaphooController controller, Token token, ViewContext context) {
        JMenuItem menuItem = createAddTokenMenu(controller, token, context);
        structurizer.add(menuItem, token.getName(), null);
    }

    public static JMenuItem createAddTokenMenu(MyjaphooController controller, Token token, ViewContext context) {
        return new JMenuItem(new AddTokenAction(controller, token, createIconForToken(token), context));
    }

    public static void addMetaTokenAssignments(MyjaphooController controller, JPopupMenu m, Collection<Token> tokens) {
        m.addSeparator();
        List<MetaToken> allMetaTokens = CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList();
        Collections.sort(allMetaTokens);
        MenuStructurizer mABCStructurizer = new MenuABCStructurizer(false);
        for (Token token : tokens) {
            JMenu tokMen = new JMenu(token.getName() + " ->"); //NOI18N
            mABCStructurizer.add(tokMen, token.getName() + " ->", null);

            for (MetaToken mtok : allMetaTokens) {
                if (!token.getAssignedMetaTokens().contains(mtok)) {
                    tokMen.add(new MetaTokenAssignment(controller, token, mtok));
                }
            }
        }
        smartStructurize(mABCStructurizer, m, "Assign MetaTags");
    }

    private static void smartStructurize(MenuStructurizer mABCStructurizer, JComponent m, String subMenuText) {
        if (mABCStructurizer.entrySize() > 10) {
            JMenu subMenu = new JMenu(subMenuText);
            m.add(subMenu);
            mABCStructurizer.structurize(subMenu, 10);
        } else {
            mABCStructurizer.structurize(m, 10);
        }
    }
    private static final List<GroupingDim> GroupTokAndDir = Arrays.asList(GroupingDim.Token, GroupingDim.Directory);
    private static final List<GroupingDim> GroupTokDirHierarchical = Arrays.asList(GroupingDim.TokenHierarchy, GroupingDim.Directory);
    private static final List<GroupingDim> GroupMetaTokHierarchyAndTokAndDir = Arrays.asList(GroupingDim.MetatokenHierarchy, GroupingDim.Token, GroupingDim.Directory);

    public static void addMenusToFilterToTokens(MyjaphooController controller, JMenu m, ViewContext context) {

        Set<Token> assignedTokensOfSelectedNodes = context.getAssignedTokens();
        if (assignedTokensOfSelectedNodes.size() > 0) {
            addMenusToFilterToTokens(controller, m, assignedTokensOfSelectedNodes);
        }
    }

    public static void addMenusToFilterToTokens(MyjaphooController controller, JMenu m, Collection<Token> tokens) {

        MenuStructurizer mABCStructurizer = new MenuABCStructurizer(false);
        for (Token token : tokens) {

            OpenViewWithFilter openViewWithTagMenu = createTagOV(controller, token);
            mABCStructurizer.add(openViewWithTagMenu, token.getName(), null);

        }
        smartStructurize(mABCStructurizer, m, "Open View by Tag");
        m.addSeparator();

        // build distinct sets:
        Set<MetaToken> allDistinctMetatoks = new TreeSet<MetaToken>();
        Set<TokenType> allDistinctTokTypes = EnumSet.noneOf(TokenType.class);
        Set<Token> allDistinctParents = new TreeSet<Token>();
        Set<MetaToken> allDistinctMetatokParents = new TreeSet<MetaToken>();
        for (Token token : tokens) {
            allDistinctTokTypes.add(token.getTokentype());
            if (token.getParent() != null) {
                allDistinctParents.add(token.getParent());
            }
            for (MetaToken mt : token.getAssignedMetaTokens()) {
                allDistinctMetatoks.add(mt);
                if (mt.getParent() != null) {
                    allDistinctMetatokParents.add(mt.getParent());
                }
            }
        }

        if (allDistinctMetatoks.size() > 0) {
            mABCStructurizer = new MenuABCStructurizer(false);
            for (MetaToken mt : allDistinctMetatoks) {
                mABCStructurizer.add(createMetaTagOV(controller, mt), mt.getName(), null);
            }
            smartStructurize(mABCStructurizer, m, "Open View by MetaTag");
            m.addSeparator();
        }

        if (allDistinctTokTypes.size() > 0) {
            mABCStructurizer = new MenuABCStructurizer(false);
            for (TokenType tt : allDistinctTokTypes) {
                mABCStructurizer.add(createTagTypeOV(controller, tt), tt.getGuiName(), null);
            }
            smartStructurize(mABCStructurizer, m, "Open View by Tag type");
            m.addSeparator();
        }

        if (allDistinctParents.size() > 0) {
            mABCStructurizer = new MenuABCStructurizer(false);
            for (Token parent : allDistinctParents) {
                mABCStructurizer.add(createTagParentsOV(controller, parent), parent.getName(), null);
            }
            smartStructurize(mABCStructurizer, m, "Open View by Tag parents");
            m.addSeparator();
        }
        mABCStructurizer = new MenuABCStructurizer(false);
        for (MetaToken parent : allDistinctMetatokParents) {
            mABCStructurizer.add(createMetaTagParentsOV(controller, parent), parent.getName(), null);
        }
        smartStructurize(mABCStructurizer, m, "Open View by MetaTag parents");
    }

    public static OpenViewWithFilter createMetaTagParentsOV(MyjaphooController controller, MetaToken parent) {
        String expr = Identifiers.METATAG.getName() + "." + Identifiers.PARENTS.getName() + " is '" + parent.getName() + "'"; //NOI18N
        return new OpenViewWithFilter(controller, expr, GroupMetaTokHierarchyAndTokAndDir);
    }

    public static OpenViewWithFilter createTagParentsOV(MyjaphooController controller, Token parent) {
        String expr = Identifiers.METATAG.getName() + "." + Identifiers.PARENTS.getName() + " is '" + parent.getName() + "'"; //NOI18N
        return new OpenViewWithFilter(controller, expr, GroupTokDirHierarchical);
    }

    public static OpenViewWithFilter createTagTypeOV(MyjaphooController controller, TokenType tt) {
        String expr = Identifiers.TAGTYPE.getName() + " is '" + tt.name() + "'"; //NOI18N
        return new OpenViewWithFilter(controller, expr, GroupTokAndDir);
    }

    public static OpenViewWithFilter createMetaTagOV(MyjaphooController controller, MetaToken mt) {
        String expr = Identifiers.METATAG.getName() + " is '" + mt.getName() + "'"; //NOI18N
        return new OpenViewWithFilter(controller, expr, GroupTokAndDir);
    }

    public static OpenViewWithFilter createTagOV(MyjaphooController controller, Token token) {
        String expr = Identifiers.TAG.getName() + " is '" + token.getName() + "'"; //NOI18N
        return new OpenViewWithFilter(controller, expr);
    }

    static class DelayedMiniIcon extends AbstractLazyIcon {

        private MovieEntry entry;

        public DelayedMiniIcon(MovieEntry entry) {
            this.entry = entry;
        }

        @Override
        protected ImageIcon createIcon(ThumbIsLoadedCallback callBack) {
            return ThreadedThumbCache.getInstance().getThumb(entry, 0, true, 22, ThumbTypeDisplayMode.NORMAL, callBack);
        }
    };

    private static Icon createIconForToken(Token token) {
        if (token.getMovieEntries().isEmpty()) {
            return null;
        }
        MovieEntry nextBest = token.getMovieEntries().iterator().next();
        return new DelayedMiniIcon(nextBest);
        //return cache.getThumb(nextBest, 22);
    }

    private static void addTokensByTreeStructure(JPopupMenu m, MyjaphooController controller, ViewContext context) {

        JMenu subMenuAddExistingToken = new JMenu(localeBundle.getString("ADD TAG (BY STRUCTURE)"));
        m.add(subMenuAddExistingToken);
        Token root = CacheManager.getCacheActor().getImmutableModel().getRootToken();
        if (root != null) {
            recursiveTreeBuild(root, subMenuAddExistingToken, controller, context);
        }
    }

    private static void recursiveTreeBuild(Token parent, JMenu parentMenu, MyjaphooController controller, ViewContext context) {
        JMenu subMenuAddExistingToken = new JMenu(parent.getName());
        parentMenu.add(subMenuAddExistingToken);
        MenuABCStructurizer structurizer = new MenuABCStructurizer(true);
        for (Token child : parent.getChildren()) {

            if (child.getChildren().size() > 0) {
                recursiveTreeBuild(child, subMenuAddExistingToken, controller, context);
            } else {
                createTokenMenuItem(structurizer, controller, child, context);
            }
        }
        structurizer.structurize(subMenuAddExistingToken, 30);
    }

    private static void addTokensByType(JPopupMenu m, List<Token> tokens, MyjaphooController controller, ViewContext context) {
        JMenu subMenuAddExistingToken = new JMenu(localeBundle.getString("ADD TAG (BY TYPES)"));
        m.add(subMenuAddExistingToken);

        for (TokenType type : TokenType.values()) {
            JMenu groupMenu = new JMenu(type.getGuiName());
            subMenuAddExistingToken.add(groupMenu);
            MenuABCStructurizer structurizer = new MenuABCStructurizer(true);
            for (Token token : tokens) {
                if (token.getTokentype() == type) {
                    createTokenMenuItem(structurizer, controller, token, context);
                }
            }
            structurizer.structurize(groupMenu, 30);
        }
    }
}
