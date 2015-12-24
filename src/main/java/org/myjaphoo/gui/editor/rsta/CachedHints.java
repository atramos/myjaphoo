package org.myjaphoo.gui.editor.rsta;

import org.apache.commons.lang.StringUtils;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.AttributedEntity;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.functions.Functions;
import org.myjaphoo.model.filterparser.idents.ExifIdentifier;
import org.myjaphoo.model.filterparser.idents.Identifiers;
import org.myjaphoo.model.filterparser.idents.Qualifier;
import org.myjaphoo.model.grouping.AutoKeyWordVeryStrongPartialGrouper;

import java.util.*;

/**
 * Caches completion hints, which are expensive to build (those from movie entries).
 */
public class CachedHints {

    private static HashMap<String, List<String>> identWords = null;

    private static HashMap<String, List<String>> exifWords = null;

    private static HashMap<String, List<String>> entryAttrWords = null;

    private static HashMap<String, List<String>> tagAttrWords = null;

    private static HashMap<String, List<String>> metaTagAttrWords = null;

    private static List<String> entryAttrKeys = null;
    private static List<String> tagAttrKeys = null;
    private static List<String> metaTagAttrKeys = null;

    public static synchronized List<String> getPathHints() {
        checkInit();
        return identWords.get("path");
    }

    public static synchronized List<String> getDirHints() {
        checkInit();
        return identWords.get("dir");
    }

    public static synchronized List<String> getTitleHints() {
        checkInit();
        return identWords.get("title");
    }

    public static synchronized List<String> getCommentHints() {
        checkInit();
        return identWords.get("comment");
    }

