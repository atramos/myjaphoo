import groovy.io.GroovyPrintWriter

/**
 * WikiPage 
 * @author mla
 * @version $Id$
 *
 */
class WikiPage implements AutoCloseable {

    File file;

    def GroovyPrintWriter writer

    def SnippetsFormatter f = new SnippetsFormatter()

    public WikiPage(String filepath) {
        file = new File(filepath);
        writer = new GroovyPrintWriter(file);
    }

    def h1(String txt) {
        writer.println(f.h1(txt))
    }

    def h2(String txt) {
        writer.println(f.h2(txt))
    }

    def h3(String txt) {
        writer.println(f.h3(txt))
    }

    def text(String text) {
        writer.println(text);
        writer.println();
    }

    def header(String... cols) {
        for (String col : cols) {
            writer.print("^" + col);
        }
        writer.println("^");
    }

    def row(String... cols) {
        for (String col : cols) {
            writer.print(" | " + col);
        }
        writer.println(" |");
    }


    @Override
    void close() throws Exception {
        writer.close()
    }
}
