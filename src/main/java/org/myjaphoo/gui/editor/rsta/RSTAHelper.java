/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.editor.rsta;

import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.editor.rsta.syntax.ParserCreator;
import org.myjaphoo.gui.editor.rsta.syntax.WmFilterParser;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.filterparser.Parser;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.idents.Qualifier;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.syntaxtree.Constant;
import org.myjaphoo.model.filterparser.syntaxtree.SelfDescriptingElement;
import org.myjaphoo.model.groupbyparser.GroupByParser;
import org.myjaphoo.model.groupbyparser.GroupingSymbols;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains helper methods to initialize rsyntaxtextarea components
 * for usage in myjaphoo.
 * @author lang
 */
public class RSTAHelper {

    public static ParserCreator filterParserCreator = new ParserCreator() {
        @Override
        public Parser createParser() {
            return new FilterParser(MainApplicationController.getInstance().createSubstitutions());
        }
    };

    public static ParserCreator groupByParserCreator = new ParserCreator() {
        @Override
        public Parser createParser() {
            return new GroupByParser(MainApplicationController.getInstance().createSubstitutions());
        }
    };

    public static void init(RSyntaxTextArea textArea, boolean asFilterEditor) {
        
        textArea.setAnimateBracketMatching(true);
        textArea.setBracketMatchingEnabled(true);
      
        
        textArea.setAntiAliasingEnabled(true);
        // set our own syntax highlighting style:
        textArea.setSyntaxEditingStyle("wmstyle");
        // set parser to mark errors:
        if (asFilterEditor) {
            textArea.addParser(new WmFilterParser(filterParserCreator));
        } else {
            textArea.addParser(new WmFilterParser(groupByParserCreator));
        }
        // A CompletionProvider is what knows of all possible completions, and
        // analyzes the contents of the text area at the caret position to
        // determine what completion choices should be presented.  Most
        // instances of CompletionProvider (such as DefaultCompletionProvider)
        // are designed so that they can be shared among multiple text
        // components.
        CompletionProvider provider = createCompletionProvider(asFilterEditor);

        // An AutoCompletion acts as a "middle-man" between a text component
        // and a CompletionProvider.  It manages any options associated with
        // the auto-completion (the popup trigger key, whether to display a
        // documentation window along with completion choices, etc.).  Unlike
        // CompletionProviders, instances of AutoCompletion cannot be shared
        // among multiple text components.
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setShowDescWindow(true);
        ac.setAutoActivationDelay(3000);
        ac.setAutoActivationEnabled(true);
        ac.setParameterAssistanceEnabled(true);
        ac.install(textArea);
        
    }

    public static void initAsFilterEditor(RSyntaxTextArea textArea) {
        init(textArea, true);
    }

    /**
     * Create a simple provider for the filter or group by language.
     *
     * @return The completion provider.
     */
    private static CompletionProvider createCompletionProvider(boolean asFilterEditor) {

        // A DefaultCompletionProvider is the simplest concrete implementation
        // of CompletionProvider.  This provider has no understanding of
        // language semantics. It simply checks the text entered up to the
        // caret position for a match against known completions. This is all
        // that is needed in the majority of cases.
        DefaultCompletionProvider provider = new MjCompletionProvider();
        provider.setParameterizedCompletionParams('(', ", ", ')');
        final CompletionCellRenderer ccr = new CompletionCellRenderer();
        ccr.setShowTypes(true);
        provider.setListCellRenderer(ccr);

        provider.setAutoActivationRules(true, "(,.{$");
        // Add completions for all ident key words:

        for (final Qualifier ident : Qualifier.getList()) {
            provider.addCompletion(createVC(provider, ident));
        }
        for (final AbstractOperator op : AbstractOperator.getList()) {
            provider.addCompletion(createBC(provider, op));
        }
        for (final Function function : Function.getList()) {
            provider.addCompletion(createFC(provider, function));
        }
        for (final Constant constant : Constant.getAllConstants()) {
            provider.addCompletion(createBC(provider, constant));
        }
        
        if (!asFilterEditor) {
            provider.addCompletion(new BasicCompletion(provider, GroupingSymbols.GROUPBY.getSymbol()));
            provider.addCompletion(new BasicCompletion(provider, GroupingSymbols.GROUP.getSymbol()));
            provider.addCompletion(new BasicCompletion(provider, GroupingSymbols.BY.getSymbol()));
            provider.addCompletion(new BasicCompletion(provider, GroupingSymbols.IF.getSymbol()));
            provider.addCompletion(new BasicCompletion(provider, GroupingSymbols.ELSE.getSymbol()));
            provider.addCompletion(new BasicCompletion(provider, GroupingSymbols.ELSEIF.getSymbol()));

            for (GroupingDim dim : GroupingDim.values()) {
                provider.addCompletion(new BasicCompletion(provider, dim.name()));
            }
        }
        return provider;

    }

