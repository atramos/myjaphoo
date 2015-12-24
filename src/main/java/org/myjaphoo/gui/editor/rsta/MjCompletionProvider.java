package org.myjaphoo.gui.editor.rsta;

import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.Script;
import org.apache.commons.lang.StringUtils;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.gui.util.TextRepresentations;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.idents.ExifIdentifier;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.syntaxtree.DescriptionFormatting;
import org.myjaphoo.model.filterparser.syntaxtree.SpecialSymbols;
import org.myjaphoo.model.groovyparser.GroovyFilterBaseClass;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.util.*;

import static org.myjaphoo.model.filterparser.idents.Identifiers.*;

/**
 * The completion provider for myjaphoo.
 * It is based on a default completion provider which just returns a fixed list of available completions,
 * and enriched by some additional guesses which use the syntax scanner to make more sophisticated guesses.
 */
public class MjCompletionProvider extends DefaultCompletionProvider {

    private static final int TAG_RELEVANCE = 100;
    private static final int VARIABLE_RELEVANCE = 200;

    /**
     * fix list of methods not intended as completion help (thos of the groovy base class; they are not dynamically
     * added, like the ones from the groovy script class, since the user defined methods are defined on this class)
     */
    private static final String[] STDBASEMETS = {"getGroovy",
            "getMetatag", "getTag", "group", "getEntry"};
    private static final Set<String> STANDARDBASECLASSMETHS = new HashSet<>(Arrays.asList(STDBASEMETS));

    static {
        // init standard base class meths: all methods of groovy Script class + the special ones
        // defined in the groovy filter class.

        MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(Script.class);
        List<MetaMethod> methods = metaClass.getMethods();
        for (MetaMethod method : methods) {
            String methName = method.getName();
            STANDARDBASECLASSMETHS.add(methName);
        }
    }
    /*
      this is sometimes useful to test the flow of completion actions.
    public String getAlreadyEnteredText(JTextComponent comp) {
        System.out.println("exec getAlreadyEnteredText");
        String s = super.getAlreadyEnteredText(comp);
        System.out.println("getAlreadyEnteredText:" + s);
        return s;
    }

    public List getCompletionsAt(JTextComponent tc, Point p) {
        System.out.println("exec getCompletionsAt");
        List l = super.getCompletionsAt(tc, p);
        System.out.println("getCompletionsAt" + l);
        return l;
    }

    public List getParameterizedCompletions(JTextComponent tc) {
        System.out.println("exec getParameterizedCompletions");
        List l = super.getParameterizedCompletions(tc);
        System.out.println("getParameterizedCompletions " + l);
        return l;
    }

    @Override
    public List getCompletionByInputText(String inputText) {
        System.out.println("exec getCompletionsByInputText");
        List l = super.getCompletionByInputText(inputText);
        System.out.println("getCompletionsByInputText:" + inputText + " ->" + l);
        return l;
    }
        @Override
    public List getCompletions(JTextComponent comp) {
        System.out.println("exec getCompletions");
        List l = super.getCompletions(comp);
        System.out.println("getCompletions: " + l);
        return l;
    }
    */

    @Override
    protected List getCompletionsImpl(JTextComponent comp) {
        System.out.println("exec my.getCompletionsImpl");
        List l = super.getCompletionsImpl(comp);
        System.out.println("completions from super:" + l);

        String alreadyEnteredText = getAlreadyEnteredText(comp);
        Collection sophisticatedCompletions = getSophisticatedCompletions(comp, alreadyEnteredText);
        l.addAll(sophisticatedCompletions);
        System.out.println("my.getCompletionsImpl: " + l);
        return l;
    }

    private Collection getSophisticatedCompletions(JTextComponent comp, String alreadyEnteredText) {
        List completions = new ArrayList();

        try {
            String txt = getEnteredTextTillCaret(comp);
            System.out.println("entered text till caret:" + txt);
            CompletionAnalyzer analyzer = new CompletionAnalyzer(txt);
            return makeSophisticatedGuess(analyzer, alreadyEnteredText);
        } catch (IOException io) {
            io.printStackTrace();
        }

        return completions;
    }

    /**
     * checks for some more sophisticated completions.
     * E.g. tag op x to propose tag names
     * metatag op x to propose meta tag names
     *
     * @param analyzer
     * @param alreadyEnteredText
     * @return
     */
    private List makeSophisticatedGuess(CompletionAnalyzer analyzer, String alreadyEnteredText) {
        List completions = new ArrayList();

        if (analyzer.match(SpecialSymbols.DOLLAR)) {
            addBms(completions, alreadyEnteredText);
        } else if (analyzer.match(TAG, AbstractOperator.class) ||
                analyzer.match(TOKEN, AbstractOperator.class)) {
            addTags(completions, alreadyEnteredText);
        } else if (analyzer.match(METATAG, AbstractOperator.class) || analyzer.match(METATOK, AbstractOperator.class)) {
            addMetaTags(completions, alreadyEnteredText);
        } else if (analyzer.match(PATH, AbstractOperator.class)) {
            addHints(completions, CachedHints.getPathHints(), alreadyEnteredText, "path extracted word");
        } else if (analyzer.match(DIR, AbstractOperator.class)) {
            addHints(completions, CachedHints.getDirHints(), alreadyEnteredText, "directory");
        } else if (analyzer.match(TITLE, AbstractOperator.class)) {
            addHints(completions, CachedHints.getTitleHints(), alreadyEnteredText, "title");
        } else if (analyzer.match(COMMENT, AbstractOperator.class)) {
            addHints(completions, CachedHints.getCommentHints(), alreadyEnteredText, "comment");
        } else if (analyzer.match(ExifIdentifier.class, AbstractOperator.class)) {
            ExifIdentifier exifIdentifier = (ExifIdentifier) analyzer.getBeforeLast();
            String exiftagname = exifIdentifier.getTagName();
            addHints(completions, CachedHints.getExifHints(exiftagname), alreadyEnteredText, "values for " + exifIdentifier.getName());
        }

        return completions;
    }

