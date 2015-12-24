/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import java.util.List;
import org.myjaphoo.model.FileType;

/**
 *
 * @author mla
 */
public abstract class AbstractLeafNode extends AbstractMovieTreeNode {

    private transient String cachedNaturalNormalizedCanonicalPath = null;

    public AbstractLeafNode() {
        super(true);
    }

    @Override
    public final int getNumOfContainingMovies() {
        return 1;
    }

    public abstract String getCanonicalDir();

    public abstract long getFileLength();

    public abstract String getCanonicalPath();

    public abstract String getComment();

    public String getNaturalNormalizedCanonicalPath() {
        if (cachedNaturalNormalizedCanonicalPath == null) {
            cachedNaturalNormalizedCanonicalPath = createNaturalNormalizedString(getCanonicalPath());
        }
        return cachedNaturalNormalizedCanonicalPath;
    }

    private static String createNaturalNormalizedString(String s) {
        char[] chs = s.toCharArray();
        StringBuilder b = new StringBuilder(chs.length + 5);
        int i = 0;

        while (i < chs.length - 1) {
            char ch = chs[i];
            if (Character.isDigit(ch)) {
                StringBuilder digitbuf = new StringBuilder();

                while (Character.isDigit(ch)) {
                    digitbuf.append(ch);
                    i++;
                    ch = chs[i];
                }
                b.append(formatPseudoNaturalNumber(digitbuf));
            } else {
                b.append(ch);
                i++;
            }
        }
        return b.toString();
    }

    private static String formatPseudoNaturalNumber(StringBuilder digitBuf) {
        // format to 10 digits:
        if (digitBuf.length() < 10) {
            for (int i = 0; i < 10 - digitBuf.length(); i++) {
                digitBuf.insert(0, '0');
            }
        }
        return digitBuf.toString();
    }

    public static boolean any(FileType ft, List<AbstractLeafNode> nodes) {
        for (AbstractLeafNode movieNode : nodes) {
            if (movieNode.is(ft)) {
                return true;
            }
        }
        return false;
    }

    public boolean is(FileType ft) {
        return ft.is(getName());
    }
}
