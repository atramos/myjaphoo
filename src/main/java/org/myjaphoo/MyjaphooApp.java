/*
 * WankmanApp.java
 */
package org.myjaphoo;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.jdesktop.application.Application;
import org.jdesktop.application.MultiFrameApplication;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.tips.TipLoader;
import org.jdesktop.swingx.tips.TipOfTheDayModel;
import org.mlsoft.eventbus.GlobalBus;
import org.myjaphoo.command.ImportCommand;
import org.myjaphoo.command.StatisticsCommand;
import org.myjaphoo.gui.protocolhanders.tagpic.Handler;
import org.myjaphoo.gui.scripting.Scripting;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.grouping.GroupingDim;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.dbhandling.WmDatabaseOpener;
import org.myjaphoo.model.util.UserDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

/**
 * The main class of the application.
 */
public class MyjaphooApp extends MultiFrameApplication {

    /**
     * logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(MyjaphooApp.class.getName());
    MyjaphooTrayIcon trayIcon = new MyjaphooTrayIcon();

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
//        System.out.println("debug: return:");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Handler.install();

        System.setProperty("wankman.WankmanApp.logDirectory", UserDirectory.getDirectory()); //NOI18N
        LoggingConfiguration.configurate();

        LookAndFeels.setPlaf();

        checkDataBaseAccess();

        initRSTAThings();

        trayIcon.initTrayIcon();

        //doLogin();
        long start = System.currentTimeMillis();
        MyjaphooView firstView = new MyjaphooView(this);
        show(firstView);
        long stop = System.currentTimeMillis();

        logger.info("Start time = " + (stop - start)); //NOI18N

        Scripting.executeUserDefinedInitScripts();

        if (MyjaphooAppPrefs.PRF_SHOWTIPOFDAY.getVal()) {
            try {
                Properties tips = new Properties();
                ResourceBundle tipsBundle = ResourceBundle.getBundle("org/myjaphoo/resources/tips"); //NOI18N
                Enumeration<String> keys = tipsBundle.getKeys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    tips.setProperty(key, tipsBundle.getString(key));
                }
                TipOfTheDayModel model = TipLoader.load(tips);
                JXTipOfTheDay totd = new JXTipOfTheDay(model);

                totd.showDialog(firstView.getFrame());
            } catch (MissingResourceException ex) {
                logger.error("error", ex); //NOI18N
            }
        }
        GlobalBus.bus.register(this);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of WankmanApp
     */
    public static MyjaphooApp getApplication() {
        return Application.getInstance(MyjaphooApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            if ("-i".equals(args[0])) {
                startGroovySh();
            } else {
                startGroovySh(args);
            }

        } else {
            launch(MyjaphooApp.class, args);
        }
    }

    private static void startGroovySh() {
        // start without args: interactive mode.
        startGroovySh(null);
    }

    private static Groovysh prepareGroovysh() {
        System.setProperty("wankman.WankmanApp.logDirectory", UserDirectory.getDirectory()); //NOI18N
        LoggingConfiguration.configurateForShellModel();
        touchDatabase();

        Groovysh groovysh = new Groovysh();

        groovysh.register(new StatisticsCommand(groovysh));
        groovysh.register(new ImportCommand(groovysh));
        return groovysh;
    }

    private static void startGroovySh(String[] args) {
        Groovysh groovysh = prepareGroovysh();
        try {
            String cmdLine = null;
            if (args != null) {
                cmdLine = StringUtils.join(args, " ");
            }
            int code = groovysh.run(cmdLine);
        } finally {
        }
        // Force the JVM to exit at this point, since shell could have created threads or
        // popped up Swing components that will cause the JVM to linger after we have been
        // asked to shutdown
        System.exit(0);
    }

    private void checkDataBaseAccess() {

        try {
            touchDatabase();
        } catch (Throwable e) {
            logger.error("unable to open database!", e); //NOI18N
            ErrorInfo info = new ErrorInfo("Wm", "unable to open database '" + WmDatabaseOpener.configuredDatabaseFileName() + "'!", //NOI18N
                    null, null, e, Level.SEVERE, null);
            JXErrorPane.showDialog(null, info);
            touchDatabase();
        }

    }

    /**
     * access the database to check if its available.
     */
    private static void touchDatabase() {
        // check it a second time: the opener will now try to open the default
        // database:
        MyjaphooDB.singleInstance().emClear();
        MovieEntryJpaController jpa = new MovieEntryJpaController();
        jpa.findBookMarkEntities();
    }

    private void initRSTAThings() {

        // ensure, that all syntax relevant objects are initialized before
        // register the syntax highlighting token maker; otherwise some weird
        // things could happen
        FilterParser.initialize();

        // define own syntax highlighting token maker and assign it to the text area:
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("wmstyle", "org.myjaphoo.gui.editor.rsta.syntax.WmTokenMaker");
        TokenMakerFactory.setDefaultInstance(atmf);
    }

    /**
     * Starts a new view which is already filtered by a filter expression
     * @param filterExpression
     */
    public void startNewView(final String filterExpression) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MyjaphooApp.getApplication().show(new MyjaphooView(MyjaphooApp.getApplication(), filterExpression));
            }
        });
    }

    /**
     * Starts a new view which is already filtered by a filter expression and grouped by a grouping list.
     * @param filterExpression
     */
    public void startNewView(final String filterExpression, final List<GroupingDim> hierarchy) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                MyjaphooApp.getApplication().show(new MyjaphooView(MyjaphooApp.getApplication(), filterExpression, hierarchy));
            }
        });
    }

}
