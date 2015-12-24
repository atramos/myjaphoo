/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mla
 */
public class StringUtilities {

    public static String retainLetters(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isLetter(ch)) {
                b.append(ch);
            }

        }
        return b.toString();
    }

    public static String removeSuffix(String name) {
        int pos = StringUtils.lastIndexOf(name, '.');
        if (pos >= 0) {
            return StringUtils.substring(name, 0, pos);
        } else {
            return name;
        }
    }

    public static String retainDigits(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isDigit(ch)) {
                b.append(ch);
            }

        }
        return b.toString();
    }

    public static String removeDigits(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!Character.isDigit(ch)) {
                b.append(ch);
            }

        }
        return b.toString();
    }

    public static String removeLetters(String s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!Character.isLetter(ch)) {
                b.append(ch);
            }

        }
        return b.toString();
    }
}
