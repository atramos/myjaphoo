package org.myjaphoo.gui.action

import groovy.xml.MarkupBuilder
import org.myjaphoo.MovieNode
import org.myjaphoo.MyjaphooController
import org.myjaphoo.gui.util.Helper
import org.myjaphoo.model.db.ChangeLogType
import org.myjaphoo.model.db.Token

import javax.swing.*
import javax.swing.undo.AbstractUndoableEdit
import javax.swing.undo.CannotRedoException
import javax.swing.undo.CannotUndoException
import javax.swing.undo.UndoableEdit
import java.awt.*
import java.awt.event.ActionEvent
import java.text.MessageFormat
import java.util.List

/**
 *
 * @author lang
 */
public class AddTokenAction extends AbstractUndoAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/AddTokenAction");

    private Token token;

    public AddTokenAction(MyjaphooController controller, Token token, Icon icon, ViewContext context) {
        super(controller, createMenuText(token), icon, context);
        this.token = token;

    }

    public static String createMenuText(Token token) {
        def writer = new StringWriter()
        def b = new MarkupBuilder(writer)
        b.html(localeBundle.getString("ADD")) {

            Color color = Color.black;
            if (token.getTokentype() != null) {
                color = Helper.getColorForTokenType(token.getTokentype());
            }
            font(color: hexColor(color), token.getName()) {}

            if (token.getDescription() != null) {
                p {
                    i {
                        small {
                            token.getDescription()
                        }
                    }
                }
            }
        }

        return writer.toString();
    }


    private static String hexColor(Color color) {
        String hexcolor = toHex(color.getRed()) + toHex(color.getGreen()) + toHex(color.getBlue());
        return "#" + hexcolor;
    }

    private static String toHex(int val) {
        String h = Integer.toHexString(val);
        if (h.length() == 1) {
            return "0" + h; //NOI18N
        } else {
            return h;
        }
    }

    @Override
    public UndoableEdit runUndoAction(final MyjaphooController controller, ActionEvent e, ViewContext context) {
        final List<MovieNode> nodes = context.getSelMovies();
        controller.assignTokenToMovieNodes(token, nodes);
        controller.createChangeLog(ChangeLogType.ASSIGNTOK, "assign tag " + token.getName(), nodes); //NOI18N

        return new AbstractUndoableEdit() {

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                controller.unassignTokenToMovieNodes(token, nodes);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                controller.assignTokenToMovieNodes(token, nodes);
                ;
            }

            @Override
            public String getPresentationName() {
                return MessageFormat.format(localeBundle.getString("ASSIGN TAG TO ENTRIES"), token.getName());
            }
        };
    }
}
