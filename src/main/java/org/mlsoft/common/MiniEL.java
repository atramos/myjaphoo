package org.mlsoft.common;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * helper functions, that we have not directly in jexl.
 */
public class MiniEL {

    public static void setVal(Object o, Object value, String elExpression) {
        Class clazz = o.getClass();
        String[] props = StringUtils.split(elExpression, '.');
        Method method = null;
        int last = props.length - 1;
        int curr = 0;
        for (String prop : props) {
            if (curr == last) {
                method = tryResolveSetterMethod(clazz, prop);
                callMethod(method, o, value);
            } else {
                method = tryResolveGetterMethod(clazz, prop);

                o = callMethod(method, o);
                if (o != null) {
                    clazz = o.getClass();
                } else {
                    break;
                }
            }
            curr++;
        }
    }

    public static Object getVal(Object o, String elExpression) {
        Class clazz = o.getClass();
        String[] props = StringUtils.split(elExpression, '.');
        Method method = null;

        for (String prop : props) {
            method = tryResolveGetterMethod(clazz, prop);

            o = callMethod(method, o);
            if (o != null) {
                clazz = o.getClass();
            } else {
                break;
            }
        }
        return o;
    }

    private static void setFieldValue(Field field, String propName, Object o, Object val) {
        field.setAccessible(true);
        Object value = null;
        try {
            field.set(o, val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("error setting field value " + propName, e);
        }
    }

    private static Object callMethod(Method method, Object o, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(o, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("error getting method " + method.getName(), e);
        }
    }

    private static Object getVal(Field field, Object o) {
        try {
            field.setAccessible(true);
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("error getting field " + field.getName(), e);
        }
    }

    /**
     * Returns teh type of a expression, walking down the fields of a class.
     * Would be nice to have something equivalent direct in jexl, but
     * have not found something like this...
     *
     * @param clazz
     * @param elExpression
     * @return
     */
    public static Class<?> getType(Class clazz, String elExpression) {
        String[] props = StringUtils.split(elExpression, '.');
        Field field = null;

        for (String prop : props) {
            field = getField(clazz, prop);
            if (field == null) {
                // try getter method:
                Method m = tryResolveGetterMethod(clazz, prop);
                clazz = m.getReturnType();
            } else {
                clazz = field.getType();
            }
        }
        return clazz;
    }

    private static Method tryResolveGetterMethod(Class clazz, String prop) {
        String getter = "get" + StringUtils.capitalize(prop);
        try {
            // try the getter:
            return clazz.getMethod(getter, null);
        } catch (NoSuchMethodException e) {
            // second try: try to resolve the prop as its pure name:
            try {
                return clazz.getMethod(prop, null);
            } catch (NoSuchMethodException e1) {
                // third try: "is" property style:
                String isGetter = "is" + StringUtils.capitalize(prop);
                try {
                    return clazz.getMethod(isGetter, null);
                } catch (NoSuchMethodException e2) {
                    throw new RuntimeException("could not find method for property " + prop);
                }
            }
        }
    }

    private static Method search(Class clazz, String methName) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methName)) {
                return method;
            }
        }
        return null;
    }

    private static Method tryResolveSetterMethod(Class clazz, String prop) {
        String getter = "set" + StringUtils.capitalize(prop);
        Method m = search(clazz, getter);
        if (m == null) {
            m = search(clazz, prop);
        }
        if (m == null) {
            throw new RuntimeException("could not find method for property " + prop);
        }
        return m;

    }

    private static Field getField(Class clazz, String propName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(propName);
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
