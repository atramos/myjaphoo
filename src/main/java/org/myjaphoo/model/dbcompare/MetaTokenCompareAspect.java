package org.myjaphoo.model.dbcompare;

import org.apache.commons.lang.ObjectUtils;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;

/**
 * Movie Entry compare aspects.
 */
public enum MetaTokenCompareAspect implements ComparisonAspect<MetaToken> {


    NAME {
        @Override
        public Object getAspect(MetaToken t) {
            return t.getName();
        }

        @Override
        public void setAspect(MetaToken t, Object aspect) {
            t.setName((String) aspect);
        }
    },
    DESCRIPTION {
        @Override
        public Object getAspect(MetaToken t) {
            return t.getDescription();
        }

        @Override
        public void setAspect(MetaToken t, Object aspect) {
            t.setDescription((String) aspect);
        }
    };

    @Override
    public String getName() {
        return name();
    }
}
