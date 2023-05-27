// Generated from DataSetSearchLexer.g4 by ANTLR 4.13.0
package com.iodesystems.db.query;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class DataSetSearchLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ESCAPED_CHAR=1, ESCAPE=2, ANY=3, STRING=4, TARGET_SEPARATOR=5, TERM_OR=6, 
		TERM_GROUP_START=7, TERM_GROUP_END=8, WS=9, ESCAPED=10;
	public static final int
		ESCAPED_MODE=1;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "ESCAPED_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ESCAPED_CHAR", "ESCAPE", "ANY", "STRING", "TARGET_SEPARATOR", "TERM_OR", 
			"TERM_GROUP_START", "TERM_GROUP_END", "WS", "ESCAPED"
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


	public DataSetSearchLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DataSetSearchLexer.g4"; }

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
		"\u0004\u0000\nH\u0006\uffff\uffff\u0006\uffff\uffff\u0002\u0000\u0007"+
		"\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007"+
		"\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007"+
		"\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0004\u0002 \b\u0002\u000b\u0002\f\u0002!\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003\'\b\u0003\n\u0003\f\u0003*"+
		"\t\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u00030"+
		"\b\u0003\n\u0003\f\u00033\t\u0003\u0001\u0003\u0003\u00036\b\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0004\bA\b\b\u000b\b\f\bB\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0000\u0000\n\u0002\u0001\u0004\u0002\u0006\u0003\b\u0004\n"+
		"\u0005\f\u0006\u000e\u0007\u0010\b\u0012\t\u0014\n\u0002\u0000\u0001\u0004"+
		"\u0006\u0000  \"\"\'),,::\\\\\u0002\u0000\"\"\\\\\u0002\u0000\'\'\\\\"+
		"\u0003\u0000\t\n\r\r  N\u0000\u0002\u0001\u0000\u0000\u0000\u0000\u0004"+
		"\u0001\u0000\u0000\u0000\u0000\u0006\u0001\u0000\u0000\u0000\u0000\b\u0001"+
		"\u0000\u0000\u0000\u0000\n\u0001\u0000\u0000\u0000\u0000\f\u0001\u0000"+
		"\u0000\u0000\u0000\u000e\u0001\u0000\u0000\u0000\u0000\u0010\u0001\u0000"+
		"\u0000\u0000\u0000\u0012\u0001\u0000\u0000\u0000\u0001\u0014\u0001\u0000"+
		"\u0000\u0000\u0002\u0016\u0001\u0000\u0000\u0000\u0004\u0019\u0001\u0000"+
		"\u0000\u0000\u0006\u001f\u0001\u0000\u0000\u0000\b5\u0001\u0000\u0000"+
		"\u0000\n7\u0001\u0000\u0000\u0000\f9\u0001\u0000\u0000\u0000\u000e;\u0001"+
		"\u0000\u0000\u0000\u0010=\u0001\u0000\u0000\u0000\u0012@\u0001\u0000\u0000"+
		"\u0000\u0014D\u0001\u0000\u0000\u0000\u0016\u0017\u0003\u0004\u0001\u0000"+
		"\u0017\u0018\u0003\u0014\t\u0000\u0018\u0003\u0001\u0000\u0000\u0000\u0019"+
		"\u001a\u0005\\\u0000\u0000\u001a\u001b\u0001\u0000\u0000\u0000\u001b\u001c"+
		"\u0006\u0001\u0000\u0000\u001c\u0005\u0001\u0000\u0000\u0000\u001d \b"+
		"\u0000\u0000\u0000\u001e \u0003\u0002\u0000\u0000\u001f\u001d\u0001\u0000"+
		"\u0000\u0000\u001f\u001e\u0001\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000"+
		"!\u001f\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"\u0007\u0001"+
		"\u0000\u0000\u0000#(\u0005\"\u0000\u0000$\'\b\u0001\u0000\u0000%\'\u0003"+
		"\u0002\u0000\u0000&$\u0001\u0000\u0000\u0000&%\u0001\u0000\u0000\u0000"+
		"\'*\u0001\u0000\u0000\u0000(&\u0001\u0000\u0000\u0000()\u0001\u0000\u0000"+
		"\u0000)+\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000\u0000+6\u0005\"\u0000"+
		"\u0000,1\u0005\'\u0000\u0000-0\b\u0002\u0000\u0000.0\u0003\u0002\u0000"+
		"\u0000/-\u0001\u0000\u0000\u0000/.\u0001\u0000\u0000\u000003\u0001\u0000"+
		"\u0000\u00001/\u0001\u0000\u0000\u000012\u0001\u0000\u0000\u000024\u0001"+
		"\u0000\u0000\u000031\u0001\u0000\u0000\u000046\u0005\'\u0000\u00005#\u0001"+
		"\u0000\u0000\u00005,\u0001\u0000\u0000\u00006\t\u0001\u0000\u0000\u0000"+
		"78\u0005:\u0000\u00008\u000b\u0001\u0000\u0000\u00009:\u0005,\u0000\u0000"+
		":\r\u0001\u0000\u0000\u0000;<\u0005(\u0000\u0000<\u000f\u0001\u0000\u0000"+
		"\u0000=>\u0005)\u0000\u0000>\u0011\u0001\u0000\u0000\u0000?A\u0007\u0003"+
		"\u0000\u0000@?\u0001\u0000\u0000\u0000AB\u0001\u0000\u0000\u0000B@\u0001"+
		"\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000C\u0013\u0001\u0000\u0000"+
		"\u0000DE\t\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000FG\u0006\t\u0001"+
		"\u0000G\u0015\u0001\u0000\u0000\u0000\n\u0000\u0001\u001f!&(/15B\u0002"+
		"\u0005\u0001\u0000\u0004\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}