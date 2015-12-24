package org.mlsoft.common;

/**
 * DefaultPropertyAccessImpl
 *
 * @author mla
 * @version $Id$
 */
public class DefaultPropertyAccessImpl implements PropertyAccessor {

    private String elExpr;

    public DefaultPropertyAccessImpl(String elExpr) {
        this.elExpr = elExpr;
    }

    @Override
    public void setVal(Object o, Object value) {
        MiniEL.setVal(o, value, elExpr);
    }

    @Override
    public Object getVal(Object o) {
        return MiniEL.getVal(o, elExpr);
    }

    @Override
    public Class<?> getType(Class clazz) {
        return MiniEL.getType(clazz, elExpr);
    }
}
