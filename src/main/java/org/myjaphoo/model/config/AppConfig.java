/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.config;

import org.apache.commons.lang.StringUtils;
import org.mlsoft.common.prefs.model.PrefsRootCommonsConfiguration;
import org.mlsoft.common.prefs.model.editors.BooleanVal;
import org.mlsoft.common.prefs.model.editors.EditorGroup;
import org.mlsoft.common.prefs.model.editors.StringSelectionVal;
import org.mlsoft.common.prefs.model.editors.StringVal;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseConfigurations;
import org.myjaphoo.model.util.UserDirectory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * preferences specific to the common part of the app (both for web and
 * for the web client version).
 * 
 * @author mla
 */
public class AppConfig {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/config/resources/AppConfig");

    // separate property file for version-info. needed for update mechanism and replaced by update:




    private final static PrefsRootCommonsConfiguration VERSIONINFO = new PrefsRootCommonsConfiguration("version.properties"); //NOI18N
    public final static StringVal PRF_CURRENT_VERSION =
            new StringVal(VERSIONINFO, "app.version", "app.version", "app.version", //NOI18N
            "0000"); //NOI18N
    public final static PrefsRootCommonsConfiguration ROOT = new PrefsRootCommonsConfiguration(
            UserDirectory.getDirectory() + "preferences.properties", "preferences.properties"); //NOI18N
    public final static EditorGroup COMMON = new EditorGroup(ROOT, 
            localeBundle.getString("COMMON GUI"), 
            localeBundle.getString("COMMON GUI"), 
            localeBundle.getString("COMMON DESC"));
    public final static EditorGroup IMPORT = new EditorGroup(ROOT, 
            localeBundle.getString("IMPORT GUI"), 
            localeBundle.getString("IMPORT GUI"), 
            localeBundle.getString("IMPORT DESC"));
    private final static EditorGroup PICIMPORT = new EditorGroup(IMPORT, 
            localeBundle.getString("PICTURE IMPORT GUI"), 
            localeBundle.getString("PICTURE IMPORT GUI"), 
            localeBundle.getString("PICTURE IMPORT DESC"));
    public final static EditorGroup MOVIMPORT = new EditorGroup(IMPORT, 
            localeBundle.getString("MOVIE IMPORT GUI"), 
            localeBundle.getString("MOVIE IMPORT GUI"), 
            localeBundle.getString("MOVIE IMPORT DESC"));
    private final static EditorGroup DB = new EditorGroup(ROOT, 
            localeBundle.getString("DB GUI"), 
            localeBundle.getString("DB GUI"), 
            localeBundle.getString("DB DESC"));
    public final static EditorGroup INTERNAL = new EditorGroup(ROOT, 
            localeBundle.getString("INTERNAL GUI"), 
            localeBundle.getString("INTERNAL GUI"), 
            localeBundle.getString("INTERNAL DESC"));
    public final static StringVal PRF_PICFILTER =
            new StringVal(PICIMPORT, "pictureimportfilter",  //NOI18N
                localeBundle.getString("PICTURE IMPORT FILTER GUI"), 
                localeBundle.getString("PICTURE IMPORT FILTER DESC"),
                ".jpg;.jpeg;.png"); //NOI18N
    public final static StringVal PRF_SPECIFICFILTER =
            new StringVal(IMPORT, "specificimportfilter",  //NOI18N
                    localeBundle.getString("SPECIFIC IMPORT FILTER GUI"),
                    localeBundle.getString("SPECIFIC IMPORT FILTER DESC"),
                    ""); //NOI18N
    public final static StringVal PRF_MOVIEFILTER =
            new StringVal(MOVIMPORT, "movieimportfilter",  //NOI18N
                localeBundle.getString("MOVIE IMPORT FILTER GUI"), 
                localeBundle.getString("MOVIE IMPORT FILTER DESC"),
                ".ts;.mpg;.mpeg;.MPG;.MPEG;.wmv;.avi;.AVI;.mp4;.WMV;.MP4;.iso;.divx;.DIVX;.mkv;.m4v;.f4v;.asf;.mov;.flv;.vob"); //NOI18N
    public final static StringVal PRF_COMPRESSEDFILESFILTER =
            new StringVal(IMPORT, "compressedfilesfilter",  //NOI18N
                localeBundle.getString("FILE FILTER FOR COMPRESSED FILES GUI"), 
                localeBundle.getString("FILE FILTER FOR COMPRESSED FILES DESC"),
                ".zip;.rar;.tar"); //NOI18N
    public final static StringVal PRF_TEXTFILTER =
            new StringVal(IMPORT, "textfilesfilter",  //NOI18N
                localeBundle.getString("FILE FILTER FOR TEXT FILES GUI"), 
                localeBundle.getString("FILE FILTER FOR TEXT FILES DESC"),
                ".txt;.log;.text"); //NOI18N
    public final static BooleanVal PRF_MOVIE_SCANFORCOVERPICS =
            new BooleanVal(IMPORT, "moviescanforcoverpics",  //NOI18N
                localeBundle.getString("SCANFORCOVERPICS GUI"), 
                localeBundle.getString("SCANFORCOVERPICS DESC"),
                Boolean.FALSE); //NOI18N
    public final static StringVal PRF_MOVIE_SCANPATTERN =
            new StringVal(IMPORT, "moviescanpattern",  //NOI18N
                localeBundle.getString("SCANPATTERN GUI"), 
                localeBundle.getString("SCANPATTERN DESC"),
                ".*$namewithoutsuffix.*"); //NOI18N
    
    
    public final static BooleanVal PRF_DB_USE_DERBY_EMBEDDEDMODE =
            new BooleanVal(DB, "db.useDerbyEmbeddedMode",  //NOI18N
                    localeBundle.getString("PRF_DB_USE_DERBY_EMBEDDEDMODE GUI"),
                    localeBundle.getString("PRF_DB_USE_DERBY_EMBEDDEDMODE DESC"), 
                    Boolean.TRUE);
    public final static StringVal PRF_DB_DERBY_HOSTNAME =
            new StringVal(DB, "db.derbyHostName",  //NOI18N
                    localeBundle.getString("PRF_DB_DERBY_HOSTNAME GUI"), 
                    localeBundle.getString("PRF_DB_DERBY_HOSTNAME DESC"),
                    "localhost"); //NOI18N
    public final static StringVal PRF_DB_DERBY_PORT =
            new StringVal(DB, "db.derbyPort",  //NOI18N
                    localeBundle.getString("PRF_DB_DERBY_PORT GUI"), 
                    localeBundle.getString("PRF_DB_DERBY_PORT DESC"),
                    "1527"); //NOI18N
    public final static StringVal PRF_DB_FILE =
            new StringVal(DB, "db.name",  //NOI18N
                    localeBundle.getString("PRF_DB_FILE GUI"), 
                    localeBundle.getString("PRF_DB_FILE DESC"),
                    UserDirectory.getDirectory() + "myjaphoodb"); //NOI18N
    
