package org.myjaphoo.gui.util

import groovy.transform.Memoized
import groovy.xml.MarkupBuilder
import org.apache.commons.lang.StringUtils
import org.jdesktop.swingx.JXTreeTable
import org.jdesktop.swingx.action.AbstractActionExt
import org.myjaphoo.MovieNode
import org.myjaphoo.MyjaphooAppPrefs
import org.myjaphoo.gui.icons.Icons
import org.myjaphoo.gui.movietree.DiffNode
import org.myjaphoo.model.FileSubstitution
import org.myjaphoo.model.FileType
import org.myjaphoo.model.cache.CacheManager
import org.myjaphoo.model.db.*
import org.myjaphoo.model.grouping.GroupingDim
import org.myjaphoo.model.logic.FasterFileSubstitution
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.awt.*
import java.awt.event.ActionEvent

/**
 * Helper 
 * @author mla
 * @version $Id$
 *
 */
class Helper {
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/util/resources/Helper");
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    public static final Color TIME_COLOR = Color.green.darker();
    public static final Color LOCATION_COLOR = Color.blue;
    public static final Color FILTEREXPR_COLOR = Color.blue.darker();
    private static Comparator<Token> TOKENTYPECOMPARATOR = new Comparator<Token>() {

        @Override
        public int compare(Token o1, Token o2) {
            return o1.getTokentype().compareTo(o2.getTokentype());
        }
    };
    /**
     * subst. mit extra entity manager construieren, da diese hier in separaten
     * thread genutzt wird.
     */
    private static FileSubstitution substitution = new FasterFileSubstitution();

    public static pic(MarkupBuilder mkb, Icons.IconRes icon) {
        mkb.img(src: icon.url.toString())
    }

    public static void uniqueHTMLFragment(MarkupBuilder mkb, MovieNode node) {
        if (!node.isUnique()) {
            mkb.div {
                pic(mkb, Icons.IR_NOTUNIQUE)
                font(color: "RED", localeBundle.getString("(NOT UNIQUE GROUPED!)"))
                br();
            }
        }
    }

    private static void addCss(MarkupBuilder mkb) {
        mkb.style(type: 'text/css', '''
            ul {
                list-style-type: none;
                margin-left: 10px
            }
  ''')
    }

    /**
     * creates a compact thumb tip text. The return values are cached (memoized), since
     * it could be rel. expensive to create the html string via a markup builder, and its often
     * repetetly called when the gui refreshes.
     * @param node
     * @return
     */
    @Memoized(maxCacheSize = 400)
    public static String createThumbTipTextCompact(MovieNode node) {
        return createThumbTipText(node, true);
    }

    @Memoized(maxCacheSize = 200)
    public static String createThumbTipText(MovieNode node) {
        return createThumbTipText(node, false);
    }

