/*
 * WankmanView.java
 */
package org.myjaphoo;

import groovy.lang.Closure;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskMonitor;
import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.mlsoft.common.acitivity.events.*;
import org.mlsoft.common.prefs.ui.GenericPrefDialog;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.mlsoft.swing.EventDispatchTools;
import org.myjaphoo.gui.BookMarkMenuCreator;
import org.myjaphoo.gui.EdtDispatcherPropertyChangeListener;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.action.*;
import org.myjaphoo.gui.action.db.SwitchToOtherDatabase;
import org.myjaphoo.gui.action.dbcompare.CloseComparatorDatabase;
import org.myjaphoo.gui.action.dbcompare.OpenAgainComparatorDatabase;
import org.myjaphoo.gui.action.historychange.HistoryBackAction;
import org.myjaphoo.gui.action.historychange.HistoryForwardAction;
import org.myjaphoo.gui.action.scriptactions.CommonAction;
import org.myjaphoo.gui.action.scriptactions.CommonScriptAction;
import org.myjaphoo.gui.db.DBConfigChangeEvent;
import org.myjaphoo.gui.errors.ErrorUpdateEvent;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.infopanel.InfoPanel;
import org.myjaphoo.gui.movieprops.PropertiesPanel2;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.events.StructTypeChangedEvent;
import org.myjaphoo.gui.movietree.events.UserDefinedStructureChangedEvent;
import org.myjaphoo.gui.perspective.ViewParts;
import org.myjaphoo.gui.picmode.FullPictureView;
import org.myjaphoo.gui.previewpanel.PicturePreviewPanel;
import org.myjaphoo.gui.repath.RelPathSubstitutionDialog;
import org.myjaphoo.gui.thumbtable.ThumbPanel;
import org.myjaphoo.gui.util.TextRepresentations;
import org.myjaphoo.gui.util.Utils;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.config.DatabaseConfigLoadSave;
import org.myjaphoo.model.db.ChronicEntry;
import org.myjaphoo.model.dbcompare.ComparisonMethod;
import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.grouping.GroupingDim;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.registry.ComponentRegistry;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * The application's main frame.
 */
public class MyjaphooView extends FrameView {

    private static int viewCounter = 0;
    private int viewNumber = 0;
    public static final Logger LOGGER = LoggerFactory.getLogger(MyjaphooView.class.getName());
    private UndoManager manager = new UndoManager();
    private MyjaphooController controller = new MyjaphooController(this);
    private BookMarkMenuCreator bookMarkMenuCreator = new BookMarkMenuCreator(controller);
    private ViewParts viewParts = new ViewParts(controller, controller.getScripting());
    private javax.swing.Action closeComparatorDatabase = new CloseComparatorDatabase(controller);
    private ButtonGroup dbComparisonButtonGroup = new ButtonGroup();
    private JMenuBar bookmarkToolbar = new JMenuBar();

    javax.swing.JMenu fileMenu = new javax.swing.JMenu();

    public void setActionStarted() {
        if (!busyIconTimer.isRunning()) {
            statusAnimationLabel.setIcon(busyIcons[0]);
            busyIconIndex = 0;
            busyIconTimer.start();
        }
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
    }

    public void setActionStopped() {
        busyIconTimer.stop();
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        progressBar.setValue(0);
    }

    private void updateUndoButtons() {
        jMenuUndo.setText(manager.getUndoPresentationName());
        jMenuRedo.setText(manager.getRedoPresentationName());
        jMenuUndo.getParent().validate();
        jMenuUndo.setEnabled(manager.canUndo());
        jMenuRedo.setEnabled(manager.canRedo());
    }

