/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;

/**
 * Beinhaltet das auslesen der Konfiguration u. konfigurieren von log4j.
 * @author mla
 */
public class LoggingConfiguration {

    public static void configurate() {
        BasicConfigurator.configure();
        DOMConfigurator.configure(MyjaphooApp.class.getResource("defaultlog4j.xml")); //NOI18N

        if (new File("log4j.xml").exists()) { //NOI18N
            DOMConfigurator.configure("log4j.xml"); //NOI18N
        }
    }

    public static void configurateForShellModel() {
        BasicConfigurator.configure();
        DOMConfigurator.configure(MyjaphooApp.class.getResource("consolelog4j.xml")); //NOI18N
    }
}
