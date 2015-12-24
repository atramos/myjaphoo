package org.myjaphoo.model.filterparser.processing;

/**
 * DefaultProcessingRequirementInformation
 * @author mla
 * @version $Id$
 */
public class DefaultProcessingRequirementInformation implements ProcessingRequirementInformation {

    private boolean needsTagRelation = false;

    private boolean needsMetaTagRelation = false;

    @Override
    public boolean needsTagRelation() {
        return needsTagRelation;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return needsMetaTagRelation;
    }

    public void join(ProcessingRequirementInformation p) {
        needsTagRelation = needsTagRelation || p.needsTagRelation();
        needsMetaTagRelation = needsMetaTagRelation || p.needsMetaTagRelation();
    }
}
