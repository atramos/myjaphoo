/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.perspective;

import net.infonode.docking.*;
import net.infonode.docking.View;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import org.myjaphoo.LookAndFeels;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.bookmark.BookMarkPanel;
import org.myjaphoo.gui.changelog.ChangeLogPanel;
import org.myjaphoo.gui.chronic.ChronicPanel;
import org.myjaphoo.gui.db.DatabaseConnectionPanel;
import org.myjaphoo.gui.errors.ErrorsPanel;
import org.myjaphoo.gui.files.FilePanel;
import org.myjaphoo.gui.filtereditor.FilterEditorPanel;
import org.myjaphoo.gui.groovyshell.ScriptPanel;
import org.myjaphoo.gui.groupbypanel.GroupByPanel;
import org.myjaphoo.gui.infopanel.InfoPanel;
import org.myjaphoo.gui.logpanel.LogPanel;
import org.myjaphoo.gui.messages.MessagePanel;
import org.myjaphoo.gui.metaToken.MetaTokenPanel;
import org.myjaphoo.gui.movieprops.PropertiesPanel2;
import org.myjaphoo.gui.movietree.MovieTreePanel;
import org.myjaphoo.gui.previewpanel.PicturePreviewPanel;
import org.myjaphoo.gui.scripting.Scripting;
import org.myjaphoo.gui.systemprops.SystemPropsPanel;
import org.myjaphoo.gui.thumbtable.ThumbPanel;
import org.myjaphoo.gui.token.TokenPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static org.myjaphoo.gui.perspective.View.*;

/**
 * @author mla
 */
public class ViewParts {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/perspective/resources/ViewParts");
    public static final Logger LOGGER = LoggerFactory.getLogger(ViewParts.class.getName());
    public static final String WMDOCKINGSTATE_DIR = "savedperspectives"; //NOI18N
    public static final String WMDOCKINGSTATE_FILENAME = "dockingstate.ser"; //NOI18N
    private InfoPanel infoPanel;
    private LogPanel logPanel;
    private ThumbPanel thumbPanel;
    private MovieTreePanel movieTreePanel;
    private TokenPanel tokenpanel;
    private MetaTokenPanel metaTokenpanel;
    private ChronicPanel chronicpanel;
    private SystemPropsPanel systemPropsPanel;
    private ChangeLogPanel changelogpanel;
    private BookMarkPanel bookmarkpanel;
    private PicturePreviewPanel previewpanel;
    private PropertiesPanel2 propertiesPanel;
    private GroupByPanel groupByPanel;
    private MessagePanel messagePanel;
    private DatabaseConnectionPanel dbPanel;
    private FilterEditorPanel filterEditorPanel;
    private ErrorsPanel errorPanel;
    private ScriptPanel groovyShellPanel;
    private FilePanel filePanel;
    private MJView infoView;
    private MJView logView;
    private MJView thumbView;
    private MJView movieTreeView;
    private MJView tokenView;
    private MJView metaTokenView;
    private MJView chronicView;
    private MJView changelogView;
    private MJView bookmarkView;
    private MJView previewView;
    private MJView propertiesView;
    private MJView groupByView;
    private MJView messageView;
    private MJView dbView;
    private MJView filterView;
    private MJView errorView;
    private MJView fileView;
    private MJView systemPropsView;
    private MJView groovyShellView;

    /** das root docking window. */
    private RootWindow rootWindow;
    private MyjaphooController controller;
    private ViewMap viewMap = new ViewMap();

    /**
     * Helper class to handle view management.
     */
    public class MJView<T extends JPanel> extends View {

        public final org.myjaphoo.gui.perspective.View viewEnum;
        public final T panel;

        public MJView(String localeHeaderTitleKey, T panel, org.myjaphoo.gui.perspective.View viewEnum) {
            super(localeBundle.getString(localeHeaderTitleKey), null, panel);
            this.panel = panel;
            this.viewEnum = viewEnum;
            viewMap.addView(viewEnum.ordinal(), this);
        }
    }

