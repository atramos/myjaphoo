/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.scripting;

import org.myjaphoo.gui.logpanel.LogPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output stream that redirects to log4j output.
 * @author mla
 */
public class LoggingOutputStream extends OutputStream {

    Logger logger = LoggerFactory.getLogger("org.myjaphoo.ScriptExe"); //NOI18N
    private StringBuilder b = new StringBuilder(100);

    @Override
    public void write(int b) throws IOException {
        char ch = (char) b;
        collect(ch);
        if (ch == '\n') {
            flush();
        }
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        final String line = b.toString();
        logger.trace(line);
        LogPanel.writeln(line);
        b = new StringBuilder(100);
    }

    @Override
    public void close() throws IOException {
        flush();
        super.close();
    }

    private void collect(char ch) {
        b.append(ch);
    }
}
