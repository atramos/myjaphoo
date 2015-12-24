/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.scripting;

import java.io.File;

/**
 *
 * @author mla
 */
public interface Script {

    String getScriptTxt();

    public String getName();

    public boolean isEditable();

    public File getFile();
}