    public final static BooleanVal PRF_DB_USE_OTHERCONFIGURATION =
            new BooleanVal(DB, "db.useOtherConfiguration",  //NOI18N
                    localeBundle.getString("PRF_DB_USE_OTHER_CONFIGURATION GUI"),
                    localeBundle.getString("PRF_DB_USE_OTHER_CONFIGURATION DESC"), 
                    Boolean.FALSE);    
    
    public final static StringSelectionVal PRF_DB_CONFIGURATIONNAME =
            new StringSelectionVal(DB, "db.otherConfigname",  //NOI18N
                    localeBundle.getString("PRF_DB_OTHER_CONFIGNAME GUI"), 
                    localeBundle.getString("PRF_DB_OTHER_CONFIGNAME DESC"),
                    "", getAllAvailableConfigurations()); //NOI18N    
    
    public final static BooleanVal PRF_HIBERNATE_SECOND_LEVEL_CACHE =
            new BooleanVal(DB, "db.hibernate.activateSecondLevelCache",  //NOI18N
                    localeBundle.getString("PRF_HIBERNATE_SECOND_LEVEL_CACHE GUI"), 
                    localeBundle.getString("PRF_HIBERNATE_SECOND_LEVEL_CACHE DESC"),
                    false);
    
    /***
     * prevents building checksum of media files. in some rare cases this might
     * be useful.
     */
    public final static BooleanVal PRF_INTERNAL_DONOTBUILDCHECKSUM =
            new BooleanVal(INTERNAL, "internal.doNotBuildChecksum",  //NOI18N
                    "do not calculate checksum of files, use filesize instead",
                    "do not calculate checksum of files, use filesize instead", 
                    Boolean.FALSE);       
    
    private static String[] getAllAvailableConfigurations() {
        DatabaseConfigurations allConfigs = DatabaseConfigLoadSave.load();
        ArrayList<String> names = new ArrayList<String>();
        for (DatabaseConfiguration config : allConfigs.getDatabaseConfigurations()) {
            if (!StringUtils.isEmpty(config.getName())) {
                names.add(config.getName());
            }
        }
        Collections.sort(names);
        return names.toArray(new String[names.size()]);
    }    
}
