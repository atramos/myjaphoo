/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author mla
 */
public class ParserException extends RuntimeException {

    private int line;

    private int pos;
    private int startpos;
    private int endpos;
    private String blankErrMsg;
    private String parsedExpression;

    private String underLineErrorText;

    public ParserException(String parsedExpression, String errmsg, int startpos, int endpos) {
        super(formatErrorMsg(parsedExpression, errmsg, startpos, endpos));
        this.parsedExpression = parsedExpression;
        this.blankErrMsg = errmsg;
        this.startpos = startpos;
        this.endpos = endpos;
    }

    public ParserException(Token token, String msg) {
         this(token, 0, 0, msg);
    }

    public ParserException(Token token, int line, int charPositionInLine, String msg) {
        super(msg);
        this.blankErrMsg = msg;
        if (token != null) {
            this.startpos = token.getCharPositionInLine();
            this.endpos = token.getCharPositionInLine();
            this.line = token.getLine();
            this.pos = token.getCharPositionInLine();
            this.underLineErrorText = underlineError(token, this.line, pos, msg);
        } else {
            this.startpos = charPositionInLine;
            this.endpos = charPositionInLine;
            this.line = line;
            this.pos = charPositionInLine;
        }
    }

    private static String formatErrorMsg(String parsedExpression, String errmsg, int startpos, int endpos) {

        String msg = "\n" + parsedExpression + "\n";
        msg += StringUtils.rightPad("", startpos - 1, ' ') + "^  " + errmsg + "\n";
        return msg;
    }
//
//    @Override
//    public String getMessage() {
//        if (underLineErrorText != null) {
//            return underLineErrorText;
//        } else {
//            return super.getMessage();
//        }
//    }

    /**
     * @return the startpos
     */
    public int getStartpos() {
        return startpos;
    }

    /**
     * @return the endpos
     */
    public int getEndpos() {
        return endpos;
    }

    public String getBlankErrMsg() {
        return blankErrMsg;
    }


    protected String underlineError(Token offendingToken, int line,
                                    int charPositionInLine, String msg) {
        String sourceText = offendingToken.getInputStream().toString();
        return underlineError(sourceText, offendingToken, line, charPositionInLine, msg);
    }

    protected String underlineError(String sourceText,
                                    Token offendingToken, int line,
                                    int charPositionInLine, String msg) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        String[] lines = sourceText.split("\n");
        String errorLine = lines[line - 1];
        ps.println(errorLine);
        for (int i = 0; i < charPositionInLine; i++) ps.print(" ");
        int start = offendingToken.getStartIndex();
        int stop = offendingToken.getStopIndex();
        if (start >= 0 && stop >= 0) {
            for (int i = start; i <= stop; i++) ps.print("^");
        }
        ps.print(" ");
        ps.print(msg);
        ps.println();
        return bos.toString();
    }

    protected String underlineError(Recognizer recognizer,
                                    Token offendingToken, int line,
                                    int charPositionInLine, String msg) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        CommonTokenStream tokens =
                (CommonTokenStream) recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        return underlineError(input, offendingToken, line, charPositionInLine, msg);
    }
}
