/*
 * delete empty directories (to cleanup generated dirs or dirs after duplicates cleanup)
 * Opens jfilechooser asks for a directory.
 * Then deletes recursively all empty directories inside that directory.
 */
import org.myjaphoo.gui.util.FileChoosing;


void scan(File dirFile) {
    if (dirFile != null) {
        dirFile.eachDir() {
            f ->
            scan(f);
            def children = f.listFiles();
            if (f.listFiles() == null || f.listFiles().length == 0) {
                print "deleting ${f}..."
                if (f.delete()) {
                    println "deleted!"
                }
            }
        }

    }
}

def dirFile = FileChoosing.chooseDir(controller, "Select dir to cleanup empty directories");
if (dirFile != null) {
    println "scanning ${dirFile} for empty dirs..."
    scan(dirFile);
}