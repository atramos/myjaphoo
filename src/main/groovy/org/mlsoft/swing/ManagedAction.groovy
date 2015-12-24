package org.mlsoft.swing

import org.apache.commons.lang.StringUtils

import javax.swing.*
import java.lang.reflect.Method

/**
 * ManagedAction 
 * @author mla
 * @version $Id$
 *
 */
class ManagedAction {

    def String actionName;
    def String enableExpr;

    def Method method;
    def boolean hasListArgument;

    public AbstractAction abstractAction;

    ConfigHandler configHandler;

    ManagedAction(ConfigHandler configHandler, String actionName, String enableExpr) {
        this.configHandler = configHandler
        this.actionName = actionName;
        this.enableExpr = enableExpr;
    }

    public String getActionName() {
        if (configHandler.localeBundle != null) {
            return configHandler.localeBundle.getString(actionName);
        } else {
            return actionName;
        }
    }

    def boolean isEnabled(Object selObject) {
        if (!StringUtils.isEmpty(enableExpr)) {
            Binding b = new Binding();
            b.setVariable("config", configHandler.configByConventionHandler);
            b.setVariable("selObj", selObject);
            GroovyShell sh = new GroovyShell(b);
            return sh.evaluate(enableExpr);
        }
        return true;
    }
}
