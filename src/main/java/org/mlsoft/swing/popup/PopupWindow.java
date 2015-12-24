package org.mlsoft.swing.popup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * popup window to open a popup which is resizable. It contains another
 * component.

 */
public class PopupWindow extends JWindow {

    private class ParentWindowListener extends ComponentAdapter
            implements WindowFocusListener {

        public void addTo(Window w) {
            w.addComponentListener(this);
            w.addWindowFocusListener(this);
        }

        public void componentHidden(ComponentEvent e) {
            hidePopupWindow();
        }

        public void componentMoved(ComponentEvent e) {
            hidePopupWindow();
        }

        public void componentResized(ComponentEvent e) {
            hidePopupWindow();
        }

        public void removeFrom(Window w) {
            if (w != null) {
                w.removeComponentListener(this);
                w.removeWindowFocusListener(this);
            }
        }

        public void windowGainedFocus(WindowEvent e) {
        }

        public void windowLostFocus(WindowEvent e) {
            hidePopupWindow();
        }
    }

    /**
     * Listens for events from the text component we're installed on.
     */
    private class TextComponentListener extends FocusAdapter
            implements HierarchyListener {

        void addTo(Component tc) {
            tc.addFocusListener(this);
            tc.addHierarchyListener(this);
        }

        /**
         * Hide the auto-completion windows when the text component loses
         * focus.
         */
        public void focusLost(FocusEvent e) {
            hidePopupWindow();
        }

        /**
         * Called when the component hierarchy for our text component changes.
         * When the text component is added to a new {@link Window}, this
         * method registers listeners on that <code>Window</code>.
         *
         * @param e The event.
         */
        public void hierarchyChanged(HierarchyEvent e) {

            // NOTE: e many be null as we call this method at other times.
            //System.out.println("Hierarchy changed! " + e);

            Window oldParentWindow = parentWindow;
            parentWindow = SwingUtilities.getWindowAncestor(invoker);
            if (parentWindow != oldParentWindow) {
                if (oldParentWindow != null) {
                    parentWindowListener.removeFrom(oldParentWindow);
                }
                if (parentWindow != null) {
                    parentWindowListener.addTo(parentWindow);
                }
            }

        }

        public void removeFrom(Component tc) {
            tc.removeFocusListener(this);
            tc.removeHierarchyListener(this);
        }
    }

    /**
     * Hides the popup window, if it is visible.
     *
     * @return Whether the popup window was visible.
     */
    private void hidePopupWindow() {
        setVisible(false);

    }
    /** the invoker of that popup window, e.g. a button acting as a popup button. */
    private Component invoker;
    private Window parentWindow;
    /**
     * Listens for events in the parent window that affect the visibility of
     * the popup windows.
     */
    private ParentWindowListener parentWindowListener;
    /**
     * Listens for events from the text component that affect the visibility
     * of the popup windows.
     */
    private TextComponentListener textComponentListener;

    /**
     * Constructor.
     *
     * @param parent The parent window (hosting the text component).
     * @param ac The auto-completion instance.
     */
    public PopupWindow(Window parent) {

        super(parent);
    }

    /**
     * Toggles the visibility of this popup window.
     *
     * @param visible Whether this window should be visible.
     */
    public void setVisible(boolean visible) {

        if (visible != isVisible()) {

            super.setVisible(visible);
        }
        Window parentWindow = SwingUtilities.getWindowAncestor(invoker);
        if (visible) {
            parentWindowListener.addTo(parentWindow);
            textComponentListener.addTo(invoker);
        } else {
            parentWindowListener.removeFrom(parentWindow);
            textComponentListener.removeFrom(invoker);
        }

    }

    public void show(Component invoker, JComponent popupComponent, int x, int y) {
        this.invoker = invoker;
        preparePopup(popupComponent);
        Point invokerOrigin;
        if (invoker != null) {
            invokerOrigin = invoker.getLocationOnScreen();

            // To avoid integer overflow
            long lx, ly;
            lx = ((long) invokerOrigin.x)
                    + ((long) x);
            ly = ((long) invokerOrigin.y)
                    + ((long) y);
            if (lx > Integer.MAX_VALUE) {
                lx = Integer.MAX_VALUE;
            }
            if (lx < Integer.MIN_VALUE) {
                lx = Integer.MIN_VALUE;
            }
            if (ly > Integer.MAX_VALUE) {
                ly = Integer.MAX_VALUE;
            }
            if (ly < Integer.MIN_VALUE) {
                ly = Integer.MIN_VALUE;
            }

            setLocation((int) lx, (int) ly);
        } else {
            setLocation(x, y);
        }
        setVisible(true);
    }

    private void preparePopup(JComponent popupComponent) {


        JPanel contentPane = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(popupComponent,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // In 1.4, JScrollPane.setCorner() has a bug where it won't accept
        // JScrollPane.LOWER_TRAILING_CORNER, even though that constant is
        // defined.  So we have to put the logic added in 1.5 to handle it
        // here.
        JPanel corner = new SizeGrip();

        String str = JScrollPane.LOWER_RIGHT_CORNER;
        sp.setCorner(str, corner);

        contentPane.add(sp);
        setContentPane(contentPane);
        applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pack();

        setFocusableWindowState(false);

        parentWindowListener = new ParentWindowListener();
        textComponentListener = new TextComponentListener();
    }

    /**
     * Updates the <tt>LookAndFeel</tt> of this window and the description
     * window.
     */
    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
    }
}