    private static BasicCompletion createBC(DefaultCompletionProvider provider, SelfDescriptingElement sde) {
        return new BasicCompletion(provider, sde.getName(), sde.getSelfShortDescription(), "<html>" + sde.getSelfDescription() + "</html>");
    }

    public static void initAsGroupByEditor(RSyntaxTextArea textArea) {
        init(textArea, false);
    }

    private static Completion createFC(DefaultCompletionProvider provider, Function function) {
        final FunctionCompletion fc = new FunctionCompletion(provider, function.getName(), function.getType().toString());
        
        fc.setShortDescription(function.getSelfDescription());
        List<ParameterizedCompletion.Parameter> params = new ArrayList<ParameterizedCompletion.Parameter>();
        int i = 0;
        for (ExprType at: function.getArgTypes()) {
            String argName = "arg" + i++;
            ParameterizedCompletion.Parameter p = new ParameterizedCompletion.Parameter(at.toString(), argName);
            params.add(p);
        }
        
        fc.setParams(params);
        
        return fc;
    }

    private static Completion createVC(DefaultCompletionProvider provider, Qualifier ident) {
        final VariableCompletion vc = new VariableCompletion(provider, ident.getName(), ident.getType().toString());
        vc.setShortDescription(ident.getSelfDescription());
       
        return vc;
    }



    public static Component getTableCellRendererComponent(RSyntaxTextArea textArea, final JTable table, Object value,
            boolean isSelected, boolean hasFocus, final int row, final int column) {
        if (value != null) {
            textArea.setText(value.toString());
        } else {
            textArea.setText("");
        }
        if (isSelected) {
            textArea.setForeground(table.getSelectionForeground());
            textArea.setBackground(table.getSelectionBackground());
        } else {
            textArea.setForeground(table.getForeground());
            textArea.setBackground(table.getBackground());
        }
        if (hasFocus) {
            if (!isSelected /**&& context.isEditable()*/
                    ) {
                Color col = table.getForeground();
                if (col != null) {
                    textArea.setForeground(col);
                }
                col = table.getBackground();
                if (col != null) {
                    textArea.setBackground(col);
                }
            }
        }
        return textArea;
    }

    public static Component getTableCellRendererComponent(RSyntaxTextArea textArea, final JTree table, Object value,
                                                          boolean isSelected, boolean hasFocus) {
        if (value != null) {
            textArea.setText(value.toString());
        } else {
            textArea.setText("");
        }
//        if (isSelected) {
//            textArea.setForeground(table.getSelectionForeground());
//            textArea.setBackground(table.getSelectionBackground());
//        } else {
            textArea.setForeground(table.getForeground());
            textArea.setBackground(table.getBackground());
//        }
        if (hasFocus) {
            if (!isSelected /**&& context.isEditable()*/
                    ) {
                Color col = table.getForeground();
                if (col != null) {
                    textArea.setForeground(col);
                }
                col = table.getBackground();
                if (col != null) {
                    textArea.setBackground(col);
                }
            }
        }
        return textArea;
    }
}
