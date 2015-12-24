package org.myjaphoo.gui.action.scriptactions;

import groovy.lang.Closure;

import javax.swing.*;

/**
 * ActionEntry.
 * This is the context of the execution of a closure for script action configuration.
 * Therefore the properties could directly set within the config closure.
 * Example:
 defTagContextAction("myaction") {
 name = "Hello World!";
 descr = "blabla"
 action= { controller, tags ->  controller.message "hi, I got the following tags: $tags"}
 }
 *
 * @author lang
 * @version $Id$
 */
public class ActionEntry {

    private String name;

    private String descr;

    private Icon icon;

    private Closure action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Closure getAction() {
        return action;
    }

    public void setAction(Closure action) {
        this.action = action;
    }
}