    private void addHints(List completions, Collection<String> hints, String alreadyEnteredText, String description) {
        alreadyEnteredText = StringUtils.lowerCase(alreadyEnteredText);
        for (String word : hints) {
            if (StringUtils.isEmpty(alreadyEnteredText) || word.contains(alreadyEnteredText)) {
                String descr = DescriptionFormatting.descFmtWithHtml(description, word, ExprType.TEXT, word, "");
                BasicCompletion c = new BasicCompletion(this, word, description, descr);
                c.setRelevance(TAG_RELEVANCE);
                completions.add(c);
            }
        }
    }

    private void addMetaTags(List completions, String alreadyEnteredText) {
        alreadyEnteredText = StringUtils.lowerCase(alreadyEnteredText);
        for (MetaToken mt : CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList()) {
            if (StringUtils.isEmpty(alreadyEnteredText) || mt.getName().toLowerCase().contains(alreadyEnteredText)) {
                String tagnameWrapped = wrapLiteral(mt.getName());
                String descr = DescriptionFormatting.descFmtWithHtml("Tag", mt.getName(), ExprType.TEXT, Helper.createMjCompletionMetaTagFragment(mt), "metatag like " + tagnameWrapped);

                BasicCompletion c = new BasicCompletion(this, tagnameWrapped, mt.getDescription(), descr);
                c.setRelevance(TAG_RELEVANCE);
                completions.add(c);
            }
        }
    }

    private void addBms(List completions, String alreadyEnteredText) {
        alreadyEnteredText = StringUtils.lowerCase(alreadyEnteredText);
        for (Object bmo : MainApplicationController.getInstance().getBookmarkList()) {
            BookMark bm = (BookMark) bmo;
            if (StringUtils.isEmpty(alreadyEnteredText) || bm.getName().toLowerCase().contains(alreadyEnteredText)) {
                String bmNameWrapped = wrapLiteral(bm.getName());
                String descr = DescriptionFormatting.descFmtWithHtml("Bookmark Substitution", bm.getName(), ExprType.TEXT, TextRepresentations.getFragmentForBookMark(bm), "$" + bmNameWrapped);

                BasicCompletion c = new BasicCompletion(this, bmNameWrapped, bm.getDescr(), descr);
                c.setRelevance(VARIABLE_RELEVANCE);
                completions.add(c);
            }
        }
        // $ is also the beginning of a groovy method (defined for the base class)
        // therefore scan baseclass for methods:
        MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(GroovyFilterBaseClass.class);
        List<MetaMethod> methods = metaClass.getMethods();
        for (MetaMethod method : methods) {
            String methName = method.getName();
            if (!isStandardBaseClassMeth(methName)) {
                if (StringUtils.isEmpty(alreadyEnteredText) || methName.toLowerCase().contains(alreadyEnteredText)) {
                    BasicCompletion c = new BasicCompletion(this, methName, "User defined groovy script method " + methName, "User defined groovy script method " + methName);
                    c.setRelevance(VARIABLE_RELEVANCE);
                    completions.add(c);
                }
            }
        }
    }

    private boolean isStandardBaseClassMeth(String methName) {
        return STANDARDBASECLASSMETHS.contains(methName);
    }

    private void addTags(List completions, String alreadyEnteredText) {
        alreadyEnteredText = StringUtils.lowerCase(alreadyEnteredText);
        for (Token tag : CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList()) {
            if (StringUtils.isEmpty(alreadyEnteredText) || tag.getName().toLowerCase().contains(alreadyEnteredText)) {
                String tagnameWrapped = wrapLiteral(tag.getName());
                String descr = DescriptionFormatting.descFmtWithHtml("Tag", tag.getName(), ExprType.TEXT, Helper.createMjCompletionTagFragment(tag), "tag like " + tagnameWrapped);

                BasicCompletion c = new BasicCompletion(this, tagnameWrapped, tag.getDescription(), descr);
                c.setRelevance(TAG_RELEVANCE);
                completions.add(c);
            }
        }
    }


    private String wrapLiteral(String name) {
        if (name.contains(" ")) {
            return "\"" + name + "\"";
        } else {
            return name;
        }
    }

    /**
     * return the entered text till the caret position.
     * Remove the inputted token from the current position, as
     * we want to parse only the actual valid inputed tags.
     *
     * @param comp
     * @return
     */
    public String getEnteredTextTillCaret(JTextComponent comp) {
        comp.getText();
        Document doc = comp.getDocument();
        int dot = comp.getCaretPosition();
        try {
            doc.getText(0, dot, seg);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            return EMPTY_STRING;
        }
        // check if caret is on last-token.
        // then we strap off this last token, as the user is currently inputting it,
        // and we do not want to take that one into account when doing our additional guesses.
        dot--;
        while (dot >= 0 && isValidChar(seg.array[dot])) {
            dot--;
        }
        dot++;
        return dot == 0 ? EMPTY_STRING : new String(seg.array, 0, dot);
    }


}
