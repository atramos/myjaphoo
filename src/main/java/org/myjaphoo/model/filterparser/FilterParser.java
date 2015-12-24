package org.myjaphoo.model.filterparser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.filterparser.expr.*;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.functions.Functions;
import org.myjaphoo.model.filterparser.functions.SubstitutionFunction;
import org.myjaphoo.model.filterparser.idents.*;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.operator.Operators;
import org.myjaphoo.model.filterparser.syntaxtree.*;
import org.myjaphoo.model.grammars.FilterLanguageLexer;
import org.myjaphoo.model.grammars.FilterLanguageParser;
import org.myjaphoo.model.groovyparser.GroovyFilterParser;
import org.myjaphoo.model.groupbyparser.GroupingSymbols;
import org.myjaphoo.model.groupbyparser.StandardGroupingSymbols;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * FilterParserAntlrVersion
 * @author mla
 * @version $Id$
 */
public class FilterParser implements Parser {

    private Map<String, Substitution> substitutions;
    private int callLevel = 1;
    private static final int MAX_CALL_LEVEL = 10;

    private static boolean isInitialized = false;

    /**
     * Ensure, that all static defined syntax objects are correctly initialized.
     */
    public static void initialize() {
        if (!isInitialized) {
            // init operator and braces, etc. mappings: (static class elements need to be loaded by classloader)
            Operators.EQUAL.toString();
            Identifiers.NAME.toString();
            Qualifiers.LEFT.toString();
            Braces.CLOSEBRACE.toString();
            LiteralListSymbol.AND_SEPARATOR.toString();
            Functions.SUBST.toString();
            SpecialSymbols.COMMA.toString();
            Unit.GB.toString();
            initConstants(Rating.class);
            StandardGroupingSymbols.initMe();
            GroupingSymbols.BY.toString();
            isInitialized = true;
        }
    }

    private static <T extends Enum> void initConstants(Class<T> clazz) {
        T[] allConstants = clazz.getEnumConstants();
        for (T t : allConstants) {
            new Constant(t.name(), t.ordinal(), ExprType.NUMBER);
        }
    }

    /**
     * Constructor with a map of possible substitutions (substitutions of bookmarks).
     * @param substitutions
     */
    public FilterParser(Map<String, Substitution> substitutions) {
        this.substitutions = substitutions;
        initialize();
    }

    /**
     * Constructor used when a bookmark gets substituted. There is only a maximum
     * of 10 levels of substitution of substitutions allowed (to prevent endless
     * loops).
     * @param substitutions
     * @param callLevel
     * @throws ParserException
     */
    protected FilterParser(Map<String, Substitution> substitutions, int callLevel) throws ParserException {
        this.substitutions = substitutions;
        this.callLevel = callLevel;
        // max call level is 10 to prevent endless loop:
        if (callLevel > MAX_CALL_LEVEL) {
            throw new ParserException("", "error: reached max substitution nesting of 10!", 0, 0);
        }
        initialize();
    }

    public AbstractBoolExpression parse(String txt) throws ParserException {
        return parseFilterExpression(txt);
    }

    public AbstractBoolExpression parseFilterExpression(String txt) throws ParserException {
        CommonTokenStream tokens = createTokenStream(txt);
        FilterLanguageParser parser = new FilterLanguageParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        FilterLanguageParser.FilterContext tree = parser.filter();
        return parse(tree, txt);
    }

    public static CommonTokenStream createTokenStream(String txt) {
        ANTLRInputStream input = new ANTLRInputStream(txt);
        FilterLanguageLexer lexer = new FilterLanguageLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ParserErrorListener());

