/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.exec;

import java.io.IOException;

/**
 *
 * @author mla
 */
public class Exec {

    public static class Result {
        public String errStream;
        public String outputStream;
        public int exitVal;
    }

    public static Result execAndWait(Process proc) throws InterruptedException {
            // any error message?
            StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            int exitVal = proc.waitFor();
            Result result = new Result();
            result.errStream = errorGobbler.toString();
            result.outputStream = outputGobbler.toString();
            result.exitVal = exitVal;

            return result;
    }

    public static Result execAndWait(String[] cmd) throws IOException, InterruptedException {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            return execAndWait(proc);
    }

    public static Result execAndWait(String cmd) throws IOException, InterruptedException {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            return execAndWait(proc);
    }

    public static void execNoWait(String[] cmd) throws IOException {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            // any error message?
            StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), "OUTPUT");

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

    }
}
