import static groovy.io.FileType.FILES

/**
 * CompileWikiSnippets 
 * @author mla
 * @version $Id$
 *
 */
class CompileWikiSnippets {

    def compile() {
        def Map<String, List> snippets = scanSnippets();
        def Map<String, String> snipps = snippets.collectEntries { filename, snippetList -> ["$filename": snippetList*.render().join("\n\n")]}

        // save the snippets:
        snipps.each { filename, txt ->
            def f = new File("src/main/dokuwiki/data/pages/manual/${filename}.txt")
            f.withWriter { w ->
                w.write(txt)
            }
        }
    }

    def scanSnippets() {
        def sourceFiles = []
        new File('src/main/groovy').eachFileRecurse(FILES) {
            if (it.name.endsWith('.groovy') || it.name.endsWith(".java")) {
                sourceFiles.add(it);
            }
        }
        new File('src/main/java').eachFileRecurse(FILES) {
            if (it.name.endsWith('.groovy') || it.name.endsWith(".java")) {
                sourceFiles.add(it);
            }
        }
        def Map<String, List> snippets = [:]
        sourceFiles.each { File f ->
            addSnippets(snippets, f.text);
        }
        return snippets;
    }


    def addSnippets(Map<String, List> snippets, String scriptText) {

        def allMatches = extractAllMultiLineComments(scriptText);
        allMatches.each {
            String snippetComment ->
                def wikiFileSpec = snippetComment.find(/@wiki:(\w*)/);
                if (wikiFileSpec != null) {
                    def boolean javadocComment = snippetComment.startsWith("/**");
                    def index = snippetComment.indexOf(wikiFileSpec);
                    def fileName = wikiFileSpec.replace("@wiki:", "");
                    def wikiText = snippetComment.substring(index + wikiFileSpec.length());
                    wikiText = extractTextSnippet(wikiText, javadocComment)
                    WikiSnippet snippet = new WikiSnippet(fileName: fileName, fullComment: snippetComment, wikiText: wikiText , javadocComment: javadocComment)

                    def wikiFileEntry = snippets.get(fileName)
                    if (wikiFileEntry == null) {
                        wikiFileEntry = []
                        snippets.put(fileName, wikiFileEntry)
                    }
                    wikiFileEntry.add(snippet)
                }
        }
    }

    def extractAllMultiLineComments(String scriptText) {
        def allComments = scriptText.findAll(/(?s)\/\*{1,2}\s*.*?\*\//);
        return allComments;
    }

    def extractTextSnippet(String rawSnippet, boolean javadocComment) {

        rawSnippet = rawSnippet.replace("/**", "").replace("/*", "").replace("*/", "")
        def result = new StringBuilder();
        rawSnippet.eachLine {
            line ->
                line = line.trim();
                if (javadocComment) {
                    if (line.startsWith("*")) {
                        line = line.substring(1);
                    }
                }
                result.append(line)
                result.append("\n")
        }
        return result.toString()
    }



    public static void main(String[] args) {
        new CompileWikiSnippets().compile();
    }
}
