/*
 * delete all wm info files. (to cleanup directories, when the data is already imported)
 * Opens jfilechooser asks for a directory.
 * Then deletes all wminfo files in that dir and all sub dirs.
 */
import org.myjaphoo.gui.util.FileChoosing;


//Pattern for wminfo files
def p = ~/.*\.wminfo/
def dirFile = FileChoosing.chooseDir(controller, "Select dir to delete wminfo files");
    if (dirFile != null) {
        println "scanning ${dirFile} for wminfo files..."
        dirFile.eachFileMatch(p) {
            f ->
            print "deleting ${f}..."
            if (f.delete()) {
                println "deleted!"
            }
        }

    }

