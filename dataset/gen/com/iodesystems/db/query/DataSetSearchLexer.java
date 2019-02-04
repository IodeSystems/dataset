// Generated from com/iodesystems/db/query/DataSetSearch.g4 by ANTLR 4.7.1
package com.iodesystems.db.query;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DataSetSearchLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STRING=1, TARGET=2, TARGET_SEPARATOR=3, TERM_OR=4, TERM_GROUP_START=5, 
		TERM_GROUP_END=6, ANY=7, WS=8;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"STRING", "TARGET", "TARGET_SEPARATOR", "TERM_OR", "TERM_GROUP_START", 
		"TERM_GROUP_END", "ANY", "WS"
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


	public DataSetSearchLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DataSetSearch.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\nB\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2"+
		"\7\2\30\n\2\f\2\16\2\33\13\2\3\2\3\2\3\2\3\2\3\2\7\2\"\n\2\f\2\16\2%\13"+
		"\2\3\2\5\2(\n\2\3\3\3\3\7\3,\n\3\f\3\16\3/\13\3\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\b\6\b:\n\b\r\b\16\b;\3\t\6\t?\n\t\r\t\16\t@\2\2\n\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\3\2\b\3\2$$\3\2))\4\2C\\c|\6\2\62;C\\aac|"+
		"\6\2\"\"*+..<<\5\2\13\f\17\17\"\"\2I\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2"+
		"\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\3\'"+
		"\3\2\2\2\5)\3\2\2\2\7\60\3\2\2\2\t\62\3\2\2\2\13\64\3\2\2\2\r\66\3\2\2"+
		"\2\179\3\2\2\2\21>\3\2\2\2\23\31\7$\2\2\24\25\7^\2\2\25\30\7$\2\2\26\30"+
		"\n\2\2\2\27\24\3\2\2\2\27\26\3\2\2\2\30\33\3\2\2\2\31\27\3\2\2\2\31\32"+
		"\3\2\2\2\32\34\3\2\2\2\33\31\3\2\2\2\34(\7$\2\2\35#\7)\2\2\36\37\7^\2"+
		"\2\37\"\7)\2\2 \"\n\3\2\2!\36\3\2\2\2! \3\2\2\2\"%\3\2\2\2#!\3\2\2\2#"+
		"$\3\2\2\2$&\3\2\2\2%#\3\2\2\2&(\7)\2\2\'\23\3\2\2\2\'\35\3\2\2\2(\4\3"+
		"\2\2\2)-\t\4\2\2*,\t\5\2\2+*\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\6"+
		"\3\2\2\2/-\3\2\2\2\60\61\7<\2\2\61\b\3\2\2\2\62\63\7.\2\2\63\n\3\2\2\2"+
		"\64\65\7*\2\2\65\f\3\2\2\2\66\67\7+\2\2\67\16\3\2\2\28:\n\6\2\298\3\2"+
		"\2\2:;\3\2\2\2;9\3\2\2\2;<\3\2\2\2<\20\3\2\2\2=?\t\7\2\2>=\3\2\2\2?@\3"+
		"\2\2\2@>\3\2\2\2@A\3\2\2\2A\22\3\2\2\2\13\2\27\31!#\'-;@\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}