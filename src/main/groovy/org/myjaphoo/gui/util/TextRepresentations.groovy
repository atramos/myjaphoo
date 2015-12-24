package org.myjaphoo.gui.util

import groovy.xml.MarkupBuilder
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.time.DateFormatUtils
import org.apache.commons.lang.time.FastDateFormat
import org.myjaphoo.MovieNode
import org.myjaphoo.MyjaphooAppPrefs
import org.myjaphoo.gui.icons.Icons
import org.myjaphoo.gui.movietree.DiffNode
import org.myjaphoo.gui.movietree.MovieStructureNode
import org.myjaphoo.model.FileSubstitution
import org.myjaphoo.model.db.BookMark
import org.myjaphoo.model.db.ChronicEntry
import org.myjaphoo.model.db.MovieEntry
import org.myjaphoo.model.dbcompare.CompareResult
import org.myjaphoo.model.dbcompare.ComparisonSetGenerator
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult
import org.myjaphoo.model.dbcompare.DatabaseComparison
import org.myjaphoo.model.dbconfig.DatabaseConfiguration
import org.myjaphoo.model.logic.FasterFileSubstitution

import java.awt.*

/**
 * TextRepresentations 
 * @author mla
 * @version $Id$
 *
 */
class TextRepresentations {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/util/resources/TextRepresentations");

    private static FileSubstitution fs = new FasterFileSubstitution();

    public static String getTextForChronicEntry(ChronicEntry chronic) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            FastDateFormat dateformat = DateFormatUtils.ISO_DATETIME_FORMAT.getDateTimeInstance(DateFormatUtils.ISO_DATETIME_FORMAT.SHORT, DateFormatUtils.ISO_DATETIME_FORMAT.SHORT);
            if (chronic.getView().getCreated() != null) {
                font(color: hexColor(Helper.TIME_COLOR), dateformat.format(chronic.getView().getCreated()))
            }

            b(": " + chronic.getView().getUserDefinedStruct())

