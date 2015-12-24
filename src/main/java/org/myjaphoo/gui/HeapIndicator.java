/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui;

import org.myjaphoo.gui.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;

/**
 * Heap indicator: a prograssbar showing the heap space,
 * with additional function to run a garbage collection on double click.
 * @author mla
 */
public class HeapIndicator extends JProgressBar {

    private Timer timer;
    private TimerEvent timerEvent;
    private long usedMem;
    private long totalMem;
    private long maxMem;
    private Component parentComponent;

    /**
     * Constructor.
     *
     * @param app The GUI application.
     */
    public HeapIndicator(Component parentComponent) {

        this.parentComponent = parentComponent;
        setMinimum(0);
        setMaximum(100);
        setStringPainted(true);
        updateData();
        setRefreshInterval(5000); // Must be called!

        ToolTipManager.sharedInstance().registerComponent(this);

    }

    /**
     * Returns the text to display for the tooltip.
     *
     * @return The tooltip text.
     */
    @Override
    public String getToolTipText() {
        String num = Utils.humanReadableByteCount(getUsedMemory());
        String denom = Utils.humanReadableByteCount(getTotalMemory());
        String max = Utils.humanReadableByteCount(getMaxMemory());
        String toolTip = "Heap size: {0} / {1}, max {2}";
        toolTip = MessageFormat.format(toolTip, num, denom, max);
        return toolTip;
    }

    /**
     * Returns the total amount of memory available to the JVM.
     *
     * @return The total memory available to the JVM, in bytes.
     * @see #getUsedMemory
     */
    public long getTotalMemory() {
        return totalMem;
    }

    /**
     * Returns the amount of memory currently being used by the JVM.
     *
     * @return The memory being used by the JVM, in bytes.
     * @see #getTotalMemory
     */
    public long getUsedMemory() {
        return usedMem;
    }

    public long getMaxMemory() {
        return maxMem;
    }

    protected void installTimer(int interval) {
        if (timer == null) {
            timerEvent = new TimerEvent();
            timer = new Timer(interval, timerEvent);
        } else {
            timer.stop();
            timer.setDelay(interval);
        }
        timer.start();
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                if (e.getClickCount() == 2) {
                    long oldMem = getUsedMemory();
                    Runtime.getRuntime().gc();
                    updateData();
                    long newMem = getUsedMemory();
                    long difference = oldMem - newMem;
                    String text = "Garbage collection freed: {0}";
                    text = MessageFormat.format(text, Utils.humanReadableByteCount(difference));
                    JOptionPane.showMessageDialog(parentComponent, text,
                            "JVM Heap Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            default:
        }
        super.processMouseEvent(e);
    }

    /**
     * Sets the refresh interval for the heap indicator.
     *
     * @param interval The new refresh interval, in milliseconds.
     * @see #getRefreshInterval
     */
    public void setRefreshInterval(int interval) {
        if (interval <= 0 || interval == getRefreshInterval()) {
            return;
        }
        installTimer(interval);
    }

    /**
     * Updates heap memory information.
     */
    private void updateData() {
        totalMem = Runtime.getRuntime().totalMemory();
        usedMem = totalMem - Runtime.getRuntime().freeMemory();
        maxMem = Runtime.getRuntime().maxMemory();
    }

    /**
     * Returns the refresh interval of the heap indicator.
     *
     * @return The refresh interval, in milliseconds.
     * @see #setRefreshInterval
     */
    public int getRefreshInterval() {
        return timer == null ? -1 : timer.getDelay();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            installTimer(getRefreshInterval());
        } else {
            uninstallTimer();
        }
        super.setVisible(visible);
    }

    /**
     * Called just before this <code>Plugin</code> is removed from an
     * <code>GUIApplication</code>.  This gives the plugin a chance to clean
     * up any loose ends (kill any threads, close any files, remove listeners,
     * etc.).
     *
     * @return Whether the uninstall went cleanly.
     * @see #install
     */
    public boolean uninstall() {
        uninstallTimer();
        return true;
    }

    protected void uninstallTimer() {
        if (timer != null) {
            timer.stop();
            timer.removeActionListener(timerEvent);
            timerEvent = null;	// May help GC.
            timer = null;		// May help GC.
        }
    }

    /**
     * Timer event that gets fired.  This refreshes the GC icon.
     */
    private class TimerEvent implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            updateData();
            long usedMem = getUsedMemory();
            long totalMem = getTotalMemory();
            long curr = usedMem * 100 / totalMem;
            if (curr >= 100) {
                curr = 100;
            }
            HeapIndicator.this.setValue((int) curr);

            String num = Utils.humanReadableByteCount(getUsedMemory());
            String denom = Utils.humanReadableByteCount(getTotalMemory());
            String max = Utils.humanReadableByteCount(getMaxMemory());
            String toolTip = "{0} of {1} max {2}";
            String progressString = MessageFormat.format(toolTip, num, denom, max);
            HeapIndicator.this.setString(progressString);
            repaint();
        }
    }
}
