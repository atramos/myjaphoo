package org.mlsoft.common;

/**
 * PropertyAccessor interface to access properties via el expressions.
 * Currently we try several implementations (own, jexl, groovy?).
 *
 * This defines a accessor for a particular property based on a el expression.
 * One instance caches the el expression or internal objects needed for the access.
 *
 * For a new expression a new object should be created.
 *
 * @author lang
 * @version $Id$
 */
public interface PropertyAccessor {

    /**
     * set the property value of that particular object.
     * @param o the object
     * @param value the value for the property to set
     */
    void setVal(Object o, Object value);

    /**
     * get the property value for that object.
     * @param o the object
     * @return the value
     */
    Object getVal(Object o);


    /**
     * gets the type of the property.
     * @param clazz the class
     * @return the property
     */
    Class<?> getType(Class clazz);
}