    public ViewParts(MyjaphooController controller, Scripting scripting) {
        this.controller = controller;
        infoPanel = new InfoPanel(controller);
        logPanel = new LogPanel();
        thumbPanel = new ThumbPanel(controller.getMainThumbController());
        movieTreePanel = new MovieTreePanel(controller.getMainMoviePanelController(), thumbPanel);
        tokenpanel = new TokenPanel(controller.getMainTokenPanelController());
        metaTokenpanel = new MetaTokenPanel(controller.getMainMetaTokenPanelController());
        chronicpanel = new ChronicPanel(controller);
        systemPropsPanel = new SystemPropsPanel(controller);
        changelogpanel = new ChangeLogPanel(controller);
        bookmarkpanel = new BookMarkPanel(controller);
        previewpanel = new PicturePreviewPanel(controller);
        propertiesPanel = new PropertiesPanel2(controller);
        groupByPanel = new GroupByPanel(controller);
        messagePanel = new MessagePanel();
        dbPanel = new DatabaseConnectionPanel(controller);
        filterEditorPanel = new FilterEditorPanel(controller);
        errorPanel = new ErrorsPanel(controller);
        filePanel = new FilePanel(controller);
        groovyShellPanel = new ScriptPanel(controller);

        infoView = new MJView("INFO", infoPanel, InfoView);
        logView = new MJView("LOG", logPanel, LogView);
        thumbView = new MJView("THUMBS", thumbPanel, ThumbView);
        movieTreeView = new MJView("MEDIA", movieTreePanel, MovieTreeView);
        tokenView = new MJView("TAGS", tokenpanel, TokenView);
        metaTokenView = new MJView("METATAGS", metaTokenpanel, MetaTokenView);
        chronicView = new MJView("CHRONIC", chronicpanel, ChronicView);
        changelogView = new MJView("CHANGELOG", changelogpanel, ChangelogView);
        bookmarkView = new MJView("BOOKMARKS", bookmarkpanel, BookmarkView);
        previewView = new MJView("PREVIEW", previewpanel, PreviewView);
        propertiesView = new MJView("PROPERTIES", propertiesPanel, PropertiesView);
        groupByView = new MJView("GROUP BY", groupByPanel, GroupByView);
        messageView = new MJView("MESSAGES", messagePanel, MessageView);
        dbView = new MJView("DATABASECONNECTIONS", dbPanel, DatabaseConnectionView);
        filterView = new MJView("FILTER", filterEditorPanel, FilterView);
        errorView = new MJView("ERRORS", errorPanel, ErrorView);
        fileView = new MJView("FILES", filePanel, FileView);
        systemPropsView = new MJView("SYSTEMPROPS", systemPropsPanel, SystemPropsView);
        groovyShellView = new MJView("GROOVYSHELL", groovyShellPanel, GroovyShellView);
    }

    /**
     * @return the movieTreePanel
     */
    public MovieTreePanel getMovieTreePanel() {
        return movieTreePanel;
    }

    /**
     * @return the thumbPanel
     */
    public ThumbPanel getThumbPanel() {
        return thumbPanel;
    }

