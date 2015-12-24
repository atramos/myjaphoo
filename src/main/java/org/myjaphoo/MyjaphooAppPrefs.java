package org.myjaphoo;

import org.mlsoft.common.prefs.model.editors.*;
import org.myjaphoo.model.logic.imp.ImportTypes;
import org.myjaphoo.model.logic.imp.thumbprovider.ThumbnailProviders;

import java.awt.*;
import java.util.ResourceBundle;

import static org.myjaphoo.model.config.AppConfig.*;

/**
 * application preferences which are specific to the fat client version.
 * These preferences extend the AppConfig preferences.
 *
 * @author unbekannt
 * @version 1.0
 */
public class MyjaphooAppPrefs {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MyjaphooAppPrefs");

    private final static EditorGroup VIEWER = new EditorGroup(ROOT,
            localeBundle.getString("VIEWER GUI"),
            localeBundle.getString("VIEWER GUI"),
            localeBundle.getString("VIEWER DESC"));

    private final static EditorGroup COLORS = new EditorGroup(ROOT,
            localeBundle.getString("COLORS GUI"),
            localeBundle.getString("COLORS GUI"),
            localeBundle.getString("COLORS DESC"));
    private final static EditorGroup SYNTAXHIGHLIGHTING = new EditorGroup(COLORS,
            localeBundle.getString("SYNTAXHIGHLIGHTING GUI"),
            localeBundle.getString("SYNTAXHIGHLIGHTING GUI"),
            localeBundle.getString("SYNTAXHIGHLIGHTING DESC"));

    private final static EditorGroup CACHING = new EditorGroup(ROOT,
            localeBundle.getString("CACHING GUI"),
            localeBundle.getString("CACHING GUI"),
            localeBundle.getString("CACHING DESC"));

    public final static BooleanVal PRF_USE_NATURALSORTING =
            new BooleanVal(COMMON, "common.useNaturalSorting",  //NOI18N
                    localeBundle.getString("PRF_USE_NATURALSORTING GUI"),
                    localeBundle.getString("PRF_USE_NATURALSORTING DESC"),
                    Boolean.TRUE);
    public final static IntegerVal PRF_THUMBCACHE_CACHESIZE =
            new IntegerVal(CACHING, "cache.thumbcache.cacheSize",  //NOI18N
                    localeBundle.getString("PRF_THUMBCACHE_CACHESIZE GUI"),
                    localeBundle.getString("PRF_THUMBCACHE_CACHESIZE DESC"),
                    200);
    public final static IntegerVal PRF_THUMBCACHE_MAXPREFETCH =
            new IntegerVal(CACHING, "cache.thumbcache.maxPrefetch",  //NOI18N
                    localeBundle.getString("PRF_THUMBCACHE_MAXPREFETCH GUI"),
                    localeBundle.getString("PRF_THUMBCACHE_MAXPREFETCH DESC"),
                    30);
    public final static IntegerVal PRF_THUMBCACHE_FETCHAROUNDSIZE =
            new IntegerVal(CACHING, "cache.thumbcache.fetcharoundSize",  //NOI18N
                    localeBundle.getString("PRF_THUMBCACHE_FETCHAROUNDSIZE GUI"),
                    localeBundle.getString("PRF_THUMBCACHE_FETCHAROUNDSIZE DESC"),
                    10);
    public final static BooleanVal PRF_THUMBCACHE_PREFETCHTHUMBS =
            new BooleanVal(CACHING, "cache.thumbcache.prefetchthumbs",  //NOI18N 
                    localeBundle.getString("PRF_THUMBCACHE_PREFETCHTHUMBS GUI"),
                    localeBundle.getString("PRF_THUMBCACHE_PREFETCHTHUMBS DESC"),
                    false);

    public final static StringVal PRF_DATABASENAME =
            new StringVal(COMMON, "databasename",  //NOI18N
                    localeBundle.getString("PRF_DATABASENAME GUI"),
                    localeBundle.getString("PRF_DATABASENAME DESC"),
                    "noname"); //NOI18N

    public final static EnumSelectionVal PRF_PLAF_CLASS =
            new EnumSelectionVal(COMMON, "app.plaf.plafnameOrStyle",  //NOI18N
                    localeBundle.getString("PRF_PLAF GUI"),
                    localeBundle.getString("PRF_PLAF DESC"),
                    AvailableLookAndFeels.CREMECOFFEESKIN, AvailableLookAndFeels.class);

    public final static BooleanVal PRF_PLAF_FRAME_DECORATED =
            new BooleanVal(COMMON, "app.plaf.frameDecorated",  //NOI18N
                    localeBundle.getString("PRF_PLAF_FRAME_DECORATED_GUI"),
                    localeBundle.getString("PRF_PLAF_FRAME_DECORATED_DESC"),
                    false);

