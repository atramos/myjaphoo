package org.myjaphoo.model.externalPrograms;

import org.apache.commons.lang.StringUtils;
import org.mlsoft.common.prefs.model.editors.FileVal;
import org.myjaphoo.model.logic.exec.Exec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * defines an external program that gets used in myjaphoo.
 *
 * @author lang
 * @version $Id$
 */
public class ExternalProgram {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExternalProgram.class.getName());

    /**
     * program name. only used for messages
     */
    private String name;

    /**
     * preferences defined for windows.
     */
    private FileVal executableDefinedForWindows;

    /**
     * preferences defined for linux.
     */
    private FileVal executableDefinedForLinux;

    /**
     * some statically predefined paths for that executable (as fallback).
     */
    private String[] standardExecutablePaths;
    private String prgExplanation;

    public ExternalProgram(String name, String prgExplanation, FileVal executableDefinedForWindows, FileVal executableDefinedForLinux, String... standardExecutablePaths) {
        this.name = name;
        this.prgExplanation = prgExplanation;
        this.executableDefinedForWindows = executableDefinedForWindows;
        this.executableDefinedForLinux = executableDefinedForLinux;
        this.standardExecutablePaths = standardExecutablePaths;
    }

    private String getUserDefinedExecutable() {
        if (org.apache.commons.lang.SystemUtils.IS_OS_LINUX) {
            return executableDefinedForLinux.getVal();
        } else {
            return executableDefinedForWindows.getVal();
        }
    }

    private boolean isUserDefined() {
        if (org.apache.commons.lang.SystemUtils.IS_OS_LINUX) {
            return executableDefinedForLinux.isValueDefined();
        } else {
            return executableDefinedForWindows.isValueDefined();
        }
    }


    public void startNoWait(final ArrayList<String> args) {
        args.add(0, getProgramExecutable());
        Thread thread = new Thread(new Runnable() {

            public void run() {

                String[] arr = new String[args.size()];
                try {
                    Exec.execNoWait(args.toArray(arr));
                } catch (IOException ex) {
                    LOGGER.error("error", ex);
                }
            }
        });
        thread.start();
    }

    public Exec.Result execAndWait(ArrayList<String> args) throws IOException, InterruptedException {
        args.add(0, getProgramExecutable());

        String commandLine = StringUtils.join(args, " ");
        LOGGER.info("execute: " + commandLine);

        String[] arr = new String[args.size()];
        Exec.Result result = Exec.execAndWait(args.toArray(arr));
        return result;
    }

    public String getProgramExecutable() {
        String path = getProgramExecutableInternal();
        if (path == null) {
            throw new RuntimeException("could not find program " + name + "! Please check paths in preferences!");
        } else {
            return path;
        }
    }

    public boolean exists() {
        return getProgramExecutableInternal() != null;
    }

    private String getProgramExecutableInternal() {
        // check first the user defined one, then try one of the
        // standard definitions:
        if (isUserDefined()) {
            return getUserDefinedExecutable();
        }
        // otherwise check the standard definitions:
        for (String path : standardExecutablePaths) {
            File file = new File(path);
            if (file.exists()) {
                return path;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getPrgExplanation() {
        return prgExplanation;
    }
}
