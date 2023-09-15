// Generated from DataSetSearchLexer.g4 by ANTLR 4.13.1
package com.iodesystems.db.query;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class DataSetSearchLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ESCAPED_CHAR=1, ESCAPE=2, NEGATE=3, ANY=4, STRING=5, TARGET_SEPARATOR=6, 
		TERM_OR=7, TERM_GROUP_START=8, TERM_GROUP_END=9, WS=10, ESCAPED=11;
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
			"ESCAPED_CHAR", "ESCAPE", "NEGATE", "ANY", "STRING", "TARGET_SEPARATOR", 
			"TERM_OR", "TERM_GROUP_START", "TERM_GROUP_END", "WS", "ESCAPED"
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
		"\u0004\u0000\u000bS\u0006\uffff\uffff\u0006\uffff\uffff\u0002\u0000\u0007"+
		"\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007"+
		"\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007"+
		"\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n"+
		"\u0007\n\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0004"+
		"\u0003$\b\u0003\u000b\u0003\f\u0003%\u0001\u0003\u0001\u0003\u0005\u0003"+
		"*\b\u0003\n\u0003\f\u0003-\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0005\u00042\b\u0004\n\u0004\f\u00045\t\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004;\b\u0004\n\u0004\f\u0004>\t\u0004"+
		"\u0001\u0004\u0003\u0004A\b\u0004\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0004\tL"+
		"\b\t\u000b\t\f\tM\u0001\n\u0001\n\u0001\n\u0001\n\u0000\u0000\u000b\u0002"+
		"\u0001\u0004\u0002\u0006\u0003\b\u0004\n\u0005\f\u0006\u000e\u0007\u0010"+
		"\b\u0012\t\u0014\n\u0016\u000b\u0002\u0000\u0001\u0005\u0005\u0000 \""+
		"\'),,::\\\\\u0006\u0000  \"\"\'),,::\\\\\u0002\u0000\"\"\\\\\u0002\u0000"+
		"\'\'\\\\\u0003\u0000\t\n\r\r  [\u0000\u0002\u0001\u0000\u0000\u0000\u0000"+
		"\u0004\u0001\u0000\u0000\u0000\u0000\u0006\u0001\u0000\u0000\u0000\u0000"+
		"\b\u0001\u0000\u0000\u0000\u0000\n\u0001\u0000\u0000\u0000\u0000\f\u0001"+
		"\u0000\u0000\u0000\u0000\u000e\u0001\u0000\u0000\u0000\u0000\u0010\u0001"+
		"\u0000\u0000\u0000\u0000\u0012\u0001\u0000\u0000\u0000\u0000\u0014\u0001"+
		"\u0000\u0000\u0000\u0001\u0016\u0001\u0000\u0000\u0000\u0002\u0018\u0001"+
		"\u0000\u0000\u0000\u0004\u001b\u0001\u0000\u0000\u0000\u0006\u001f\u0001"+
		"\u0000\u0000\u0000\b#\u0001\u0000\u0000\u0000\n@\u0001\u0000\u0000\u0000"+
		"\fB\u0001\u0000\u0000\u0000\u000eD\u0001\u0000\u0000\u0000\u0010F\u0001"+
		"\u0000\u0000\u0000\u0012H\u0001\u0000\u0000\u0000\u0014K\u0001\u0000\u0000"+
		"\u0000\u0016O\u0001\u0000\u0000\u0000\u0018\u0019\u0003\u0004\u0001\u0000"+
		"\u0019\u001a\u0003\u0016\n\u0000\u001a\u0003\u0001\u0000\u0000\u0000\u001b"+
		"\u001c\u0005\\\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\u001e"+
		"\u0006\u0001\u0000\u0000\u001e\u0005\u0001\u0000\u0000\u0000\u001f \u0005"+
		"!\u0000\u0000 \u0007\u0001\u0000\u0000\u0000!$\b\u0000\u0000\u0000\"$"+
		"\u0003\u0002\u0000\u0000#!\u0001\u0000\u0000\u0000#\"\u0001\u0000\u0000"+
		"\u0000$%\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000%&\u0001\u0000"+
		"\u0000\u0000&+\u0001\u0000\u0000\u0000\'*\b\u0001\u0000\u0000(*\u0003"+
		"\u0002\u0000\u0000)\'\u0001\u0000\u0000\u0000)(\u0001\u0000\u0000\u0000"+
		"*-\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000"+
		"\u0000,\t\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000.3\u0005\""+
		"\u0000\u0000/2\b\u0002\u0000\u000002\u0003\u0002\u0000\u00001/\u0001\u0000"+
		"\u0000\u000010\u0001\u0000\u0000\u000025\u0001\u0000\u0000\u000031\u0001"+
		"\u0000\u0000\u000034\u0001\u0000\u0000\u000046\u0001\u0000\u0000\u0000"+
		"53\u0001\u0000\u0000\u00006A\u0005\"\u0000\u00007<\u0005\'\u0000\u0000"+
		"8;\b\u0003\u0000\u00009;\u0003\u0002\u0000\u0000:8\u0001\u0000\u0000\u0000"+
		":9\u0001\u0000\u0000\u0000;>\u0001\u0000\u0000\u0000<:\u0001\u0000\u0000"+
		"\u0000<=\u0001\u0000\u0000\u0000=?\u0001\u0000\u0000\u0000><\u0001\u0000"+
		"\u0000\u0000?A\u0005\'\u0000\u0000@.\u0001\u0000\u0000\u0000@7\u0001\u0000"+
		"\u0000\u0000A\u000b\u0001\u0000\u0000\u0000BC\u0005:\u0000\u0000C\r\u0001"+
		"\u0000\u0000\u0000DE\u0005,\u0000\u0000E\u000f\u0001\u0000\u0000\u0000"+
		"FG\u0005(\u0000\u0000G\u0011\u0001\u0000\u0000\u0000HI\u0005)\u0000\u0000"+
		"I\u0013\u0001\u0000\u0000\u0000JL\u0007\u0004\u0000\u0000KJ\u0001\u0000"+
		"\u0000\u0000LM\u0001\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000MN\u0001"+
		"\u0000\u0000\u0000N\u0015\u0001\u0000\u0000\u0000OP\t\u0000\u0000\u0000"+
		"PQ\u0001\u0000\u0000\u0000QR\u0006\n\u0001\u0000R\u0017\u0001\u0000\u0000"+
		"\u0000\f\u0000\u0001#%)+13:<@M\u0002\u0005\u0001\u0000\u0004\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}