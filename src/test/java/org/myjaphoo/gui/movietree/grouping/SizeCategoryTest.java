/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.movietree.grouping;

import org.myjaphoo.model.grouping.SizeCategory;
import junit.framework.TestCase;

/**
 *
 * @author mla
 */
public class SizeCategoryTest extends TestCase {
    


 
    public void testSizeCategorization()
    {
        SizeCategory cat = SizeCategory.searchNearesCatBySize(3 * 1024);
        assertEquals(SizeCategory.VERY_LOW_PIC_QUAL, cat);

        cat = SizeCategory.searchNearesCatBySize(0);
        assertEquals(SizeCategory.VERY_LOW_PIC_QUAL, cat);     

        cat = SizeCategory.searchNearesCatBySize(Long.MAX_VALUE);
        assertEquals(SizeCategory.VERY_HIGH_MOV_QUAL, cat);

        cat = SizeCategory.searchNearesCatBySize(500 * 1024 * 1025);
        assertEquals(SizeCategory.HIGH_MOV_QUAL, cat);

    }

}
