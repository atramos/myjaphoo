package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.Token;

/**
 * ProposalEntry
 *
 * @author lang
 * @version $Id$
 */
public class ProposalEntry {
    public String name;
    public Token tag;

    public ProposalEntry(String name, Token tag) {
        this.name = name;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProposalEntry that = (ProposalEntry) o;

        if (!name.equals(that.name)) return false;
        if (!tag.equals(that.tag)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }
}
