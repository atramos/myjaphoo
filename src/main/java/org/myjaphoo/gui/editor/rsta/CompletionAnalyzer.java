package org.myjaphoo.gui.editor.rsta;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.idents.Qualifier;
import org.myjaphoo.model.filterparser.syntaxtree.AbstractParsedPiece;
import org.myjaphoo.model.filterparser.syntaxtree.SpecialCharSymbol;

import java.io.IOException;

/**
 * Holds scanned results of filter or grouping text, and has helper methods to check, on which context position
 * the curser is for a completion.
 */
public class CompletionAnalyzer {

    private AbstractParsedPiece beforeLast = null;
    private AbstractParsedPiece last = null;

    /**
     * analyzes the text. the text must contain the full text ending at the cursor position.
     * and reduzed by the last word where the cursor hangs.
     * This way we can analyze all previous syntax tokens of the text and try to guess
     * completions.
     * @param text
     */
    public CompletionAnalyzer(String text) throws IOException {
        CommonTokenStream tokStream = FilterParser.createTokenStream(text);
        tokStream.fill();
        // get last and before last: take into account that the "last" token is eof, which is not of interest for us.
        if (tokStream.size() >= 2) {
            last = toPiece(tokStream.get(tokStream.size() -2));
        }
        if (tokStream.size() >= 3) {
            beforeLast = toPiece(tokStream.get(tokStream.size() - 3));
        }
    }

    private AbstractParsedPiece toPiece(Token token) {
        String txt = token.getText();
        Qualifier ident = Qualifier.mapIdent(txt);
        if (ident != null) {
            return ident;
        }

        Function function = Function.mapIdent(txt);
        if (function != null) {
            return function;
        }

        // symbol aus mehreren chars?
        SpecialCharSymbol symbol = SpecialCharSymbol.mapIdent(txt);
        if (symbol != null) {
            return symbol;
        }
        return null;
    }

    public boolean match(AbstractParsedPiece p1, AbstractParsedPiece p2) {
        // analyze the last two pieces:
        return p1 == beforeLast && p2 == last;
    }

    public boolean match(AbstractParsedPiece p1, Class clazz) {
        // analyze the last two pieces:
        return p1 == beforeLast && last != null && clazz.isInstance(last);
    }

    public boolean match(Class clazz1, Class clazz2) {
        // analyze the last two pieces:
        return beforeLast != null && clazz1.isInstance(beforeLast) && last != null && clazz2.isInstance(last);
    }

    public boolean match(AbstractParsedPiece p1) {
        // analyze the last piece:
        return p1 == last;
    }

    public AbstractParsedPiece getBeforeLast() {
        return beforeLast;
    }

    public AbstractParsedPiece getLast() {
        return last;
    }
}
