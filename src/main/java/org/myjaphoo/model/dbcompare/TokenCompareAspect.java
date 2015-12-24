package org.myjaphoo.model.dbcompare;

import org.apache.commons.lang.ObjectUtils;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.db.TokenType;

/**
 * Movie Entry compare aspects.
 */
public enum TokenCompareAspect implements ComparisonAspect<Token> {


    NAME {
        @Override
        public Object getAspect(Token t) {
            return t.getName();
        }

        @Override
        public void setAspect(Token t, Object aspect) {
            t.setName((String) aspect);
        }
    },
    TOKTYPE {
        @Override
        public Object getAspect(Token t) {
            return t.getTokentype();
        }

        @Override
        public void setAspect(Token t, Object aspect) {
            t.setTokentype((TokenType) aspect);
        }
    },
    DESCRIPTION {
        @Override
        public Object getAspect(Token t) {
            return t.getDescription();
        }

        @Override
        public void setAspect(Token t, Object aspect) {
            t.setDescription((String) aspect);
        }
    };
    @Override
    public String getName() {
        return name();
    }
}
