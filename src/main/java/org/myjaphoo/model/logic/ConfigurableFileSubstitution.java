package org.myjaphoo.model.logic;

/**
 * ConfigurableFileSubstitution
 * @author lang
 * @version $Id$
 */
public interface ConfigurableFileSubstitution {

    /**
     * Try to locate a file. If it is not possible then returns null, which indicates to try out the
     * next configurable file substitution.
     * @param canonicalPath canonical path of this entry.
     * @return  the located file path on the file system or null
     */
    String locateFileOnDrive(String canonicalPath);

}
