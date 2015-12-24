/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui;

import com.eekboom.utils.Strings;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.pictureComparison.PictureComparison;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.util.ComparatorUtils;

/**
 *
 * @author mla
 */
public enum OrderType {

    BY_DIR_AND_NAME("order by dir and name") {

        public Comparator<AbstractLeafNode> getComparator() {
            return DIR_FILENAME_COMPARATOR;
        }
    },
    BY_NAME("order by name") {

        public Comparator<AbstractLeafNode> getComparator() {
            return FILENAME_COMPARATOR;
        }
    },
    BY_DIR_AND_REVERSE_NAME("order by dir and name desc") {

        public Comparator<AbstractLeafNode> getComparator() {
            return DIR_FILENAME_REVERT_COMPARATOR;
        }
    },
    BY_EXIF_CREATE_DATE("order by exif create date") {

        public Comparator<AbstractLeafNode> getComparator() {
            return EXIF_CREATE_DATE_COMPARATOR;
        }
    },
    BY_AFFINITY("order by similarity") {

        public Comparator<AbstractLeafNode> getComparator() {
            return AFFINITY_COMPARATOR;
        }
    },
    BY_AFFINITY_V2("order by similarity II") {

        public Comparator<AbstractLeafNode> getComparator() {
            return AFFINITY_COMPARATOR_V2;
        }
    },
    BY_AFFINITY_V2_NORMIERT("order by similarity III") {

        public Comparator<AbstractLeafNode> getComparator() {
            return AFFINITY_COMPARATOR_V2_NORMIERT;
        }
    };
    private static final Comparator<AbstractLeafNode> DIR_FILENAME_COMPARATOR = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            int val = o1.getCanonicalDir().compareTo(o2.getCanonicalDir());
            if (val != 0) {
                return val;
            }
            return Strings.compareNaturalAscii(o1.getName(), o2.getName());
        }
    };
    private static final Comparator<AbstractLeafNode> EXIF_CREATE_DATE_COMPARATOR = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            if (o1 instanceof MovieNode && o2 instanceof MovieNode) {
                Date d1 = ((MovieNode) o1).getMovieEntry().getExifData().getExifCreateDate();
                Date d2 = ((MovieNode) o2).getMovieEntry().getExifData().getExifCreateDate();
                return ComparatorUtils.compareTo(d1, d2);
            } else {
                return 0;
            }
        }
    };
    private static final Comparator<AbstractLeafNode> FILENAME_COMPARATOR = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            return Strings.compareNaturalIgnoreCaseAscii(o1.getName(), o2.getName());
        }
    };
    private static final Comparator<AbstractLeafNode> DIR_FILENAME_REVERT_COMPARATOR = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            int val = o1.getCanonicalDir().compareTo(o2.getCanonicalDir());
            if (val != 0) {
                return val;
            }
            return Strings.compareNaturalIgnoreCaseAscii(StringUtils.reverse(o1.getName()), StringUtils.reverse(o2.getName()));
        }
    };
    private static final Comparator<AbstractLeafNode> AFFINITY_COMPARATOR = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            if (o1 instanceof MovieNode && o2 instanceof MovieNode) {
                MovieEntry m1 = MyjaphooDB.singleInstance().ensureObjIsAttached(((MovieNode) o1).getMovieEntry());
                MovieEntry m2 = MyjaphooDB.singleInstance().ensureObjIsAttached(((MovieNode) o2).getMovieEntry());

                return PictureComparison.compareAehnlichkeit(m1, m2);
            } else {
                return 0;
            }
        }
    };
    private static final Comparator<AbstractLeafNode> AFFINITY_COMPARATOR_V2 = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            if (o1 instanceof MovieNode && o2 instanceof MovieNode) {

                MovieEntry m1 = MyjaphooDB.singleInstance().ensureObjIsAttached(((MovieNode) o1).getMovieEntry());
                MovieEntry m2 = MyjaphooDB.singleInstance().ensureObjIsAttached(((MovieNode) o2).getMovieEntry());

                return PictureComparison.compareAehnlichkeitV2(m1, m2);
            } else {
                return 0;
            }
        }
    };
    private static final Comparator<AbstractLeafNode> AFFINITY_COMPARATOR_V2_NORMIERT = new Comparator<AbstractLeafNode>() {

        @Override
        public int compare(AbstractLeafNode o1, AbstractLeafNode o2) {
            if (o1 instanceof MovieNode && o2 instanceof MovieNode) {

                MovieEntry m1 = MyjaphooDB.singleInstance().ensureObjIsAttached(((MovieNode) o1).getMovieEntry());
                MovieEntry m2 = MyjaphooDB.singleInstance().ensureObjIsAttached(((MovieNode) o2).getMovieEntry());

                return PictureComparison.compareAehnlichkeitV3(m1, m2);
            } else {
                return 0;
            }
        }
    };
    private String guiName;

    private OrderType(String guiName) {
        this.guiName = guiName;
    }

    @Override
    public String toString() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/resources/OrderType");
        return localeBundle.getString(guiName);
    }

    abstract public Comparator<AbstractLeafNode> getComparator();
}
