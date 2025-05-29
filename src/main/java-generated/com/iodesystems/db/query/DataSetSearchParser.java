// Generated from /Users/nthalk/local/src/iodesystems/dataset/src/main/antlr4/DataSetSearchParser.g4 by ANTLR 4.13.2
package com.iodesystems.db.query;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class DataSetSearchParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ESCAPED_CHAR=1, ESCAPE=2, NEGATE=3, ANY=4, STRING=5, TARGET_SEPARATOR=6, 
		TERM_OR=7, TERM_GROUP_START=8, TERM_GROUP_END=9, WS=10, ESCAPED=11;
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
			null, null, "'\\'", "'!'", null, null, "':'", "','", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ESCAPED_CHAR", "ESCAPE", "NEGATE", "ANY", "STRING", "TARGET_SEPARATOR", 
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
	public String getGrammarFileName() { return "DataSetSearchParser.g4"; }

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
	}

	public final SimpleTermContext simpleTerm() throws RecognitionException {
		SimpleTermContext _localctx = new SimpleTermContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_simpleTerm);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(51);
					match(WS);
					}
					} 
				}
				setState(56);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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
	}

	public final AndTermContext andTerm() throws RecognitionException {
		AndTermContext _localctx = new AndTermContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_andTerm);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(65); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(64);
					match(WS);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(67); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	}

	public final OrTermContext orTerm() throws RecognitionException {
		OrTermContext _localctx = new OrTermContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_orTerm);
		int _la;
		try {
			int _alt;
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
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(78);
					match(WS);
					}
					} 
				}
				setState(83);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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
		public TerminalNode NEGATE() { return getToken(DataSetSearchParser.NEGATE, 0); }
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
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_term);
		int _la;
		try {
			int _alt;
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
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(88);
						match(WS);
						}
						} 
					}
					setState(93);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				}
				}
				break;
			}
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(96);
				match(NEGATE);
				}
				break;
			}
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(99);
				match(WS);
				}
				}
				setState(104);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(105);
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
	}

	public final TermTargetContext termTarget() throws RecognitionException {
		TermTargetContext _localctx = new TermTargetContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_termTarget);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
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
	}

	public final TermValueGroupContext termValueGroup() throws RecognitionException {
		TermValueGroupContext _localctx = new TermValueGroupContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_termValueGroup);
		int _la;
		try {
			int _alt;
			setState(146);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TERM_GROUP_START:
				enterOuterAlt(_localctx, 1);
				{
				setState(109);
				match(TERM_GROUP_START);
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(110);
					match(WS);
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 58L) != 0)) {
					{
					setState(116);
					simpleValue();
					setState(120);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(117);
							match(WS);
							}
							} 
						}
						setState(122);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
					}
					setState(127);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							setState(125);
							_errHandler.sync(this);
							switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
							case 1:
								{
								setState(123);
								andValue();
								}
								break;
							case 2:
								{
								setState(124);
								orValue();
								}
								break;
							}
							} 
						}
						setState(129);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
					}
					setState(133);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==WS) {
						{
						{
						setState(130);
						match(WS);
						}
						}
						setState(135);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(138);
				match(TERM_GROUP_END);
				}
				break;
			case ESCAPED_CHAR:
			case NEGATE:
			case ANY:
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(139);
				simpleValue();
				setState(143);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(140);
						unprotectedOrValue();
						}
						} 
					}
					setState(145);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
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
	}

	public final SimpleValueContext simpleValue() throws RecognitionException {
		SimpleValueContext _localctx = new SimpleValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_simpleValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
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
	public static class TermValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DataSetSearchParser.STRING, 0); }
		public TerminalNode ANY() { return getToken(DataSetSearchParser.ANY, 0); }
		public TerminalNode ESCAPED_CHAR() { return getToken(DataSetSearchParser.ESCAPED_CHAR, 0); }
		public TerminalNode NEGATE() { return getToken(DataSetSearchParser.NEGATE, 0); }
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
	}

	public final TermValueContext termValue() throws RecognitionException {
		TermValueContext _localctx = new TermValueContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_termValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NEGATE) {
				{
				setState(150);
				match(NEGATE);
				}
			}

			setState(153);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 50L) != 0)) ) {
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
	}

	public final AndValueContext andValue() throws RecognitionException {
		AndValueContext _localctx = new AndValueContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_andValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(155);
				match(WS);
				}
				}
				setState(158); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(160);
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
	}

	public final OrValueContext orValue() throws RecognitionException {
		OrValueContext _localctx = new OrValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_orValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(162);
				match(WS);
				}
				}
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(168);
			match(TERM_OR);
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(169);
				match(WS);
				}
				}
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(175);
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
	}

	public final UnprotectedOrValueContext unprotectedOrValue() throws RecognitionException {
		UnprotectedOrValueContext _localctx = new UnprotectedOrValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_unprotectedOrValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(TERM_OR);
			setState(178);
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
		"\u0004\u0001\u000b\u00b5\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u001c\b\u0000\n\u0000"+
		"\f\u0000\u001f\t\u0000\u0003\u0000!\b\u0000\u0001\u0000\u0005\u0000$\b"+
		"\u0000\n\u0000\f\u0000\'\t\u0000\u0001\u0000\u0003\u0000*\b\u0000\u0001"+
		"\u0000\u0005\u0000-\b\u0000\n\u0000\f\u00000\t\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0005\u00015\b\u0001\n\u0001\f\u00018\t\u0001\u0001"+
		"\u0001\u0001\u0001\u0005\u0001<\b\u0001\n\u0001\f\u0001?\t\u0001\u0001"+
		"\u0002\u0004\u0002B\b\u0002\u000b\u0002\f\u0002C\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0005\u0003I\b\u0003\n\u0003\f\u0003L\t\u0003\u0001\u0003"+
		"\u0001\u0003\u0005\u0003P\b\u0003\n\u0003\f\u0003S\t\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004Z\b\u0004"+
		"\n\u0004\f\u0004]\t\u0004\u0003\u0004_\b\u0004\u0001\u0004\u0003\u0004"+
		"b\b\u0004\u0001\u0004\u0005\u0004e\b\u0004\n\u0004\f\u0004h\t\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0005"+
		"\u0006p\b\u0006\n\u0006\f\u0006s\t\u0006\u0001\u0006\u0001\u0006\u0005"+
		"\u0006w\b\u0006\n\u0006\f\u0006z\t\u0006\u0001\u0006\u0001\u0006\u0005"+
		"\u0006~\b\u0006\n\u0006\f\u0006\u0081\t\u0006\u0001\u0006\u0005\u0006"+
		"\u0084\b\u0006\n\u0006\f\u0006\u0087\t\u0006\u0003\u0006\u0089\b\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u008e\b\u0006\n\u0006"+
		"\f\u0006\u0091\t\u0006\u0003\u0006\u0093\b\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0003\b\u0098\b\b\u0001\b\u0001\b\u0001\t\u0004\t\u009d\b\t\u000b"+
		"\t\f\t\u009e\u0001\t\u0001\t\u0001\n\u0005\n\u00a4\b\n\n\n\f\n\u00a7\t"+
		"\n\u0001\n\u0001\n\u0005\n\u00ab\b\n\n\n\f\n\u00ae\t\n\u0001\n\u0001\n"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0000\u0000\f\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0000\u0002\u0001\u0000"+
		"\u0004\u0005\u0002\u0000\u0001\u0001\u0004\u0005\u00c3\u0000 \u0001\u0000"+
		"\u0000\u0000\u00026\u0001\u0000\u0000\u0000\u0004A\u0001\u0000\u0000\u0000"+
		"\u0006J\u0001\u0000\u0000\u0000\b^\u0001\u0000\u0000\u0000\nk\u0001\u0000"+
		"\u0000\u0000\f\u0092\u0001\u0000\u0000\u0000\u000e\u0094\u0001\u0000\u0000"+
		"\u0000\u0010\u0097\u0001\u0000\u0000\u0000\u0012\u009c\u0001\u0000\u0000"+
		"\u0000\u0014\u00a5\u0001\u0000\u0000\u0000\u0016\u00b1\u0001\u0000\u0000"+
		"\u0000\u0018\u001d\u0003\u0002\u0001\u0000\u0019\u001c\u0003\u0004\u0002"+
		"\u0000\u001a\u001c\u0003\u0006\u0003\u0000\u001b\u0019\u0001\u0000\u0000"+
		"\u0000\u001b\u001a\u0001\u0000\u0000\u0000\u001c\u001f\u0001\u0000\u0000"+
		"\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000"+
		"\u0000\u001e!\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000"+
		" \u0018\u0001\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000!%\u0001\u0000"+
		"\u0000\u0000\"$\u0005\n\u0000\u0000#\"\u0001\u0000\u0000\u0000$\'\u0001"+
		"\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000%&\u0001\u0000\u0000\u0000"+
		"&)\u0001\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000(*\u0005\u0007\u0000"+
		"\u0000)(\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*.\u0001\u0000"+
		"\u0000\u0000+-\u0005\n\u0000\u0000,+\u0001\u0000\u0000\u0000-0\u0001\u0000"+
		"\u0000\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/1\u0001"+
		"\u0000\u0000\u00000.\u0001\u0000\u0000\u000012\u0005\u0000\u0000\u0001"+
		"2\u0001\u0001\u0000\u0000\u000035\u0005\n\u0000\u000043\u0001\u0000\u0000"+
		"\u000058\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u000067\u0001\u0000"+
		"\u0000\u000079\u0001\u0000\u0000\u000086\u0001\u0000\u0000\u00009=\u0003"+
		"\b\u0004\u0000:<\u0005\n\u0000\u0000;:\u0001\u0000\u0000\u0000<?\u0001"+
		"\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000"+
		">\u0003\u0001\u0000\u0000\u0000?=\u0001\u0000\u0000\u0000@B\u0005\n\u0000"+
		"\u0000A@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000CA\u0001\u0000"+
		"\u0000\u0000CD\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000EF\u0003"+
		"\b\u0004\u0000F\u0005\u0001\u0000\u0000\u0000GI\u0005\n\u0000\u0000HG"+
		"\u0001\u0000\u0000\u0000IL\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000"+
		"\u0000JK\u0001\u0000\u0000\u0000KM\u0001\u0000\u0000\u0000LJ\u0001\u0000"+
		"\u0000\u0000MQ\u0005\u0007\u0000\u0000NP\u0005\n\u0000\u0000ON\u0001\u0000"+
		"\u0000\u0000PS\u0001\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001"+
		"\u0000\u0000\u0000RT\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000"+
		"TU\u0003\b\u0004\u0000U\u0007\u0001\u0000\u0000\u0000VW\u0003\n\u0005"+
		"\u0000W[\u0005\u0006\u0000\u0000XZ\u0005\n\u0000\u0000YX\u0001\u0000\u0000"+
		"\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001\u0000"+
		"\u0000\u0000\\_\u0001\u0000\u0000\u0000][\u0001\u0000\u0000\u0000^V\u0001"+
		"\u0000\u0000\u0000^_\u0001\u0000\u0000\u0000_a\u0001\u0000\u0000\u0000"+
		"`b\u0005\u0003\u0000\u0000a`\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000"+
		"\u0000bf\u0001\u0000\u0000\u0000ce\u0005\n\u0000\u0000dc\u0001\u0000\u0000"+
		"\u0000eh\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000\u0000fg\u0001\u0000"+
		"\u0000\u0000gi\u0001\u0000\u0000\u0000hf\u0001\u0000\u0000\u0000ij\u0003"+
		"\f\u0006\u0000j\t\u0001\u0000\u0000\u0000kl\u0007\u0000\u0000\u0000l\u000b"+
		"\u0001\u0000\u0000\u0000mq\u0005\b\u0000\u0000np\u0005\n\u0000\u0000o"+
		"n\u0001\u0000\u0000\u0000ps\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000"+
		"\u0000qr\u0001\u0000\u0000\u0000r\u0088\u0001\u0000\u0000\u0000sq\u0001"+
		"\u0000\u0000\u0000tx\u0003\u000e\u0007\u0000uw\u0005\n\u0000\u0000vu\u0001"+
		"\u0000\u0000\u0000wz\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000"+
		"xy\u0001\u0000\u0000\u0000y\u007f\u0001\u0000\u0000\u0000zx\u0001\u0000"+
		"\u0000\u0000{~\u0003\u0012\t\u0000|~\u0003\u0014\n\u0000}{\u0001\u0000"+
		"\u0000\u0000}|\u0001\u0000\u0000\u0000~\u0081\u0001\u0000\u0000\u0000"+
		"\u007f}\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080"+
		"\u0085\u0001\u0000\u0000\u0000\u0081\u007f\u0001\u0000\u0000\u0000\u0082"+
		"\u0084\u0005\n\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0084\u0087"+
		"\u0001\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0086"+
		"\u0001\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085"+
		"\u0001\u0000\u0000\u0000\u0088t\u0001\u0000\u0000\u0000\u0088\u0089\u0001"+
		"\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u0093\u0005"+
		"\t\u0000\u0000\u008b\u008f\u0003\u000e\u0007\u0000\u008c\u008e\u0003\u0016"+
		"\u000b\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008e\u0091\u0001\u0000"+
		"\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000"+
		"\u0000\u0000\u0090\u0093\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000"+
		"\u0000\u0000\u0092m\u0001\u0000\u0000\u0000\u0092\u008b\u0001\u0000\u0000"+
		"\u0000\u0093\r\u0001\u0000\u0000\u0000\u0094\u0095\u0003\u0010\b\u0000"+
		"\u0095\u000f\u0001\u0000\u0000\u0000\u0096\u0098\u0005\u0003\u0000\u0000"+
		"\u0097\u0096\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000"+
		"\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a\u0007\u0001\u0000\u0000"+
		"\u009a\u0011\u0001\u0000\u0000\u0000\u009b\u009d\u0005\n\u0000\u0000\u009c"+
		"\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e"+
		"\u009c\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f"+
		"\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a1\u0003\u0010\b\u0000\u00a1\u0013"+
		"\u0001\u0000\u0000\u0000\u00a2\u00a4\u0005\n\u0000\u0000\u00a3\u00a2\u0001"+
		"\u0000\u0000\u0000\u00a4\u00a7\u0001\u0000\u0000\u0000\u00a5\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a8\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00ac\u0005"+
		"\u0007\u0000\u0000\u00a9\u00ab\u0005\n\u0000\u0000\u00aa\u00a9\u0001\u0000"+
		"\u0000\u0000\u00ab\u00ae\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001\u0000"+
		"\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000\u00ad\u00af\u0001\u0000"+
		"\u0000\u0000\u00ae\u00ac\u0001\u0000\u0000\u0000\u00af\u00b0\u0003\u0010"+
		"\b\u0000\u00b0\u0015\u0001\u0000\u0000\u0000\u00b1\u00b2\u0005\u0007\u0000"+
		"\u0000\u00b2\u00b3\u0003\u0010\b\u0000\u00b3\u0017\u0001\u0000\u0000\u0000"+
		"\u001b\u001b\u001d %).6=CJQ[^afqx}\u007f\u0085\u0088\u008f\u0092\u0097"+
		"\u009e\u00a5\u00ac";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}