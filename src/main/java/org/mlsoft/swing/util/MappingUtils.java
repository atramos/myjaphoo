package org.mlsoft.swing.util;

import org.mlsoft.common.MiniEL;

/**
 * Mapping utils.
 */
public class MappingUtils {

    public static java.lang.Class<?> getCorrectType(Class<?> cl) {
        if (cl.isPrimitive()) {
            return primitiveClassType(cl);
        } else {
            return cl;
        }
    }

    private static Class<?> primitiveClassType(Class<?> cl) {
        if (cl == Boolean.TYPE) {
            return Boolean.class;
        } else if (cl == Character.TYPE) {
            return Character.class;
        } else if (cl == Byte.TYPE) {
            return Byte.class;
        } else if (cl == Short.TYPE) {
            return Short.class;
        } else if (cl == Integer.TYPE) {
            return Integer.class;
        } else if (cl == Long.TYPE) {
            return Long.class;
        } else if (cl == Float.TYPE) {
            return Float.class;
        } else if (cl == Double.TYPE) {
            return Double.class;
        } else {
            return cl;
        }
    }
}
