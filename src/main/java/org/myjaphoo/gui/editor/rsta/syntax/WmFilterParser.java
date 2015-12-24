/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.editor.rsta.syntax;

import org.apache.commons.lang.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.myjaphoo.model.filterparser.Parser;
import org.myjaphoo.model.filterparser.ParserException;

import javax.swing.text.BadLocationException;

/**
 *
 * @author mla
 */
public class WmFilterParser extends AbstractParser {

    private DefaultParseResult result;
    private ParserCreator parserCreator;

    public WmFilterParser(ParserCreator parserCreator) {
        result = new DefaultParseResult(this);
        this.parserCreator = parserCreator;
    }

    @Override
    public ParseResult parse(RSyntaxDocument doc, String style) {
        result = new DefaultParseResult(this);
        result.clearNotices();

        int lineCount = doc.getDefaultRootElement().getElementCount();
        result.setParsedLines(0, lineCount - 1);



        String txtToParse;
        try {
            txtToParse = doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {
            result.setError(ex);
            return result;
        }
        if (!StringUtils.isEmpty(txtToParse)) {
            try {
                parserCreator.createParser().checkCorrectness(txtToParse);
            } catch (ParserException pe) {
                DefaultParserNotice pn = new DefaultParserNotice(
                        this, pe.getBlankErrMsg(), 1, pe.getStartpos(), pe.getEndpos());
                pn.setToolTipText(pe.getMessage());
                result.addNotice(pn);
            }
        }
        return result;
    }
}
