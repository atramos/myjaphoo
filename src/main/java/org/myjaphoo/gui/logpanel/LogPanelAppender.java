/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.logpanel;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * LogPanel Appender. Konfigurierbar über log4j.xml.
 * Intern delegiert dieser nur an LogPanel, welches die Ausgabe übernimmt.
 * @author mla
 */
public class LogPanelAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent le) {
        LogPanel.appendLogEvent(le);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }



}
