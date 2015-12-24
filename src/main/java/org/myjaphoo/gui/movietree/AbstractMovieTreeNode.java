/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import com.eekboom.utils.Strings;
import java.util.Comparator;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.MyjaphooAppPrefs;

/**
 *
 * @author mla
 */
public abstract class AbstractMovieTreeNode extends AbstractTreeNode<AbstractMovieTreeNode> {

    public AbstractMovieTreeNode() {
    }

    public AbstractMovieTreeNode(boolean isLeaf) {
        super(isLeaf);
    }

    public abstract String getName();

    public abstract String getTitle();

    public abstract int getNumOfContainingMovies();

    public abstract long getSizeOfContainingMovies();
    // Object[] getListOfColumnValues(); abstrakter für den treetable
    private static Comparator<String> strComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Strings.compareNatural(o1, o2, true, null);
        }
    };

    //private static Comparator<String> strComparator = Strings.getNaturalComparator();

    static {
        if (!MyjaphooAppPrefs.PRF_USE_NATURALSORTING.getVal()) {
            strComparator = new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            };
        }
    }
    private static final Comparator<AbstractMovieTreeNode> COMPARATOR = new Comparator<AbstractMovieTreeNode>() {

        @Override
        public int compare(AbstractMovieTreeNode o1, AbstractMovieTreeNode o2) {
            // sortierung:
            // 1. nach dir, 2. nach name:
            boolean o1haschildren = o1.getChildCount() > 0;
            boolean o2haschildren = o2.getChildCount() > 0;
            if (o1haschildren && !o2haschildren) {
                return 1;
            }
            if (!o1haschildren && o2haschildren) {
                return -1;
            }
            return strComparator.compare(o1.getName(), o2.getName());
        }
    };

    public void sortNaturalByName() {
        sortList(COMPARATOR);
        for (AbstractMovieTreeNode node : getChildren()) {
            node.sortNaturalByName();
        }
    }

    public String getPathName() {
        StringBuilder b = new StringBuilder();
        AbstractMovieTreeNode node = this;
        while (node != null) {
            if (node.getParent() != null) {
                if (b.length() > 0) {
                    b.insert(0, "/"); //NOI18N
                }
                b.insert(0, node.getName());
            } else {
                // für root setzen wir nicht den namen:
                //b.insert(0, "Structure/");
            }
            node = (AbstractMovieTreeNode) node.getParent();
        }
        return b.toString();
    }
}
