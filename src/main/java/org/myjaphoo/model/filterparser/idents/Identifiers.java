/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.idents;

import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.mlsoft.common.StringUtilities;
import org.mlsoft.structures.TreeStructure;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;


/**
 *
 * @author mla
 */
public class Identifiers {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/filterparser/idents/resources/Identifiers");
    private static final String NAME_DESCR_PARAM = localeBundle.getString("NAME_DESCR");
    private static final String NAME_WITHOUT_SUFFIX_DESCR_PARAM = localeBundle.getString("NAME_WITHOUT_SUFFIX_DESCR");
    private static final String PATH_DESCR_PARAM = localeBundle.getString("PATH_DESCR");
    private static final String TITLE_DESCR_PARAM = localeBundle.getString("TITLE_DESCR");
    private static final String COMMENT_DESCR_PARAM = localeBundle.getString("COMMENT_DESCR");
    private static final String DIR_DESCR_PARAM = localeBundle.getString("DIR_DESCR");
    private static final String TOK_DESCR_PARAM = localeBundle.getString("TOK_DESCR");
    private static final String TOK_PARENTS_DESCR_PARAM = localeBundle.getString("TOK_PARENTS_DESCR");
    private static final String TOKCOUNT_DESCR_PARAM = localeBundle.getString("TOKCOUNT_DESCR");
    private static final String TOKTYPE_DESCR_PARAM = localeBundle.getString("TOKTYPE_DESCR");
    private static final String METATOK_DESCR_PARAM = localeBundle.getString("METATOK_DESCR");
    private static final String METATOKCOUNT_DESCR_PARAM = localeBundle.getString("METATOKCOUNT_DESCR");
    private static final String METATOK_PARENTS_DESCR_PARAM = localeBundle.getString("METATOK_PARENTS_DESCR");
    private static final String METATOKDESCR_DESCR_PARAM = localeBundle.getString("METATOKDESCR_DESCR");
    private static final String TOKDESCR_DESCR_PARAM = localeBundle.getString("TOKDESCR_DESCR");
    private static final String SIZE_DESCR_PARAM = localeBundle.getString("SIZE_DESCR");
    private static final String WIDTH_DESCR_PARAM = localeBundle.getString("WIDTH_DESCR");
    private static final String HEIGHT_DESCR_PARAM = localeBundle.getString("HEIGHT_DESCR");
    private static final String LEN_DESCR_PARAM = localeBundle.getString("LEN_DESCR");
    private static final String FORMAT_DESCR_PARAM = localeBundle.getString("FORMAT_DESCR");
    private static final String RATING_DESCR_PARAM = localeBundle.getString("RATING_DESCR");
    private static final String CHECKSUM_DESCR_PARAM = localeBundle.getString("CHECKSUM_DESCR");
    private static final String SOMETHING_DESCR_PARAM = localeBundle.getString("SOMETHING_DESCR");

    private static final String NAME_USAGE_PARAM = localeBundle.getString("NAME_USAGE");
    private static final String NAME_WITHOUT_SUFFIX_USAGE_PARAM = localeBundle.getString("NAME_WITHOUT_SUFFIX_USAGE");
    private static final String PATH_USAGE_PARAM = localeBundle.getString("PATH_USAGE");
    private static final String TITLE_USAGE_PARAM = localeBundle.getString("TITLE_USAGE");
    private static final String COMMENT_USAGE_PARAM = localeBundle.getString("COMMENT_USAGE");
    private static final String DIR_USAGE_PARAM = localeBundle.getString("DIR_USAGE");
    private static final String TOK_USAGE_PARAM = localeBundle.getString("TOK_USAGE");
    private static final String TOK_PARENTS_USAGE_PARAM = localeBundle.getString("TOK_PARENTS_USAGE");
    private static final String TOKCOUNT_USAGE_PARAM = localeBundle.getString("TOKCOUNT_USAGE");
    private static final String TOKTYPE_USAGE_PARAM = localeBundle.getString("TOKTYPE_USAGE");
    private static final String METATOK_USAGE_PARAM = localeBundle.getString("METATOK_USAGE");
    private static final String METATOKCOUNT_USAGE_PARAM = localeBundle.getString("METATOKCOUNT_USAGE");
    private static final String METATOK_PARENTS_USAGE_PARAM = localeBundle.getString("METATOK_PARENTS_USAGE");
    private static final String METATOKDESCR_USAGE_PARAM = localeBundle.getString("METATOKDESCR_USAGE");
    private static final String TOKDESCR_USAGE_PARAM = localeBundle.getString("TOKDESCR_USAGE");
    private static final String SIZE_USAGE_PARAM = localeBundle.getString("SIZE_USAGE");
    private static final String WIDTH_USAGE_PARAM = localeBundle.getString("WIDTH_USAGE");
    private static final String HEIGHT_USAGE_PARAM = localeBundle.getString("HEIGHT_USAGE");
    private static final String LEN_USAGE_PARAM = localeBundle.getString("LEN_USAGE");
    private static final String FORMAT_USAGE_PARAM = localeBundle.getString("FORMAT_USAGE");
    private static final String RATING_USAGE_PARAM = localeBundle.getString("RATING_USAGE");
    private static final String CHECKSUM_USAGE_PARAM = localeBundle.getString("CHECKSUM_USAGE");
    private static final String SOMETHING_USAGE_PARAM = localeBundle.getString("SOMETHING_USAGE");