    public final static EnumSelectionVal PRF_IMPORTMODE =
            new EnumSelectionVal(IMPORT, "importmode",  //NOI18N
                    localeBundle.getString("PRF_IMPORTMODE GUI"),
                    localeBundle.getString("PRF_IMPORTMODE DESC"),
                    ImportTypes.Pictures,
                    ImportTypes.class);

    public final static IntegerVal PRF_THUMBSIZE =
            new IntegerVal(IMPORT, "thumbsize",  //NOI18N
                    localeBundle.getString("PRF_THUMBSIZE GUI"),
                    localeBundle.getString("PRF_THUMBSIZE DESC"),
                    128);
    public final static IntegerVal PRF_IMPORT_NUMWORKERTHREADS =
            new IntegerVal(IMPORT, "numWorkerThreads",  //NOI18N
                    localeBundle.getString("PRF_NUMWORKERTHREADS GUI"),
                    localeBundle.getString("PRF_NUMWORKERTHREADS DESC"),
                    1);

    public final static IntegerVal PRF_MAXCHRONIC =
            new IntegerVal(COMMON, "maxchronic",  //NOI18N
                    localeBundle.getString("PRF_MAXCHRONIC GUI"),
                    localeBundle.getString("PRF_MAXCHRONIC DESC"),
                    20);

    public final static EnumSelectionVal<ThumbnailProviders> PRF_PREFERED_THUMB_PROVIDER =
            new EnumSelectionVal(VIEWER, "app.thumbprovider.preferedProvider",  //NOI18N
                    localeBundle.getString("PRF_PREFERED_THUMB_PROVIDER GUI"),
                    localeBundle.getString("PRF_PREFERED_THUMB_PROVIDER DESC"),
                    ThumbnailProviders.NONE,
                    ThumbnailProviders.class);


    public final static FileVal PRF_LINUX_VLCEXE =
            new FileVal(VIEWER, "vlc.linux.exe",  //NOI18N
                    localeBundle.getString("PRF_LINUX_VLCEXE GUI"),
                    localeBundle.getString("PRF_LINUX_VLCEXE DESC"),
                    "vlc"); //NOI18N
    public final static FileVal PRF_WINDOWS_VLCEXE =
            new FileVal(VIEWER, "vlc.windows.exe",  //NOI18N
                    localeBundle.getString("PRF_WINDOWS_VLCEXE GUI"),
                    localeBundle.getString("PRF_WINDOWS_VLCEXE DESC"),
                    "C:\\\\Program Files\\\\VideoLAN\\\\VLC\\\\vlc.exe"); //NOI18N
    public final static FileVal PRF_LINUX_MPLAYER_EXE =
            new FileVal(VIEWER, "mplayer.linux.exe",  //NOI18N
                    localeBundle.getString("PRF_LINUX_MPLAYER_EXE GUI"),
                    localeBundle.getString("PRF_LINUX_MPLAYER_EXE DESC"),
                    "mplayer"); //NOI18N

    public final static FileVal PRF_WINDOWS_MPLAYER_EXE =
            new FileVal(VIEWER, "mplayer.windows.exe",  //NOI18N
                    localeBundle.getString("PRF_WINDOWS_MPLAYER_EXE GUI"),
                    localeBundle.getString("PRF_WINDOWS_MPLAYER_EXE DESC"),
                    "c:/programme/mplayer/mplayer.exe"); //NOI18N

    public final static FileVal PRF_LINUX_KMPLAYER_EXE =
            new FileVal(VIEWER, "kmplayer.linux.exe",  //NOI18N
                    localeBundle.getString("PRF_LINUX_KMPLAYER_EXE GUI"),
                    localeBundle.getString("PRF_LINUX_KMPLAYER_EXE DESC"),
                    "kmplayer"); //NOI18N

    public final static FileVal PRF_LINUX_FFMPEGTHUMBNAILER_EXE =
            new FileVal(VIEWER, "ffmpegthumbnailer.linux.exe",  //NOI18N
                    localeBundle.getString("PRF_LINUX_FFMPEGTHUMBNAILER_EXE GUI"),
                    localeBundle.getString("PRF_LINUX_FFMPEGTHUMBNAILER_EXE DESC"),
                    "ffmpegthumbnailer"); //NOI18N
    public final static FileVal PRF_WINDOWS_FFMPEGTHUMBNAILER_EXE =
            new FileVal(VIEWER, "ffmpegthumbnailer.windows.exe",  //NOI18N
                    localeBundle.getString("PRF_WINDOWS_FFMPEGTHUMBNAILER_EXE GUI"),
                    localeBundle.getString("PRF_WINDOWS_FFMPEGTHUMBNAILER_EXE DESC"),
                    "c:/programme/ffmpegthumbnailer/ffmpegthumbnailer.exe"); //NOI18N

