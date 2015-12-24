/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import java.util.Arrays;
import java.util.Comparator;
import static org.apache.commons.io.FileUtils.*;
/**
 *
 * @author mla
 */
public enum SizeCategory  {



    VERY_LOW_PIC_QUAL("01 0-5KB", 5* ONE_KB),
    LOW_TO_MID_QUAL("02 5-20KB", 20 * ONE_KB),
    MID_TO_SEMI_HIGH("03 20-70KB", 70 * ONE_KB),
    SEMI_HIGH_TO_HIGH("04 70-120KB", 120 * ONE_KB),
    HIGH_PIC_QUAL("05 120-300KB", 300 * ONE_KB),
    POOR_MOV_QUAL("06 300KB-1.2MB", 1200 * ONE_KB),
    LOW_MOV_QUAL("07 1.2MB-10MB", 10 * ONE_MB),
    LOW_TO_MID_MOV_QUAL("08 10-35MB", 35 * ONE_MB),
    MID_TO_HIGH("09 35-300MB", 300 * ONE_MB),
    HIGH_MOV_QUAL("10 300-800MB", 800 * ONE_MB),
    VERY_HIGH_MOV_QUAL("11 >800MB-", 8000 * ONE_MB);

    private long max;
    private String name;

    private static long[] ORDEREDSIZES = new long[SizeCategory.values().length];
    static {
        for (int i = 0; i<SizeCategory.values().length; i++) {
            ORDEREDSIZES[i] = SizeCategory.values()[i].max;
            //System.out.println(SizeCategory.values()[i].name + ": "+ SizeCategory.values()[i].max);
        }
    }

    private SizeCategory(String name, long max) {
        this.max = max;
        this.name = name;

    }

    public static SizeCategory searchNearesCatBySize(long size) {
        int index = Arrays.binarySearch(ORDEREDSIZES, size);
        if (index < 0) {
            // not direct size found, but the one, with the higher size: this is marked as negative index:
            index = -index -1;

        }
        if (index > SizeCategory.values().length-1) {
            index = SizeCategory.values().length-1;
        }
        if (index < 0) {
            index = 0;
        }
        return SizeCategory.values()[index];
    }

    public static Comparator<SizeCategory> getSizeComparator () {
        return new Comparator<SizeCategory>() {

            @Override
            public int compare(SizeCategory o1, SizeCategory o2) {
                return (int) (o1.max - o2.max);
            }
        };
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    
}
