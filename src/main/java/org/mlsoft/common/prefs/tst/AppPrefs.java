package org.mlsoft.common.prefs.tst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javax.swing.UIManager;
import org.mlsoft.common.prefs.model.PrefsRoot;
import org.mlsoft.common.prefs.model.editors.*;


/**
 * test prefs
 * 
 * @author unbekannt
 * @version 1.0
 */

public class AppPrefs {

    // load up the user preferences
    private final static Preferences userPrefs = Preferences.userNodeForPackage(AppPrefs.class);


    private final static String PRESERVESTRUCTURE = "preserveStructure";

    private final static String HTTP_USERAGENT = "HTTP_UserAgent";

    private final static String HTTP_LOGHEADERS = "HTTP_LogHeaders";

    private final static String DOWNLOADLOCATION = "downloadLocation";

    private final static String PLAF = "plaf";

    private final static String WINDOWWIDTH = "windowWidth";

    private final static String WINDOWHEIGHT = "windowHeight";

    private final static String WINDOWX = "windowX";

    private final static String WINDOWY = "windowY";

    private final static PrefsRoot NOTEDITABLEPREFS = new PrefsRoot(userPrefs);

    public final static IntegerVal PRF_WINDOWWIDTH =
            new IntegerVal(NOTEDITABLEPREFS, WINDOWWIDTH, WINDOWWIDTH, WINDOWWIDTH, 750);

    public final static IntegerVal PRF_WINDOWHEIGHT =
            new IntegerVal(NOTEDITABLEPREFS, WINDOWHEIGHT, WINDOWHEIGHT, WINDOWHEIGHT, 550);

    public final static IntegerVal PRF_WINDOWX =
            new IntegerVal(NOTEDITABLEPREFS, WINDOWX, WINDOWX, WINDOWX, -999);

    public final static IntegerVal PRF_WINDOWY =
            new IntegerVal(NOTEDITABLEPREFS, WINDOWY, WINDOWY, WINDOWY, -999);

    private final static PrefsRoot ROOT = new PrefsRoot(userPrefs);

    public final static EditorGroup COMMON = new EditorGroup(ROOT, "Common", "Common", "Common");

    public final static EditorGroup GENERAL =
            new EditorGroup(ROOT, "blablab",
                    "falfalsf", "falsdflasldf");

    public final static EditorGroup HTTP = new EditorGroup(ROOT, "HTTP", "HTTP", "HTTP");

    public final static EditorGroup PROXY = new EditorGroup(HTTP, "Proxy", "Proxy", "Proxy");

    public final static EditorGroup VIEWER = new EditorGroup(ROOT, "Viewer", "Viewer", "Viewer");
    
    public final static EditorGroup SPIDER = new EditorGroup(ROOT, "Spider", "Spider", "Spider Options");
    public final static EditorGroup MUTATION = new EditorGroup(SPIDER, "Mutation", "Mutation", "Mutation Options");
    
    

