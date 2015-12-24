package org.mlsoft.swing.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: mla
 * Date: 04.12.12
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ToolbarAction {

    java.lang.String name() default "";

    int order() default -1;

    /** is this command context relevant*/
    boolean contextRelevant() default true;

    java.lang.String enableExpr() default "";

}