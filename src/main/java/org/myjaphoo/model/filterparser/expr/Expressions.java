package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains helper functions for expressions.
 */
public class Expressions {



    public static boolean needsTagRelation(Collection<? extends ProcessingRequirementInformation> expressions) {
        for (ProcessingRequirementInformation arg : expressions) {
            if (arg.needsTagRelation()) {
                return true;
            }
        }
        return false;
    }

    public static boolean needsMetaTagRelation(Collection<? extends ProcessingRequirementInformation> expressions) {
        for (ProcessingRequirementInformation arg : expressions) {
            if (arg.needsMetaTagRelation()) {
                return true;
            }
        }
        return false;
    }

    public static <T> T findFirstDeepSearch(Expression e, Class<T> clazz) {
        Expression current = e;
        if (clazz.isInstance(e)) {
            return (T) e;
        }
        for (Expression child : e.getChildren()) {
            T match = findFirstDeepSearch(child, clazz);
            if (match != null) {
                return match;
            }
        }
        return null;
    }




    public static interface Predicate {
         boolean eval(Expression expr);
    }

    private static final Predicate TRUE_PREDICATE = new Predicate() {
        @Override
        public boolean eval(Expression expr) {
            return true;
        }
    };
    public static <T> List<T> findAllExprParts(Expression expr, Class<T> termClass) {
        List<T> result = new ArrayList<>();
        if (expr != null) {
            findAllParts(expr, termClass, result, TRUE_PREDICATE);
        }
        return result;
    }

    public static <T> List<T> findAllExprParts(Expression expr, Class<T> termClass, Predicate visitChildren) {
        List<T> result = new ArrayList<>();
        if (expr != null) {
            findAllParts(expr, termClass, result, visitChildren);
        }
        return result;
    }

    private static <T> void findAllParts(Expression expr, Class<T> termClass, List<T> result, Predicate visitChildren) {
        if (termClass.isInstance(expr)) {
            result.add((T) expr);
        }
        if (visitChildren.eval(expr)) {
        for (Expression child : expr.getChildren()) {
            findAllParts(child, termClass, result, visitChildren);
        }
        }
    }
}
