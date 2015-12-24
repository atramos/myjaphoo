/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import java.util.Comparator;

/**
 *
 * @author mla
 */
public class MetaTokenComparator implements Comparator<MetaToken> {

    @Override
    public int compare(MetaToken o1, MetaToken o2) {
        return o1.getName().compareTo(o2.getName());
    }

}
