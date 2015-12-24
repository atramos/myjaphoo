package org.myjaphoo.gui.editor.rsta.syntax;
import org.myjaphoo.model.filterparser.Parser;
/**
 * Factory to create configured parser for usage within WmFilterParser for syntax highlighting and ad hoc error
 * checking.
 * User: lang
 */
public interface ParserCreator {

    Parser createParser();
}
