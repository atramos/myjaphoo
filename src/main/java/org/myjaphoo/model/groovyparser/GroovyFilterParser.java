package org.myjaphoo.model.groovyparser;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * GroovyFilterParser
 *
 * @author mla
 * @version $Id$
 */
public class GroovyFilterParser {

    public static class GroovyScriptWrapper {
        private Script script;

        public GroovyScriptWrapper(Script script) {
            this.script = script;
        }

        public void setRowProperty(JoinedDataRow row) {
            script.setProperty("row", row);
        }

        public Object runScript() {
            try {
                return script.run();
            } catch (Exception e) {
                throw new ParserException(null, e.getLocalizedMessage(), 0, 0);
            }
        }

        public Script getScript() {
            return script;
        }

        public Closure[] getGrouper() {
            try {
                Closure[] gr = (Closure[]) script.getProperty("grouper");
                return gr;
            } catch (Exception e) {
                throw new ParserException(null, e.getLocalizedMessage(), 0, 0);
            }
        }
    }

    public GroovyScriptWrapper createScript(String filterExpr) {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setScriptBaseClass(GroovyFilterBaseClass.class.getName());
        GroovyShell shell = new GroovyShell(config);
        try {
            return new GroovyScriptWrapper(shell.parse(filterExpr));
        } catch (CompilationFailedException cfe) {
            throw new ParserException(filterExpr, cfe.getLocalizedMessage(), 0, 0);
        }
    }

}
