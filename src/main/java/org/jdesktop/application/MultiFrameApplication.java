
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */
package org.jdesktop.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * An application base class for simple GUIs with one primary JFrame.
 * <p>
 * This class takes care of component property injection, exit processing,
 * and saving/restoring session state in a way that's appropriate for
 * simple single-frame applications.  The application's JFrame is created
 * automatically, with a WindowListener that calls exit() when the
 * window is closed.  Session state is stored when the application
 * shuts down, and restored when the GUI is shown.
 * <p>
 * To use {@code SingleFrameApplication}, one need only override
 * {@code startup}, create the GUI's main panel, and apply
 * {@code show} to that.  Here's an example:
 * <pre>
 *class MyApplication extends SingleFrameApplication {
 *    &#064;Override protected void startup() {
 *        show(new JLabel("Hello World"));
 *    }
 *}
 * </pre>
 * The call to {@code show} in this example creates a JFrame (named
 * "mainFrame"), that contains the "Hello World" JLabel.  Before the
 * frame is made visible, the properties of all of the components in
 * the hierarchy are initialized with
 * {@link ResourceMap#injectComponents ResourceMap.injectComponents}
 * and then restored from saved session state (if any) with
 * {@link SessionStorage#restore SessionStorage.restore}.
 * When the application shuts down, session state is saved.
 * <p>
 * A more realistic tiny example would rely on a ResourceBundle for
 * the JLabel's string and the main frame's title.  The automatic
 * injection step only initializes the properties of named
 * components, so:
 * <pre>
 * class MyApplication extends SingleFrameApplication {
 *     &#064;Override protected void startup() {
 *         JLabel label = new JLabel();
 *         label.setName("label");
 *         show(label);
 *     }
 * }
 * </pre>
 * The ResourceBundle should contain definitions for all of the
 * standard Application resources, as well the main frame's title
 * and the label's text.  Note that the JFrame that's implicitly
 * created by the {@code show} method  is named "mainFrame".
 * <pre>
 * # resources/MyApplication.properties
 * Application.id = MyApplication
 * Application.title = My Hello World Application
 * Application.version = 1.0
 * Application.vendor = Sun Microsystems, Inc.
 * Application.vendorId = Sun
 * Application.homepage = http://www.javadesktop.org
 * Application.description =  An example of SingleFrameApplication
 * Application.lookAndFeel = system
 *
 * mainFrame.title = ${Application.title} ${Application.version}
 * label.text = Hello World
 * </pre>
 */
public abstract class MultiFrameApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MultiFrameApplication.class.getName());
    private ResourceMap appResources = null;
    private int numOfOpenFrames = 0;
    private List<FrameView> allOpenViews = new ArrayList<FrameView>();

    public void updateAllUIComponents() {
        for (FrameView fv : allOpenViews) {
            SwingUtilities.updateComponentTreeUI(fv.getFrame());
        }
    }

    private String sessionFilename(Window window) {
        if (window == null) {
            return null;
        } else {
            String name = window.getName();
            return (name == null) ? null : name + ".session.xml";
        }
    }

    /**
     * Initialize the hierarchy with the specified root by
     * injecting resources.
     * <p>
     * By default the {@code show} methods
     * {@link ResourceMap#injectComponents inject resources} before
     * initializing the JFrame or JDialog's size, location,
     * and restoring the window's session state.  If the app
     * is showing a window whose resources have already been injected,
     * or that shouldn't be initialized via resource injection,
     * this method can be overridden to defeat the default
     * behavior.
     *
     * @param root the root of the component hierarchy
     * @see ResourceMap#injectComponents
     * @see #show(JComponent)
     * @see #show(JFrame)
     * @see #show(JDialog)
     */
    protected void configureWindow(Window root) {
        getContext().getResourceMap().injectComponents(root);
    }

    private void initRootPaneContainer(RootPaneContainer c) {
        JComponent rootPane = c.getRootPane();
        // These initializations are only done once
        Object k = "SingleFrameApplication.initRootPaneContainer";
        if (rootPane.getClientProperty(k) != null) {
            return;
        }
        rootPane.putClientProperty(k, Boolean.TRUE);
        // Inject resources
        Container root = rootPane.getParent();
        if (root instanceof Window) {
            configureWindow((Window) root);
        } // If this is the mainFrame, then close == exit
        /*
        if (c instanceof FrameView) {
        JFrame mainFrame = ((FrameView)c).getFrame();
        mainFrame.addWindowListener(new MainFrameListener((FrameView) c));
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
         */ else if (root instanceof Window) { // close == save session state
            Window window = (Window) root;
            window.addHierarchyListener(new SecondaryWindowListener());
        }
        // If this is a JFrame monitor "normal" (not maximized) bounds
        if (root instanceof JFrame) {
            root.addComponentListener(new FrameBoundsListener());
        }
        // If the window's bounds don't appear to have been set, do it
        if (root instanceof Window) {
            Window window = (Window) root;
            if (!root.isValid() || (root.getWidth() == 0) || (root.getHeight() == 0)) {
                window.pack();
            }
            if (!window.isLocationByPlatform() && (root.getX() == 0) && (root.getY() == 0)) {
                Component owner = window.getOwner();
                if (owner == null) {
                    //owner = (window != mainFrame) ? mainFrame : null;
                }
                window.setLocationRelativeTo(owner);  // center the window
            }
        }
        // Restore session state
        if (root instanceof Window) {
            String filename = sessionFilename((Window) root);
            if (filename != null) {
                try {
                    getContext().getSessionStorage().restore(root, filename);
                } catch (Exception e) {
                    String msg = String.format("couldn't restore sesssion [%s]", filename);
                    logger.warn(msg, e);
                }
            }
        }
    }

    /**
     * Show the specified component in the {@link #getMainFrame main frame}.
     * Typical applications will call this method after constructing their
     * main GUI panel in the {@code startup} method.
     * <p>
     * Before the main frame is made visible, the properties of all of
     * the components in the hierarchy are initialized with {@link
     * ResourceMap#injectComponents ResourceMap.injectComponents} and
     * then restored from saved session state (if any) with {@link
     * SessionStorage#restore SessionStorage.restore}.  When the
     * application shuts down, session state is saved.
     * <p>
     * Note that the name of the lazily created main frame (see
     * {@link #getMainFrame getMainFrame}) is set by default.
     * Session state is only saved for top level windows with
     * a valid name and then only for component descendants
     * that are named.
     * <p>
     * Throws an IllegalArgumentException if {@code c} is null
     *
     * @param c the main frame's contentPane child
     */
    /*
    protected void show(JComponent c) {
    if (c == null) {
    throw new IllegalArgumentException("null JComponent");
    }
    JFrame f = getMainFrame();
    f.getContentPane().add(c, BorderLayout.CENTER);
    initRootPaneContainer(f);
    f.setVisible(true);
    }
     */
    /**
     * Initialize and show the JDialog.
     * <p>
     * This method is intended for showing "secondary" windows, like
     * message dialogs, about boxes, and so on.  Unlike the {@code mainFrame},
     * dismissing a secondary window will not exit the application.
     * <p>
     * Session state is only automatically saved if the specified
     * JDialog has a name, and then only for component descendants
     * that are named.
     * <p>
     * Throws an IllegalArgumentException if {@code c} is null
     *
     * @param c the main frame's contentPane child
     * @see #show(JComponent)
     * @see #show(JFrame)
     * @see #configureWindow
     */
    public void show(JDialog c) {
        if (c == null) {
            throw new IllegalArgumentException("null JDialog");
        }
        initRootPaneContainer(c);
        c.setVisible(true);
    }

    /**
     * Initialize and show the secondary JFrame.
     * <p>
     * This method is intended for showing "secondary" windows, like
     * message dialogs, about boxes, and so on.  Unlike the {@code mainFrame},
     * dismissing a secondary window will not exit the application.
     * <p>
     * Session state is only automatically saved if the specified
     * JFrame has a name, and then only for component descendants
     * that are named.
     * <p>
     * Throws an IllegalArgumentException if {@code c} is null
     *
     * @see #show(JComponent)
     * @see #show(JDialog)
     * @see #configureWindow
     */
    public void show(JFrame c) {
        if (c == null) {
            throw new IllegalArgumentException("null JFrame");
        }
        initRootPaneContainer(c);
        c.setVisible(true);
    }

    private void saveSession(Window window) {
        String filename = sessionFilename(window);
        if (filename != null) {
            try {
                getContext().getSessionStorage().save(window, filename);
            } catch (IOException e) {
                logger.warn("couldn't save sesssion", e);
            }
        }
    }

    private boolean isVisibleWindow(Window w) {
        return w.isVisible()
                && ((w instanceof JFrame) || (w instanceof JDialog) || (w instanceof JWindow));
    }

    /**
     * Return all of the visible JWindows, JDialogs, and JFrames per
     * Window.getWindows() on Java SE 6, or Frame.getFrames() for earlier
     * Java versions.
     */
    private List<Window> getVisibleSecondaryWindows() {
        List<Window> rv = new ArrayList<Window>();
        Method getWindowsM = null;
        try {
            getWindowsM = Window.class.getMethod("getWindows");
        } catch (Exception ignore) {
        }
        if (getWindowsM != null) {
            Window[] windows = null;
            try {
                windows = (Window[]) getWindowsM.invoke(null);
            } catch (Exception e) {
                throw new Error("HCTB - can't get top level windows list", e);
            }
            if (windows != null) {
                for (Window window : windows) {
                    if (isVisibleWindow(window)) {
                        rv.add(window);
                    }
                }
            }
        } else {
            Frame[] frames = Frame.getFrames();
            if (frames != null) {
                for (Frame frame : frames) {
                    if (isVisibleWindow(frame)) {
                        rv.add(frame);
                    }
                }
            }
        }
        return rv;
    }

    /**
     * Save session state for the component hierarchy rooted by
     * the mainFrame.  SingleFrameApplication subclasses that override
     * shutdown need to remember call {@code super.shutdown()}.
     */
    @Override
    protected void shutdown() {
        //saveSession(getMainFrame());
        for (Window window : getVisibleSecondaryWindows()) {
            saveSession(window);
        }
    }

    private class MainFrameListener extends WindowAdapter {

        private JFrame frame;
        private FrameView frameView;

        private MainFrameListener(FrameView frameView) {
            this.frame = frameView.getFrame();
            allOpenViews.add(frameView);
            this.frameView = frameView;
            numOfOpenFrames++;
        }

        public void windowClosing(WindowEvent e) {
            frame.setVisible(false);
            allOpenViews.remove(frameView);
            numOfOpenFrames--;
            if (numOfOpenFrames == 0) {

                exit(e);
            }
        }
    }

    /* Although it would have been simpler to listen for changes in
     * the secondary window's visibility per either a
     * PropertyChangeEvent on the "visible" property or a change in
     * visibility per ComponentListener, neither listener is notified
     * if the secondary window is disposed.
     * HierarchyEvent.SHOWING_CHANGED does report the change in all
     * cases, so we use that.
     */
    private class SecondaryWindowListener implements HierarchyListener {

        public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (e.getSource() instanceof Window) {
                    Window secondaryWindow = (Window) e.getSource();
                    if (!secondaryWindow.isShowing()) {
                        saveSession(secondaryWindow);
                    }
                }
            }
        }
    }

    /* In order to properly restore a maximized JFrame, we need to
     * record it's normal (not maximized) bounds.  They're recorded
     * under a rootPane client property here, so that they've can be
     * session-saved by WindowProperty#getSessionState().
     */
    private static class FrameBoundsListener implements ComponentListener {

        private void maybeSaveFrameSize(ComponentEvent e) {
            if (e.getComponent() instanceof JFrame) {
                JFrame f = (JFrame) e.getComponent();
                if ((f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0) {
                    String clientPropertyKey = "WindowState.normalBounds";
                    f.getRootPane().putClientProperty(clientPropertyKey, f.getBounds());
                }
            }
        }

        public void componentResized(ComponentEvent e) {
            maybeSaveFrameSize(e);
        }
        /* BUG: on Windows XP, with JDK6, this method is called once when the
         * frame is a maximized, with x,y=-4 and getExtendedState() == 0.
         */

        public void componentMoved(ComponentEvent e) { /* maybeSaveFrameSize(e); */ }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }
    }

    @Override
    public void show(View view) {
        if (view instanceof FrameView) {
            FrameView fv = (FrameView) view;
            fv.getFrame().addWindowListener(new MainFrameListener(fv));
            fv.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        RootPaneContainer c = (RootPaneContainer) view.getRootPane().getParent();
        initRootPaneContainer(c);
        ((Window) c).setVisible(true);
    }

    public void iconifyAllViews() {
        for (FrameView fv : allOpenViews) {
            fv.getFrame().setState(Frame.ICONIFIED);
        }
    }
}
