// Generated from java-escape by ANTLR 4.11.1
package com.iodesystems.db.query;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class DataSetSearchParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ESCAPED_CHAR=1, ESCAPE=2, ANY=3, STRING=4, TARGET_SEPARATOR=5, TERM_OR=6, 
		TERM_GROUP_START=7, TERM_GROUP_END=8, WS=9, ESCAPED=10;
	public static final int
		RULE_search = 0, RULE_simpleTerm = 1, RULE_andTerm = 2, RULE_orTerm = 3, 
		RULE_term = 4, RULE_termTarget = 5, RULE_termValueGroup = 6, RULE_simpleValue = 7, 
		RULE_termValue = 8, RULE_andValue = 9, RULE_orValue = 10, RULE_unprotectedOrValue = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"search", "simpleTerm", "andTerm", "orTerm", "term", "termTarget", "termValueGroup", 
			"simpleValue", "termValue", "andValue", "orValue", "unprotectedOrValue"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'\\'", null, null, "':'", "','", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ESCAPED_CHAR", "ESCAPE", "ANY", "STRING", "TARGET_SEPARATOR", 
			"TERM_OR", "TERM_GROUP_START", "TERM_GROUP_END", "WS", "ESCAPED"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DataSetSearchParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SearchContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(DataSetSearchParser.EOF, 0); }
		public SimpleTermContext simpleTerm() {
			return getRuleContext(SimpleTermContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public TerminalNode TERM_OR() { return getToken(DataSetSearchParser.TERM_OR, 0); }
		public List<AndTermContext> andTerm() {
			return getRuleContexts(AndTermContext.class);
		}
		public AndTermContext andTerm(int i) {
			return getRuleContext(AndTermContext.class,i);
		}
		public List<OrTermContext> orTerm() {
			return getRuleContexts(OrTermContext.class);
		}
		public OrTermContext orTerm(int i) {
			return getRuleContext(OrTermContext.class,i);
		}
		public SearchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_search; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterSearch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitSearch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitSearch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchContext search() throws RecognitionException {
		SearchContext _localctx = new SearchContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_search);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(24);
				simpleTerm();
				setState(29);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						setState(27);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
						case 1:
							{
							setState(25);
							andTerm();
							}
							break;
						case 2:
							{
							setState(26);
							orTerm();
							}
							break;
						}
						} 
					}
					setState(31);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				}
				break;
			}
			setState(37);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(34);
					match(WS);
					}
					} 
				}
				setState(39);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TERM_OR) {
				{
				setState(40);
				match(TERM_OR);
				}
			}

			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(43);
				match(WS);
				}
				}
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(49);
			match(EOF);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleTermContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public SimpleTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterSimpleTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitSimpleTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitSimpleTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTermContext simpleTerm() throws RecognitionException {
		SimpleTermContext _localctx = new SimpleTermContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_simpleTerm);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(51);
				match(WS);
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(57);
			term();
			setState(61);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(58);
					match(WS);
					}
					} 
				}
				setState(63);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AndTermContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public AndTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterAndTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitAndTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitAndTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndTermContext andTerm() throws RecognitionException {
		AndTermContext _localctx = new AndTermContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_andTerm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(64);
				match(WS);
				}
				}
				setState(67); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(69);
			term();
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

	@SuppressWarnings("CheckReturnValue")
	public static class OrTermContext extends ParserRuleContext {
		public TerminalNode TERM_OR() { return getToken(DataSetSearchParser.TERM_OR, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public OrTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterOrTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitOrTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitOrTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrTermContext orTerm() throws RecognitionException {
		OrTermContext _localctx = new OrTermContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_orTerm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(71);
				match(WS);
				}
				}
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77);
			match(TERM_OR);
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(78);
				match(WS);
				}
				}
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(84);
			term();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public TermValueGroupContext termValueGroup() {
			return getRuleContext(TermValueGroupContext.class,0);
		}
		public TermTargetContext termTarget() {
			return getRuleContext(TermTargetContext.class,0);
		}
		public TerminalNode TARGET_SEPARATOR() { return getToken(DataSetSearchParser.TARGET_SEPARATOR, 0); }
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(86);
				termTarget();
				setState(87);
				match(TARGET_SEPARATOR);
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(88);
					match(WS);
					}
					}
					setState(93);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
			setState(96);
			termValueGroup();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TermTargetContext extends ParserRuleContext {
		public TerminalNode ANY() { return getToken(DataSetSearchParser.ANY, 0); }
		public TerminalNode STRING() { return getToken(DataSetSearchParser.STRING, 0); }
		public TermTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termTarget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterTermTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitTermTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitTermTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermTargetContext termTarget() throws RecognitionException {
		TermTargetContext _localctx = new TermTargetContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_termTarget);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			_la = _input.LA(1);
			if ( !(_la==ANY || _la==STRING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TermValueGroupContext extends ParserRuleContext {
		public TerminalNode TERM_GROUP_START() { return getToken(DataSetSearchParser.TERM_GROUP_START, 0); }
		public TerminalNode TERM_GROUP_END() { return getToken(DataSetSearchParser.TERM_GROUP_END, 0); }
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public SimpleValueContext simpleValue() {
			return getRuleContext(SimpleValueContext.class,0);
		}
		public List<AndValueContext> andValue() {
			return getRuleContexts(AndValueContext.class);
		}
		public AndValueContext andValue(int i) {
			return getRuleContext(AndValueContext.class,i);
		}
		public List<OrValueContext> orValue() {
			return getRuleContexts(OrValueContext.class);
		}
		public OrValueContext orValue(int i) {
			return getRuleContext(OrValueContext.class,i);
		}
		public List<UnprotectedOrValueContext> unprotectedOrValue() {
			return getRuleContexts(UnprotectedOrValueContext.class);
		}
		public UnprotectedOrValueContext unprotectedOrValue(int i) {
			return getRuleContext(UnprotectedOrValueContext.class,i);
		}
		public TermValueGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termValueGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterTermValueGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitTermValueGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitTermValueGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermValueGroupContext termValueGroup() throws RecognitionException {
		TermValueGroupContext _localctx = new TermValueGroupContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_termValueGroup);
		int _la;
		try {
			int _alt;
			setState(137);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TERM_GROUP_START:
				enterOuterAlt(_localctx, 1);
				{
				setState(100);
				match(TERM_GROUP_START);
				setState(104);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(101);
					match(WS);
					}
					}
					setState(106);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 26L) != 0) {
					{
					setState(107);
					simpleValue();
					setState(111);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(108);
							match(WS);
							}
							} 
						}
						setState(113);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
					}
					setState(118);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							setState(116);
							_errHandler.sync(this);
							switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
							case 1:
								{
								setState(114);
								andValue();
								}
								break;
							case 2:
								{
								setState(115);
								orValue();
								}
								break;
							}
							} 
						}
						setState(120);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
					}
					setState(124);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==WS) {
						{
						{
						setState(121);
						match(WS);
						}
						}
						setState(126);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(129);
				match(TERM_GROUP_END);
				}
				break;
			case ESCAPED_CHAR:
			case ANY:
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(130);
				simpleValue();
				setState(134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(131);
						unprotectedOrValue();
						}
						} 
					}
					setState(136);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
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

	@SuppressWarnings("CheckReturnValue")
	public static class SimpleValueContext extends ParserRuleContext {
		public TermValueContext termValue() {
			return getRuleContext(TermValueContext.class,0);
		}
		public SimpleValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterSimpleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitSimpleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitSimpleValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleValueContext simpleValue() throws RecognitionException {
		SimpleValueContext _localctx = new SimpleValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_simpleValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			termValue();
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

	@SuppressWarnings("CheckReturnValue")
	public static class TermValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DataSetSearchParser.STRING, 0); }
		public TerminalNode ANY() { return getToken(DataSetSearchParser.ANY, 0); }
		public TerminalNode ESCAPED_CHAR() { return getToken(DataSetSearchParser.ESCAPED_CHAR, 0); }
		public TermValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterTermValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitTermValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitTermValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermValueContext termValue() throws RecognitionException {
		TermValueContext _localctx = new TermValueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_termValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 26L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	@SuppressWarnings("CheckReturnValue")
	public static class AndValueContext extends ParserRuleContext {
		public TermValueContext termValue() {
			return getRuleContext(TermValueContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public AndValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterAndValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitAndValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitAndValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndValueContext andValue() throws RecognitionException {
		AndValueContext _localctx = new AndValueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_andValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(143);
				match(WS);
				}
				}
				setState(146); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(148);
			termValue();
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

	@SuppressWarnings("CheckReturnValue")
	public static class OrValueContext extends ParserRuleContext {
		public TerminalNode TERM_OR() { return getToken(DataSetSearchParser.TERM_OR, 0); }
		public TermValueContext termValue() {
			return getRuleContext(TermValueContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(DataSetSearchParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DataSetSearchParser.WS, i);
		}
		public OrValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterOrValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitOrValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitOrValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrValueContext orValue() throws RecognitionException {
		OrValueContext _localctx = new OrValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_orValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(150);
				match(WS);
				}
				}
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(156);
			match(TERM_OR);
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(157);
				match(WS);
				}
				}
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(163);
			termValue();
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

	@SuppressWarnings("CheckReturnValue")
	public static class UnprotectedOrValueContext extends ParserRuleContext {
		public TerminalNode TERM_OR() { return getToken(DataSetSearchParser.TERM_OR, 0); }
		public TermValueContext termValue() {
			return getRuleContext(TermValueContext.class,0);
		}
		public UnprotectedOrValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unprotectedOrValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).enterUnprotectedOrValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchParserListener ) ((DataSetSearchParserListener)listener).exitUnprotectedOrValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchParserVisitor ) return ((DataSetSearchParserVisitor<? extends T>)visitor).visitUnprotectedOrValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnprotectedOrValueContext unprotectedOrValue() throws RecognitionException {
		UnprotectedOrValueContext _localctx = new UnprotectedOrValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_unprotectedOrValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			match(TERM_OR);
			setState(166);
			termValue();
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

	public static final String _serializedATN =
		"\u0004\u0001\n\u00a9\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u001c\b\u0000\n\u0000\f\u0000"+
		"\u001f\t\u0000\u0003\u0000!\b\u0000\u0001\u0000\u0005\u0000$\b\u0000\n"+
		"\u0000\f\u0000\'\t\u0000\u0001\u0000\u0003\u0000*\b\u0000\u0001\u0000"+
		"\u0005\u0000-\b\u0000\n\u0000\f\u00000\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0005\u00015\b\u0001\n\u0001\f\u00018\t\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001<\b\u0001\n\u0001\f\u0001?\t\u0001\u0001\u0002"+
		"\u0004\u0002B\b\u0002\u000b\u0002\f\u0002C\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0005\u0003I\b\u0003\n\u0003\f\u0003L\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0005\u0003P\b\u0003\n\u0003\f\u0003S\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004Z\b\u0004\n\u0004"+
		"\f\u0004]\t\u0004\u0003\u0004_\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0005\u0006g\b\u0006\n\u0006\f\u0006"+
		"j\t\u0006\u0001\u0006\u0001\u0006\u0005\u0006n\b\u0006\n\u0006\f\u0006"+
		"q\t\u0006\u0001\u0006\u0001\u0006\u0005\u0006u\b\u0006\n\u0006\f\u0006"+
		"x\t\u0006\u0001\u0006\u0005\u0006{\b\u0006\n\u0006\f\u0006~\t\u0006\u0003"+
		"\u0006\u0080\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u0085"+
		"\b\u0006\n\u0006\f\u0006\u0088\t\u0006\u0003\u0006\u008a\b\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0004\t\u0091\b\t\u000b\t\f"+
		"\t\u0092\u0001\t\u0001\t\u0001\n\u0005\n\u0098\b\n\n\n\f\n\u009b\t\n\u0001"+
		"\n\u0001\n\u0005\n\u009f\b\n\n\n\f\n\u00a2\t\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0000\u0000\f\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0000\u0002\u0001\u0000\u0003\u0004"+
		"\u0002\u0000\u0001\u0001\u0003\u0004\u00b4\u0000 \u0001\u0000\u0000\u0000"+
		"\u00026\u0001\u0000\u0000\u0000\u0004A\u0001\u0000\u0000\u0000\u0006J"+
		"\u0001\u0000\u0000\u0000\b^\u0001\u0000\u0000\u0000\nb\u0001\u0000\u0000"+
		"\u0000\f\u0089\u0001\u0000\u0000\u0000\u000e\u008b\u0001\u0000\u0000\u0000"+
		"\u0010\u008d\u0001\u0000\u0000\u0000\u0012\u0090\u0001\u0000\u0000\u0000"+
		"\u0014\u0099\u0001\u0000\u0000\u0000\u0016\u00a5\u0001\u0000\u0000\u0000"+
		"\u0018\u001d\u0003\u0002\u0001\u0000\u0019\u001c\u0003\u0004\u0002\u0000"+
		"\u001a\u001c\u0003\u0006\u0003\u0000\u001b\u0019\u0001\u0000\u0000\u0000"+
		"\u001b\u001a\u0001\u0000\u0000\u0000\u001c\u001f\u0001\u0000\u0000\u0000"+
		"\u001d\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000"+
		"\u001e!\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000 \u0018"+
		"\u0001\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000!%\u0001\u0000\u0000"+
		"\u0000\"$\u0005\t\u0000\u0000#\"\u0001\u0000\u0000\u0000$\'\u0001\u0000"+
		"\u0000\u0000%#\u0001\u0000\u0000\u0000%&\u0001\u0000\u0000\u0000&)\u0001"+
		"\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000(*\u0005\u0006\u0000\u0000"+
		")(\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*.\u0001\u0000\u0000"+
		"\u0000+-\u0005\t\u0000\u0000,+\u0001\u0000\u0000\u0000-0\u0001\u0000\u0000"+
		"\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/1\u0001\u0000"+
		"\u0000\u00000.\u0001\u0000\u0000\u000012\u0005\u0000\u0000\u00012\u0001"+
		"\u0001\u0000\u0000\u000035\u0005\t\u0000\u000043\u0001\u0000\u0000\u0000"+
		"58\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u000067\u0001\u0000\u0000"+
		"\u000079\u0001\u0000\u0000\u000086\u0001\u0000\u0000\u00009=\u0003\b\u0004"+
		"\u0000:<\u0005\t\u0000\u0000;:\u0001\u0000\u0000\u0000<?\u0001\u0000\u0000"+
		"\u0000=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>\u0003\u0001"+
		"\u0000\u0000\u0000?=\u0001\u0000\u0000\u0000@B\u0005\t\u0000\u0000A@\u0001"+
		"\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000"+
		"CD\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000EF\u0003\b\u0004\u0000"+
		"F\u0005\u0001\u0000\u0000\u0000GI\u0005\t\u0000\u0000HG\u0001\u0000\u0000"+
		"\u0000IL\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000JK\u0001\u0000"+
		"\u0000\u0000KM\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000\u0000MQ\u0005"+
		"\u0006\u0000\u0000NP\u0005\t\u0000\u0000ON\u0001\u0000\u0000\u0000PS\u0001"+
		"\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000"+
		"RT\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000TU\u0003\b\u0004\u0000"+
		"U\u0007\u0001\u0000\u0000\u0000VW\u0003\n\u0005\u0000W[\u0005\u0005\u0000"+
		"\u0000XZ\u0005\t\u0000\u0000YX\u0001\u0000\u0000\u0000Z]\u0001\u0000\u0000"+
		"\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001\u0000\u0000\u0000\\_\u0001\u0000"+
		"\u0000\u0000][\u0001\u0000\u0000\u0000^V\u0001\u0000\u0000\u0000^_\u0001"+
		"\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`a\u0003\f\u0006\u0000a\t"+
		"\u0001\u0000\u0000\u0000bc\u0007\u0000\u0000\u0000c\u000b\u0001\u0000"+
		"\u0000\u0000dh\u0005\u0007\u0000\u0000eg\u0005\t\u0000\u0000fe\u0001\u0000"+
		"\u0000\u0000gj\u0001\u0000\u0000\u0000hf\u0001\u0000\u0000\u0000hi\u0001"+
		"\u0000\u0000\u0000i\u007f\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000"+
		"\u0000ko\u0003\u000e\u0007\u0000ln\u0005\t\u0000\u0000ml\u0001\u0000\u0000"+
		"\u0000nq\u0001\u0000\u0000\u0000om\u0001\u0000\u0000\u0000op\u0001\u0000"+
		"\u0000\u0000pv\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000ru\u0003"+
		"\u0012\t\u0000su\u0003\u0014\n\u0000tr\u0001\u0000\u0000\u0000ts\u0001"+
		"\u0000\u0000\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000"+
		"vw\u0001\u0000\u0000\u0000w|\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000"+
		"\u0000y{\u0005\t\u0000\u0000zy\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000"+
		"\u0000|z\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\u0080\u0001"+
		"\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000\u007fk\u0001\u0000\u0000"+
		"\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000"+
		"\u0000\u0081\u008a\u0005\b\u0000\u0000\u0082\u0086\u0003\u000e\u0007\u0000"+
		"\u0083\u0085\u0003\u0016\u000b\u0000\u0084\u0083\u0001\u0000\u0000\u0000"+
		"\u0085\u0088\u0001\u0000\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000"+
		"\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u008a\u0001\u0000\u0000\u0000"+
		"\u0088\u0086\u0001\u0000\u0000\u0000\u0089d\u0001\u0000\u0000\u0000\u0089"+
		"\u0082\u0001\u0000\u0000\u0000\u008a\r\u0001\u0000\u0000\u0000\u008b\u008c"+
		"\u0003\u0010\b\u0000\u008c\u000f\u0001\u0000\u0000\u0000\u008d\u008e\u0007"+
		"\u0001\u0000\u0000\u008e\u0011\u0001\u0000\u0000\u0000\u008f\u0091\u0005"+
		"\t\u0000\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0091\u0092\u0001\u0000"+
		"\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000"+
		"\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0094\u0095\u0003\u0010"+
		"\b\u0000\u0095\u0013\u0001\u0000\u0000\u0000\u0096\u0098\u0005\t\u0000"+
		"\u0000\u0097\u0096\u0001\u0000\u0000\u0000\u0098\u009b\u0001\u0000\u0000"+
		"\u0000\u0099\u0097\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000"+
		"\u0000\u009a\u009c\u0001\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000"+
		"\u0000\u009c\u00a0\u0005\u0006\u0000\u0000\u009d\u009f\u0005\t\u0000\u0000"+
		"\u009e\u009d\u0001\u0000\u0000\u0000\u009f\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a0\u009e\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000"+
		"\u00a1\u00a3\u0001\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a4\u0003\u0010\b\u0000\u00a4\u0015\u0001\u0000\u0000\u0000\u00a5"+
		"\u00a6\u0005\u0006\u0000\u0000\u00a6\u00a7\u0003\u0010\b\u0000\u00a7\u0017"+
		"\u0001\u0000\u0000\u0000\u0018\u001b\u001d %).6=CJQ[^hotv|\u007f\u0086"+
		"\u0089\u0092\u0099\u00a0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}