// Generated from org\myjaphoo\model\grammars\FilterLanguage.g4 by ANTLR 4.3
package org.myjaphoo.model.grammars;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FilterLanguageParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__18=1, T__17=2, T__16=3, T__15=4, T__14=5, T__13=6, T__12=7, T__11=8, 
		T__10=9, T__9=10, T__8=11, T__7=12, T__6=13, T__5=14, T__4=15, T__3=16, 
		T__2=17, T__1=18, T__0=19, DateLiteral=20, StringLiteral=21, GroovyCode=22, 
		INT=23, DOUBLE=24, WS=25, BlockComment=26, LineComment=27, REGEX2=28, 
		RegexLiteral=29, SimpleRegExSearchAllStatement=30, MUL=31, DIV=32, ADD=33, 
		SUB=34, LOWER=35, HIGHER=36, EQUAL=37, LOWEREQUALTHEN=38, HIGHEREQUALTHEN=39, 
		UNEQUAL=40, LIKE=41, IS=42, STARTSWITH=43, WIE=44, ENDSWITH=45, REGEX=46, 
		REGEXFIND=47, AND=48, OR=49, NOT=50, NOTHING=51, KB=52, MB=53, GB=54, 
		ID=55;
	public static final String[] tokenNames = {
		"<INVALID>", "'&'", "'groupby'", "','", "'('", "'if'", "'--'", "'{'", 
		"'by'", "'else'", "'}'", "'$'", "'elseif'", "')'", "'.'", "'group'", "';'", 
		"'#'", "'having'", "'|'", "DateLiteral", "StringLiteral", "GroovyCode", 
		"INT", "DOUBLE", "WS", "BlockComment", "LineComment", "'~'", "RegexLiteral", 
		"SimpleRegExSearchAllStatement", "'*'", "'/'", "'+'", "'-'", "'<'", "'>'", 
		"'='", "'<='", "'>='", "'<>'", "'like'", "'is'", "'startswith'", "'wie'", 
		"'endswith'", "'regex'", "'regexfind'", "'and'", "'or'", "'not'", "NOTHING", 
		"KB", "MB", "GB", "ID"
	};
	public static final int
		RULE_filter = 0, RULE_boolexpr = 1, RULE_simpleSearchLiteral = 2, RULE_identWithSimpleRegExSearchStatement = 3, 
		RULE_negatedBoolExpr = 4, RULE_expr = 5, RULE_number = 6, RULE_sizePrefix = 7, 
		RULE_brackedExpr = 8, RULE_literal = 9, RULE_andLiteral = 10, RULE_orLiteral = 11, 
		RULE_qualifier = 12, RULE_substIdentifier = 13, RULE_funcCall = 14, RULE_simpleArg = 15, 
		RULE_argList = 16, RULE_brackedBoolExpr = 17, RULE_group = 18, RULE_groupInstruction = 19, 
		RULE_groupExprList = 20, RULE_havingClause = 21, RULE_groupExprPart = 22, 
		RULE_groupExpr = 23, RULE_groupTerm = 24, RULE_ifStmt = 25, RULE_thenStmt = 26, 
		RULE_elseIfStmt = 27, RULE_elseStmt = 28;
	public static final String[] ruleNames = {
		"filter", "boolexpr", "simpleSearchLiteral", "identWithSimpleRegExSearchStatement", 
		"negatedBoolExpr", "expr", "number", "sizePrefix", "brackedExpr", "literal", 
		"andLiteral", "orLiteral", "qualifier", "substIdentifier", "funcCall", 
		"simpleArg", "argList", "brackedBoolExpr", "group", "groupInstruction", 
		"groupExprList", "havingClause", "groupExprPart", "groupExpr", "groupTerm", 
		"ifStmt", "thenStmt", "elseIfStmt", "elseStmt"
	};

	@Override
	public String getGrammarFileName() { return "FilterLanguage.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FilterLanguageParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FilterContext extends ParserRuleContext {
		public BoolexprContext boolexpr() {
			return getRuleContext(BoolexprContext.class,0);
		}
		public SimpleSearchLiteralContext simpleSearchLiteral() {
			return getRuleContext(SimpleSearchLiteralContext.class,0);
		}
		public TerminalNode GroovyCode() { return getToken(FilterLanguageParser.GroovyCode, 0); }
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(58); match(GroovyCode);
				}
				break;

			case 2:
				{
				setState(59); simpleSearchLiteral();
				}
				break;

			case 3:
				{
				setState(60); boolexpr(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolexprContext extends ParserRuleContext {
		public Token op;
		public NegatedBoolExprContext negatedBoolExpr() {
			return getRuleContext(NegatedBoolExprContext.class,0);
		}
		public List<BoolexprContext> boolexpr() {
			return getRuleContexts(BoolexprContext.class);
		}
		public IdentWithSimpleRegExSearchStatementContext identWithSimpleRegExSearchStatement() {
			return getRuleContext(IdentWithSimpleRegExSearchStatementContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public BoolexprContext boolexpr(int i) {
			return getRuleContext(BoolexprContext.class,i);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode SimpleRegExSearchAllStatement() { return getToken(FilterLanguageParser.SimpleRegExSearchAllStatement, 0); }
		public BrackedBoolExprContext brackedBoolExpr() {
			return getRuleContext(BrackedBoolExprContext.class,0);
		}
		public BoolexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolexpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterBoolexpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitBoolexpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitBoolexpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolexprContext boolexpr() throws RecognitionException {
		return boolexpr(0);
	}

	private BoolexprContext boolexpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		BoolexprContext _localctx = new BoolexprContext(_ctx, _parentState);
		BoolexprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_boolexpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(64); brackedBoolExpr();
				}
				break;

			case 2:
				{
				setState(65); expr(0);
				setState(66);
				((BoolexprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REGEX2) | (1L << LOWER) | (1L << HIGHER) | (1L << EQUAL) | (1L << LOWEREQUALTHEN) | (1L << HIGHEREQUALTHEN) | (1L << UNEQUAL) | (1L << LIKE) | (1L << IS) | (1L << STARTSWITH) | (1L << WIE) | (1L << ENDSWITH) | (1L << REGEX) | (1L << REGEXFIND))) != 0)) ) {
					((BoolexprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				consume();
				setState(67); expr(0);
				}
				break;

			case 3:
				{
				setState(69); expr(0);
				}
				break;

			case 4:
				{
				setState(70); negatedBoolExpr();
				}
				break;

			case 5:
				{
				setState(71); match(SimpleRegExSearchAllStatement);
				}
				break;

			case 6:
				{
				setState(72); identWithSimpleRegExSearchStatement();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(83);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(81);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new BoolexprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_boolexpr);
						setState(75);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(76); ((BoolexprContext)_localctx).op = match(AND);
						setState(77); boolexpr(5);
						}
						break;

					case 2:
						{
						_localctx = new BoolexprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_boolexpr);
						setState(78);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(79); ((BoolexprContext)_localctx).op = match(OR);
						setState(80); boolexpr(4);
						}
						break;
					}
					} 
				}
				setState(85);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class SimpleSearchLiteralContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public SimpleSearchLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleSearchLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterSimpleSearchLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitSimpleSearchLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitSimpleSearchLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleSearchLiteralContext simpleSearchLiteral() throws RecognitionException {
		SimpleSearchLiteralContext _localctx = new SimpleSearchLiteralContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_simpleSearchLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86); literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentWithSimpleRegExSearchStatementContext extends ParserRuleContext {
		public TerminalNode SimpleRegExSearchAllStatement() { return getToken(FilterLanguageParser.SimpleRegExSearchAllStatement, 0); }
		public QualifierContext qualifier() {
			return getRuleContext(QualifierContext.class,0);
		}
		public IdentWithSimpleRegExSearchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identWithSimpleRegExSearchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterIdentWithSimpleRegExSearchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitIdentWithSimpleRegExSearchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitIdentWithSimpleRegExSearchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentWithSimpleRegExSearchStatementContext identWithSimpleRegExSearchStatement() throws RecognitionException {
		IdentWithSimpleRegExSearchStatementContext _localctx = new IdentWithSimpleRegExSearchStatementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_identWithSimpleRegExSearchStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88); qualifier();
			setState(89); match(SimpleRegExSearchAllStatement);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NegatedBoolExprContext extends ParserRuleContext {
		public BrackedBoolExprContext brackedBoolExpr() {
			return getRuleContext(BrackedBoolExprContext.class,0);
		}
		public NegatedBoolExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negatedBoolExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterNegatedBoolExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitNegatedBoolExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitNegatedBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NegatedBoolExprContext negatedBoolExpr() throws RecognitionException {
		NegatedBoolExprContext _localctx = new NegatedBoolExprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_negatedBoolExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91); match(NOT);
			setState(92); brackedBoolExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public Token op;
		public BrackedExprContext brackedExpr() {
			return getRuleContext(BrackedExprContext.class,0);
		}
		public FuncCallContext funcCall() {
			return getRuleContext(FuncCallContext.class,0);
		}
		public TerminalNode NOTHING() { return getToken(FilterLanguageParser.NOTHING, 0); }
		public TerminalNode RegexLiteral() { return getToken(FilterLanguageParser.RegexLiteral, 0); }
		public QualifierContext qualifier() {
			return getRuleContext(QualifierContext.class,0);
		}
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public SubstIdentifierContext substIdentifier() {
			return getRuleContext(SubstIdentifierContext.class,0);
		}
		public AndLiteralContext andLiteral() {
			return getRuleContext(AndLiteralContext.class,0);
		}
		public TerminalNode StringLiteral() { return getToken(FilterLanguageParser.StringLiteral, 0); }
		public TerminalNode DateLiteral() { return getToken(FilterLanguageParser.DateLiteral, 0); }
		public OrLiteralContext orLiteral() {
			return getRuleContext(OrLiteralContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(95); brackedExpr();
				}
				break;

			case 2:
				{
				setState(96); funcCall();
				}
				break;

			case 3:
				{
				setState(97); substIdentifier();
				}
				break;

			case 4:
				{
				setState(98); number();
				}
				break;

			case 5:
				{
				setState(99); match(DateLiteral);
				}
				break;

			case 6:
				{
				setState(100); match(StringLiteral);
				}
				break;

			case 7:
				{
				setState(101); match(NOTHING);
				}
				break;

			case 8:
				{
				setState(102); qualifier();
				}
				break;

			case 9:
				{
				setState(103); andLiteral();
				}
				break;

			case 10:
				{
				setState(104); orLiteral();
				}
				break;

			case 11:
				{
				setState(105); match(RegexLiteral);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(116);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(114);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(108);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(109);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MUL || _la==DIV) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(110); expr(6);
						}
						break;

					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(111);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(112);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(113); expr(5);
						}
						break;
					}
					} 
				}
				setState(118);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(FilterLanguageParser.INT, 0); }
		public SizePrefixContext sizePrefix() {
			return getRuleContext(SizePrefixContext.class,0);
		}
		public TerminalNode DOUBLE() { return getToken(FilterLanguageParser.DOUBLE, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==DOUBLE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(121);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(120); sizePrefix();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SizePrefixContext extends ParserRuleContext {
		public TerminalNode GB() { return getToken(FilterLanguageParser.GB, 0); }
		public TerminalNode KB() { return getToken(FilterLanguageParser.KB, 0); }
		public TerminalNode MB() { return getToken(FilterLanguageParser.MB, 0); }
		public SizePrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sizePrefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterSizePrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitSizePrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitSizePrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SizePrefixContext sizePrefix() throws RecognitionException {
		SizePrefixContext _localctx = new SizePrefixContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_sizePrefix);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << KB) | (1L << MB) | (1L << GB))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BrackedExprContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public BrackedExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_brackedExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterBrackedExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitBrackedExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitBrackedExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BrackedExprContext brackedExpr() throws RecognitionException {
		BrackedExprContext _localctx = new BrackedExprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_brackedExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125); match(T__15);
			setState(126); expr(0);
			setState(127); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(FilterLanguageParser.ID, 0); }
		public TerminalNode StringLiteral() { return getToken(FilterLanguageParser.StringLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_la = _input.LA(1);
			if ( !(_la==StringLiteral || _la==ID) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndLiteralContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public AndLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterAndLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitAndLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitAndLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndLiteralContext andLiteral() throws RecognitionException {
		AndLiteralContext _localctx = new AndLiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_andLiteral);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(131); literal();
			setState(136);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(132); match(T__18);
					setState(133); literal();
					}
					} 
				}
				setState(138);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrLiteralContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public OrLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterOrLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitOrLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitOrLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrLiteralContext orLiteral() throws RecognitionException {
		OrLiteralContext _localctx = new OrLiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_orLiteral);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(139); literal();
			setState(144);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(140); match(T__0);
					setState(141); literal();
					}
					} 
				}
				setState(146);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifierContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(FilterLanguageParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(FilterLanguageParser.ID, i);
		}
		public QualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifierContext qualifier() throws RecognitionException {
		QualifierContext _localctx = new QualifierContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_qualifier);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(147); match(ID);
			setState(152);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(148); match(T__5);
					setState(149); match(ID);
					}
					} 
				}
				setState(154);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubstIdentifierContext extends ParserRuleContext {
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public SubstIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_substIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterSubstIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitSubstIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitSubstIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubstIdentifierContext substIdentifier() throws RecognitionException {
		SubstIdentifierContext _localctx = new SubstIdentifierContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_substIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155); match(T__8);
			setState(156); literal();
			setState(158);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(157); argList();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncCallContext extends ParserRuleContext {
		public SimpleArgContext simpleArg() {
			return getRuleContext(SimpleArgContext.class,0);
		}
		public TerminalNode ID() { return getToken(FilterLanguageParser.ID, 0); }
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public FuncCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterFuncCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitFuncCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitFuncCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncCallContext funcCall() throws RecognitionException {
		FuncCallContext _localctx = new FuncCallContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_funcCall);
		try {
			setState(165);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(160); match(ID);
				setState(161); argList();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(162); match(ID);
				setState(163); match(T__2);
				setState(164); simpleArg();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleArgContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public SimpleArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterSimpleArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitSimpleArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitSimpleArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleArgContext simpleArg() throws RecognitionException {
		SimpleArgContext _localctx = new SimpleArgContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_simpleArg);
		try {
			setState(169);
			switch (_input.LA(1)) {
			case INT:
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(167); number();
				}
				break;
			case StringLiteral:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(168); literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitArgList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitArgList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgListContext argList() throws RecognitionException {
		ArgListContext _localctx = new ArgListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_argList);
		int _la;
		try {
			setState(184);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(171); match(T__15);
				setState(172); match(T__6);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(173); match(T__15);
				setState(174); expr(0);
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__16) {
					{
					{
					setState(175); match(T__16);
					setState(176); expr(0);
					}
					}
					setState(181);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(182); match(T__6);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BrackedBoolExprContext extends ParserRuleContext {
		public BoolexprContext boolexpr() {
			return getRuleContext(BoolexprContext.class,0);
		}
		public BrackedBoolExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_brackedBoolExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterBrackedBoolExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitBrackedBoolExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitBrackedBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BrackedBoolExprContext brackedBoolExpr() throws RecognitionException {
		BrackedBoolExprContext _localctx = new BrackedBoolExprContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_brackedBoolExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186); match(T__15);
			setState(187); boolexpr(0);
			setState(188); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupContext extends ParserRuleContext {
		public GroupInstructionContext groupInstruction(int i) {
			return getRuleContext(GroupInstructionContext.class,i);
		}
		public List<GroupInstructionContext> groupInstruction() {
			return getRuleContexts(GroupInstructionContext.class);
		}
		public TerminalNode GroovyCode() { return getToken(FilterLanguageParser.GroovyCode, 0); }
		public GroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_group; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupContext group() throws RecognitionException {
		GroupContext _localctx = new GroupContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_group);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			switch (_input.LA(1)) {
			case GroovyCode:
				{
				setState(190); match(GroovyCode);
				}
				break;
			case T__17:
			case T__15:
			case T__11:
			case T__4:
			case ID:
				{
				setState(191); groupInstruction();
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__13) {
					{
					{
					setState(192); match(T__13);
					setState(193); groupInstruction();
					}
					}
					setState(198);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupInstructionContext extends ParserRuleContext {
		public HavingClauseContext havingClause() {
			return getRuleContext(HavingClauseContext.class,0);
		}
		public GroupExprListContext groupExprList() {
			return getRuleContext(GroupExprListContext.class,0);
		}
		public GroupInstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupInstruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterGroupInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitGroupInstruction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitGroupInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupInstructionContext groupInstruction() throws RecognitionException {
		GroupInstructionContext _localctx = new GroupInstructionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_groupInstruction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			_la = _input.LA(1);
			if (_la==T__17) {
				{
				setState(201); match(T__17);
				}
			}

			setState(208);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(204); match(T__4);
				setState(206);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(205); match(T__11);
					}
					break;
				}
				}
			}

			setState(211);
			_la = _input.LA(1);
			if (_la==T__11) {
				{
				setState(210); match(T__11);
				}
			}

			setState(213); groupExprList();
			setState(215);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(214); havingClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupExprListContext extends ParserRuleContext {
		public List<GroupExprPartContext> groupExprPart() {
			return getRuleContexts(GroupExprPartContext.class);
		}
		public GroupExprPartContext groupExprPart(int i) {
			return getRuleContext(GroupExprPartContext.class,i);
		}
		public GroupExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupExprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterGroupExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitGroupExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitGroupExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupExprListContext groupExprList() throws RecognitionException {
		GroupExprListContext _localctx = new GroupExprListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_groupExprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217); groupExprPart();
			setState(222);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__16 || _la==T__3) {
				{
				{
				setState(218);
				_la = _input.LA(1);
				if ( !(_la==T__16 || _la==T__3) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(219); groupExprPart();
				}
				}
				setState(224);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HavingClauseContext extends ParserRuleContext {
		public BoolexprContext boolexpr() {
			return getRuleContext(BoolexprContext.class,0);
		}
		public HavingClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_havingClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterHavingClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitHavingClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitHavingClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HavingClauseContext havingClause() throws RecognitionException {
		HavingClauseContext _localctx = new HavingClauseContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_havingClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225); match(T__1);
			setState(226); boolexpr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupExprPartContext extends ParserRuleContext {
		public GroupExprContext groupExpr() {
			return getRuleContext(GroupExprContext.class,0);
		}
		public QualifierContext qualifier() {
			return getRuleContext(QualifierContext.class,0);
		}
		public GroupExprPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupExprPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterGroupExprPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitGroupExprPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitGroupExprPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupExprPartContext groupExprPart() throws RecognitionException {
		GroupExprPartContext _localctx = new GroupExprPartContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_groupExprPart);
		try {
			setState(233);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(228); qualifier();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 2);
				{
				setState(229); match(T__15);
				setState(230); groupExpr();
				setState(231); match(T__6);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupExprContext extends ParserRuleContext {
		public GroupTermContext groupTerm() {
			return getRuleContext(GroupTermContext.class,0);
		}
		public IfStmtContext ifStmt() {
			return getRuleContext(IfStmtContext.class,0);
		}
		public GroupExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterGroupExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitGroupExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitGroupExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupExprContext groupExpr() throws RecognitionException {
		GroupExprContext _localctx = new GroupExprContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_groupExpr);
		try {
			setState(237);
			switch (_input.LA(1)) {
			case T__14:
				enterOuterAlt(_localctx, 1);
				{
				setState(235); ifStmt();
				}
				break;
			case T__15:
			case T__8:
			case DateLiteral:
			case StringLiteral:
			case INT:
			case DOUBLE:
			case RegexLiteral:
			case NOTHING:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(236); groupTerm();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupTermContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public GroupTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterGroupTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitGroupTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitGroupTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupTermContext groupTerm() throws RecognitionException {
		GroupTermContext _localctx = new GroupTermContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_groupTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239); expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfStmtContext extends ParserRuleContext {
		public BoolexprContext boolexpr() {
			return getRuleContext(BoolexprContext.class,0);
		}
		public ElseStmtContext elseStmt() {
			return getRuleContext(ElseStmtContext.class,0);
		}
		public ElseIfStmtContext elseIfStmt(int i) {
			return getRuleContext(ElseIfStmtContext.class,i);
		}
		public List<ElseIfStmtContext> elseIfStmt() {
			return getRuleContexts(ElseIfStmtContext.class);
		}
		public ThenStmtContext thenStmt() {
			return getRuleContext(ThenStmtContext.class,0);
		}
		public IfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStmtContext ifStmt() throws RecognitionException {
		IfStmtContext _localctx = new IfStmtContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_ifStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241); match(T__14);
			setState(242); boolexpr(0);
			setState(243); thenStmt();
			setState(247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(244); elseIfStmt();
				}
				}
				setState(249);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(251);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(250); elseStmt();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ThenStmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ThenStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_thenStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterThenStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitThenStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitThenStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThenStmtContext thenStmt() throws RecognitionException {
		ThenStmtContext _localctx = new ThenStmtContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_thenStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253); match(T__12);
			setState(254); expr(0);
			setState(255); match(T__9);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseIfStmtContext extends ParserRuleContext {
		public BoolexprContext boolexpr() {
			return getRuleContext(BoolexprContext.class,0);
		}
		public ThenStmtContext thenStmt() {
			return getRuleContext(ThenStmtContext.class,0);
		}
		public ElseIfStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterElseIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitElseIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitElseIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStmtContext elseIfStmt() throws RecognitionException {
		ElseIfStmtContext _localctx = new ElseIfStmtContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_elseIfStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257); match(T__7);
			setState(258); boolexpr(0);
			setState(259); thenStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseStmtContext extends ParserRuleContext {
		public ThenStmtContext thenStmt() {
			return getRuleContext(ThenStmtContext.class,0);
		}
		public ElseStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).enterElseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FilterLanguageListener ) ((FilterLanguageListener)listener).exitElseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FilterLanguageVisitor ) return ((FilterLanguageVisitor<? extends T>)visitor).visitElseStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStmtContext elseStmt() throws RecognitionException {
		ElseStmtContext _localctx = new ElseStmtContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_elseStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261); match(T__10);
			setState(262); thenStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1: return boolexpr_sempred((BoolexprContext)_localctx, predIndex);

		case 5: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2: return precpred(_ctx, 5);

		case 3: return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean boolexpr_sempred(BoolexprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 4);

		case 1: return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\39\u010b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\5\2@\n"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3L\n\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\7\3T\n\3\f\3\16\3W\13\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7m\n\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\7\7u\n\7\f\7\16\7x\13\7\3\b\3\b\5\b|\n\b\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\13\3\13\3\f\3\f\3\f\7\f\u0089\n\f\f\f\16\f\u008c\13\f\3\r\3\r\3\r\7"+
		"\r\u0091\n\r\f\r\16\r\u0094\13\r\3\16\3\16\3\16\7\16\u0099\n\16\f\16\16"+
		"\16\u009c\13\16\3\17\3\17\3\17\5\17\u00a1\n\17\3\20\3\20\3\20\3\20\3\20"+
		"\5\20\u00a8\n\20\3\21\3\21\5\21\u00ac\n\21\3\22\3\22\3\22\3\22\3\22\3"+
		"\22\7\22\u00b4\n\22\f\22\16\22\u00b7\13\22\3\22\3\22\5\22\u00bb\n\22\3"+
		"\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\7\24\u00c5\n\24\f\24\16\24\u00c8"+
		"\13\24\5\24\u00ca\n\24\3\25\5\25\u00cd\n\25\3\25\3\25\5\25\u00d1\n\25"+
		"\5\25\u00d3\n\25\3\25\5\25\u00d6\n\25\3\25\3\25\5\25\u00da\n\25\3\26\3"+
		"\26\3\26\7\26\u00df\n\26\f\26\16\26\u00e2\13\26\3\27\3\27\3\27\3\30\3"+
		"\30\3\30\3\30\3\30\5\30\u00ec\n\30\3\31\3\31\5\31\u00f0\n\31\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\7\33\u00f8\n\33\f\33\16\33\u00fb\13\33\3\33\5\33"+
		"\u00fe\n\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36"+
		"\2\4\4\f\37\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66"+
		"8:\2\t\4\2\36\36%\61\3\2!\"\3\2#$\3\2\31\32\3\2\668\4\2\27\2799\4\2\5"+
		"\5\22\22\u0117\2?\3\2\2\2\4K\3\2\2\2\6X\3\2\2\2\bZ\3\2\2\2\n]\3\2\2\2"+
		"\fl\3\2\2\2\16y\3\2\2\2\20}\3\2\2\2\22\177\3\2\2\2\24\u0083\3\2\2\2\26"+
		"\u0085\3\2\2\2\30\u008d\3\2\2\2\32\u0095\3\2\2\2\34\u009d\3\2\2\2\36\u00a7"+
		"\3\2\2\2 \u00ab\3\2\2\2\"\u00ba\3\2\2\2$\u00bc\3\2\2\2&\u00c9\3\2\2\2"+
		"(\u00cc\3\2\2\2*\u00db\3\2\2\2,\u00e3\3\2\2\2.\u00eb\3\2\2\2\60\u00ef"+
		"\3\2\2\2\62\u00f1\3\2\2\2\64\u00f3\3\2\2\2\66\u00ff\3\2\2\28\u0103\3\2"+
		"\2\2:\u0107\3\2\2\2<@\7\30\2\2=@\5\6\4\2>@\5\4\3\2?<\3\2\2\2?=\3\2\2\2"+
		"?>\3\2\2\2@\3\3\2\2\2AB\b\3\1\2BL\5$\23\2CD\5\f\7\2DE\t\2\2\2EF\5\f\7"+
		"\2FL\3\2\2\2GL\5\f\7\2HL\5\n\6\2IL\7 \2\2JL\5\b\5\2KA\3\2\2\2KC\3\2\2"+
		"\2KG\3\2\2\2KH\3\2\2\2KI\3\2\2\2KJ\3\2\2\2LU\3\2\2\2MN\f\6\2\2NO\7\62"+
		"\2\2OT\5\4\3\7PQ\f\5\2\2QR\7\63\2\2RT\5\4\3\6SM\3\2\2\2SP\3\2\2\2TW\3"+
		"\2\2\2US\3\2\2\2UV\3\2\2\2V\5\3\2\2\2WU\3\2\2\2XY\5\24\13\2Y\7\3\2\2\2"+
		"Z[\5\32\16\2[\\\7 \2\2\\\t\3\2\2\2]^\7\64\2\2^_\5$\23\2_\13\3\2\2\2`a"+
		"\b\7\1\2am\5\22\n\2bm\5\36\20\2cm\5\34\17\2dm\5\16\b\2em\7\26\2\2fm\7"+
		"\27\2\2gm\7\65\2\2hm\5\32\16\2im\5\26\f\2jm\5\30\r\2km\7\37\2\2l`\3\2"+
		"\2\2lb\3\2\2\2lc\3\2\2\2ld\3\2\2\2le\3\2\2\2lf\3\2\2\2lg\3\2\2\2lh\3\2"+
		"\2\2li\3\2\2\2lj\3\2\2\2lk\3\2\2\2mv\3\2\2\2no\f\7\2\2op\t\3\2\2pu\5\f"+
		"\7\bqr\f\6\2\2rs\t\4\2\2su\5\f\7\7tn\3\2\2\2tq\3\2\2\2ux\3\2\2\2vt\3\2"+
		"\2\2vw\3\2\2\2w\r\3\2\2\2xv\3\2\2\2y{\t\5\2\2z|\5\20\t\2{z\3\2\2\2{|\3"+
		"\2\2\2|\17\3\2\2\2}~\t\6\2\2~\21\3\2\2\2\177\u0080\7\6\2\2\u0080\u0081"+
		"\5\f\7\2\u0081\u0082\7\17\2\2\u0082\23\3\2\2\2\u0083\u0084\t\7\2\2\u0084"+
		"\25\3\2\2\2\u0085\u008a\5\24\13\2\u0086\u0087\7\3\2\2\u0087\u0089\5\24"+
		"\13\2\u0088\u0086\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a"+
		"\u008b\3\2\2\2\u008b\27\3\2\2\2\u008c\u008a\3\2\2\2\u008d\u0092\5\24\13"+
		"\2\u008e\u008f\7\25\2\2\u008f\u0091\5\24\13\2\u0090\u008e\3\2\2\2\u0091"+
		"\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\31\3\2\2"+
		"\2\u0094\u0092\3\2\2\2\u0095\u009a\79\2\2\u0096\u0097\7\20\2\2\u0097\u0099"+
		"\79\2\2\u0098\u0096\3\2\2\2\u0099\u009c\3\2\2\2\u009a\u0098\3\2\2\2\u009a"+
		"\u009b\3\2\2\2\u009b\33\3\2\2\2\u009c\u009a\3\2\2\2\u009d\u009e\7\r\2"+
		"\2\u009e\u00a0\5\24\13\2\u009f\u00a1\5\"\22\2\u00a0\u009f\3\2\2\2\u00a0"+
		"\u00a1\3\2\2\2\u00a1\35\3\2\2\2\u00a2\u00a3\79\2\2\u00a3\u00a8\5\"\22"+
		"\2\u00a4\u00a5\79\2\2\u00a5\u00a6\7\23\2\2\u00a6\u00a8\5 \21\2\u00a7\u00a2"+
		"\3\2\2\2\u00a7\u00a4\3\2\2\2\u00a8\37\3\2\2\2\u00a9\u00ac\5\16\b\2\u00aa"+
		"\u00ac\5\24\13\2\u00ab\u00a9\3\2\2\2\u00ab\u00aa\3\2\2\2\u00ac!\3\2\2"+
		"\2\u00ad\u00ae\7\6\2\2\u00ae\u00bb\7\17\2\2\u00af\u00b0\7\6\2\2\u00b0"+
		"\u00b5\5\f\7\2\u00b1\u00b2\7\5\2\2\u00b2\u00b4\5\f\7\2\u00b3\u00b1\3\2"+
		"\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6"+
		"\u00b8\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\7\17\2\2\u00b9\u00bb\3"+
		"\2\2\2\u00ba\u00ad\3\2\2\2\u00ba\u00af\3\2\2\2\u00bb#\3\2\2\2\u00bc\u00bd"+
		"\7\6\2\2\u00bd\u00be\5\4\3\2\u00be\u00bf\7\17\2\2\u00bf%\3\2\2\2\u00c0"+
		"\u00ca\7\30\2\2\u00c1\u00c6\5(\25\2\u00c2\u00c3\7\b\2\2\u00c3\u00c5\5"+
		"(\25\2\u00c4\u00c2\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6"+
		"\u00c7\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c9\u00c0\3\2"+
		"\2\2\u00c9\u00c1\3\2\2\2\u00ca\'\3\2\2\2\u00cb\u00cd\7\4\2\2\u00cc\u00cb"+
		"\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00d2\3\2\2\2\u00ce\u00d0\7\21\2\2"+
		"\u00cf\u00d1\7\n\2\2\u00d0\u00cf\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00d3"+
		"\3\2\2\2\u00d2\u00ce\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d5\3\2\2\2\u00d4"+
		"\u00d6\7\n\2\2\u00d5\u00d4\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d7\3\2"+
		"\2\2\u00d7\u00d9\5*\26\2\u00d8\u00da\5,\27\2\u00d9\u00d8\3\2\2\2\u00d9"+
		"\u00da\3\2\2\2\u00da)\3\2\2\2\u00db\u00e0\5.\30\2\u00dc\u00dd\t\b\2\2"+
		"\u00dd\u00df\5.\30\2\u00de\u00dc\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0\u00de"+
		"\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1+\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e3"+
		"\u00e4\7\24\2\2\u00e4\u00e5\5\4\3\2\u00e5-\3\2\2\2\u00e6\u00ec\5\32\16"+
		"\2\u00e7\u00e8\7\6\2\2\u00e8\u00e9\5\60\31\2\u00e9\u00ea\7\17\2\2\u00ea"+
		"\u00ec\3\2\2\2\u00eb\u00e6\3\2\2\2\u00eb\u00e7\3\2\2\2\u00ec/\3\2\2\2"+
		"\u00ed\u00f0\5\64\33\2\u00ee\u00f0\5\62\32\2\u00ef\u00ed\3\2\2\2\u00ef"+
		"\u00ee\3\2\2\2\u00f0\61\3\2\2\2\u00f1\u00f2\5\f\7\2\u00f2\63\3\2\2\2\u00f3"+
		"\u00f4\7\7\2\2\u00f4\u00f5\5\4\3\2\u00f5\u00f9\5\66\34\2\u00f6\u00f8\5"+
		"8\35\2\u00f7\u00f6\3\2\2\2\u00f8\u00fb\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9"+
		"\u00fa\3\2\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fc\u00fe\5:"+
		"\36\2\u00fd\u00fc\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\65\3\2\2\2\u00ff\u0100"+
		"\7\t\2\2\u0100\u0101\5\f\7\2\u0101\u0102\7\f\2\2\u0102\67\3\2\2\2\u0103"+
		"\u0104\7\16\2\2\u0104\u0105\5\4\3\2\u0105\u0106\5\66\34\2\u01069\3\2\2"+
		"\2\u0107\u0108\7\13\2\2\u0108\u0109\5\66\34\2\u0109;\3\2\2\2\36?KSUlt"+
		"v{\u008a\u0092\u009a\u00a0\u00a7\u00ab\u00b5\u00ba\u00c6\u00c9\u00cc\u00d0"+
		"\u00d2\u00d5\u00d9\u00e0\u00eb\u00ef\u00f9\u00fd";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}