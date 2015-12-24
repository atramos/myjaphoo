package org.myjaphoo.model.dbcompare;

import java.awt.*;

/**
 * comparison result.
 */
public enum CompareResult {
    EQUAL(new Color(184, 184, 184), "=="),
    CHANGED(new Color(255, 235, 131), "<>"),
    NEW(new Color(158, 255, 157), "<-"),
    REMOVED(new Color(255, 191, 139), "->");



    private Color diffColor;
    private String direction;

    private CompareResult(Color diffColor, String direction) {
        this.diffColor = diffColor;
        this.direction = direction;
    }

    public Color getDiffColor() {
        return diffColor;
    }

    public Color getDiffColorForground() {
        return getDiffColor().darker();
    }

    public String getDirection() {
        return direction;
    }

    /**
     * Returns the "worstest" compare result. e.g. new is more then changed, changed is more then equal.
     * We use here simply the order of the enums.
     * @param cr1
     * @param cr2
     * @return
     */
    public static CompareResult max (CompareResult cr1, CompareResult cr2) {
       return cr1.ordinal() > cr2.ordinal() ? cr1: cr2;
    }
}