    public MyjaphooView(MyjaphooApp app, MovieNode node2DisplayInitially) {
        this(app);

        // wir müssen warten, bis wir zur node springen können.
        // da der Baum erst in einem separaten thread erzeugt wird.
        // mittels de ignoreFlags können wir prüfen, ob der Hintergrund prozess noch läuft:
        // FIXME : wait via event (or actor message!?) or something instead!!!!
        //while (controller.isIgnoreEvents()) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            LOGGER.error("error", ex);
        }
        //}
        // jetzt erst die node selektieren, vorher würde sie ja noch gar nicht existieren:
        viewParts.getMovieTreePanel().selectNode(node2DisplayInitially);
    }

    /**
     * View mit einer bestimmten Filter-Expression öffnen.
     * @param app
     * @param filterExpression
     */
    public MyjaphooView(MyjaphooApp app, String filterExpression) {
        super(app);
        controller.getFilter().setFilterPattern(filterExpression);
        initEverything();
    }

    /**
     * View mit einer bestimmten Filter-Expression öffnen und mit einer
     * bestimmten Strukturierung öffnen.
     * @param app
     * @param filterExpression
     */
    public MyjaphooView(MyjaphooApp app, String filterExpression, List<GroupingDim> hierarchy) {
        super(app);
        controller.getFilter().setFilterPattern(filterExpression);
        controller.getFilter().setUserDefinedStructure(hierarchy);
        controller.getFilter().setUserDefinedStructureActivated(true);
        initEverything();
    }

    public MyjaphooView(MyjaphooApp app, ChronicEntry chronicEntry) {
        super(app);
        controller.getFilter().popChronic(chronicEntry);
        initEverything();
    }

    public MyjaphooView(MyjaphooApp app) {
        super(app);
        initEverything();

    }

    /**
     * Führt alle anfänglichen Initialisierungen des Views durch.
     * Wird von den Konstruktoren aufgerufen.
     */
    private void initEverything() {

        initComponents();

        // use DWG docking windows:


        // build jpanel from mainpanel + bookmarktoolbar:
        JPanel mainComponentPanel = new JPanel(new BorderLayout());
        mainComponentPanel.add(bookmarkToolbar, BorderLayout.NORTH);

        mainComponentPanel.add(viewParts.createDockingViews(jMenuWindows), BorderLayout.CENTER);

        controller.getScripting().createOrUpdateScriptingMenu(jMenuScripting);

        setComponent(mainComponentPanel);

        updateUndoButtons();
        viewParts.getFilterEditorPanel().setFilterInfoText(controller.getFilter().getFilterInfoText());

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        // todo -> use chanellemanager und stacked channels/progresses
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    setStatusMessage(text);
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        fillLastFilterExpressions();
        viewNumber = ++viewCounter;
        String title = resourceMap.getString("Application.title");
        String databasename = MyjaphooAppPrefs.PRF_DATABASENAME.getVal();
        String fullTitle = databasename + " " + title + " View " + Integer.toString(viewNumber);
        this.getFrame().setTitle(fullTitle);
        this.getFrame().setIconImage(Icons.IR_APP16.icon.getImage());

        Utils.registerKeyAction(viewParts.getThumbPanel(), KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.ALT_DOWN_MASK), "newTagToSelectedThumbs", new LastActionContextActionWrapper(controller, new AddNewTokenAction(controller, ViewContext.EMPTYCONTEXT)));
        Utils.registerKeyAction(viewParts.getThumbPanel(), KeyStroke.getKeyStroke(KeyEvent.VK_A, java.awt.event.InputEvent.ALT_DOWN_MASK), "assignTokensToSelectedThumbs", new LastActionContextActionWrapper(controller, new AssignTokensViaFilterDialog(controller, ViewContext.EMPTYCONTEXT)));
        Utils.registerKeyAction(viewParts.getThumbPanel(), KeyStroke.getKeyStroke(KeyEvent.VK_D, java.awt.event.InputEvent.ALT_DOWN_MASK), "deleteSelectedThumbs", new LastActionContextActionWrapper(controller, new DeleteFilesAction(controller, ViewContext.EMPTYCONTEXT)));
        Utils.registerKeyAction(viewParts.getThumbPanel(), KeyStroke.getKeyStroke((char) KeyEvent.VK_DELETE), "deleteSelectedThumbs", new LastActionContextActionWrapper(controller, new DeleteFilesAction(controller, ViewContext.EMPTYCONTEXT)));
        Utils.registerKeyAction(viewParts.getThumbPanel(), KeyStroke.getKeyStroke((char) KeyEvent.VK_BACK_SPACE), "deleteSelectedThumbs", new LastActionContextActionWrapper(controller, new DeleteFilesAction(controller, ViewContext.EMPTYCONTEXT)));

        Utils.registerKeyAction(viewParts.getThumbPanel(), KeyStroke.getKeyStroke(KeyEvent.VK_R, java.awt.event.InputEvent.ALT_DOWN_MASK), "removeTokenRelation", new LastActionContextActionWrapper(controller, new RemoveTokenRelations(controller, ViewContext.EMPTYCONTEXT)));

        Utils.registerKeyAction(mainComponentPanel, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, java.awt.event.InputEvent.ALT_DOWN_MASK), "historyBack", new HistoryBackAction(controller));
        Utils.registerKeyAction(mainComponentPanel, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, java.awt.event.InputEvent.ALT_DOWN_MASK), "historyForward", new HistoryForwardAction(controller));

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
                new KeyEventDispatcher() {

                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == KeyEvent.KEY_TYPED) {
                            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                                if (!FullPictureView.exeEscAction()) {
                                    MyjaphooApp.getApplication().iconifyAllViews();
                                }
                                return true;
                            }
                        }
                        boolean discardEvent = false;
                        return discardEvent;
                    }
                });

        // init compare database menu
        fillDatabaseComparisonMenus();

        fillCommonScriptMenus();

        // register by the controllers eventbus to recieve events:
        controller.getEventBus().register(this);
        // we are also interested in application global events:
        GlobalBus.bus.register(this);

        this.getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);

        // register listener on bookmark changes:
        MainApplicationController.getInstance().getBookmarkList().addPropertyChangeListener(new EdtDispatcherPropertyChangeListener() {
            @Override
            public void propertyChangeDispatchedOnEdt(PropertyChangeEvent evt) {
                updateBookmark();
            }
        });
        MainApplicationController.getInstance().getScriptList().addPropertyChangeListener(new EdtDispatcherPropertyChangeListener() {
            @Override
            public void propertyChangeDispatchedOnEdt(PropertyChangeEvent evt) {
                updateScripts();
            }
        });
        updateAllViews();

        if (MyjaphooAppPrefs.PRF_PLAF_FRAME_DECORATED.getVal()) {
            this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            this.getFrame().setUndecorated(true);
        }

        SubstanceLookAndFeel.setWidgetVisible(this.getRootPane(), true, SubstanceConstants.SubstanceWidgetType.MENU_SEARCH,
                SubstanceConstants.SubstanceWidgetType.TITLE_PANE_HEAP_STATUS);

    }

    private void fillCommonScriptMenus() {
        Collection<CommonAction> entries = ComponentRegistry.registry.getEntryCollection(CommonAction.class);
        if (entries.size() > 0) {
            fileMenu.addSeparator();
            for (CommonAction entry : entries) {
                CommonScriptAction action = new CommonScriptAction(controller, null, entry);
                fileMenu.add(action);
            }
        }
    }

    public ArrayList<AbstractLeafNode> getAllSelectedMovieNodes() {
        return viewParts.getThumbPanel().getAllSelectedMovieNodes();
    }

    private void refreshTokenRelevantThings() {
        StopWatch w = new StopWatch();
        w.start();
        viewParts.getTokenpanel().refreshView();
        viewParts.getMetaTokenpanel().refreshView();

        w.stop();
        LOGGER.info("refresh token relevant things (token model, popups, table popups) duration: " + w.toString());
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = getFrame();
            aboutBox = new MyjaphooAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MyjaphooApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();

        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuDBPrefs = new JMenuItem();

        jSeparator6 = new javax.swing.JSeparator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuUndo = new javax.swing.JMenuItem();
        jMenuRedo = new javax.swing.JMenuItem();
        jMenuChronic = new javax.swing.JMenu();
        jMenuBookmarks = new javax.swing.JMenu();
        jMenuCompare = new javax.swing.JMenu();
        jMenuCompareDatabaseSubMenu = new javax.swing.JMenu();
        jMenuDatabase = new JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
        jMenuScripting = new javax.swing.JMenu();
        jMenuWindows = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jMenuItem12 = new javax.swing.JMenuItem(new ClearEntityManagerCache(controller));
        jMenuItem13 = new javax.swing.JMenuItem(new CompleteRefresh(controller));
        jSeparator8 = new javax.swing.JSeparator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuExperimental = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 947, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(MyjaphooView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem3);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem8);

        jMenuDBPrefs.setText(resourceMap.getString("jMenuDBPrefs.text")); // NOI18N
        jMenuDBPrefs.setName("jMenuDBPrefs"); // NOI18N
        jMenuDBPrefs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDBPrefsActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuDBPrefs);

        jSeparator6.setName("jSeparator6"); // NOI18N
        fileMenu.add(jSeparator6);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText(resourceMap.getString("jMenuItem11.text")); // NOI18N
        jMenuItem11.setName("jMenuItem11"); // NOI18N
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem11);

        jSeparator5.setName("jSeparator5"); // NOI18N
        fileMenu.add(jSeparator5);

        exitMenuItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getApplication().quit(e);
            }
        }); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N

        jMenuUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMenuUndo.setText(resourceMap.getString("jMenuUndo.text")); // NOI18N
        jMenuUndo.setName("jMenuUndo"); // NOI18N
        jMenuUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuUndoActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuUndo);

        jMenuRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuRedo.setText(resourceMap.getString("jMenuRedo.text")); // NOI18N
        jMenuRedo.setName("jMenuRedo"); // NOI18N
        jMenuRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuRedoActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuRedo);

        menuBar.add(jMenu3);

        jMenuChronic.setText(resourceMap.getString("jMenuChronic.text")); // NOI18N
        jMenuChronic.setName("jMenuChronic"); // NOI18N
        menuBar.add(jMenuChronic);

        jMenuBookmarks.setText(resourceMap.getString("jMenuBookmarks.text")); // NOI18N
        jMenuBookmarks.setName("jMenuBookmarks"); // NOI18N
        menuBar.add(jMenuBookmarks);

        jMenuCompare.setText(resourceMap.getString("jMenuCompare.text")); // NOI18N
        jMenuCompare.setName("jMenuCompare"); // NOI18N

        jMenuCompareDatabaseSubMenu.setText(resourceMap.getString("jMenuCompareDatabaseSubMenu.text")); // NOI18N
        jMenuCompareDatabaseSubMenu.setName("jMenuCompareDatabaseSubMenu"); // NOI18N
        jMenuCompare.add(jMenuCompareDatabaseSubMenu);

        jMenuItem7.setAction(closeComparatorDatabase);
        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenuCompare.add(jMenuItem7);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jMenuCompare.add(jSeparator4);

        dbComparisonButtonGroup.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText(resourceMap.getString("jRadioButtonMenuItem1.text")); // NOI18N
        jRadioButtonMenuItem1.setName("jRadioButtonMenuItem1"); // NOI18N
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenuCompare.add(jRadioButtonMenuItem1);

        dbComparisonButtonGroup.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setText(resourceMap.getString("jRadioButtonMenuItem2.text")); // NOI18N
        jRadioButtonMenuItem2.setName("jRadioButtonMenuItem2"); // NOI18N
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenuCompare.add(jRadioButtonMenuItem2);

        dbComparisonButtonGroup.add(jRadioButtonMenuItem3);
        jRadioButtonMenuItem3.setText(resourceMap.getString("jRadioButtonMenuItem3.text")); // NOI18N
        jRadioButtonMenuItem3.setName("jRadioButtonMenuItem3"); // NOI18N
        jRadioButtonMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem3ActionPerformed(evt);
            }
        });
        jMenuCompare.add(jRadioButtonMenuItem3);

        menuBar.add(jMenuCompare);

        jMenuScripting.setText(resourceMap.getString("jMenuScripting.text")); // NOI18N
        jMenuScripting.setName("jMenuScripting"); // NOI18N
        menuBar.add(jMenuScripting);

        jMenuWindows.setText(resourceMap.getString("jMenuWindows.text")); // NOI18N
        jMenuWindows.setName("jMenuWindows"); // NOI18N
        menuBar.add(jMenuWindows);

        jMenuDatabase.setText(resourceMap.getString("jMenuDatabase.text")); // NOI18N
        menuBar.add(jMenuDatabase);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutBox();
            }
        });
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        aboutMenuItem.setText("about");
        helpMenu.add(aboutMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        helpMenu.add(jSeparator2);

        jSeparator7.setName("jSeparator7"); // NOI18N
        helpMenu.add(jSeparator7);

        jMenuItem12.setText(resourceMap.getString("jMenuItem12.text")); // NOI18N
        jMenuItem12.setName("Clear Entity Manager Cache");
        helpMenu.add(jMenuItem12);

        jMenuItem13.setText(resourceMap.getString("jMenuItem13.text")); // NOI18N
        jMenuItem13.setName("jMenuItem13"); // NOI18N
        jMenuItem13.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        helpMenu.add(jMenuItem13);

        jSeparator8.setName("jSeparator8"); // NOI18N
        helpMenu.add(jSeparator8);

        jMenuItem14.setAction(new ShowProperties(controller));
        jMenuItem14.setText(resourceMap.getString("jMenuItem14.text")); // NOI18N
        jMenuItem14.setName("jMenuItem14"); // NOI18N
        helpMenu.add(jMenuItem14);

        menuBar.add(helpMenu);

        jMenuExperimental.setText(resourceMap.getString("jMenuExperimental.text")); // NOI18N
        jMenuExperimental.setName("jMenuExperimental"); // NOI18N

        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenuExperimental.add(jMenuItem2);

        menuBar.add(jMenuExperimental);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 777, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        RelPathSubstitutionDialog dlg = new RelPathSubstitutionDialog(getFrame(), controller);
        dlg.setLocationRelativeTo(getFrame());
        MyjaphooApp.getApplication().show(dlg);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        controller.showImportDialog();

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuUndoActionPerformed
        try {
            manager.undo();
        } catch (CannotUndoException ex) {
            LOGGER.error("can not undo!", ex);
        } finally {
            updateUndoButtons();
        }
}//GEN-LAST:event_jMenuUndoActionPerformed

    private void jMenuRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuRedoActionPerformed
        try {
            manager.redo();

        } catch (CannotRedoException ex) {
            LOGGER.error("can not redo!", ex);
        } finally {
            updateUndoButtons();
        }
    }//GEN-LAST:event_jMenuRedoActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // einstellungsdialog öffnen:
        JFrame mainFrame = getFrame();
        GenericPrefDialog dlg = new GenericPrefDialog(MyjaphooAppPrefs.getPrefStructure(), mainFrame, false);
        dlg.setLocationRelativeTo(mainFrame);
        MyjaphooApp.getApplication().show(dlg);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuDBPrefsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // einstellungsdialog öffnen:
        JFrame mainFrame = getFrame();
        GenericPrefDialog dlg = new GenericPrefDialog(MyjaphooDBPreferences.getDBPrefStructure(), mainFrame, false);
        dlg.setLocationRelativeTo(mainFrame);
        MyjaphooApp.getApplication().show(dlg);
    }//GEN-LAST:event_jMenuItem8ActionPerformed


    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        if (evt.getSource() == jRadioButtonMenuItem1) {
            DatabaseComparison.getInstance().setComparisonMethod(ComparisonMethod.COMPARE_CHECKSUM);
            updateMovieAndThumbViews();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
        if (evt.getSource() == jRadioButtonMenuItem2) {
            DatabaseComparison.getInstance().setComparisonMethod(ComparisonMethod.COMPARE_PATH);
            updateMovieAndThumbViews();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

    private void jRadioButtonMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem3ActionPerformed
        if (evt.getSource() == jRadioButtonMenuItem3) {
            DatabaseComparison.getInstance().setComparisonMethod(ComparisonMethod.COMPARE_PATH_AND_CHECKSUM);
            updateMovieAndThumbViews();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem3ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        MyjaphooApp.getApplication().show(new MyjaphooView(MyjaphooApp.getApplication()));
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    public void addUndoableEdit(UndoableEdit edit) {
        manager.addEdit(edit);
        updateUndoButtons();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenuBookmarks;
    private javax.swing.JMenu jMenuChronic;
    private javax.swing.JMenu jMenuCompare;
    private javax.swing.JMenu jMenuCompareDatabaseSubMenu;
    private JMenu jMenuDatabase;

    private javax.swing.JMenu jMenuExperimental;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private JMenuItem jMenuDBPrefs;
    private javax.swing.JMenuItem jMenuRedo;
    private javax.swing.JMenu jMenuScripting;
    private javax.swing.JMenuItem jMenuUndo;
    private javax.swing.JMenu jMenuWindows;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private Timer messageTimer;
    private Timer busyIconTimer;
    private Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;



    /**
     * updates the movie tree and the thumbnail views.
     * */
    public void updateMovieAndThumbViews() {
        try {
            viewParts.getThumbPanel().resetModels();
            MyjaphooDB.singleInstance().emClear();

            RefreshMovieAndThumbViews job = new RefreshMovieAndThumbViews();
            MyjaphooApp.getApplication().getContext().getTaskService().execute(job);
        } finally {
        }
    }
    /**
     * updates the movie tree and the thumbnail views, but not the filter bricks.
     * */
    public void updateViewAfterFilterBrickChange() {
        try {
            viewParts.getThumbPanel().resetModels();
            MyjaphooDB.singleInstance().emClear();

            RefreshMovieAndThumbViewsWithoutFilterBricks job = new RefreshMovieAndThumbViewsWithoutFilterBricks();
            MyjaphooApp.getApplication().getContext().getTaskService().execute(job);
        } finally {
        }
    }


    /**
     * updates all views.
     * */
    public void updateAllViews() {
        try {
            // clear entity manager: and also all cached data, that hangs somwhere in the gui elements:
            EventDispatchTools.onEDTWait(new Runnable() {
                @Override
                public void run() {
                    viewParts.getTokenpanel().clearView();
                    viewParts.getMetaTokenpanel().clearView();
                    viewParts.getThumbPanel().resetModels();
                }
            });

            MyjaphooDB.singleInstance().emClear();

            RefreshViews job = new RefreshViews();
            MyjaphooApp.getApplication().getContext().getTaskService().execute(job);
        } finally {
        }
    }

    public ThumbPanel getThumbPanel() {
        return viewParts.getThumbPanel();
    }

    public void updateChronic() {
        jMenuChronic.removeAll();
        for (final ChronicEntry c : controller.getFilter().getChronicList()) {
            JMenuItem item = new JMenuItem();
            item.setText(TextRepresentations.getTextForChronicEntry(c));

            item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.getFilter().popChronic(c);
                    updateChronic(c);
                    updateAllViews();
                }
            });
            jMenuChronic.add(item);
            jMenuChronic.addSeparator();
        }
    }

    /**
     * Build the bookmarks menu and the bookmark toolbar:
     */
    private void updateBookmark() {
        bookMarkMenuCreator.updateBookmark(jMenuBookmarks, bookmarkToolbar);
    }

    private void updateScripts() {
        controller.getScripting().createOrUpdateScriptingMenu(jMenuScripting);
    }

    public void updateChronic(ChronicEntry c) {
        viewParts.getFilterEditorPanel().setFilter(c.getView().getFilterExpression());
        viewParts.getFilterEditorPanel().setPreFilter(c.getView().getPreFilterExpression());
        viewParts.getGroupByPanel().setGroupByExpression(c.getView().getUserDefinedStruct());

        viewParts.getMovieTreePanel().updateChronic(c);
        updateMovieAndThumbViews();
    }

    private void fillDatabaseComparisonMenus() {
        jMenuCompareDatabaseSubMenu.removeAll();
        List<DatabaseConfiguration> databaseConfigurations = DatabaseConfigLoadSave.load().getDatabaseConfigurations();
        for (DatabaseConfiguration config : databaseConfigurations) {
            jMenuCompareDatabaseSubMenu.add(createComparisonMenuItem(config));
        }
        // fill also the db selection menu:
        jMenuDatabase.removeAll();
        jMenuDatabase.add(createDBSwitchMenuItem(null));
        jMenuDatabase.addSeparator();
        for (DatabaseConfiguration config : databaseConfigurations) {
            jMenuDatabase.add(createDBSwitchMenuItem(config));
        }
    }

    private JMenuItem createDBSwitchMenuItem(DatabaseConfiguration config) {
        String name = config != null ? config.getName() : "default";
        SwitchToOtherDatabase action = new SwitchToOtherDatabase(controller, name);
        JMenuItem item = new JMenuItem(action);
        if (config != null) {
            item.setText(TextRepresentations.getTextForDatabaseConfiguration(config));
        }
        return item;
    }

    private JMenuItem createComparisonMenuItem(DatabaseConfiguration config) {
        OpenAgainComparatorDatabase action = new OpenAgainComparatorDatabase(controller, config.getName());
        JMenuItem item = new JMenuItem(action);
        item.setText(TextRepresentations.getTextForDatabaseConfiguration(config));
        return item;
    }

    static class Results {

        public MovieStructureNode rootNode;
        public ThumbDisplayFilterResult thumbModelResult;
    }

    /**
     * background job to refresh the movie and thumb view (and all imediatly
     * related views, e.g. filter and grouping view).
     */
    class RefreshMovieAndThumbViewsWithoutFilterBricks extends MyjaphooBackgroundJob<Results, Object> {

        public RefreshMovieAndThumbViewsWithoutFilterBricks() {
            super(MyjaphooView.this, controller);
        }

        @Override
        protected Results doInBackground() throws Exception {
            setTitle("reloading tree and thumbs...");
            Results results = new Results();
            try {

                this.firePropertyChange("started", this, this);
                message("reloading tree ...", (Object) null);
                results.rootNode = controller.getFilter().createMovieTreeModel();
                ThumbMode mode = controller.getMainThumbController().getThumbMode();
                boolean distinctThumbs = controller.getMainThumbController().isPreventGroupingDups();
                message("reloading thumbs ...", (Object) null);
                ThumbDisplayFilterResult thumbModelResult = controller.getFilter().getThumbsModel(mode.getMode(), distinctThumbs);
                results.thumbModelResult = thumbModelResult;
                this.firePropertyChange("done", this, this);
            } catch (Exception e) {
                LOGGER.error("error happened during refreshing view!", e);
                throw e;

            }
            return results;
        }

        @Override
        protected void succeeded(Results result) {
            super.succeeded(result);
            viewParts.getMovieTreePanel().updateMovieTree(result.rootNode);
            viewParts.getThumbPanel().refillThumbView(result.thumbModelResult);
            viewParts.getFilterEditorPanel().setFilterInfoText(controller.getFilter().getFilterInfoText());
            viewParts.getInfoPanel().structureUpdated(controller);
            viewParts.getFilterEditorPanel().setFilter(controller.getFilter().getFilterPattern());
            viewParts.getGroupByPanel().setGroupByExpression(controller.getFilter().getUserDefinedStruct());

            MyjaphooDB.singleInstance().printStatistics();
        }

        @Override
        protected void failed(Throwable throwable) {
            super.failed(throwable);
            if (throwable instanceof ParserException) {
                controller.getEventBus().post(new ErrorUpdateEvent("Filter", (ParserException) throwable));
            }
        }
    }
    /**
     * background job to refresh the movie and thumb view (and all imediatly
     * related views, e.g. filter and grouping view).
     */
    class BackgroundClosureJob extends MyjaphooBackgroundJob<Results, Object> {
        Closure closure;
        public BackgroundClosureJob(Closure closure) {
            super(MyjaphooView.this, controller);
            this.closure = closure;
        }

        @Override
        protected Results doInBackground() throws Exception {
            setTitle("working...");
            Results results = new Results();
            try {
                this.firePropertyChange("started", this, this);
                closure.call();
                this.firePropertyChange("done", this, this);
            } catch (Exception e) {
                LOGGER.error("error happened during background job!", e);
                throw e;

            }
            return results;
        }

        @Override
        protected void failed(Throwable throwable) {
            super.failed(throwable);
            if (throwable instanceof ParserException) {
                controller.getEventBus().post(new ErrorUpdateEvent("Background Job", (ParserException) throwable));
            }
        }
    }

    public void executeBackground(Closure closure) {
        BackgroundClosureJob job = new BackgroundClosureJob(closure);
        MyjaphooApp.getApplication().getContext().getTaskService().execute(job);
    }

    /**
     * background job to refresh the movie and thumb view (and all imediatly
     * related views, e.g. filter and grouping view).
     */
    class RefreshMovieAndThumbViews extends MyjaphooBackgroundJob<Results, Object> {

        public RefreshMovieAndThumbViews() {
            super(MyjaphooView.this, controller);
        }

        @Override
        protected Results doInBackground() throws Exception {
            Channel channel = ChannelManager.createChannel(this.getClass(), "reloading tree and thumbs...");
            Results results = new Results();
            try {
                channel.startActivity();
                channel.message("reloading tree ...");
                results.rootNode = controller.getFilter().createMovieTreeModel();
                ThumbMode mode = controller.getMainThumbController().getThumbMode();
                boolean distinctThumbs = controller.getMainThumbController().isPreventGroupingDups();
                channel.message("reloading thumbs ...");
                ThumbDisplayFilterResult thumbModelResult = controller.getFilter().getThumbsModel(mode.getMode(), distinctThumbs);
                results.thumbModelResult = thumbModelResult;
            }  catch (Exception e) {
                channel.errormessage("error happened during refreshing view!", e);
                LOGGER.error("error happened during refreshing view!", e);
                throw e;

            } finally {
                channel.stopActivity();
            }
            return results;
        }

        @Override
        protected void succeeded(Results result) {
            super.succeeded(result);
            viewParts.getMovieTreePanel().updateMovieTree(result.rootNode);
            viewParts.getThumbPanel().refillThumbView(result.thumbModelResult);
            viewParts.getFilterEditorPanel().setFilterInfoText(controller.getFilter().getFilterInfoText());
            viewParts.getInfoPanel().structureUpdated(controller);
            viewParts.getFilterEditorPanel().setFilter(controller.getFilter().getFilterPattern());
            viewParts.getFilterEditorPanel().setPreFilter(controller.getFilter().getPreFilterPattern());
            viewParts.getGroupByPanel().setGroupByExpression(controller.getFilter().getUserDefinedStruct());

            MyjaphooDB.singleInstance().printStatistics();
        }

        @Override
        protected void failed(Throwable throwable) {
            super.failed(throwable);
            if (throwable instanceof ParserException) {
                controller.getEventBus().post(new ErrorUpdateEvent("Filter", (ParserException) throwable));
            }
        }
    }
    class RefreshViews extends MyjaphooBackgroundJob<Results, Object> {

        public RefreshViews() {
            super(MyjaphooView.this, controller);
        }

        @Override
        protected Results doInBackground() throws Exception {
            Channel channel = ChannelManager.createChannel(this.getClass(), "reloading tree and thumbs...");
            Results results = new Results();
            try {
                channel.startActivity();
                channel.message("reloading tree ...");
                results.rootNode = controller.getFilter().createMovieTreeModel();
                ThumbMode mode = controller.getMainThumbController().getThumbMode();
                boolean distinctThumbs = controller.getMainThumbController().isPreventGroupingDups();
                channel.message("reloading thumbs ...");
                ThumbDisplayFilterResult thumbModelResult = controller.getFilter().getThumbsModel(mode.getMode(), distinctThumbs);
                results.thumbModelResult = thumbModelResult;
                this.firePropertyChange("done", this, this);
            } catch (Exception e) {
                channel.errormessage("error happened during refreshing view!", e);
                LOGGER.error("error happened during refreshing view!", e);
                throw e;
            }  finally {
                channel.stopActivity();
            }
            return results;
        }

        @Override
        protected void succeeded(Results result) {
            super.succeeded(result);
            viewParts.getMovieTreePanel().updateMovieTree(result.rootNode);
            viewParts.getThumbPanel().refillThumbView(result.thumbModelResult);
            refreshTokenRelevantThings();
            viewParts.getFilterEditorPanel().setFilterInfoText(controller.getFilter().getFilterInfoText());
            updateChronic();
            updateBookmark();

            viewParts.getInfoPanel().structureUpdated(controller);
            viewParts.getFilterEditorPanel().setFilter(controller.getFilter().getFilterPattern());
            viewParts.getFilterEditorPanel().setPreFilter(controller.getFilter().getPreFilterPattern());
            viewParts.getGroupByPanel().setGroupByExpression(controller.getFilter().getUserDefinedStruct());

            MyjaphooDB.singleInstance().printStatistics();
        }

        @Override
        protected void failed(Throwable throwable) {
            super.failed(throwable);
            if (throwable instanceof ParserException) {
                controller.getEventBus().post(new ErrorUpdateEvent("Filter", (ParserException) throwable));
            }
        }
    }

    private void fillLastFilterExpressions() {

        Collection<String> items = new ArrayList<String>();
        HashSet<String> alreadyAdded = new HashSet<String>();
        List<ChronicEntry> chronicEntries = MainApplicationController.getInstance().getChronicList();
        for (ChronicEntry entry : chronicEntries) {
            if (items.size() > 20) {
                break;
            }
            if (entry.getView().isFilter()) {
                final String filterExpression = StringUtils.trim(entry.getView().getFilterExpression());
                if (!alreadyAdded.contains(filterExpression)) {
                    items.add(filterExpression);
                    alreadyAdded.add(filterExpression);
                }
            }
        }
        viewParts.getFilterEditorPanel().setLastFilters(items);
    }

    public InfoPanel getInfoPanel() {
        return viewParts.getInfoPanel();
    }

    public PropertiesPanel2 getPropertiesPanel() {
        return viewParts.getPropertiesPanel();
    }

    public PicturePreviewPanel getPreviewPanel() {
        viewParts.getPreviewpanel().isShowing();
        return viewParts.getPreviewpanel();
    }

    public boolean isPreviewVisible() {
        return viewParts.getPreviewpanel().isShowing();
    }

    /****************************************************************/
    // event handlers, registered by the controllers event bus */
    /***************************************************************/

    @Subscribe(onETD=true)
    public void structTypeSelectionChanged(StructTypeChangedEvent event) {
        viewParts.getGroupByPanel().setGroupByExpression(event.getUserDefinedStruct());
    }

    @Subscribe(onETD=true)
    public void userdefinedStructureChaned(UserDefinedStructureChangedEvent event) {
        viewParts.getGroupByPanel().setGroupByExpression(event.getUserDefinedStruct());
    }

    @Subscribe(onETD=true)
    public void dbConfigurationsChanged(DBConfigChangeEvent dbChangeEvent) {
        fillDatabaseComparisonMenus();
    }


    @Subscribe(onETD = true)
    public void activityStarted(ActivityStartedEvent event) {
        if (!busyIconTimer.isRunning()) {
            statusAnimationLabel.setIcon(busyIcons[0]);
            busyIconIndex = 0;
            busyIconTimer.start();
        }
        progressBar.setVisible(true);
        if (!event.isNestedActivity()) {
            progressBar.setIndeterminate(true);
        }
        setStatusMessage(event.getActivityName());
    }

    @Subscribe(onETD = true)
    public void progress(ProgressEvent event) {
        int value = event.getOverallPercentage();
        progressBar.setVisible(true);
        progressBar.setIndeterminate(false);
        progressBar.setValue(value);
    }

    @Subscribe(onETD = true)
    public void message(MessageEvent event) {
        String text = event.getMessage();
        setStatusMessage(text);
    }

    @Subscribe(onETD = true)
    public void errormessage(ErrorMessageEvent event) {
        String text = event.getErrorMessage();
        setStatusMessage(text);
    }

    @Subscribe(onETD = true)
    public void activityStopped(ActivityFinishedEvent event) {
        if (!event.getActivitiesLeft()) {
            busyIconTimer.stop();
            statusAnimationLabel.setIcon(idleIcon);
            progressBar.setVisible(false);
            progressBar.setValue(0);
        }
    }

    protected void setStatusMessage(String text) {
        statusMessageLabel.setText((text == null) ? "" : text);
        messageTimer.restart();
    }
}
