/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.scripting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

/**
 * Script, welches als File in dem macro ordner liegt.
 * @author mla
 */
public class FileScript implements Script {

    private File file;
    private static final Logger logger = LoggerFactory.getLogger(FileScript.class);

    public FileScript(File file) {
        this.file = file;
    }

    @Override
    public String getScriptTxt() {
        try {
            Reader reader = new FileReader(file);
            String txt = org.apache.commons.io.IOUtils.toString(reader);
            reader.close();
            return txt;
        } catch (Exception ex) {
            logger.error("error loading script!", ex); //NOI18N
            return ""; //NOI18N
        }
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public File getFile() {
        return file;
    }
}
