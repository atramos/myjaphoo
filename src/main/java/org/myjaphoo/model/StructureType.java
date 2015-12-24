/*
 * Strukturierungstypen, nach denen Movies gruppiert werden können.
 * Bei manchen gruppierungen können die Movies auch mehrfach erscheinen (z.b. nach Tokens).
 */
package org.myjaphoo.model;

import java.util.ResourceBundle;
import org.myjaphoo.model.grouping.GroupingDim;
import static org.myjaphoo.model.grouping.GroupingDim.*;
import org.myjaphoo.model.grouping.PartialPathBuilder;

/**
 * Vordefinierte Gruppierungen. 
 * @author mla
 */
public enum StructureType {

    DIRECTORY("Directory Structure", Directory),
    DUPLETTES("Duplicate Structure", Duplicates),
    DUPLETTES_IN_DIRS("Duplicates with directory structure", DuplicatesWithDirs),
    FLAT_TOKEN("Flat Tag Structure", Token),
    HIERARCHICAL_TOKEN("Hierarchical Tag Structure", TokenHierarchy),
    TOKEN_DIR_GROUPER("Hierarchical Tag/Dir Structure", Token, Directory),
    AUTO_KEYWORD_GROUPER("By Keywords build from Pathname", AutoKeyWord),
    AUTO_KEYWORD_STRONG_GROUPER("By Keywords build from Pathname II", AutoKeyWordStrong),
    AUTO_KEYWORD_VERY_STRONG_GROUPER("By Keywords build from Pathname III", AutoKeyWordVeryStrong),
    SIZE_DIR_GROUPER("File size and directory structure", Size, Directory),
    RATING_DIR_GROUPER("Rating and directory structure", Rating, Directory),
    RATING__TOKEN_DIR_GROUPER("Rating/Tag/Dir Structure", Rating, Token, Directory),
    DBCOMP_DIR_GROUPER("DB Comparison/Dir Structure", DB_Comparison, Directory),
    LOCALIZED_PATH_GROUPER("Localisation on file system", LocatedDir),
    EXIF_CREATE_DATE("Exif create date", ExifCreateDate);
    
    private String guiName;
    private GroupingDim[] dims;

    private StructureType(String guiName, GroupingDim... dims) {
        this.guiName = guiName;
        this.dims = dims;
    }

    @Override
    public String toString() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/resources/StructureType");
        return localeBundle.getString(guiName);
    }

    public final PartialPathBuilder[] createPartialPathBuilder() {
        PartialPathBuilder[] builder = new PartialPathBuilder[getDims().length];
        for (int i = 0; i < getDims().length; i++) {
            builder[i] = getDims()[i].createPartialPathBuilder();
        }
        return builder;
    }

    public String buildUserDefinedEquivalentExpr() {
        StringBuilder b = new StringBuilder();
        for (GroupingDim dim : getDims()) {
            if (b.length() > 0) {
                b.append(", ");
            }
            b.append(dim.name());
        }
        return b.toString();
    }

    /**
     * @return the dims
     */
    public GroupingDim[] getDims() {
        return dims;
    }
}
