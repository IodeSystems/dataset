// Generated from com/iodesystems/db/query/DataSetSearch.g4 by ANTLR 4.7.1
package com.iodesystems.db.query;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DataSetSearchParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, TARGET=2, TARGET_SEPARATOR=3, TERM_OR=4, TERM_GROUP_START=5, 
		TERM_GROUP_END=6, ANY=7, WS=8;
	public static final int
		RULE_search = 0, RULE_simpleTerm = 1, RULE_andTerm = 2, RULE_orTerm = 3, 
		RULE_term = 4, RULE_termTarget = 5, RULE_termValueGroup = 6, RULE_simpleValue = 7, 
		RULE_termValue = 8, RULE_andValue = 9, RULE_orValue = 10, RULE_unprotectedOrValue = 11;
	public static final String[] ruleNames = {
		"search", "simpleTerm", "andTerm", "orTerm", "term", "termTarget", "termValueGroup", 
		"simpleValue", "termValue", "andValue", "orValue", "unprotectedOrValue"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, "':'", "','", "'('", "')'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "STRING", "TARGET", "TARGET_SEPARATOR", "TERM_OR", "TERM_GROUP_START", 
		"TERM_GROUP_END", "ANY", "WS"
	};
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
	public String getGrammarFileName() { return "DataSetSearch.g4"; }

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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterSearch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitSearch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitSearch(this);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterSimpleTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitSimpleTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitSimpleTerm(this);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterAndTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitAndTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitAndTerm(this);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterOrTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitOrTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitOrTerm(this);
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
			setState(79); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(78);
				match(WS);
				}
				}
				setState(81); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(83);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitTerm(this);
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
			setState(93);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(85);
				termTarget();
				setState(86);
				match(TARGET_SEPARATOR);
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(87);
					match(WS);
					}
					}
					setState(92);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			}
			setState(95);
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

	public static class TermTargetContext extends ParserRuleContext {
		public TerminalNode TARGET() { return getToken(DataSetSearchParser.TARGET, 0); }
		public TerminalNode STRING() { return getToken(DataSetSearchParser.STRING, 0); }
		public TermTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termTarget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterTermTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitTermTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitTermTarget(this);
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
			setState(97);
			_la = _input.LA(1);
			if ( !(_la==STRING || _la==TARGET) ) {
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

	public static class TermValueGroupContext extends ParserRuleContext {
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterTermValueGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitTermValueGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitTermValueGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermValueGroupContext termValueGroup() throws RecognitionException {
		TermValueGroupContext _localctx = new TermValueGroupContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_termValueGroup);
		int _la;
		try {
			int _alt;
			setState(136);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TERM_GROUP_START:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				match(TERM_GROUP_START);
				setState(103);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==WS) {
					{
					{
					setState(100);
					match(WS);
					}
					}
					setState(105);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(126);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << TARGET) | (1L << ANY))) != 0)) {
					{
					setState(106);
					simpleValue();
					setState(110);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(107);
							match(WS);
							}
							} 
						}
						setState(112);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
					}
					setState(117);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							setState(115);
							_errHandler.sync(this);
							switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
							case 1:
								{
								setState(113);
								andValue();
								}
								break;
							case 2:
								{
								setState(114);
								orValue();
								}
								break;
							}
							} 
						}
						setState(119);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
					}
					setState(123);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==WS) {
						{
						{
						setState(120);
						match(WS);
						}
						}
						setState(125);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(128);
				match(TERM_GROUP_END);
				}
				break;
			case STRING:
			case TARGET:
			case ANY:
				enterOuterAlt(_localctx, 2);
				{
				setState(129);
				simpleValue();
				setState(133);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(130);
						unprotectedOrValue();
						}
						} 
					}
					setState(135);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterSimpleValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitSimpleValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitSimpleValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleValueContext simpleValue() throws RecognitionException {
		SimpleValueContext _localctx = new SimpleValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_simpleValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
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

	public static class TermValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DataSetSearchParser.STRING, 0); }
		public TerminalNode TARGET() { return getToken(DataSetSearchParser.TARGET, 0); }
		public TerminalNode ANY() { return getToken(DataSetSearchParser.ANY, 0); }
		public TermValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterTermValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitTermValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitTermValue(this);
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
			setState(140);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << TARGET) | (1L << ANY))) != 0)) ) {
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterAndValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitAndValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitAndValue(this);
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
			setState(143); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(142);
				match(WS);
				}
				}
				setState(145); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(147);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterOrValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitOrValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitOrValue(this);
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
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(149);
				match(WS);
				}
				}
				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(155);
			match(TERM_OR);
			setState(159);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WS) {
				{
				{
				setState(156);
				match(WS);
				}
				}
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(162);
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
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).enterUnprotectedOrValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataSetSearchListener ) ((DataSetSearchListener)listener).exitUnprotectedOrValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DataSetSearchVisitor ) return ((DataSetSearchVisitor<? extends T>)visitor).visitUnprotectedOrValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnprotectedOrValueContext unprotectedOrValue() throws RecognitionException {
		UnprotectedOrValueContext _localctx = new UnprotectedOrValueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_unprotectedOrValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(TERM_OR);
			setState(165);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\n\u00aa\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\7\2\36\n\2\f\2\16\2!\13\2\5\2#\n\2\3\2"+
		"\7\2&\n\2\f\2\16\2)\13\2\3\2\5\2,\n\2\3\2\7\2/\n\2\f\2\16\2\62\13\2\3"+
		"\2\3\2\3\3\7\3\67\n\3\f\3\16\3:\13\3\3\3\3\3\7\3>\n\3\f\3\16\3A\13\3\3"+
		"\4\6\4D\n\4\r\4\16\4E\3\4\3\4\3\5\7\5K\n\5\f\5\16\5N\13\5\3\5\3\5\6\5"+
		"R\n\5\r\5\16\5S\3\5\3\5\3\6\3\6\3\6\7\6[\n\6\f\6\16\6^\13\6\5\6`\n\6\3"+
		"\6\3\6\3\7\3\7\3\b\3\b\7\bh\n\b\f\b\16\bk\13\b\3\b\3\b\7\bo\n\b\f\b\16"+
		"\br\13\b\3\b\3\b\7\bv\n\b\f\b\16\by\13\b\3\b\7\b|\n\b\f\b\16\b\177\13"+
		"\b\5\b\u0081\n\b\3\b\3\b\3\b\7\b\u0086\n\b\f\b\16\b\u0089\13\b\5\b\u008b"+
		"\n\b\3\t\3\t\3\n\3\n\3\13\6\13\u0092\n\13\r\13\16\13\u0093\3\13\3\13\3"+
		"\f\7\f\u0099\n\f\f\f\16\f\u009c\13\f\3\f\3\f\7\f\u00a0\n\f\f\f\16\f\u00a3"+
		"\13\f\3\f\3\f\3\r\3\r\3\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\4"+
		"\3\2\3\4\4\2\3\4\t\t\2\u00b5\2\"\3\2\2\2\48\3\2\2\2\6C\3\2\2\2\bL\3\2"+
		"\2\2\n_\3\2\2\2\fc\3\2\2\2\16\u008a\3\2\2\2\20\u008c\3\2\2\2\22\u008e"+
		"\3\2\2\2\24\u0091\3\2\2\2\26\u009a\3\2\2\2\30\u00a6\3\2\2\2\32\37\5\4"+
		"\3\2\33\36\5\6\4\2\34\36\5\b\5\2\35\33\3\2\2\2\35\34\3\2\2\2\36!\3\2\2"+
		"\2\37\35\3\2\2\2\37 \3\2\2\2 #\3\2\2\2!\37\3\2\2\2\"\32\3\2\2\2\"#\3\2"+
		"\2\2#\'\3\2\2\2$&\7\n\2\2%$\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(+"+
		"\3\2\2\2)\'\3\2\2\2*,\7\6\2\2+*\3\2\2\2+,\3\2\2\2,\60\3\2\2\2-/\7\n\2"+
		"\2.-\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\63\3\2\2\2\62\60"+
		"\3\2\2\2\63\64\7\2\2\3\64\3\3\2\2\2\65\67\7\n\2\2\66\65\3\2\2\2\67:\3"+
		"\2\2\28\66\3\2\2\289\3\2\2\29;\3\2\2\2:8\3\2\2\2;?\5\n\6\2<>\7\n\2\2="+
		"<\3\2\2\2>A\3\2\2\2?=\3\2\2\2?@\3\2\2\2@\5\3\2\2\2A?\3\2\2\2BD\7\n\2\2"+
		"CB\3\2\2\2DE\3\2\2\2EC\3\2\2\2EF\3\2\2\2FG\3\2\2\2GH\5\n\6\2H\7\3\2\2"+
		"\2IK\7\n\2\2JI\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2MO\3\2\2\2NL\3\2\2"+
		"\2OQ\7\6\2\2PR\7\n\2\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2TU\3\2\2"+
		"\2UV\5\n\6\2V\t\3\2\2\2WX\5\f\7\2X\\\7\5\2\2Y[\7\n\2\2ZY\3\2\2\2[^\3\2"+
		"\2\2\\Z\3\2\2\2\\]\3\2\2\2]`\3\2\2\2^\\\3\2\2\2_W\3\2\2\2_`\3\2\2\2`a"+
		"\3\2\2\2ab\5\16\b\2b\13\3\2\2\2cd\t\2\2\2d\r\3\2\2\2ei\7\7\2\2fh\7\n\2"+
		"\2gf\3\2\2\2hk\3\2\2\2ig\3\2\2\2ij\3\2\2\2j\u0080\3\2\2\2ki\3\2\2\2lp"+
		"\5\20\t\2mo\7\n\2\2nm\3\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2\2\2qw\3\2\2\2"+
		"rp\3\2\2\2sv\5\24\13\2tv\5\26\f\2us\3\2\2\2ut\3\2\2\2vy\3\2\2\2wu\3\2"+
		"\2\2wx\3\2\2\2x}\3\2\2\2yw\3\2\2\2z|\7\n\2\2{z\3\2\2\2|\177\3\2\2\2}{"+
		"\3\2\2\2}~\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\u0080l\3\2\2\2\u0080\u0081"+
		"\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u008b\7\b\2\2\u0083\u0087\5\20\t\2"+
		"\u0084\u0086\5\30\r\2\u0085\u0084\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085"+
		"\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u008a"+
		"e\3\2\2\2\u008a\u0083\3\2\2\2\u008b\17\3\2\2\2\u008c\u008d\5\22\n\2\u008d"+
		"\21\3\2\2\2\u008e\u008f\t\3\2\2\u008f\23\3\2\2\2\u0090\u0092\7\n\2\2\u0091"+
		"\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2"+
		"\2\2\u0094\u0095\3\2\2\2\u0095\u0096\5\22\n\2\u0096\25\3\2\2\2\u0097\u0099"+
		"\7\n\2\2\u0098\u0097\3\2\2\2\u0099\u009c\3\2\2\2\u009a\u0098\3\2\2\2\u009a"+
		"\u009b\3\2\2\2\u009b\u009d\3\2\2\2\u009c\u009a\3\2\2\2\u009d\u00a1\7\6"+
		"\2\2\u009e\u00a0\7\n\2\2\u009f\u009e\3\2\2\2\u00a0\u00a3\3\2\2\2\u00a1"+
		"\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a4\3\2\2\2\u00a3\u00a1\3\2"+
		"\2\2\u00a4\u00a5\5\22\n\2\u00a5\27\3\2\2\2\u00a6\u00a7\7\6\2\2\u00a7\u00a8"+
		"\5\22\n\2\u00a8\31\3\2\2\2\32\35\37\"\'+\608?ELS\\_ipuw}\u0080\u0087\u008a"+
		"\u0093\u009a\u00a1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}