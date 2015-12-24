/*
 * delete all wm log files.
 */
import org.myjaphoo.gui.util.FileChoosing;


//Pattern for wminfo files
def p = ~/.*\.log.*/
def dirFile = new File(".");

println "scanning ${dirFile} for log files..."
dirFile.eachFileMatch(p) {
    f ->
    print "deleting ${f}..."
    if (f.delete()) {
        println "deleted!"
    }
}
