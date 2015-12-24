package org.myjaphoo.model.dbcompare;

/**
 * A certain aspect that gets used to compare myjaphoo database objects.
 * This is most times a specific attribute of an entity.
 * This interface has methods for comparison as well as to build compare descriptions and copying methods.
 */
public interface ComparisonAspect<T> {

    Object getAspect(T o);

    void setAspect(T o, Object aspect);

    String getName();
}