    public static synchronized List<String> getExifHints(String exifTagName) {
        checkExifInit();
        List<String> result = exifWords.get(exifTagName);
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    private static final TagInfo[] separateSavedExifTags = new TagInfo[]{TiffConstants.EXIF_TAG_MAKE, TiffConstants.EXIF_TAG_MODEL, TiffConstants.EXIF_TAG_CREATE_DATE, TiffConstants.EXIF_TAG_FNUMBER,
            TiffConstants.EXIF_TAG_ISO, TiffConstants.EXIF_TAG_EXPOSURE_TIME};


    private static void checkExifInit() {
        if (exifWords == null) {
            HashMapSet map = new HashMapSet();
            for (MovieEntry entry : CacheManager.getCacheActor().getImmutableModel().getMovies()) {
                Map<String, String> exifValues = entry.getExifData().getExifValues();
                if (exifValues != null) {
                    map.put(exifValues);
                }
                // add also the extra-saved attributes:
                for (TagInfo ti : separateSavedExifTags) {
                    Object val = entry.getExifData().getExifValue(ti.name);
                    if (val != null) {
                        map.put(ti.name, val.toString());
                    }
                }
            }
            // prepare exifWords:
            exifWords = map.toTargetStructure();
        }
    }


    static class HashMapSet {
        HashMap<String, Set<String>> mapSet = new HashMap<>();


        public void put(Map<String, String> map) {
            for (Map.Entry<String, String> attrEntry : map.entrySet()) {
                put(attrEntry.getKey(), attrEntry.getValue());
            }
        }

        public void put(String key, String val) {
            Set<String> entry = mapSet.get(key);
            if (entry == null) {
                entry = new HashSet<>();
                mapSet.put(key, entry);
            }
            entry.add(val);
        }

        public HashMap<String, List<String>> toTargetStructure() {
            HashMap<String, List<String>> targetMap = new HashMap<>();
            for (Map.Entry<String, Set<String>> entry : mapSet.entrySet()) {
                List<String> list = new ArrayList<>(entry.getValue());
                // order the list:
                Collections.sort(list);
                targetMap.put(entry.getKey(), list);
            }
            return targetMap;
        }

        public List<String> allKeysAsList() {
            List<String> list = new ArrayList<>(mapSet.keySet());
            // order the list:
            Collections.sort(list);
            return list;
        }
    }

    private static void checkInit() {
        if (identWords == null) {

            HashMapSet entryAttrMap = new HashMapSet();
            HashMapSet tagAttrMap = new HashMapSet();
            HashMapSet metaTagAttrMap = new HashMapSet();

            HashMapSet map = new HashMapSet();
            Set<String> pathWords = new HashSet<>();
            Set<String> dirSet = new HashSet<>();
            Set<String> titleSet = new HashSet<>();
            Set<String> commentSet = new HashSet<>();
            Set<String> allSet = new HashSet<>();

            map.mapSet.put(Identifiers.PATH.getName(), pathWords);
            map.mapSet.put(Identifiers.DIR.getName(), dirSet);
            map.mapSet.put(Identifiers.TITLE.getName(), titleSet);
            map.mapSet.put(Identifiers.COMMENT.getName(), commentSet);
            map.mapSet.put(Identifiers.TEXT.getName(), allSet);
            map.mapSet.put(Identifiers.X.getName(), allSet);
            map.mapSet.put(Identifiers.ANYTHING.getName(), allSet);
            map.mapSet.put(Identifiers.SOMETHING.getName(), allSet);

            for (MovieEntry entry : CacheManager.getCacheActor().getImmutableModel().getMovies()) {
                String[] hints = org.apache.commons.lang.StringUtils.split(entry.getCanonicalPath().toLowerCase(), AutoKeyWordVeryStrongPartialGrouper.SEPARATORS);
                for (String hint : hints) {
                    if (hint.length() > 3) {
                        pathWords.add(hint);
                        allSet.add(hint);
                    }
                }
                dirSet.add(entry.getCanonicalDir());
                if (!StringUtils.isEmpty(entry.getTitle())) {
                    titleSet.add(entry.getTitle());
                    allSet.add(entry.getTitle());
                }
                if (!StringUtils.isEmpty(entry.getComment())) {
                    commentSet.add(entry.getComment());
                    allSet.add(entry.getComment());
                }

                entryAttrMap.put(entry.getAttributes());
            }

            identWords = map.toTargetStructure();

            for (Token token : CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList()) {
                tagAttrMap.put(token.getAttributes());
            }
            for (MetaToken token : CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList()) {
                metaTagAttrMap.put(token.getAttributes());
            }

            entryAttrWords = entryAttrMap.toTargetStructure();
            entryAttrKeys = entryAttrMap.allKeysAsList();

            tagAttrWords = tagAttrMap.toTargetStructure();
            tagAttrKeys = tagAttrMap.allKeysAsList();

            metaTagAttrWords = metaTagAttrMap.toTargetStructure();
            metaTagAttrKeys = metaTagAttrMap.allKeysAsList();
        }
    }

    public static List<String> getHintsForIdentifier(Qualifier ident) {
        if (ident.getName().startsWith("exif")) {
            ExifIdentifier exifident = (ExifIdentifier) ident;
            return getExifHints(exifident.getTagName());
        }
        checkInit();
        if (ident == Identifiers.TAG || ident == Identifiers.TOKEN) {
            return asStringList(CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList());
        }

        List<String> result = identWords.get(ident.getName());
        if (result == null) {
            result = Collections.emptyList();
        }
        return result;
    }

    public static List<String> getHintsForAttributeKeys(AttributedEntity ae) {
        checkInit();
        if (ae instanceof MovieEntry) {
            return entryAttrKeys;
        } else if (ae instanceof Token) {
            return tagAttrKeys;
        } else if (ae instanceof MetaToken) {
            return metaTagAttrKeys;
        } else {
            throw new RuntimeException("unknown attributed entity!");
        }
    }

    public static List<String> getEntryAttrKeys() {
        checkInit();
        return entryAttrKeys;
    }

    public static List<String> getTagAttrKeys() {
        checkInit();
        return tagAttrKeys;
    }

    public static List<String> getMetaTagAttrKeys() {
        checkInit();
        return metaTagAttrKeys;
    }

    public static List<String> getHintsForAttributes(Function func, String attributeName) {
        checkInit();
        if (func == Functions.ENTRYATTR) {
            return entryAttrWords.get(attributeName);
        } else if (func == Functions.TAGATTR) {
            return tagAttrWords.get(attributeName);
        } else if (func == Functions.METATAGATTR) {
            return metaTagAttrWords.get(attributeName);
        } else {
            throw new RuntimeException("unknown attributed entity!");
        }
    }

    public static List<String> getHintsForAttributes(AttributedEntity ae, String attributeName) {
        checkInit();
        if (ae instanceof MovieEntry) {
            return entryAttrWords.get(attributeName);
        } else if (ae instanceof Token) {
            return tagAttrWords.get(attributeName);
        } else if (ae instanceof MetaToken) {
            return metaTagAttrWords.get(attributeName);
        } else {
            throw new RuntimeException("unknown attributed entity!");
        }
    }

    private static List<String> asStringList(List<Token> tokens) {
        ArrayList<String> result = new ArrayList<>();
        for (Token tok : tokens) {
            result.add(tok.getName());
        }
        Collections.sort(result);
        return result;
    }
}
