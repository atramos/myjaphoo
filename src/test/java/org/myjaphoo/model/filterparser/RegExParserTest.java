/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.values.Value;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mla
 */
public class RegExParserTest extends AbstractParserTest {

    public void testRegEx() throws ParserException {
        tstTrue("path regex '.*mpg'");
    }

    public void testRegExStatement() throws ParserException {
        tstTrue("?.*mpg ");
    }

    public void testRegExStatement2() throws ParserException {
        tstTrue("tag?.*mytok ");
    }

    public void testRegEx2() throws ParserException {
        tstTrue("path regex 'a{3}.b{3}.c{3}.*'");
    }

    public void testRegEx2Statement() throws ParserException {
        tstTrue("?a{3}.b{3}.c{3}.* ");
    }

    public void testRegEx3() throws ParserException {
        tstTrue("path regex 'a{3}/b{3}/c{3}.*'");
    }

    public void testRegEx3Statement() throws ParserException {
        tstTrue("?a{3}/b{3}/c{3}.* ");
    }

    public void testRegEx4() throws ParserException {
        tstTrue("'ktr.abc.def' regex 'ktr\\.[a-zA-Z]{3}\\..*'");
    }

    public void testRegEx4Literal() throws ParserException {
        tstTrue("'ktr.abc.def' regex ~ktr\\.[a-zA-Z]{3}\\..* ");
    }

    public void testRegExSym() throws ParserException {
        tstTrue("path ~ '.*mpg'");
    }

    public void testRegEx2Sym() throws ParserException {
        tstTrue("path ~ 'a{3}.b{3}.c{3}.*'");
    }

    public void testRegEx3Sym() throws ParserException {
        tstTrue("path ~ 'a{3}/b{3}/c{3}.*'");
    }

    public void testRegEx4Sym() throws ParserException {
        tstTrue("'ktr.abc.def' ~ 'ktr\\.[a-zA-Z]{3}\\..*'");
    }

    public void testRegEx5() throws ParserException {
        tstTrue("regexextract('ktr.abc.def', 'ktr\\.[a-zA-Z]{3}\\..*') = 'ktr.abc.def'");
    }

    public void testRegEx6() throws ParserException {
        tstTrue("regexextract('ktr.abc.def', 'ktr\\.[a-zA-Z]{3}\\.') = 'ktr.abc.'");
    }
    public void testRegEx7() throws ParserException {
        tstTrue("regexextract('01234ktr.abc.def', 'ktr\\.[a-zA-Z]{3}\\.') = 'ktr.abc.'");
    }

    public void testRegEx8() throws ParserException, IOException {
        Value value = evalConstToValue("regexextract('bla/201203_02/bla/blab/qwerqwer', '\\d\\d\\d\\d\\d\\d')");
        assertEquals("201203", value.asString());
    }

    public void testit() {
        Pattern pattern = Pattern.compile("\\d\\d\\d\\d\\d\\d");
        String searchstring = "bla/201203_02/bla/blab/qwerqwer";
        Matcher matcher = pattern.matcher(searchstring);
        if (matcher.find()) {
            String result = searchstring.substring(matcher.start(), matcher.end());
            System.out.println(result);
        }
    }

    public void testReplaceall() throws ParserException {

        String result = "01234ktr.abc.def".replaceAll("\\w*ktr(\\.[a-zA-Z]{3}\\.).*", "ktr$1");
        assertEquals("ktr.abc.", result);

        tstTrue("replaceall('01234ktr.abc.def', '\\w*ktr(\\.[a-zA-Z]{3}\\.).*', 'ktr$1') like 'ktr.abc.'");
    }
}
