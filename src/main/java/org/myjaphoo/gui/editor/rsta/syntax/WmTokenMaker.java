/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.editor.rsta.syntax;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.idents.Qualifier;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.groupbyparser.GroupingSymbols;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.swing.text.Segment;

/**
 * @author mla
 */
public class WmTokenMaker extends AbstractTokenMaker {

    protected final String operators = "@:*<>=?.";
    private int currentTokenStart;
    private int currentTokenType;

    /**
     * Constructor.
     */
    public WmTokenMaker() {
        super();    // Initializes tokensToHighlight.
    }

    /**
     * Checks the token to give it the exact ID it deserves before
     * being passed up to the super method.
     *
     * @param segment     <code>Segment</code> to get text from.
     * @param start       Start offset in <code>segment</code> of token.
     * @param end         End offset in <code>segment</code> of token.
     * @param tokenType   The token's type.
     * @param startOffset The offset in the document at which the token occurs.
     */
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {

        switch (tokenType) {
            // Since reserved words, functions, and data types are all passed
            // into here as "identifiers," we have to see what the token
            // really is...
            case Token.IDENTIFIER:
                int value = wordsToHighlight.get(segment, start, end);
                if (value != -1) {
                    tokenType = value;
                }
                break;
        }

        super.addToken(segment, start, end, tokenType, startOffset);

    }

    /**
     * Returns the text to place at the beginning and end of a
     * line to "comment" it in a this programming language.
     *
     * @return The start and end strings to add to a line to "comment"
     *         it out.
     */
    public String[] getLineCommentStartAndEnd() {
        return new String[]{"rem ", null};
    }

    /**
     * Returns whether tokens of the specified type should have "mark
     * occurrences" enabled for the current programming language.
     *
     * @param type The token type.
     * @return Whether tokens of this type should have "mark occurrences"
     *         enabled.
     */
    public boolean getMarkOccurrencesOfTokenType(int type) {
        return type == Token.IDENTIFIER || type == Token.VARIABLE;
    }

    /**
     * Returns the words to highlight for Windows batch files.
     *
     * @return A <code>TokenMap</code> containing the words to highlight for
     *         Windows batch files.
     * @see org.fife.ui.rsyntaxtextarea.AbstractTokenMaker#getWordsToHighlight
     */
    @Override
    public TokenMap getWordsToHighlight() {

        TokenMap tokenMap = new TokenMap(true); // Ignore case.

        for (final Qualifier ident : Qualifier.getList()) {
            tokenMap.put(ident.getName(), Token.RESERVED_WORD);
        }
        for (final AbstractOperator op : AbstractOperator.getList()) {
            tokenMap.put(op.getSymbol(), Token.OPERATOR);
        }
        for (final Function function : Function.getList()) {
            tokenMap.put(function.getName(), Token.FUNCTION);
        }

        tokenMap.put(GroupingSymbols.GROUPBY.getSymbol(), Token.RESERVED_WORD);
        tokenMap.put(GroupingSymbols.GROUP.getSymbol(), Token.RESERVED_WORD);
        tokenMap.put(GroupingSymbols.BY.getSymbol(), Token.RESERVED_WORD);
        tokenMap.put(GroupingSymbols.IF.getSymbol(), Token.RESERVED_WORD);
        tokenMap.put(GroupingSymbols.ELSE.getSymbol(), Token.RESERVED_WORD);
        tokenMap.put(GroupingSymbols.ELSEIF.getSymbol(), Token.RESERVED_WORD);

        for (GroupingDim dim : GroupingDim.values()) {
            // mit dem namen lowercase initialisieren:
            tokenMap.put(dim.name().toLowerCase(), Token.RESERVED_WORD);
            // und für kompatiblität mit bisherigen user definierten gruppierungen
            // auch noch mal mit dem eigentlichen namen (camelcase oder so ähnlich)
            tokenMap.put(dim.name(), Token.RESERVED_WORD);
        }


        return tokenMap;

    }

