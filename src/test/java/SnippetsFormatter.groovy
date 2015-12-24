/**
 * Formatter 
 * @author mla
 * @version $Id$
 *
 */
class SnippetsFormatter {
    def h1(txt) {
        return "====== $txt ======\n"
    }

    def h2(String txt) {
        return "===== $txt =====\n"
    }

    def h3(String txt) {
        return "==== $txt ====\n"
    }

    def h4(String txt) {
        return "=== $txt ===\n"
    }

    def h5(String txt) {
        return "== $txt ==\n"
    }

    def listing(Class clazz) {
        def path = clazz.name.replace('.', '/');
        def f = new File("src/main/groovy/${path}.groovy")
        if (f.exists()) {
            return codeJava(f.text);
        }
        f = new File("src/main/java/${path}.java")
        if (f.exists()) {
            return codeJava(f.text);
        }
        f = new File("src/main/java/${path}.groovy")
        if (f.exists()) {
            return codeJava(f.text);
        }
        throw new IllegalArgumentException("unable to find class " + clazz.name);
    }

    def code(String txt) {
        return "\n\n<code>\n $txt </code>\n\n";
    }

    def codeJava(String txt) {
        return "\n\n<code java>\n $txt </code>\n\n";
    }
}