    public final static EditorGroup COMMANDSERVER = new EditorGroup(ROOT, "Commandserver", "CommandServer", "Command Server");
    
    
    // test: neue prefs-struktur:
    private static final String[] userAgents =
            {"appversion",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.5) Gecko/20031007",
                    "Internet Explorer 5.0", "Mozilla M18"};

    public final static DirectoryVal PRF_DOWNLOADLOCATION =
            new DirectoryVal(GENERAL, DOWNLOADLOCATION,
                    "downlaodlocation", "Location where to Download",
                    "/home/");

    public final static BooleanVal PRF_PRESERVESTRUCTURE =
            new BooleanVal(GENERAL, PRESERVESTRUCTURE,
                    "createdirstruct",
                    "Should the Directory structure be preserved?", Boolean.TRUE);
    
    public final static BooleanVal PRF_PREFIXDATE =
        new BooleanVal(GENERAL, "prefixdownloadlocationwithdate",
                "Prefix the downloadlocation with date in format YYYY/MM/DD",
                "Should prefix the downloadlocation with date in format YYYY/MM/DD?", Boolean.TRUE);    

    public final static IntegerVal PRF_DOUNLOADTASKCOUNT =
            new IntegerVal(GENERAL, "downloadtaskcount", "Anzahl gleichzeitiger Downloads",
                    "Legt die Anzahl der gleichzeiten Downloadjobs fest", 5);

    public final static IntegerVal PRF_MINIMUMFILESIZE =
            new IntegerVal(GENERAL, "downloadminsize", "Mindestdateigr��e",
                    "Mindestgr��e von Dateien, die gedownloaded werden sollen in KB", 4);

    public final static BooleanVal PRF_LOGHEADERS =
            new BooleanVal(HTTP, HTTP_LOGHEADERS, "logheader",
                    "should log headers?", Boolean.TRUE);

    public final static StringSelectionVal PRF_USERAGENT =
            new StringSelectionVal(HTTP, HTTP_USERAGENT, "useragent",
                    "User Agent", userAgents[0], userAgents);

    public final static IntegerVal PRF_CONNECTION_TIMEOUT =
            new IntegerVal(HTTP, "Connection Timeout", "Connection Timeout",
                    "Connection time out in milli-seconds", 30000);

    public final static IntegerVal PRF_READ_TIMEOUT =
            new IntegerVal(HTTP, "Read Timeout", "Read Timeout", "Read time out in milli-seconds",
                    30000);

    public final static BooleanVal PRF_USE_PROXY =
            new BooleanVal(PROXY, "http.useProxy", "Use Proxy", "should use Proxy?", Boolean.TRUE);

    public final static StringVal PRF_HTTPPROXYHOST =
            new StringVal(PROXY, "http.proxyHost", "HTTP Proxy Host", "HTTP Proxy Host",
                    "192.168.0.2");

    public final static StringVal PRF_HTTPPROXYPORT =
            new StringVal(PROXY, "http.proxyPort", "HTTP Proxy Port", "HTTP Proxy Port", "3128");

    public final static StringVal PRF_FTPPROXYHOST =
            new StringVal(PROXY, "ftp.proxyHost", "FTP Proxy Host", "FTP Proxy Host", "192.168.0.2");

    public final static StringVal PRF_FTPPROXYPORT =
            new StringVal(PROXY, "ftp.proxyPort", "FTP Proxy Port", "FTP Proxy Port", "3128");

    public final static StringSelectionVal PRF_PLAF =
            new StringSelectionVal(COMMON, PLAF, "Look and Feel", "Look and Feel", UIManager
                    .getSystemLookAndFeelClassName(), createPLAFs());

    public final static IntegerVal PRF_VIEW_THUMBNAIL_WIDTH =
            new IntegerVal(VIEWER, "Thumbnailwidth", "Thumbnail Width", "Thumbnail Width", 50);

    public final static IntegerVal PRF_VIEW_THUMBNAIL_HEIGHT =
            new IntegerVal(VIEWER, "Thumbnailheight", "Thumbnail Heigth", "Thumbnail Height", 50);

    public final static StringSelectionVal PRF_VIEW_THUMBNAIL_SCALE_QUALITY =
            new StringSelectionVal(VIEWER, "scalequality", "Scale Quality", "Scale Quality",
                    "Fast", new String[] {"Fast", "Bilinear"});

    public final static BooleanVal PRF_VIEW_THUMBNAIL_PRESERVE_RATIO =
            new BooleanVal(VIEWER, "preserveRatio", "Preserve Ratio when scaling",
                    "should preserve Ratio when scaling?", Boolean.TRUE);

    public final static BooleanVal PRF_VIEW_FLAT_MODE =
            new BooleanVal(VIEWER, "flatMode", "FlatMode", "Flatmode", Boolean.TRUE);

    public final static DirectoryVal PRF_VIEW_THUMBCACHEDIR =
            new DirectoryVal(VIEWER, "thumbcachedir", "Thumbnail Cache Dir",
                    "Location where the cache for thumbnails is", "./.mongocache");

    public final static DirectoryVal PRF_VIEW_ROOTDIR =
            new DirectoryVal(VIEWER, "viewrootdir", "Directory Root in Viewer",
                    "DirectoryRoot in Viewer", null);

    public final static BooleanVal PRF_SPIDERDEEPHTMLCONTENTCHECK =
        new BooleanVal(SPIDER, "Should deeply check for html-content-files",
            "Should deeply check for html-content-files",
            "Should deeply check for html-content-files instead of checking by file-extension", Boolean.FALSE);
    
    public final static IntegerVal CMDSERVER_LISTENINGPORT =
        new IntegerVal(COMMANDSERVER, "ListeningPort", "Listening Port", "Port to listen", 3141);    
    

    public final static IntegerVal MUTATION_MAXMUTATIONCOUNT =
        new IntegerVal(MUTATION, "Mutation.maxmutationcount", "Max count for mutation", "max count for mutation", 10000);     
    

    private AppPrefs() {
        // private constructor
    }


    public final static PrefsRoot getPrefStructure() {
        return ROOT;
    }

    public static void exportPreferences(File outputFile) throws FileNotFoundException,
            IOException, BackingStoreException
    {
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        userPrefs.exportSubtree(outputStream);
        outputStream.close();
    }

    public static void importPreferences(File inputFile) throws FileNotFoundException, IOException,
            InvalidPreferencesFormatException
    {
        FileInputStream inputStream = new FileInputStream(inputFile);
        Preferences.importPreferences(inputStream);
        inputStream.close();
    }

    private static String[] createPLAFs() {
        UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
        String[] plafs = new String[laf.length];
        for (int i = 0; i < laf.length; i++) {
            plafs[i] = laf[i].getClassName();
        }
        return plafs;
    }

}
