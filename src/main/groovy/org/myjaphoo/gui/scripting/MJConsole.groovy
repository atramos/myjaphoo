package org.myjaphoo.gui.scripting

import groovy.transform.ThreadInterrupt
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import org.myjaphoo.MyjaphooController
import org.myjaphoo.model.db.SavedGroovyScript

/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.swing.*
import javax.swing.event.DocumentListener
import java.awt.event.ActionEvent

/**
 * Groovy Swing console.
 *
 * Small extension to the normal groovy console which just inits the compiler configuration with
 * our own base script class.
 */
class MJConsole extends groovy.ui.Console {

    SavedGroovyScript dbscript = null;

    MyjaphooController myjaphooController;

    /**
     * override newScript to init always a compiler configuration with our script base class.
     * Unfortunately this is not possible in the groovy console itself,
     * since the original newScript always creates a new compiler configuration which overrides
     * any previously made configurations....
     * @param parent
     * @param binding
     */
    @Override
    void newScript(ClassLoader parent, Binding binding) {
        config = new CompilerConfiguration();
        config.setScriptBaseClass(MJScriptBaseClass.class.name);
        if (threadInterrupt) config.addCompilationCustomizers(new ASTTransformationCustomizer(ThreadInterrupt))

        shell = new GroovyShell(parent, binding, config)
    }

    void run() {
        run(frameConsoleDelegates)
        JMenu dbMenu = new JMenu("Database")
        JMenuItem dbSave = new JMenuItem("Save Script");

        dbSave.setAction(new AbstractAction("Save Script") {
            @Override
            void actionPerformed(ActionEvent e) {
                saveDbScript();
            }
        })
        dbMenu.add(dbSave);
        this.frame.JMenuBar.add(dbMenu, 0);
    }

    // Return false if use elected to cancel
    boolean askToSaveFile() {
        if ((scriptFile == null && dbscript == null) || !dirty) {
            return true
        }
        String scriptName = scriptFile != null ? "File $scriptFile.name" : "DB Script $dbscript.name";
        switch (JOptionPane.showConfirmDialog(frame,
                "Save changes to " + scriptName + "?",
                "GroovyConsole", JOptionPane.YES_NO_CANCEL_OPTION)) {
            case JOptionPane.YES_OPTION:
                if (scriptFile != null) {
                    return fileSave()
                } else {
                    saveDbScript();
                    return true;
                }

            case JOptionPane.NO_OPTION:
                return true
            default:
                return false
        }
    }

    void updateTitle() {
        if (frame.properties.containsKey('title')) {
            if (scriptFile != null) {
                frame.title = scriptFile.name + (dirty ? " * " : "") + " - MJ GroovyConsole"
            } else if (dbscript != null) {
                frame.title = dbscript.name + (dirty ? " * " : "") + " - MJ GroovyConsole"
            } else {
                frame.title = "MJ GroovyConsole"
            }
        }
    }

    void saveDbScript() {
        if (dbscript != null) {
            dbscript.setScriptText(inputArea.text)
            myjaphooController.updateScript(dbscript);
            setDirty(false)
        }
    }

    void loadDbScript(SavedGroovyScript script) {
        swing.edt {
            inputArea.editable = false
        }
        swing.doOutside {
            try {
                consoleText = script.scriptText
                scriptFile = null
                dbscript = script;
                swing.edt {
                    def listeners = inputArea.document.getListeners(DocumentListener)
                    listeners.each { inputArea.document.removeDocumentListener(it) }
                    updateTitle()
                    inputArea.document.remove 0, inputArea.document.length
                    inputArea.document.insertString 0, consoleText, null
                    listeners.each { inputArea.document.addDocumentListener(it) }
                    setDirty(false)
                    inputArea.caretPosition = 0
                }
            } finally {
                swing.edt { inputArea.editable = true }
                // GROOVY-3684: focus away and then back to inputArea ensures caret blinks
                swing.doLater outputArea.&requestFocusInWindow
                swing.doLater inputArea.&requestFocusInWindow
            }
        }
    }
};



