/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.exec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author mla
 */
public class StreamGobbler extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
    InputStream is;
    String type;

    StringBuilder input = new StringBuilder();

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.debug(type + ">" + line);
                input.append(line); input.append("\n");
            }
        } catch (IOException ioe) {
            logger.error("running command failed with io exception!" , ioe);
        }
    }

    @Override
    public String toString() {
        return input.toString();
    }


}

