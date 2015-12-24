import groovy.text.GStringTemplateEngine
import groovy.transform.Canonical

/**
 * WikiSnippet 
 * @author mla
 * @version $Id$
 *
 */
@Canonical
class WikiSnippet {

    def String fileName;
    def String wikiText;
    def boolean javadocComment

    def String fullComment;

    /**
     * render templates into text for the snippet
     * @param str
     * @return
     */
    def String render() {
        def GStringTemplateEngine engine = new GStringTemplateEngine();
        def binding = [f: new SnippetsFormatter()]
        def template = engine.createTemplate(wikiText).make(binding)
        return template;
    }
}
