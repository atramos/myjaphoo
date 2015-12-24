package org.myjaphoo.gui.systemprops;

/**
 * Sys props entry.
 */
public class SysPropsEntry {

    private String name;

    private String value;

    public SysPropsEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
