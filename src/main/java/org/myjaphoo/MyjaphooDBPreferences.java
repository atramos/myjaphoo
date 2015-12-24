package org.myjaphoo;

import org.mlsoft.common.prefs.model.editors.BooleanVal;
import org.mlsoft.common.prefs.model.editors.DirectoryVal;
import org.mlsoft.common.prefs.model.editors.EditorGroup;
import org.mlsoft.common.prefs.model.editors.EditorRoot;
import org.myjaphoo.model.prefs.PrefsRootDatabase;

import java.util.ResourceBundle;

/**
 * The Preferences that are saved in the database (and which are related to the database).
 *
 */
public class MyjaphooDBPreferences {
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MyjaphooAppPrefs");

    public final static PrefsRootDatabase DBROOT = new PrefsRootDatabase();

    public static EditorRoot getDBPrefStructure() {
        return DBROOT;
    }

    private final static EditorGroup FILEOPERATIONS = new EditorGroup(DBROOT,
            localeBundle.getString("FILEOPERATIONS GUI"),
            localeBundle.getString("FILEOPERATIONS GUI"),
            localeBundle.getString("FILEOPERATIONS DESC"));

    public final static DirectoryVal PRF_PRESELECTEDDIR_IN_FILEDIALOG =
            new DirectoryVal(FILEOPERATIONS, "preselectedDirInFileDialog",  //NOI18N
                    localeBundle.getString("PRF_PRESELECTEDDIR_IN_FILEDIALOG GUI"),
                    localeBundle.getString("PRF_PRESELECTEDDIR_IN_FILEDIALOG DESC"),
                    "."); //NOI18N
    public final static DirectoryVal PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG =
            new DirectoryVal(FILEOPERATIONS, "preselectedDirInImportFileDialog",  //NOI18N
                    localeBundle.getString("PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG GUI"),
                    localeBundle.getString("PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG DESC"),
                    "."); //NOI18N

    public final static BooleanVal PRF_FO_DELETION_ALLOWED =
            new BooleanVal(FILEOPERATIONS, "fileoperations.deletionAllowed",  //NOI18N
                    localeBundle.getString("PRF_FO_DELETION_ALLOWED GUI"),
                    localeBundle.getString("PRF_FO_DELETION_ALLOWED DESC"),
                    Boolean.FALSE);
    public final static BooleanVal PRF_FO_REMOVING_ALLOWED =
            new BooleanVal(FILEOPERATIONS, "fileoperations.removeEntriesFromDatabaseAllowed",  //NOI18N
                    localeBundle.getString("PRF_FO_REMOVING_ALLOWED GUI"),
                    localeBundle.getString("PRF_FO_REMOVING_ALLOWED DESC"),
                    Boolean.FALSE);
}