    public final static FileVal PRF_LINUX_FFMPEG_EXE =
            new FileVal(VIEWER, "ffmpeg.linux.exe",  //NOI18N
                    localeBundle.getString("PRF_LINUX_FFMPEG_EXE GUI"),
                    localeBundle.getString("PRF_LINUX_FFMPEG_EXE DESC"),
                    "ffmpeg"); //NOI18N
    public final static FileVal PRF_WINDOWS_FFMPEG_EXE =
            new FileVal(VIEWER, "ffmpeg.windows.exe",  //NOI18N
                    localeBundle.getString("PRF_WINDOWS_FFMPEG_EXE GUI"),
                    localeBundle.getString("PRF_WINDOWS_FFMPEG_EXE DESC"),
                    "c:/programme/ffmpeg/bin/ffmpeg.exe"); //NOI18N


    public final static BooleanVal PRF_SHOWTIPOFDAY =
            new BooleanVal(COMMON, "gui.showTipOfDay",  //NOI18N
                    localeBundle.getString("PRF_SHOWTIPOFDAY GUI"),
                    localeBundle.getString("PRF_SHOWTIPOFDAY DESC"),
                    Boolean.TRUE);

    // intermediate pref for the first java7 versions to verify what is faster: apache commons or java7
    public final static BooleanVal PRF_USE_COMMONS_IO_FOR_FILECOPYING =
            new BooleanVal(INTERNAL, "java7Version.copyFileWithCommonsIO",  //NOI18N
                    "use commons io for file copying",
                    "use commons io for file copying",
                    Boolean.TRUE);

    public final static BooleanVal PRF_SHOW_FILLOCALISATION_HINTS =
            new BooleanVal(INTERNAL, "intern.filelocalisation_hints.showHints",  //NOI18N
                    "show infos/hints, if a file could be localized on the file system",
                    "deactivating may make the application more responsive, if most of the files are located on networks which are not available most of the time",
                    Boolean.TRUE);


    public final static ColorVal PRF_SYNTAXH_IDENTS = new ColorVal(SYNTAXHIGHLIGHTING, "syntaxhighlighting.idents",  //NOI18N
            localeBundle.getString("PRF_SYNTAXH_IDENTS GUI"),
            localeBundle.getString("PRF_SYNTAXH_IDENTS DESC"),
            new Color(0, 0, 255));
    public final static ColorVal PRF_SYNTAXH_OPERATORS = new ColorVal(SYNTAXHIGHLIGHTING, "syntaxhighlighting.operators",  //NOI18N
            localeBundle.getString("PRF_SYNTAXH_OPERATORS GUI"),
            localeBundle.getString("PRF_SYNTAXH_OPERATORS DESC"),
            new Color(255, 20, 240).darker());
    public final static ColorVal PRF_SYNTAXH_UNITS = new ColorVal(SYNTAXHIGHLIGHTING, "syntaxhighlighting.units",  //NOI18N
            localeBundle.getString("PRF_SYNTAXH_UNITS GUI"),
            localeBundle.getString("PRF_SYNTAXH_UNITS DESC"),
            new Color(150, 150, 150));
    public final static ColorVal PRF_SYNTAXH_LITERALS = new ColorVal(SYNTAXHIGHLIGHTING, "syntaxhighlighting.literals",  //NOI18N
            localeBundle.getString("PRF_SYNTAXH_LITERALS GUI"),
            localeBundle.getString("PRF_SYNTAXH_LITERALS DESC"),
            new Color(206, 123, 0));
    public final static ColorVal PRF_SYNTAXH_ERRORS = new ColorVal(SYNTAXHIGHLIGHTING, "syntaxhighlighting.errors",  //NOI18N
            localeBundle.getString("PRF_SYNTAXH_ERRORS GUI"),
            localeBundle.getString("PRF_SYNTAXH_ERRORS DESC"),
            new Color(255, 0, 0));
    public final static ColorVal PRF_SYNTAXH_BRACES = new ColorVal(SYNTAXHIGHLIGHTING, "syntaxhighlighting.braces",  //NOI18N
            localeBundle.getString("PRF_SYNTAXH_BRACES GUI"),
            localeBundle.getString("PRF_SYNTAXH_BRACES DESC"),
            new Color(255, 200, 100).darker());


    private MyjaphooAppPrefs() {
        // private constructor
    }

    public final static EditorRoot getPrefStructure() {
        return ROOT;
    }

}
