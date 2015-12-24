/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;

/**
 * @author lang
 */
public enum ExprType {

    TEXT(String.class),
    NUMBER(Long.class),
    BOOLEAN(Boolean.class),
    DATE(java.util.Date.class),
    NULL(java.lang.Object.class),

    /**
     * unspecific type which is returned by groovy methods.
     */
    OBJECT(java.lang.Object.class);

    private Class javaType;

    private ExprType(Class clazz) {
        javaType = clazz;
    }

    public boolean isCompatible(ExprType otherType) {
        if (this == otherType) {
            return true;
        }
        if (this == ExprType.NULL || otherType == ExprType.NULL) {
            return true;
        }
        if (this == ExprType.OBJECT || otherType == ExprType.OBJECT) {
            return true;
        }

        return false;
    }

    public Class getJavaType() {
        return javaType;
    }
}
