/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;

/**
 *
 * @author mla
 */
public interface FileSubstitution {

    /**
     * quasi dasselbe wie substitute: suche mittels substitution, ob u. wo das file existiert.
     * Falls es nicht auffindbar ist, dann returniere null
     * @param canonicalPath
     * @return
     */
    String locateFileOnDrive(String canonicalPath);

    String substitude(String canonicalPath);

    /**
     * tries to map back a existing file name to a substituted path.
     * Is the inverse of locateFileOnDrive.
     * Note, that it is not necessarily a full inverse function. It depends of course on the order of the mappings
     * and if the mappings overlay and therefore which mappings matches first.
     * So you will not necessarily get a valid entry path of an entry which itself points by
     * a path mapping to this valid file path
     * @param realFilePath
     * @return
     */
    String mapBack(String realFilePath);
}