    public static final FixIdentifier NAME = new EntryContextIdentifier(
            "name", //NOI18N
            NAME_DESCR_PARAM, NAME_USAGE_PARAM, ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.strVal(movieEntry.getName());
        }
    };
    public static final FixIdentifier NAME_WITHOUT_SUFFIX = new EntryContextIdentifier(
            "namewithoutsuffix", //NOI18N
            NAME_WITHOUT_SUFFIX_DESCR_PARAM, NAME_WITHOUT_SUFFIX_USAGE_PARAM,  ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.strVal(StringUtilities.removeSuffix(movieEntry.getName()));
        }
    };
    public static final FixIdentifier PATH = new EntryContextIdentifier(
            "path", //NOI18N
            PATH_DESCR_PARAM, PATH_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.strVal(movieEntry.getCanonicalPath());
        }
    };
    public static final FixIdentifier TITLE = new EntryContextIdentifier(
            "title", //NOI18N
            TITLE_DESCR_PARAM, TITLE_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.strVal(movieEntry.getTitle());
        }
    };
    public static final FixIdentifier COMMENT = new EntryContextIdentifier(
            "comment", //NOI18N
            COMMENT_DESCR_PARAM, COMMENT_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.strVal(movieEntry.getComment());
        }
    };
    public static final FixIdentifier DIR = new EntryContextIdentifier(
            "dir", //NOI18N
            DIR_DESCR_PARAM, DIR_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.strVal(movieEntry.getCanonicalDir());
        }
    };
    public static final FixIdentifier TOKEN = new TagContextIdentifier(
            "tok", //NOI18N
            TOK_DESCR_PARAM, TOK_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(Token tag) {
            return Values.strVal(tag.getName());
        }
    };
    public static final FixIdentifier TAG = new SynonymIdentifier("tag", TOKEN); //NOI18N
    public static final FixIdentifier PARENTS = new TreeStructureContextIdentifier(
            "parents", //NOI18N
            TOK_PARENTS_DESCR_PARAM, TOK_PARENTS_USAGE_PARAM, ExprType.TEXT) {

        @Override
        public Value extractIdentValue(TreeStructure tag) {
            return Values.strVal(tag.getParent() != null ? tag.getParent().getName(): "");
        }
    };
    //public static final FixIdentifier TAGPARENTS = new SynonymIdentifier("parents", TOKPARENTS); //NOI18N
    public static final FixIdentifier TOKENCOUNT = new EntryContextIdentifier(
            "tokcount", //NOI18N
            TOKCOUNT_DESCR_PARAM, TOKCOUNT_USAGE_PARAM,ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry movieEntry) {
            return Values.numVal(movieEntry.getTokens().size());
        }
    };
    public static final FixIdentifier TAGCOUNT = new SynonymIdentifier("tagcount", TOKENCOUNT); //NOI18N
    public static final FixIdentifier TOKENTYPE = new TagContextIdentifier(
            "toktype", //NOI18N
            TOKTYPE_DESCR_PARAM, TOKTYPE_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(Token tag) {
            if (tag.getTokentype() != null) {
                return Values.strVal(tag.getTokentype().name());
            } else {
                return new StringValue(null);
            }
        }
    };
    public static final FixIdentifier TAGTYPE = new SynonymIdentifier("tagtype", TOKENTYPE); //NOI18N
    public static final FixIdentifier METATOK = new MetaTagContextIdentifier(
            "metatok", //NOI18N
            METATOK_DESCR_PARAM, METATOK_USAGE_PARAM, ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MetaToken tag) {
            return Values.strVal(tag.getName());
        }
    };
    public static final FixIdentifier METATAG = new SynonymIdentifier("metatag", METATOK); //NOI18N
    public static final FixIdentifier METATOKENCOUNT = new EntryContextIdentifier(
            "metatokcount", //NOI18N
            METATOKCOUNT_DESCR_PARAM, METATOKCOUNT_USAGE_PARAM,ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            int count = 0;
            for (Token token2 : entry.getTokens()) {
                count += token2.getAssignedMetaTokens().size();
            }
            return Values.numVal(count);
        }
    };
    public static final FixIdentifier METATAGCOUNT = new SynonymIdentifier("metatagcount", METATOKENCOUNT); //NOI18N

    public static final FixIdentifier METATOKDESCR = new MetaTagContextIdentifier(
            "metatokdescr", //NOI18N
            METATOKDESCR_DESCR_PARAM, METATOKDESCR_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MetaToken mt) {
            return Values.strVal(mt.getDescription());
        }
    };
    public static final FixIdentifier METATAGDESCR = new SynonymIdentifier("metatagdescr", METATOKDESCR); //NOI18N

    public static final FixIdentifier TOKENDESCR = new TagContextIdentifier(
            "tokdescr", //NOI18N
            TOKDESCR_DESCR_PARAM, TOKDESCR_USAGE_PARAM,ExprType.TEXT) {

        @Override
        public Value extractIdentValue(Token tag) {
            return Values.strVal(tag.getDescription());
        }
    };
    public static final FixIdentifier TAGDESCR = new SynonymIdentifier("tagdescr", TOKENDESCR); //NOI18N

    public static final FixIdentifier SIZE = new EntryContextIdentifier(
            "size", //NOI18N
            SIZE_DESCR_PARAM, SIZE_USAGE_PARAM, ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            return Values.numVal(entry.getFileLength());
        }
    };
    public static final FixIdentifier WIDTH = new EntryContextIdentifier(
            "width", //NOI18N
            WIDTH_DESCR_PARAM, WIDTH_USAGE_PARAM,ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            return Values.numVal(entry.getMovieAttrs().getWidth());
        }
    };
    public static final FixIdentifier HEIGHT = new EntryContextIdentifier(
            "height", //NOI18N
            HEIGHT_DESCR_PARAM, HEIGHT_USAGE_PARAM,ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            return Values.numVal(entry.getMovieAttrs().getHeight());
        }
    };
    public static final FixIdentifier LEN = new EntryContextIdentifier(
            "len", //NOI18N
            LEN_DESCR_PARAM, LEN_USAGE_PARAM,ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            return Values.numVal(entry.getMovieAttrs().getLength());
        }
    };
    public static final FixIdentifier FORMAT = new EntryContextIdentifier(
            "format", //NOI18N
            FORMAT_DESCR_PARAM, FORMAT_USAGE_PARAM, ExprType.TEXT) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            return Values.strVal(entry.getMovieAttrs().getFormat());
        }
    };
    public static final FixIdentifier RATING = new EntryContextIdentifier(
            "rating", //NOI18N
            RATING_DESCR_PARAM, RATING_USAGE_PARAM, ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            if (entry.getRating() != null) {
                return Values.numVal(entry.getRating().ordinal());
            } else {
                return Values.numVal(-1);
            }
        }
    };
    public static final FixIdentifier CHECKSUM = new EntryContextIdentifier(
            "checksum", //NOI18N
            CHECKSUM_DESCR_PARAM, CHECKSUM_USAGE_PARAM,ExprType.NUMBER) {

        @Override
        public Value extractIdentValue(MovieEntry entry) {
            return Values.numVal(savetyLong(entry.getChecksumCRC32()));
        }
    };
    public static final FixIdentifier SOMETHING = new FixIdentifier<JoinedDataRow, JoinedDataRow>(
            "something", //NOI18N
            SOMETHING_DESCR_PARAM, SOMETHING_USAGE_PARAM,ExprType.TEXT, JoinedDataRow.class, JoinedDataRow.class, true, true) {

        private FixIdentifier list[] = {PATH, TOKEN, TITLE, COMMENT, METATOK, TOKENDESCR, METATOKDESCR};

        @Override
        public JoinedDataRow extractQualifierContext(JoinedDataRow row) {
            return row;
        }

        @Override
        public Value extractIdentValue(JoinedDataRow context) {
            Collection<Value> allValues = new ArrayList<Value>();
            for (FixIdentifier i : list) {
                Value v = i.extractIdentValue(i.extractQualifierContext(context));
                if (v instanceof ValueSet) {
                    allValues.addAll(((ValueSet) v).getValues());
                } else {
                    allValues.add(v);
                }
            }
            // add all attributes of entry, tag, metatag to this set:
            add(allValues, context.getEntry().getAttributes().values());
            add(allValues, context.getToken().getAttributes().values());
            add(allValues, context.getMetaToken().getAttributes().values());

            return new ValueSet(allValues);
        }

    };

    private static void add(Collection<Value> collection, Collection<String> values) {
        for (String s: values) {
            collection.add(new StringValue(s));
        }
    }

    public static final FixIdentifier X = new SynonymIdentifier("x", SOMETHING); //NOI18N
    public static final FixIdentifier ANYTHING = new SynonymIdentifier("anything", SOMETHING); //NOI18N
    public static final FixIdentifier TEXT = new SynonymIdentifier("text", SOMETHING); //NOI18N

    static {
        // define identifiers for all exif tags
        for (final TagInfo tag : ExifTagConstants.ALL_EXIF_TAGS) {
            if (tag != ExifTagConstants.EXIF_TAG_CREATE_DATE) {

                new ExifIdentifier(tag,
                        MessageFormat.format(localeBundle.getString("EXIF TAG"), tag.name), "", ExprType.TEXT) {

                    @Override
                    public Value extractIdentValue(MovieEntry entry) {
                        Object value = entry.getExifData().getExifValue(tag.name);
                        return Values.strVal((String) value);
                    }
                };
            }
        }
        // special case for exif_create_date:
        new ExifIdentifier(ExifTagConstants.EXIF_TAG_CREATE_DATE,
                MessageFormat.format(localeBundle.getString("EXIF TAG DATE"), ExifTagConstants.EXIF_TAG_CREATE_DATE.name), "", ExprType.DATE) {

            @Override
            public Value extractIdentValue(MovieEntry entry) {
                return new DateValue(entry.getExifData().getExifCreateDate());
            }
        };

    }

    private static long savetyLong(Long val) {
        if (val == null) {
            return 0;
        } else {
            return val.longValue();
        }
    }

    public static final FixIdentifier DIFFENTRY = new FixIdentifier<JoinedDataRow, DBDiffCombinationResult>(
            "diffentry", //NOI18N
            "diffentry", "diffentry" , ExprType.TEXT, JoinedDataRow.class, DBDiffCombinationResult.class, false, false) {

        @Override
        public DBDiffCombinationResult extractQualifierContext(JoinedDataRow row) {
            return (DBDiffCombinationResult) row;
        }

        @Override
        public Value extractIdentValue(DBDiffCombinationResult context) {
            return Values.strVal(context.getDiffEntry().name());
        }
    };
    public static final FixIdentifier DIFFTAG = new FixIdentifier<JoinedDataRow, DBDiffCombinationResult>(
            "difftag", //NOI18N
            "difftag", "difftag" , ExprType.TEXT, JoinedDataRow.class, DBDiffCombinationResult.class, true, false) {

        @Override
        public DBDiffCombinationResult extractQualifierContext(JoinedDataRow row) {
            return (DBDiffCombinationResult) row;
        }

        @Override
        public Value extractIdentValue(DBDiffCombinationResult context) {
            return Values.strVal(context.getDiffTag().name());
        }
    };
    public static final FixIdentifier DIFFMETATAG = new FixIdentifier<JoinedDataRow, DBDiffCombinationResult>(
            "diffmetatag", //NOI18N
            "diffmetatag", "diffmetatag" , ExprType.TEXT, JoinedDataRow.class, DBDiffCombinationResult.class, false, true) {

        @Override
        public DBDiffCombinationResult extractQualifierContext(JoinedDataRow row) {
            return (DBDiffCombinationResult) row;
        }

        @Override
        public Value extractIdentValue(DBDiffCombinationResult context) {
            return Values.strVal(context.getDiffMetaTag().name());
        }
    };
}
