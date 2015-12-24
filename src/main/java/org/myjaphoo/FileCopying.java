/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.imp.WmInfoImExport;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author mla
 */
public class FileCopying {


    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();

    public void copyMovie(FileCopyInstruction fci) throws IOException, JAXBException {

        WmInfoImExport exp = new WmInfoImExport();
        String sourcepath = fileSubstitution.locateFileOnDrive(fci.getMovieEntry().getCanonicalPath());
        if (sourcepath == null) {
            throw new RuntimeException("can not localize file " + fci.getMovieEntry().getCanonicalPath());
        }

        String destpath = createOutputPathForCopying(fci);

        if (MyjaphooAppPrefs.PRF_USE_COMMONS_IO_FOR_FILECOPYING.getVal()) {
            org.apache.commons.io.FileUtils.copyFile(new File(sourcepath), new File(destpath));
        } else {
            Path sourcePath = new File(sourcepath).toPath();
            Path destPath = new File(destpath).toPath();
            new File(destpath).getParentFile().mkdirs();
            Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        }

        if (fci.isCreateWmInfoFile()) {
            MovieEntry movieEntry = MyjaphooDB.singleInstance().ensureObjIsAttached(fci.getMovieEntry());

            String infoFile = destpath + ".wminfo";
            exp.export(new File(infoFile), movieEntry);
        }
    }

    public static enum PathOptionForCopying {

        NOADDITIONAL_PATH("flat"),
        PRESERVE_SOURCE_PATH("preserve source path"),
        USE_STRUCTURE_PATH("use structure as path");

        private String descr;

        private PathOptionForCopying(String descr) {
            this.descr = descr;
        }

        public String getDescription() {
            return descr;
        }
    }

    private String createOutputPathForCopying(FileCopyInstruction fci) {

        String toDir = fci.getToDir();
        switch (fci.getPathOption()) {
            case NOADDITIONAL_PATH:
                toDir = toDir + "/" + fci.getMovieEntry().getName();
                break;
            case PRESERVE_SOURCE_PATH:
                String[] pathelems = org.apache.commons.lang.StringUtils.split(fci.getMovieEntry().getCanonicalDir(), "//:\\");
                toDir = toDir + "/" + org.apache.commons.lang.StringUtils.join(pathelems, "/");
                toDir = toDir + "/" + fci.getMovieEntry().getName();
                break;
            case USE_STRUCTURE_PATH:
                toDir = toDir + "/" + fci.getPathStructureName();
        }


        return toDir;
    }
}
