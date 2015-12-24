package org.myjaphoo.model.filterparser.values;

/**
 * Helper functions for values.
 */
public class Values {

    public static Value strVal(String str) {
        return new StringValue(str);
    }

    public static Value numVal(long num) {
        return new LongValue(num);
    }
}
