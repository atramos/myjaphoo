/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * ressource bundle helper class with formatting and parameter subst. functions.
 * @author lang
 */
public class FRessourceBundle {

    private ResourceBundle bundle;

    public FRessourceBundle(String basename) {
        bundle = java.util.ResourceBundle.getBundle(basename);
    }

    public final String getString(String key) {
        return bundle.getString(key);
    }

    public final String getString(String key, Object... messageArguments) {
        String msg = bundle.getString(key);
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(msg);
        String output = formatter.format(messageArguments);
        return output;
    }
}