    public static String createThumbTipText(MovieNode node, boolean compact) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {

            addCss(mkp)

            uniqueHTMLFragment(mkp, node);

            addTipText(mkp, node.getMovieEntry(), compact);

            boolean hasDups = node.isHasDups();
            if (hasDups) {
                hr()
                br()
                pic(mkp, Icons.IR_DUPLICATES)
            }
            // duplikate anzeigen: entweder die kondensieren im kondensierungsmodus, oder aber einfach die duplikate:
            if (node.getCondensedDuplicatesSize() > 0) {
                pic(mkp, Icons.IR_CONDENSED);
                b(localeBundle.getString("CONDENSED DUPLICATES:"))
                br();
                i {
                    small {
                        for (MovieEntry dupEntry : node.getCondensedDuplicates()) {
                            br()
                            addTipText(mkp, dupEntry, compact);
                        }
                    }
                }
            } else if (hasDups) {
                b(localeBundle.getString("DUPLICATES IN DATABASE:"))
                br()
                i {
                    small {
                        for (MovieEntry dupEntry : node.getDupsInDatabase()) {
                            br();
                            addTipText(mkp, dupEntry, compact);
                        }
                    }
                }
            }

            TextRepresentations.addComparisonTipTextInfo(mkp, node.getMovieEntry());

            if (node instanceof DiffNode) {
                DiffNode dn = (DiffNode) node;
                TextRepresentations.addDiffComparisonTipTextInfo(mkp, dn, compact);
            }
        }
        return writer.toString();
    }


    private static void addTipText(MarkupBuilder mkb, MovieEntry entry, boolean compact) {
        entry = CacheManager.getCacheActor().getImmutableModel().getMovieEntrySet().find(entry);
        if (entry == null) {
            return;
        }
        addTipTextForMovieEntryFragment(mkb, entry, compact);
    }

    public static void addTipTextForMovieEntryFragment(MarkupBuilder mkb, MovieEntry entry, boolean compact) {
        if (entry == null) {
            return;
        }
        mkb.div() {
            b(entry.getName())
            br();
            a(href: "file=$entry.canonicalDir", entry.canonicalDir)
            br(Utils.humanReadableByteCount(entry.getFileLength()))

            if (entry.getChecksumCRC32() != null) {
                br(localeBundle.getString("CRC32: ") + Long.toHexString(entry.getChecksumCRC32()))
            }
            htmlAttributesFragment(mkb, entry);

            if (entry.getRating() != null) {
                br()
                for (int i = 0; i < entry.getRating().ordinal(); i++) {
                    pic(mkb, Icons.IR_RATING)
                }
                b(entry.getRating().getName())
            }
            internBuildTokenText(mkb, entry);

            listEntryAttributes(mkb, entry, compact);

            if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal()) {
                String located = substitution.locateFileOnDrive(entry.getCanonicalPath());
                if (located != null) {
                    br(localeBundle.getString("LOCATED:") + substitution.substitude(located))
                } else {
                    br()
                    pic(mkb, Icons.IR_NOTLOCATED)
                    font(color: "RED", localeBundle.getString("FILE CAN NOT BE FOUND, CHECK SUBSTITUTIONS!"))
                }
            }

        }
    }

    public static void listEntryAttributes(MarkupBuilder mkb, MovieEntry entry, boolean compact) {
        mkb.div(style: "width: 300px; height: 50px; overflow: auto;") {
            table(border:"1", style: "border: 1px black solid;") {
                listAttributes(mkb, entry, compact);
                entry.getTokens().each { listAttributes(mkb, it, compact); it.assignedMetaTokens.each { listAttributes(mkb, it, compact) } }
            }
        }
    }

    private static void listAttributes(MarkupBuilder mkb, AttributedEntity entity, boolean compact) {
        if (entity.attributes.size() > 0) {
            mkb.tr {
                th(style: "border:1px solid black", colspan:"2", entity.name)
            }
            mkb.tr {
                th(style: "border:1px solid black", "Attribute")
                th(style: "border:1px solid black", "Value")
            }
            entity.attributes.sort{ a,b -> a.key <=> b.key }each { k, v ->
                mkb.tr {
                    td(style: "border:0px solid black", k);
                    if (compact) {
                        v = StringUtils.abbreviate(v, 120);
                    }
                    td(style: "border:0px solid black", v);
                }
            }
        }
    }

    public static void htmlAttributesFragment(MarkupBuilder mkb, MovieEntry entry) {
        mkb.br(/*style: "font-family:verdana;padding:5px;border-radius:5px;border:2px solid #00235A;"*/) {

            String fmt = ""; //NOI18N
            if (entry.getMovieAttrs().getFormat() != null) {
                fmt = prepareFormat(entry.getMovieAttrs().getFormat());
            }
            b(fmt + " " + entry.getMovieAttrs().getWidth() + "x" + entry.getMovieAttrs().getHeight())

            // add only movieattributes for movies, not for pictures
            if (FileType.Movies.is(entry)) {

                br();
                String fmtBitRate = Utils.humanReadableByteCount(entry.getMovieAttrs().getBitrate());
                b(entry.getMovieAttrs().getFps() + " FPS");
                br();
                b(fmtBitRate + " bitrate");
                String len = org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS(entry.getMovieAttrs().getLength() * 1000);
                br();
                b(len); //NOI18N

            }
            br()
        }

    }

    public static void internBuildTokenText(MarkupBuilder mkb, MovieEntry nodeitem) {
        if (nodeitem.getTokens() != null && nodeitem.getTokens().size() > 0) {
            mkb.div(style: "background-color:silver;" /*font-family:verdana;padding:5px;-moz-border-radius:15px;border:2px solid #66235A;"*/) {
                internBuildTokenPlainList(mkb, nodeitem);
            }
        }
    }

    public static void internBuildTokenPlainList(MarkupBuilder mkb, MovieEntry nodeitem) {
        if (nodeitem.getTokens() != null && nodeitem.getTokens().size() > 0) {
            mkb.p {
                ul {
                    // zugeordnete tokens auflisten:
                    Token[] tokens = nodeitem.getTokens().toArray(new Token[nodeitem.getTokens().size()]);
                    Arrays.sort(tokens, TOKENTYPECOMPARATOR);
                    for (Token token : tokens) {
                        internTokenHtmlText(mkb, token);
                    }
                }
            }
        }
    }

    private static void internTokenHtmlText(MarkupBuilder mkb, Token token) {
        mkb.li {
            b() {
                colorToken(mkb, token);
                parentListing(mkb, token);
            }
            // now show also metatokens:
            metataginfo(mkb, token);
        }
    }

    public static void parentListing(MarkupBuilder mkb, Token token) {
        Token parent = token.getParent();
        while (parent != null && parent.getParent() != null) {
            mkb.i(" <-")
            colorToken(mkb, parent);
            parent = parent.getParent();
        }
    }

    public static void metataginfo(MarkupBuilder mkb, Token token) {
        if (token.getAssignedMetaTokens().size() > 0) {
            def namesList = token.getAssignedMetaTokens().collect({ it.name });
            mkb.b {
                b("[")
                namesList.eachWithIndex { String entry, int i ->
                    mkb.a(href: "metatagref=$entry", entry)
                    if (i < namesList.size() - 1) {
                        b(";")
                    }
                }
                b("]")
            }
        }
    }

    public static void colorToken(MarkupBuilder mkb, Token token) {
        pic(mkb, Icons.IR_TAG)
        mkb.font(color: hexColor(tokentypeColorMap.get(token.getTokentype()))) {
            mkb.a(href: "tagref=$token.name", token.getName())
        };
    }

    public static String createMjCompletionTagFragment(Token tag) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)

        mkp.div {
            b {
                img(src: "tagpic:$tag.name")
                colorToken(mkp, tag)
            }
            p();
            // show also the parent tokens (the indirect tokens); all but except the root:
            Helper.parentListing(mkp, tag);

            // now show also metatokens:
            if (tag.getAssignedMetaTokens().size() > 0) {
                i("assigned Meta tags");
                br();
                metataginfo(mkp, tag);
            }
            br();
        }
        return writer.toString();
    }


    public static String createMjCompletionMetaTagFragment(MetaToken tag) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.div {
            b {
                img(src: "mtagpic:$tag.name")
            }
            br();
            b(tag.name);
            p();
        }
        return writer.toString();
    }

    public static String createTokenText(MovieEntry nodeitem) {
        if (nodeitem == null) {
            return null;
        }
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)
        mkp.html {
            internBuildTokenText(mkp, nodeitem)
        }
        return writer.toString()
    }

    private static final Map<Rating, String> ratingMap = new EnumMap<Rating, String>(Rating.class);

    static {
        ratingMap.put(Rating.NONE, ""); //NOI18N
        ratingMap.put(Rating.VERY_BAD, "*"); //NOI18N
        ratingMap.put(Rating.BAD, "**"); //NOI18N
        ratingMap.put(Rating.MIDDLE, "***"); //NOI18N
        ratingMap.put(Rating.GOOD, "****"); //NOI18N
        ratingMap.put(Rating.VERY_GOOD, "*****"); //NOI18N

    }

    public static void addRating(MarkupBuilder mkb, Rating rating) {
        mkb.font(color: "RED", ratingMap.get(rating))
    }

    private static String toHex(int val) {
        String h = Integer.toHexString(val);
        if (h.length() == 1) {
            return "0" + h; //NOI18N
        } else {
            return h;
        }
    }

    public static String wrapColored(Color color, String text) {
        return "<FONT COLOR=" + hexColor(color) + ">" + text + "</FONT>"; //NOI18N
    }

    public static String hexColor(Color color) {
        String hexcolor = "#" + toHex(color.getRed()) + toHex(color.getGreen()) + toHex(color.getBlue());
        return hexcolor;
    }

    private static final Map<TokenType, Color> tokentypeColorMap = new EnumMap(TokenType.class);

    static {
        tokentypeColorMap.put(TokenType.DARSTELLER, new Color(10, 30, 200));
        tokentypeColorMap.put(TokenType.MOVIENAME, new Color(20, 140, 150));
        tokentypeColorMap.put(TokenType.SERIE, new Color(180, 10, 200));
        tokentypeColorMap.put(TokenType.THEMA, new Color(10, 180, 50));
        tokentypeColorMap.put(TokenType.UNBESTIMMT, Color.BLACK);
    }

    public static Color getColorForTokenType(TokenType type) {
        return tokentypeColorMap.get(type);
    }

    private static final Map<GroupingDim, Color> dimColorMap = new EnumMap(GroupingDim.class);

    static {
        dimColorMap.put(GroupingDim.AutoKeyWord, new Color(10, 30, 200));
        dimColorMap.put(GroupingDim.AutoKeyWordStrong, new Color(10, 30, 180));
        dimColorMap.put(GroupingDim.AutoKeyWordVeryStrong, new Color(10, 60, 180));
        dimColorMap.put(GroupingDim.DB_Comparison, new Color(10, 180, 50));
        dimColorMap.put(GroupingDim.Directory, Color.BLACK);
        dimColorMap.put(GroupingDim.Dup_Links_ByLocating, new Color(20, 140, 150));
        dimColorMap.put(GroupingDim.Duplicates, new Color(180, 10, 200));
        dimColorMap.put(GroupingDim.File_Or_Entry_Divider, new Color(10, 180, 80));
        dimColorMap.put(GroupingDim.LocatedDir, new Color(10, 140, 80));
        dimColorMap.put(GroupingDim.Rating, new Color(10, 220, 50));
        dimColorMap.put(GroupingDim.Size, new Color(10, 220, 10));
        dimColorMap.put(GroupingDim.Token, new Color(10, 140, 20));
        dimColorMap.put(GroupingDim.TokenHierarchy, new Color(70, 180, 50));
        dimColorMap.put(GroupingDim.TokenType, new Color(30, 170, 90));
        dimColorMap.put(GroupingDim.Bookmark, new Color(60, 110, 20));
        dimColorMap.put(GroupingDim.Metatoken, new Color(220, 110, 100));
        dimColorMap.put(GroupingDim.MetatokenHierarchy, new Color(210, 140, 140));
        dimColorMap.put(GroupingDim.TokenProposal, new Color(40, 140, 140));
        dimColorMap.put(GroupingDim.VGroupYielding, new Color(160, 140, 10));
        dimColorMap.put(GroupingDim.Title, new Color(160, 90, 10));
        dimColorMap.put(GroupingDim.DuplicatesWithDirs, new Color(20, 10, 50));
        dimColorMap.put(GroupingDim.Assocation, new Color(40, 10, 50));
        dimColorMap.put(GroupingDim.ExifCreateDate, new Color(140, 10, 80));
    }

    public static Color getColorForDim(GroupingDim dim) {
        Color color = dimColorMap.get(dim);
        if (color == null) {
            if (dim != null) {
                logger.warn("no color defined for dimension " + dim); //NOI18N
            }
            return Color.black;
        }
        return color;
    }

    /**
     * FÃ¼gt die MenÃ¼s fÃ¼r die HÃ¶henauswahl fÃ¼r einen treetabel hinzu. ist identisch bei
     * dem MoiveTree als auch beim TokenTree.
     */
    public static void initHeightMenusForJXTreeTable(final JXTreeTable treetable) {
        final int smallHeight = treetable.getRowHeight();
        final int midHeight = smallHeight * 2;
        final int bigHeight = smallHeight * 3;
        final int normalThumbHeight = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();

        treetable.getActionMap().put("column.height1NormalHeight", //NOI18N
                new AbstractActionExt(localeBundle.getString("SMALL ROW SIZE")) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        treetable.setRowHeight(smallHeight);
                    }
                });
        treetable.getActionMap().put("column.height2MidHeight", //NOI18N
                new AbstractActionExt(localeBundle.getString("MID ROW SIZE")) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        treetable.setRowHeight(midHeight);
                    }
                });
        treetable.getActionMap().put("column.height3MaxHeight", //NOI18N
                new AbstractActionExt(localeBundle.getString("BIG ROW SIZE")) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        treetable.setRowHeight(bigHeight);
                    }
                });
        treetable.getActionMap().put("column.height4NormalThumbHeight", //NOI18N
                new AbstractActionExt(localeBundle.getString("THUMB ROW SIZE")) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        treetable.setRowHeight(normalThumbHeight);
                    }
                });
    }

    /**
     * Some format descriptions are far too long. just convert them to a
     * shorter string form
     *
     * @param format
     * @return
     */
    public static String prepareFormat(String format) {
        format = StringUtils.replace(format, "JPEG (Joint Photographic Experts Group)", "JPEG"); //NOI18N
        return format;
    }
}
