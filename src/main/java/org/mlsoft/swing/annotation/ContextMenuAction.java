package org.mlsoft.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * a definition of a context menu action within a table or tree.
 * User: lang

 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ContextMenuAction {

    String name() default "";

    int order() default -1;

    /** is this command context relevant*/
    boolean contextRelevant() default true;

    java.lang.String enableExpr() default "";

}