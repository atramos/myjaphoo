/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import org.myjaphoo.model.cache.AbstractCache;
import org.myjaphoo.model.WmEntitySet;

/**
 * mock cache: hält statisch einfach ein wmentity set.
 * @author mla
 */
class MockCache extends AbstractCache<WmEntitySet> {

    private WmEntitySet wmset;

    public MockCache(WmEntitySet wmset) {
        this.wmset = wmset;
    }

    @Override
    protected WmEntitySet createCachedObject() {
        return wmset;
    }
}
