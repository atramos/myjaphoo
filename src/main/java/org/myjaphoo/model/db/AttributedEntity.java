package org.myjaphoo.model.db;

import java.util.Map;
import java.util.Set;

/**
 * AttributedEntity
 *
 * @author lang
 * @version $Id$
 */
public interface AttributedEntity {
    String getName();

    String getComment();

    void setComment(String str);

    Map<String, String> getAttributes();

    void setAttributes(Map<String, String> attributes);

    Set<? extends AttributedEntity> getReferences();
}
