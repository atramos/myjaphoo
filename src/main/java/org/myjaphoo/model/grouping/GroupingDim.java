/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import java.util.ResourceBundle;

/**
 *

 */
public enum GroupingDim {

    Directory("Directory") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new DirectoryPartialGrouper();
        }
    },
    LocatedDir("located Dir") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new LocatedFilePartialGrouper();
        }
    },
    Token("Tag") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new TokenPartialGrouper();
        }
    },
    TokenHierarchy("Tag hierarchical") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new TokenHierarchyPartialGrouper();
        }
    },
    TokenType("Tag Type") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new TokenTypePartialGrouper();
        }
    },
    Rating("Rating") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new RatingPathBuilder();
        }
    },
    Size("File Size") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new SizePartialGrouper();
        }
    },
    AutoKeyWord("Keywords") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new AutoKeyWordPartialGrouper();
        }
    },
    AutoKeyWordStrong("Keywords II") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new AutoKeyWordStrongPartialGrouper();
        }
    },
    AutoKeyWordVeryStrong("Keywords III") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new AutoKeyWordVeryStrongPartialGrouper();
        }
    },
    Duplicates("Duplicates") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new DuplicatePartialGrouper();
        }
    },
    DuplicatesWithDirs("Duplicates with Dir") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new DupGrouperWithDir();
        }
    },
    DB_Comparison("DB comparison") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new DBComparePartialGrouper();
        }
    },
    Dup_Links_ByLocating("dupl. links located") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new DupLocatedFilePartialGrouper();
        }
    },
    File_Or_Entry_Divider("File or Entry Divider") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new OwnsDBPartialGrouper();
        }
    },
    Bookmark("Bookmark") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new BookmarkPartialGrouper();
        }
    },
    Metatoken("Metatag") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new MetaTokenPartialGrouper();
        }
    },
    MetatokenHierarchy("Metatag hierarchical") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new MetaTokenHierarchyPartialGrouper();
        }
    },
    TokenProposal("Tag proposal") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new TokenAssignemntProposolerPartialGrouper();
        }
    },
    VGroupYielding("Virtual group") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new DirectoryPartialGrouper();
        }
    },
    Title("Title") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new TitlePathBuilder();
        }
    },
    Assocation("Association") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new AssociationGrouper();
        }
    },
    
    ExifCreateDate("Exif Create Date") {

        @Override
        public PartialPathBuilder createPartialPathBuilder() {
            return new ExifCreateDatePartialGrouper();
        }
    };

    abstract public PartialPathBuilder createPartialPathBuilder();
    private String guiName;
    
    public String getGuiName() {
        final ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/grouping/resources/GroupingDim");
        return localeBundle.getString(guiName);
    }
    
    private GroupingDim(String guiName) {
        this.guiName = guiName;
    }
}
