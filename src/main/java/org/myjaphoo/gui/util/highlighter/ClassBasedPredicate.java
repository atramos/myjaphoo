package org.myjaphoo.gui.util.highlighter;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

import java.awt.*;

/**
 * Abstract predicate which needs to cast the cell value to make the highlighting decision.
 */
public abstract class ClassBasedPredicate<T> implements HighlightPredicate {

    private Class<T> classOfNode;

    public ClassBasedPredicate(Class<T> classOfNode) {
        this.classOfNode = classOfNode;
    }

    @Override
    public boolean isHighlighted(Component component, ComponentAdapter componentAdapter) {
        if (!(classOfNode.isAssignableFrom(componentAdapter.getValue().getClass()))) {
            return false;
        }
        T node = (T) componentAdapter.getValue();
        return isHighlighted(node);
    }

    public abstract boolean isHighlighted(T node);
}