    public RootWindow createDockingViews(javax.swing.JMenu jMenuWindows) {

        // build menus for the views:
        buildMenu(jMenuWindows);
        JMenu plafMenus = new JMenu("Look and Feel");
        jMenuWindows.add(plafMenus);
        LookAndFeels.prepareLaFMenus(plafMenus);

        rootWindow = DockingUtil.createRootWindow(viewMap, true);

        setDefaultWindowDockingStructure(rootWindow);

        // set a docking theme:
        DockingWindowsTheme theme = new ShapedGradientDockingTheme();
        rootWindow.getRootWindowProperties().addSuperObject(theme.getRootWindowProperties());
        /*
        RootWindowProperties titleBarStyleProperties =
                PropertiesUtil.createTitleBarStyleRootWindowProperties();
        // Enable title bar style
        rootWindow.getRootWindowProperties().addSuperObject(titleBarStyleProperties);
        */

        rootWindow.setPopupMenuFactory(new WindowPopupMenuFactory() {
            public JPopupMenu createPopupMenu(final DockingWindow window) {
                JPopupMenu menu = new JPopupMenu();
// Check that the window is a View
                if (window instanceof View) {
                    menu.add("Close").addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            window.close();
                        }
                    });
                    //buildMenu(menu);
                }
                return menu;
            }
        });

        return rootWindow;
    }

    private void buildMenu(JMenu jMenuWindows) {
        JMenu viewPartMenu = new JMenu(localeBundle.getString("VIEWS"));
        jMenuWindows.add(viewPartMenu);
        for (int i = 0; i < viewMap.getViewCount(); i++) {
            final View view = viewMap.getViewAtIndex(i);

            JMenuItem item = new JMenuItem(
                    MessageFormat.format(localeBundle.getString("SHOW VIEW"), view.getViewProperties().getTitle()));
            viewPartMenu.add(item);
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    view.restore();
                }
            });
        }
        jMenuWindows.addSeparator();
        JMenu perspectivesMenu = new JMenu(localeBundle.getString("PERSPECTIVS"));
        jMenuWindows.add(perspectivesMenu);
        for (final Perspective perspective : Perspective.values()) {
            JMenuItem item = new JMenuItem(
                    MessageFormat.format(localeBundle.getString("SHOW PERSPECTIVE"), perspective.getGuiName()));
            perspectivesMenu.add(item);
            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setPerspective(perspective);
                }
            });
        }
        scanSavedPerspectives(perspectivesMenu);


        jMenuWindows.addSeparator();
        JMenuItem saveState = new JMenuItem(localeBundle.getString("SAVE WINDOW PERSPECTIVE"));
        jMenuWindows.add(saveState);
        saveState.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = controller.getInputValue(localeBundle.getString("SAVE WINDOW PERSPECTIVE AS"));
                if (filename != null) {
                    saveSerializedState(filename);
                }
            }
        });

        JMenuItem resetState = new JMenuItem(localeBundle.getString("RESET WINDOW PERSPECTIVE"));
        jMenuWindows.add(resetState);
        resetState.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetWindowState();
            }
        });
    }

    private void setDefaultWindowDockingStructure(RootWindow rootWindow) {

        TabWindow upperPart = new TabWindow(new DockingWindow[]{filterView});
        upperPart.getTabWindowProperties().setRespectChildWindowMinimumSize(true);

        TabWindow leftBottomPart = tabbed(tokenView, metaTokenView, previewView);
        leftBottomPart.setSelectedTab(0);

        TabWindow tabGroupByView = new TabWindow(new DockingWindow[]{groupByView});
        tabGroupByView.getTabWindowProperties().setRespectChildWindowMinimumSize(true);

        TabWindow movieAndFiles = tabbed(movieTreeView, fileView);
        movieAndFiles.setSelectedTab(0);
        SplitWindow leftTopPart = horizSplit(0.15f, tabGroupByView, movieAndFiles);
        DockingWindow part1 = horizSplit(0.7f, leftTopPart, leftBottomPart);

        TabWindow infoAndExif = tabbed(infoView, propertiesView);
        SplitWindow part2 = vertSplit(0.8f, thumbView, infoAndExif);
        infoAndExif.setSelectedTab(0);

        TabWindow logAndProps = tabbed(errorView, logView, chronicView, changelogView, bookmarkView, messageView, dbView, systemPropsView);
        logAndProps.setSelectedTab(0);

        SplitWindow thumbAndShell = horizSplit(0.8f, part2, groovyShellView);


        SplitWindow thumbLogPart = horizSplit(0.8f, thumbAndShell, logAndProps);
        SplitWindow mainPart = vertSplit(0.4f, part1, thumbLogPart);

        SplitWindow mainAndFilter = horizSplit(0.1f, upperPart, mainPart);

        rootWindow.setWindow(mainAndFilter);

        rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
        rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);
        infoView.setEnabled(false);

        viewMap.getView(SystemPropsView.ordinal()).minimize();
        viewMap.getView(GroovyShellView.ordinal()).minimize();
        //viewMap.getView(SystemPropsView.ordinal()).close();
        //viewMap.getView(GroovyShellView.ordinal()).close();
    }

    private SplitWindow horizSplit(float weight, DockingWindow v1, DockingWindow v2) {
        return new SplitWindow(false, weight, v1, v2);
    }

    private SplitWindow vertSplit(float weight, DockingWindow v1, DockingWindow v2) {
        return new SplitWindow(true, weight, v1, v2);
    }

    /**
     * for nicer construction of tabbed windows.
     * @param mjviews
     * @return
     */
    private TabWindow tabbed(MJView... mjviews) {
        return new TabWindow(mjviews);
    }

    public void setPerspective(Perspective perspective) {
        List<org.myjaphoo.gui.perspective.View> visibleViews = Arrays.asList(perspective.getVisibleViews());
        for (org.myjaphoo.gui.perspective.View view : org.myjaphoo.gui.perspective.View.values()) {
            if (visibleViews.contains(view)) {
                viewMap.getView(view.ordinal()).restore();
            } else {
                viewMap.getView(view.ordinal()).minimize();
            }
        }
    }

    private void saveSerializedState(String filename) {
        FileOutputStream bos = null;
        {
            File dir = new File(WMDOCKINGSTATE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            ObjectOutputStream out = null;
            try {
                bos = new FileOutputStream(new File(WMDOCKINGSTATE_DIR + "/" + filename)); //NOI18N
                out = new ObjectOutputStream(bos);
                rootWindow.write(out);
                out.close();
            } catch (Exception ex) {
                LOGGER.error("error", ex); //NOI18N
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    LOGGER.error("error", ex); //NOI18N
                }
            }
        }
    }

    private void scanSavedPerspectives(JMenu perspectivesMenu) {
        File dir = new File(WMDOCKINGSTATE_DIR);
        if (dir.exists()) {
            if (dir.listFiles().length > 0) {
                perspectivesMenu.addSeparator();
            }
            for (final File file : dir.listFiles()) {
                JMenuItem item = new JMenuItem(localeBundle.getString("SHOW PERSPECTIVE") + file.getName());
                perspectivesMenu.add(item);
                item.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        readSerializedState(rootWindow, file);
                    }
                });
            }
        }
    }

    private void readSerializedState(RootWindow rootWindow, File serFile) {
        if (serFile.exists()) {
            try {
                rootWindow.read(new ObjectInputStream(new FileInputStream(serFile)));
            } catch (IOException ex) {
                LOGGER.error("error loading serialized file!", ex); //NOI18N
            }
        }
    }

    private void resetWindowState() {
        File serFile = new File(WMDOCKINGSTATE_FILENAME);
        if (serFile.exists()) {
            serFile.delete();
        }
        setDefaultWindowDockingStructure(rootWindow);
    }

    /**
     * @return the tokenpanel
     */
    public TokenPanel getTokenpanel() {
        return tokenpanel;
    }

    /**
     * @return the previewpanel
     */
    public PicturePreviewPanel getPreviewpanel() {
        return previewpanel;
    }

    /**
     * @return the metaTokenpanel
     */
    public MetaTokenPanel getMetaTokenpanel() {
        return metaTokenpanel;
    }

    /**
     * @return the infoPanel
     */
    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    /**
     * @return the propertiesView
     */
    public PropertiesPanel2 getPropertiesPanel() {
        return propertiesPanel;
    }

    public GroupByPanel getGroupByPanel() {
        return groupByPanel;
    }

    /**
     * @return the filterEditorPanel
     */
    public FilterEditorPanel getFilterEditorPanel() {
        return filterEditorPanel;
    }

    public ErrorsPanel getErrorPanel() {
        return errorPanel;
    }
}
