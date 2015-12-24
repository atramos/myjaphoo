/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.imp;

/**
 * @author mla
 */
public enum ImportTypes {

    Movies() {
        public ImportDelegator createImportDelegator() {
            return new MovieDelegator();
        }
    },
    Pictures() {
        public ImportDelegator createImportDelegator() {
            return new PicDelegator();
        }
    },
    Text() {
        public ImportDelegator createImportDelegator() {
            return new TextDelegator();
        }
    },

    AllMedia() {
        public ImportDelegator createImportDelegator() {
            return new AllMediaDelegator();
        }
    },

    AllFiles() {
        public ImportDelegator createImportDelegator() {
            return new AllFilesDelegator();
        }
    },

    SpecificFiles() {
        public ImportDelegator createImportDelegator() {
            return new SpecificFilesDelegator();
        }
    };

    public abstract ImportDelegator createImportDelegator();
}