    /**
     * Returns a list of tokens representing the given text.
     *
     * @param text           The text to break into tokens.
     * @param startTokenType The token with which to start tokenizing.
     * @param startOffset    The offset at which the line of tokens begins.
     * @return A linked list of tokens representing <code>text</code>.
     */
    @Override
    public Token getTokenList(Segment text, int startTokenType, final int startOffset) {

        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        // See, when we find a token, its starting position is always of the form:
        // 'startOffset + (currentTokenStart-offset)'; but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart' for one less subtraction operation.
        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType = startTokenType;

//beginning:
        for (int i = offset; i < end; i++) {

            char c = array[i];

            switch (currentTokenType) {

                case Token.NULL:

                    currentTokenStart = i;    // Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            currentTokenType = Token.ERROR_STRING_DOUBLE;
                            break;

                        case '%':
                        case '$':
                            currentTokenType = Token.VARIABLE;
                            break;

                        // The "separators".
                        case '(':
                        case ')':


                            addToken(text, currentTokenStart, i, Token.SEPARATOR, newStartOffset + currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;

                        // The "separators2".
                        case '.':
                        case ',':
                        case ';':
                            addToken(text, currentTokenStart, i, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenType = Token.NULL;
                            break;

                        // Newer version of EOL comments, or a label
                        case ':':
                            // If this will be the first token added, it is
                            // a new-style comment or a label
                            if (firstToken == null) {
                                if (i < end - 1 && array[i + 1] == ':') { // new-style comment
                                    currentTokenType = Token.COMMENT_EOL;
                                } else { // Label
                                    currentTokenType = Token.PREPROCESSOR;
                                }
                            } else { // Just a colon
                                currentTokenType = Token.IDENTIFIER;
                            }
                            break;

                        default:

                            // Just to speed things up a tad, as this will usually be the case (if spaces above failed).
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }

                            int indexOf = operators.indexOf(c, 0);
                            if (indexOf > -1) {
                                addToken(text, currentTokenStart, i, Token.OPERATOR, newStartOffset + currentTokenStart);
                                currentTokenType = Token.NULL;
                                break;
                            } else {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }

                    } // End of switch (c).

                    break;

                case Token.WHITESPACE:

                    switch (c) {

                        case ' ':
                        case '\t':
                            break;    // Still whitespace.

                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.ERROR_STRING_DOUBLE;
                            break;

                        case '%':
                        case '$':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            break;

                        // The "separators".
                        case '(':
                        case ')':

                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                            currentTokenType = Token.NULL;
                            break;

                        // The "separators2".
                        case '.':
                        case ',':
                        case ';':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                            currentTokenType = Token.NULL;
                            break;

                        // Newer version of EOL comments, or a label
                        case ':':
                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            // If the previous (whitespace) token was the first token
                            // added, this is a new-style comment or a label
                            if (firstToken.getNextToken() == null) {
                                if (i < end - 1 && array[i + 1] == ':') { // new-style comment
                                    currentTokenType = Token.COMMENT_EOL;
                                } else { // Label
                                    currentTokenType = Token.PREPROCESSOR;
                                }
                            } else { // Just a colon
                                currentTokenType = Token.IDENTIFIER;
                            }
                            break;

                        default:    // Add the whitespace token and start anew.

                            addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
                            currentTokenStart = i;

                            // Just to speed things up a tad, as this will usually be the case (if spaces above failed).
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }

                            int indexOf = operators.indexOf(c, 0);
                            if (indexOf > -1) {
                                addToken(text, currentTokenStart, i, Token.OPERATOR, newStartOffset + currentTokenStart);
                                currentTokenType = Token.NULL;
                                break;
                            } else {
                                currentTokenType = Token.IDENTIFIER;
                            }

                    } // End of switch (c).

                    break;

                default: // Should never happen
                case Token.IDENTIFIER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            // Check for REM comments.
                            if (i - currentTokenStart == 3
                                    && (array[i - 3] == 'r' || array[i - 3] == 'R')
                                    && (array[i - 2] == 'e' || array[i - 2] == 'E')
                                    && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
                                currentTokenType = Token.COMMENT_EOL;
                                break;
                            }
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '"':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.ERROR_STRING_DOUBLE;
                            break;

                        case '%':
                        case '$':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            break;

                        // Should be part of identifiers, but not at end of "REM".
                        case '\\':
                            // Check for REM comments.
                            if (i - currentTokenStart == 3
                                    && (array[i - 3] == 'r' || array[i - 3] == 'R')
                                    && (array[i - 2] == 'e' || array[i - 2] == 'E')
                                    && (array[i - 1] == 'm' || array[i - 1] == 'M')) {
                                currentTokenType = Token.COMMENT_EOL;
                            }
                            break;

                        case '_':
                            break;    // Characters good for identifiers.

                        // The "separators".
                        case '(':
                        case ')':

                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            addToken(text, i, i, Token.SEPARATOR, newStartOffset + i);
                            currentTokenType = Token.NULL;
                            break;

                        // The "separators2".
                        case '.':
                        case ',':
                        case ';':
                            addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                            addToken(text, i, i, Token.IDENTIFIER, newStartOffset + i);
                            currentTokenType = Token.NULL;
                            break;

                        default:

                            // Just to speed things up a tad, as this will usually be the case.
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c == '\\') {
                                break;
                            }

                            int indexOf = operators.indexOf(c);
                            if (indexOf > -1) {
                                addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
                                addToken(text, i, i, Token.OPERATOR, newStartOffset + i);
                                currentTokenType = Token.NULL;
                                break;
                            }

                            // Otherwise, fall through and assume we're still okay as an IDENTIFIER...

                    } // End of switch (c).

                    break;

                case Token.COMMENT_EOL:
                    i = end - 1;
                    addToken(text, currentTokenStart, i, Token.COMMENT_EOL, newStartOffset + currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;
                    break;

                case Token.PREPROCESSOR: // Used for labels
                    i = end - 1;
                    addToken(text, currentTokenStart, i, Token.PREPROCESSOR, newStartOffset + currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;
                    break;

                case Token.ERROR_STRING_DOUBLE:

                    if (c == '"') {
                        addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset + currentTokenStart);
                        currentTokenStart = i + 1;
                        currentTokenType = Token.NULL;
                    }
                    // Otherwise, we're still an unclosed string...

                    break;

                case Token.VARIABLE:

                    if (RSyntaxUtilities.isLetterOrDigit(c)) {
                        break;
                    } else { // Anything else, ???.
                        addToken(text, currentTokenStart, i - 1, Token.VARIABLE, newStartOffset + currentTokenStart); // ???
                        i--;
                        currentTokenType = Token.NULL;
                        break;
                    }

            } // End of switch (currentTokenType).

        } // End of for (int i=offset; i<end; i++).

        // Deal with the (possibly there) last token.
        if (currentTokenType != Token.NULL) {

            // Check for REM comments.
            if (end - currentTokenStart == 3
                    && (array[end - 3] == 'r' || array[end - 3] == 'R')
                    && (array[end - 2] == 'e' || array[end - 2] == 'E')
                    && (array[end - 1] == 'm' || array[end - 1] == 'M')) {
                currentTokenType = Token.COMMENT_EOL;
            }

            addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
        }

        addNullToken();

        // Return the first token in our linked list.
        return firstToken;

    }
}
