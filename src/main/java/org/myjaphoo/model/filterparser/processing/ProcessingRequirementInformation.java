/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.processing;

/**
 * Returns information about processing requirements.
 * Important information is here if a cartesian product to tags or metatags
 * is requried for filtering and grouping.
 * @author lang
 */
public interface ProcessingRequirementInformation {
    
    /**
     * is a cartesian relation to tags required?
     * @return 
     */
    public boolean needsTagRelation();
    
    /**
     * is a cartesian relation to metatags required?
     * @return 
     */
    public boolean needsMetaTagRelation();
}