        return new CommonTokenStream(lexer);
    }

    /**
     * parses a normal expression.
     * @param txt
     * @return
     */
    public Expression parseExpression(String txt) {
        CommonTokenStream tokens = createTokenStream(txt);
        FilterLanguageParser parser = new FilterLanguageParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ParserErrorListener());

        FilterLanguageParser.ExprContext tree = parser.expr();
        return parse(tree);
    }

    private AbstractBoolExpression parse(FilterLanguageParser.FilterContext filter, String txt) {
        if (filter.GroovyCode() != null) {
            // remove "groovy" from the txt:
            String textWithoutGroovyKeyWord = txt.substring(6);
            GroovyFilterParser gfp = new GroovyFilterParser();
            GroovyFilterParser.GroovyScriptWrapper script = gfp.createScript(textWithoutGroovyKeyWord);
            return new GroovyBoolExpression(script);
        } else if (filter.simpleSearchLiteral() != null) {
            // simple search literal gets interpreted as "x like <literal>"
            Literal searchLiteral = parseLiteral(filter.simpleSearchLiteral().literal());
            return new BoolExpression(new QualifierIdentExpression(Identifiers.X), Operators.LIKE, searchLiteral);
        } else {
            return parse(filter.boolexpr());
        }
    }

    protected AbstractBoolExpression parse(FilterLanguageParser.BoolexprContext boolexpr) {
        if (boolexpr.brackedBoolExpr() != null) {
            return parse(boolexpr.brackedBoolExpr().boolexpr());
        } else if (boolexpr.negatedBoolExpr() != null) {
            return new NegatedBoolExpression(parse(boolexpr.negatedBoolExpr().brackedBoolExpr().boolexpr()));
        } else if (boolexpr.boolexpr().size() == 2) {
            AbstractBoolExpression b1 = parse(boolexpr.boolexpr(0));
            AbstractBoolExpression b2 = parse(boolexpr.boolexpr(1));
            AbstractOperator operator = AbstractOperator.getOperatorByToken(boolexpr.op.getType());
            typeCompatibilityCheck(boolexpr.boolexpr(1).getStart(), b1, operator, b2);
            return new BoolTerm(b1, operator, b2);
        } else if (boolexpr.expr().size() == 2) {
            Expression e1 = parse(boolexpr.expr(0));
            Expression e2 = parse(boolexpr.expr(1));
            AbstractOperator operator = AbstractOperator.getOperatorByToken(boolexpr.op.getType());
            typeCompatibilityCheck(boolexpr.expr(1).getStart(), e1, operator, e2);
            return new BoolExpression(e1, operator, e2);
        } else if (boolexpr.expr().size() == 1) {
            Expression e1 = parse(boolexpr.expr(0));
            return new WrappedBoolExpression(e1);
        } else if (boolexpr.SimpleRegExSearchAllStatement() != null) {
            // this is just a short form of "x regexfind literal":
            return new BoolExpression(new QualifierIdentExpression(Identifiers.X), Operators.REGEXFIND, parseSpecialRegExToken(boolexpr.SimpleRegExSearchAllStatement()));
        } else if (boolexpr.identWithSimpleRegExSearchStatement() != null) {
            Expression qExpr = parseQualifier(boolexpr.identWithSimpleRegExSearchStatement().qualifier());
            StringLiteral regexLiteral = parseSpecialRegExToken(boolexpr.identWithSimpleRegExSearchStatement().SimpleRegExSearchAllStatement());
            return new BoolExpression(qExpr, Operators.REGEXFIND, regexLiteral);
        } else {
            throw new ParserException(boolexpr.getStart(), "parse error!");
        }
    }

    protected Expression parse(FilterLanguageParser.ExprContext expr) {
        if (expr.brackedExpr() != null) {
            return parse(expr.brackedExpr().expr());
        } else if (expr.funcCall() != null) {
            return parseFuncCall(expr.funcCall());
        } else if (expr.substIdentifier() != null) {
            return parseSubstIdentifier(expr.substIdentifier());
        } else if (expr.andLiteral() != null) {
            return parseAndLiteral(expr.andLiteral());
        } else if (expr.orLiteral() != null) {
            return parseOrLiteral(expr.orLiteral());
        } else if (expr.qualifier() != null) {
            return parseQualifier(expr.qualifier());
        } else if (expr.DateLiteral() != null) {
            // wir lassen auch zu, dass ein string-literal ein datum darstellt:
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date = df.parse(expr.DateLiteral().getText());
                return new DateLiteral(date);
            } catch (ParseException ex) {
                return new StringLiteral(expr.DateLiteral().getText());
            }
        } else if (expr.StringLiteral() != null) {
            return parseStringLiteral(expr.StringLiteral());
        } else if (expr.RegexLiteral() != null) {
            return parseSpecialRegExToken(expr.RegexLiteral());
        } else if (expr.NOTHING() != null) {
            return new NullLiteral();
        } else if (expr.number() != null) {
            return parseNumber(expr.number());
        } else if (expr.expr().size() == 2) {
            Expression e1 = parse(expr.expr(0));
            Expression e2 = parse(expr.expr(1));
            AbstractOperator operator = AbstractOperator.getOperatorByToken(expr.op.getType());
            if (operator == null) {
                throw new ParserException(expr.op, "unknown Operator!");
            }
            typeCompatibilityCheck(expr.expr(1).getStart(), e1, operator, e2);
            return new Term(e1, operator, e2);
        } else {
            throw new ParserException(expr.getStart(), "parse error!");
        }
    }

    private StringLiteral parseSpecialRegExToken(TerminalNode node) {
        String regExLiteral = node.getSymbol().getText();
        // since this a token that starts with either the marker ':', or '?', strip off the marker:
        regExLiteral = regExLiteral.substring(1);
        return new StringLiteral(regExLiteral);
    }

    protected Expression parseQualifier(FilterLanguageParser.QualifierContext qualifierContext) {
        Class contextClass = JoinedDataRow.class;
        ArrayList<Qualifier> quals = new ArrayList<Qualifier>();
        for (TerminalNode id : qualifierContext.ID()) {
            String idName = id.getText();
            Qualifier qualifier = Qualifier.mapIdent(idName);
            if (qualifier == null) {
                if (qualifierContext.ID().size() > 1) {
                    throw new ParserException(id.getSymbol(), "Expect qualifier or identifier! found " + idName + "!");
                } else {
                    // is it a constant?
                    // todo not so great here. maybe could be made easier by changing the grammar?
                    SpecialCharSymbol found = Constant.mapIdent(idName);
                    if (found != null && found instanceof Constant) {
                        return (Constant) found;
                    }
                    // in this case it is our lenient version of a literal, so just return a string literal from the text:
                    return new StringLiteral(idName);
                }
            }
            // validate context class type compatiblity:
            if (!qualifier.getInputContextClass().isAssignableFrom(contextClass)) {
                throw new ParserException(id.getSymbol(), "Qualifier " + qualifier.getName() + " can not be used here!");
            }
            contextClass = qualifier.getOutputContextClass();

            // collect all qual sub expressions:
            quals.add(qualifier);
        }
        // the last qualifier must now be a real identifier:
        Qualifier lastOne = quals.remove(quals.size() - 1);
        if (!(lastOne instanceof FixIdentifier)) {
            throw new ParserException(qualifierContext.getStop(), "identifier expected!");
        }
        if (quals.size() > 0) {
            return new QualifierIdentExpression(quals, (FixIdentifier) lastOne);
        } else {
            // expression is only a qualifier which is also a ident.
            // in this case we can simply return the identifier:
            return new QualifierIdentExpression((FixIdentifier) lastOne);
        }
    }

    private Expression parseFuncCall(FilterLanguageParser.FuncCallContext funcCallContext) {
        String functionName = funcCallContext.ID().getText();
        Function function = Function.mapIdent(functionName);
        if (function == null) {
            throw new ParserException(funcCallContext.ID().getSymbol(), "Unknown Function!");
        }


        List<Expression> args = null;
        if (funcCallContext.argList() != null) {
            args = parseArgList(funcCallContext.argList());
        } else {
            args = parseSimpleArg(funcCallContext.simpleArg());
        }
        checkFunctionArgumentList(funcCallContext.getStart(), function, args);

        // check pseudo subst function to make a substitution:
        if (function instanceof SubstitutionFunction) {
            String bm = args.get(0).evaluate(null, new JoinedDataRow()).asString();
            return substitute(bm);
        }

        return new FunctionCall(function, args);
    }

    private List<Expression> parseSimpleArg(FilterLanguageParser.SimpleArgContext simpleArgContext) {
        if (simpleArgContext.number() != null) {
            return Arrays.asList(parseNumber(simpleArgContext.number()));
        } else {
            ArrayList<Expression> list = new ArrayList<Expression>();
            list.add(parseLiteral(simpleArgContext.literal()));
            return list;
        }
    }

    private ArrayList<Expression> parseArgList(FilterLanguageParser.ArgListContext argListContext) {
        ArrayList<Expression> result = new ArrayList<>();
        for (FilterLanguageParser.ExprContext exprContext : argListContext.expr()) {
            result.add(parse(exprContext));
        }
        return result;
    }

    private Expression parseSubstIdentifier(FilterLanguageParser.SubstIdentifierContext substIdentifierContext) {
        String variableName = substIdentifierContext.literal().getText();
        Substitution substitution = substitutions.get(variableName);
        if (substitution == null) {
            // check if there is a groovy meta-function defined with that name:
            List<Expression> argList = substIdentifierContext.argList() != null ? parseArgList(substIdentifierContext.argList()) : new ArrayList<Expression>();
            if (isUserDefinedFunction(variableName, argList)) {
                return new GroovyFunctionCall(variableName, argList);
            } else {
                throw new ParserException(substIdentifierContext.getStart(), "can not find " + variableName + " for substitution or groovy method!");
            }
        } else {
            return substitute(variableName);
        }

    }

    private Expression substitute(String variableName) {
        Substitution substitution = substitutions.get(variableName);
        FilterParser parser = new FilterParser(substitutions, callLevel + 1);
        AbstractBoolExpression expression = parser.parse(substitution.getExpression());
        return new SubstitutionExpression(variableName, expression);

    }


    /**
     * checks type compatibility for a functions argument list.
     * Checks if the types of the expected arguments of the function match
     * the types of the real arguments.
     */

    private boolean isUserDefinedFunction(String variableName, List<Expression> argList) {
        return true; // todo
    }

    private Expression parseNumber(FilterLanguageParser.NumberContext number) {
        Unit unit = null;
        long val = 0;
        if (number.DOUBLE() != null) {
            val = new Double(number.DOUBLE().getText()).longValue();
        } else if (number.INT() != null) {
            val = Long.parseLong(number.INT().getText());
        }
        if (number.sizePrefix() != null) {
            // maybe a bit risky using start?
            unit = Unit.getUnitByTok(number.sizePrefix().getStart().getType());
        }
        NumberLiteral literal = new NumberLiteralImpl(val);
        literal.setUnit(unit);
        return literal;
    }

    private Expression parseLiteralList(LiteralListSymbol symbol, List<FilterLanguageParser.LiteralContext> literals) {
        List<Literal> list = new ArrayList<>();
        for (FilterLanguageParser.LiteralContext literalContext : literals) {
            list.add(parseLiteral(literalContext));
        }
        LiteralList ll = new LiteralList(symbol, list);
        return ll;
    }

    Literal parseLiteral(FilterLanguageParser.LiteralContext literalContext) {
        if (literalContext.ID() != null) {
            return new StringLiteral(literalContext.ID().getText());
        } else {
            return new StringLiteral(literalContext.StringLiteral().getText());
        }
    }

    private Expression parseAndLiteral(FilterLanguageParser.AndLiteralContext andLiteralContext) {
        return parseLiteralList(LiteralListSymbol.AND_SEPARATOR, andLiteralContext.literal());
    }

    private Expression parseOrLiteral(FilterLanguageParser.OrLiteralContext orLiteralContext) {
        return parseLiteralList(LiteralListSymbol.OR_SEPARATOR, orLiteralContext.literal());
    }

    private Expression parseStringLiteral(TerminalNode node) {
        return new StringLiteral(node.getText());
    }

    @Override
    public void checkCorrectness(String txt) throws ParserException {
        parse(txt);
    }

    private boolean typeCompatibilityCheck(Token token, Expression e1, AbstractOperator comparison, Expression e2) throws ParserException {
        if (!typeCompatible(e1, comparison)) {
            throw new ParserException(token, "type of '" + e1.getDisplayExprTxt() + "' is not compatible with operator '" + comparison.getSymbol() + "'");
        }
        if (!typeCompatible(e2, comparison)) {
            throw new ParserException(token, "type of '" + e2.getDisplayExprTxt() + "' is not compatible with operator '" + comparison.getSymbol() + "'");
        }
        if (e1.getType().isCompatible(e2.getType())) {
            return true;
        }
        throw new ParserException(token, "type compatibility error: " + e1.getDisplayExprTxt() + " has type " + e1.getType()
                + "; but " + e2.getDisplayExprTxt() + " has type " + e2.getType() + "!");
    }

    private boolean typeCompatible(Expression e, AbstractOperator op) {
        return op.worksWithTypes().contains(e.getType()) || e.getType() == ExprType.OBJECT;
    }

    private void checkFunctionArgumentList(Token token, Function function, List<Expression> args) throws ParserException {
        // first check num of arguments:
        if (function.getArgTypes().length != args.size()) {
            throw new ParserException(token, "function expects " + function.getArgTypes().length + " arguments!");
        }
        // check type of each argument:
        for (int i = 0; i < function.getArgTypes().length; i++) {
            ExprType expectedType = function.getArgTypes()[i];
            Expression argExpr = args.get(i);
            ExprType concreteType = argExpr.getType();
            if (expectedType != concreteType) {
                throw new ParserException(token, "function " + function.getName() + " parameter " + (i + 1) + " expects type "
                        + expectedType + " but " + argExpr.getDisplayExprTxt() + " is " + concreteType + "!");
            }
        }
    }
}
