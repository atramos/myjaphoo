/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.ui.Console;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.util.MenuPathStructurizer;
import org.myjaphoo.model.db.SavedGroovyScript;
import org.myjaphoo.model.db.ScriptType;
import org.myjaphoo.model.util.UserDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class Scripting {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/scripting/resources/Scripting");

    private static final Logger logger = LoggerFactory.getLogger(Scripting.class);
    private MyjaphooController controller;

    public Scripting(MyjaphooController controller) {
        this.controller = controller;
    }

    private static final String mainPath = UserDirectory.getDirectory(); //NOI18N

    private static MJConsole buildMJConsole(MyjaphooController controller) {
        // load our special console:
        MJConsole console = new MJConsole();
        console.setMyjaphooController(controller);

        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(MJScriptBaseClass.class.getName());
        console.setVariable("controller", controller); //NOI18N
        console.setConfig(compilerConfiguration);
        return console;
    }

    public void createOrUpdateScriptingMenu(JMenu jMenuScripting) {
        jMenuScripting.removeAll();
        jMenuScripting.add(new AbstractAction("Open Groovy Console", null) {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Console console = buildMJConsole(controller);
                    console.run();
                } catch (Exception ex) {
                    logger.error("error during script execution!", ex); //NOI18N
                }
            }
        });

        // add the edit menus for the existing scripts:
        jMenuScripting.addSeparator();
        addEditActions(jMenuScripting);
    }

    public static GroovyShell createMjShell(/*MyjaphooController controller*/) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(MJScriptBaseClass.class.getName());
        LoggingOutputStream out = new LoggingOutputStream();
        PrintWriter output = new PrintWriter(out);
        config.setOutput(output);

        Binding binding = new Binding();
//        binding.setVariable("controller", controller);

        GroovyShell shell = new GroovyShell(binding, config);

        return shell;
    }


    private void addEditActions(JMenu menuEditing) {
        // scale groovy console pic to 16x16:
        Image scaledImage = Icons.IR_GROOVY.icon.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        List<Script> scriptFiles = scanForScriptingFiles(mainPath + "/macros/scripts/");
        for (final Script file : scriptFiles) {

            menuEditing.add(new AbstractAction(file.getName(), scaledImageIcon) {

                @Override
                public void actionPerformed(ActionEvent e) {

                    // start console and open the script:
                    Console console = buildMJConsole(controller);
                    console.run();
                    console.loadScriptFile(file.getFile());
                }
            });
        }

        // display also the ones saved in the database:
        List<SavedGroovyScript> scripts = MainApplicationController.getInstance().getScriptList();
        MenuPathStructurizer structurizer = new MenuPathStructurizer(false);

        structurizer.setFolderIcon(scaledImageIcon);
        for (final SavedGroovyScript script : scripts) {
            String title = script.getName();
            structurizer.add(new AbstractAction(title, scaledImageIcon) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // start console and open the script:
                    editScript(controller, script);
                }
            }, script.getName(), script.getMenuPath());
        }
        structurizer.structurize(menuEditing, 10);

    }

    /**
     * Scans recursively a path for script files for that scripting engine.
     * Returns all found files.
     *
     * @param pathToScan
     */
    public List<Script> scanForScriptingFiles(String pathToScan) {
        File file = new File(pathToScan);
        OrFileFilter suffixFilter = null;

        String[] filters = new String[]{".groovy"}; //NOI18N
        suffixFilter = new OrFileFilter();
        suffixFilter.addFileFilter(new SuffixFileFilter(filters));
        suffixFilter.addFileFilter(DirectoryFileFilter.DIRECTORY);

        ArrayList<Script> scriptFiles = new ArrayList<Script>();
        scanFiles(file, suffixFilter, scriptFiles);

        return scriptFiles;
    }

    private void scanFiles(File dir, OrFileFilter filefilter, ArrayList<Script> movFiles) {
        File[] files = dir.listFiles((FilenameFilter) filefilter);
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanFiles(file, filefilter, movFiles);
                } else {
                    movFiles.add(new FileScript(file));
                }
            }
        }
    }

    public static void editScript(MyjaphooController controller, SavedGroovyScript node) {
        MJConsole console = buildMJConsole(controller);
        console.run();
        console.loadDbScript(node);
    }

    public static void executeUserDefinedInitScripts() {
        List<SavedGroovyScript> scripts = MainApplicationController.getInstance().getScriptList();
        for (final SavedGroovyScript script : scripts) {
            if (script.getScriptType() == ScriptType.INITSCRIPT) {
                runInitScript(script);
            }
        }
    }

    private static void runInitScript(SavedGroovyScript script) {
        try {
            createMjShell().evaluate(script.getScriptText());
        } catch (Exception e) {
            logger.error("error executing init script!", e);
        }
    }
}