            if (chronic.getView().getCurrentSelectedDir() != null) {
                p()
                i {
                    small {
                        font(color: hexColor(Helper.LOCATION_COLOR), chronic.getView().getCurrentSelectedDir())
                    }
                }
            }
            if (chronic.getView().isFilter()) {
                if (!StringUtils.isEmpty(chronic.getView().getPreFilterExpression())) {
                    br()
                    em {
                        font(color: hexColor(Helper.FILTEREXPR_COLOR), localeBundle.getString("PREFILTER:")
                                + chronic.getView().getPreFilterExpression())
                    }
                }
                if (!StringUtils.isEmpty(chronic.getView().getFilterExpression())) {
                    p()
                    em {
                        font(color: hexColor(Helper.FILTEREXPR_COLOR), localeBundle.getString("FILTER:") + chronic.getView().getFilterExpression())
                    }
                }
            }
        }
        return writer.toString();
    }

    public static String getTextForDatabaseConfiguration(DatabaseConfiguration config) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            b {
                u {
                    font(color: hexColor(Color.blue.darker()), config.getName())
                }
            }
            br()
            b {
                font(color: hexColor(Color.BLACK), config.getDatabaseDriver().name())
            }
            br()
            b {
                small {
                    font(color: hexColor(Color.blue.darker()), config.getFilledConnectionUrl())
                }
            }
        }
        return writer.toString();
    }

    public static txtWithColor(MarkupBuilder mkp, Color color, String txt) {
        mkp.font(color: hexColor(color), txt)
    }

    private static String hexColor(Color color) {
        String hexcolor = toHex(color.getRed()) + toHex(color.getGreen()) + toHex(color.getBlue());
        return "#" + hexcolor;
    }

    private static String toHex(int val) {
        String h = Integer.toHexString(val);
        if (h.length() == 1) {
            return "0" + h; //NOI18N
        } else {
            return h;
        }
    }

    public static String getTextForBookmark(BookMark bm) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            fragmentForBookmark(mkp, bm);
        }
        return writer.toString();
    }

    @Deprecated
    public static String getFragmentForBookMark(BookMark bm) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        fragmentForBookmark(mkp, bm)
        return writer.toString();
    }

    public static void fragmentForBookmark(MarkupBuilder mkp, BookMark bm) {
        mkp.div {
            b {
                u {
                    font(color: hexColor(Color.blue.darker()), bm.getName())
                }
            }
            p()

            if (!StringUtils.isEmpty(bm.getDescr())) {
                b {
                    small {
                        font(color: hexColor(Color.blue.darker()), bm.getDescr())
                    }
                }
                p()
            }
            small {
                b(bm.getView().getUserDefinedStruct())

                if (bm.getView().getCurrentSelectedDir() != null) {
                    br();
                    i {
                        font(color: hexColor(Helper.LOCATION_COLOR), bm.getView().getCurrentSelectedDir())
                    }
                }
                if (bm.getView().isFilter()) {
                    if (!StringUtils.isEmpty(bm.getView().getPreFilterExpression())) {
                        br()
                        em {
                            font(color: hexColor(Helper.FILTEREXPR_COLOR), localeBundle.getString("PREFILTER:")
                                    + bm.getView().getPreFilterExpression())
                        }
                    }
                    if (!StringUtils.isEmpty(bm.getView().getFilterExpression())) {
                        br()
                        em {
                            font(color: hexColor(Helper.FILTEREXPR_COLOR), localeBundle.getString("FILTER:")
                                    + bm.getView().getFilterExpression())
                        }
                    }
                }
            }
        }
    }

    public static String getShortTextForBookmark(BookMark bm) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            b {
                u {
                    font(color: hexColor(Color.blue.darker()), bm.getName())
                }
            }
        }
        return writer.toString()
    }


    public static void addComparisonTipTextInfo(MarkupBuilder mkp, MovieEntry entry) {

        if (!DatabaseComparison.getInstance().getInfo().isComparisonDBOpened) {
            return;
        }
        if (DatabaseComparison.getInstance().hasSameEntry(entry)) {
            ArrayList<MovieEntry> dupsInOtherDatabase = DatabaseComparison.getInstance().getDups(entry);
            mkp.div {
                hr()
                br()
                Helper.pic(mkp, Icons.IR_INCOMPDATABASE)
                b {
                    font(color: "BLUE", localeBundle.getString("DB VERGLEICH MIT") + DatabaseComparison.getInstance().getInfo().comparisonDBName + ":")
                }
                br();
                i {
                    small {
                        for (MovieEntry dupEntry : dupsInOtherDatabase) {
                            font(color: "BLUE", dupEntry.getName())
                            br()
                            div(dupEntry.getCanonicalDir())
                            br();
                            if (dupEntry.getRating() != null) {
                                br()
                                div(dupEntry.getRating().getName());
                            }
                        }
                    }
                }

            }
        } else {
        }
    }

    public static void addDiffComparisonTipTextInfo(MarkupBuilder mkp, DiffNode dn, boolean compact) {
        DBDiffCombinationResult diff = dn.getDbdiff();
        appendDiff(mkp, diff.getDiffEntry(), diff.getDiffInfoEntry(), nullCheck(diff.getEntry().getName()), nullCheck(diff.getCDBEntry().getName()));
        appendDiff(mkp, diff.getDiffTag(), diff.getDiffInfoTag(), nullCheck(diff.getToken().getName()), nullCheck(diff.getCDBToken().getName()));
        appendDiff(mkp, diff.getDiffMetaTag(), diff.getDiffInfoMetaTag(), nullCheck(diff.getMetaToken().getName()), nullCheck(diff.getCDBMetaToken().getName()));

        if (diff.getCDBEntry() != ComparisonSetGenerator.NULL_ENTRY) {
            mkp.i {
                small {
                    br();
                    div("Info from Comparison DB:") {
                        Helper.addTipTextForMovieEntryFragment(mkp, diff.getCDBEntry(), compact);
                    }
                }
            }
        }
    }

    private static String nullCheck(String name) {
        return name == null ? "" : name;
    }

    private static void appendDiff(MarkupBuilder mkb, CompareResult comparison, String diffInfo, String left, String right) {
        if (StringUtils.isEmpty(left) && StringUtils.isEmpty(right)) {
            return;
        }
        String txt = left + " " + comparison.getDirection() + " " + right;
        mkb.br();
        mkb.font(color: hexColor(comparison.getDiffColor()), style: "BACKGROUND-COLOR: " + Helper.hexColor(comparison.getDiffColorForground()), txt);


        if (comparison == CompareResult.CHANGED) {
            // show what aspect has changed:
            mkb.br();
            mkb.yield(diffInfo);
        }
    }


    public static String getShortDiffHtmlFragment(MarkupBuilder mkb, DBDiffCombinationResult dbdiff) {
        mkb.font(COLOR: hexColor(dbdiff.getDiffEntry().getDiffColor()), style: "BACKGROUND-COLOR: " + Helper.hexColor(dbdiff.getDiffEntry().getDiffColorForground()), " E ");
        mkb.font(COLOR: hexColor(dbdiff.getDiffTag().getDiffColor()), style: "BACKGROUND-COLOR: " + Helper.hexColor(dbdiff.getDiffTag().getDiffColorForground()), " T ");
        mkb.font(COLOR: hexColor(dbdiff.getDiffMetaTag().getDiffColor()), style: "BACKGROUND-COLOR: " + Helper.hexColor(dbdiff.getDiffMetaTag().getDiffColorForground()), " M ");
    }


    public static String createMovieTreeCellRendererIconLabelTextForMovieNode(MovieNode movieNode) {
        boolean added = false;
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal()) {
                boolean located  = fs.locateFileOnDrive(movieNode.getMovieEntry().getCanonicalPath()) != null;
                if (!located) {
                    Icons.IR_NOTLOCATED.append(mkp)
                    added = true;
                }
            }
            if (!movieNode.isUnique()) {
                Icons.IR_NOTUNIQUE.append(mkp);
                added = true;
            }
            if (movieNode.isHasDups()) {
                Icons.IR_DUPLICATES.append(mkp);
                added = true;
            }
            if (DatabaseComparison.getInstance().hasSameEntry(movieNode.getMovieEntry())) {
                Icons.IR_INCOMPDATABASE.append(mkp)
                added = true;
            }
            if (movieNode instanceof DiffNode) {
                DBDiffCombinationResult dbdiff = ((DiffNode) movieNode).getDbdiff();
                getShortDiffHtmlFragment(mkp, dbdiff);
                added = true;
            }
        }
        if (!added) {
            return "";
        } else {
            return writer.toString()
        }
    }

    public static String createMovieTreeCellRendererIconLabelTextForStructureNode(MovieStructureNode movieDirNode) {
        if (movieDirNode.getCanonicalDir() != null) {
            if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal()) {
                boolean located = fs.locateFileOnDrive(movieDirNode.getCanonicalDir()) != null;
                if (located) {
                    return "";
                } else {
                    def writer = new StringWriter()
                    def mkp = new MarkupBuilder(writer)
                    mkp.html {
                        if (!located) {
                            Icons.IR_NOTLOCATED.append(mkp);
                        }
                    }
                    return writer.toString()
                }
            } else return "";
        }
    }

    public static String createCardLabelText(MovieNode node, String name) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            Helper.addRating(mkp, node.getMovieEntry().getRating());
            font(color: "BLACK", name);
            Helper.uniqueHTMLFragment(mkp, node);
            Helper.htmlAttributesFragment(mkp, node.getMovieEntry());
            Helper.internBuildTokenPlainList(mkp, node.getMovieEntry());
        }
        return writer.toString()
    }
}
