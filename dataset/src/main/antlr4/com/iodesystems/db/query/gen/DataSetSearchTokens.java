// Generated from java-escape by ANTLR 4.11.1

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class DataSetSearchTokens extends Lexer {
    public static final int
            ESCAPED_CHAR = 1, ESCAPE = 2, TARGET = 3, ANY = 4, STRING = 5, TARGET_SEPARATOR = 6,
            TERM_OR = 7, TERM_GROUP_START = 8, TERM_GROUP_END = 9, WS = 10, ESCAPED = 11;
    public static final int
            ESCAPED_MODE = 1;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\u0004\u0000\u000bQ\u0006\uffff\uffff\u0006\uffff\uffff\u0002\u0000\u0007" +
                    "\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007" +
                    "\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007" +
                    "\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n" +
                    "\u0007\n\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0005\u0002\"\b\u0002\n\u0002" +
                    "\f\u0002%\t\u0002\u0001\u0003\u0001\u0003\u0004\u0003)\b\u0003\u000b\u0003" +
                    "\f\u0003*\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u00040\b\u0004\n\u0004" +
                    "\f\u00043\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005" +
                    "\u00049\b\u0004\n\u0004\f\u0004<\t\u0004\u0001\u0004\u0003\u0004?\b\u0004" +
                    "\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007" +
                    "\u0001\b\u0001\b\u0001\t\u0004\tJ\b\t\u000b\t\f\tK\u0001\n\u0001\n\u0001" +
                    "\n\u0001\n\u0000\u0000\u000b\u0002\u0001\u0004\u0002\u0006\u0003\b\u0004" +
                    "\n\u0005\f\u0006\u000e\u0007\u0010\b\u0012\t\u0014\n\u0016\u000b\u0002" +
                    "\u0000\u0001\u0006\u0002\u0000AZaz\u0004\u000009AZ__az\u0006\u0000  \"" +
                    "\"\'),,::\\\\\u0002\u0000\"\"\\\\\u0002\u0000\'\'\\\\\u0003\u0000\t\n" +
                    "\r\r  X\u0000\u0002\u0001\u0000\u0000\u0000\u0000\u0004\u0001\u0000\u0000" +
                    "\u0000\u0000\u0006\u0001\u0000\u0000\u0000\u0000\b\u0001\u0000\u0000\u0000" +
                    "\u0000\n\u0001\u0000\u0000\u0000\u0000\f\u0001\u0000\u0000\u0000\u0000" +
                    "\u000e\u0001\u0000\u0000\u0000\u0000\u0010\u0001\u0000\u0000\u0000\u0000" +
                    "\u0012\u0001\u0000\u0000\u0000\u0000\u0014\u0001\u0000\u0000\u0000\u0001" +
                    "\u0016\u0001\u0000\u0000\u0000\u0002\u0018\u0001\u0000\u0000\u0000\u0004" +
                    "\u001b\u0001\u0000\u0000\u0000\u0006\u001f\u0001\u0000\u0000\u0000\b(" +
                    "\u0001\u0000\u0000\u0000\n>\u0001\u0000\u0000\u0000\f@\u0001\u0000\u0000" +
                    "\u0000\u000eB\u0001\u0000\u0000\u0000\u0010D\u0001\u0000\u0000\u0000\u0012" +
                    "F\u0001\u0000\u0000\u0000\u0014I\u0001\u0000\u0000\u0000\u0016M\u0001" +
                    "\u0000\u0000\u0000\u0018\u0019\u0003\u0004\u0001\u0000\u0019\u001a\u0003" +
                    "\u0016\n\u0000\u001a\u0003\u0001\u0000\u0000\u0000\u001b\u001c\u0005\\" +
                    "\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\u001e\u0006\u0001" +
                    "\u0000\u0000\u001e\u0005\u0001\u0000\u0000\u0000\u001f#\u0007\u0000\u0000" +
                    "\u0000 \"\u0007\u0001\u0000\u0000! \u0001\u0000\u0000\u0000\"%\u0001\u0000" +
                    "\u0000\u0000#!\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$\u0007" +
                    "\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000&)\b\u0002\u0000\u0000" +
                    "\')\u0003\u0002\u0000\u0000(&\u0001\u0000\u0000\u0000(\'\u0001\u0000\u0000" +
                    "\u0000)*\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000\u0000*+\u0001\u0000" +
                    "\u0000\u0000+\t\u0001\u0000\u0000\u0000,1\u0005\"\u0000\u0000-0\b\u0003" +
                    "\u0000\u0000.0\u0003\u0002\u0000\u0000/-\u0001\u0000\u0000\u0000/.\u0001" +
                    "\u0000\u0000\u000003\u0001\u0000\u0000\u00001/\u0001\u0000\u0000\u0000" +
                    "12\u0001\u0000\u0000\u000024\u0001\u0000\u0000\u000031\u0001\u0000\u0000" +
                    "\u00004?\u0005\"\u0000\u00005:\u0005\'\u0000\u000069\b\u0004\u0000\u0000" +
                    "79\u0003\u0002\u0000\u000086\u0001\u0000\u0000\u000087\u0001\u0000\u0000" +
                    "\u00009<\u0001\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001\u0000" +
                    "\u0000\u0000;=\u0001\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000=?\u0005" +
                    "\'\u0000\u0000>,\u0001\u0000\u0000\u0000>5\u0001\u0000\u0000\u0000?\u000b" +
                    "\u0001\u0000\u0000\u0000@A\u0005:\u0000\u0000A\r\u0001\u0000\u0000\u0000" +
                    "BC\u0005,\u0000\u0000C\u000f\u0001\u0000\u0000\u0000DE\u0005(\u0000\u0000" +
                    "E\u0011\u0001\u0000\u0000\u0000FG\u0005)\u0000\u0000G\u0013\u0001\u0000" +
                    "\u0000\u0000HJ\u0007\u0005\u0000\u0000IH\u0001\u0000\u0000\u0000JK\u0001" +
                    "\u0000\u0000\u0000KI\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000" +
                    "L\u0015\u0001\u0000\u0000\u0000MN\t\u0000\u0000\u0000NO\u0001\u0000\u0000" +
                    "\u0000OP\u0006\n\u0001\u0000P\u0017\u0001\u0000\u0000\u0000\u000b\u0000" +
                    "\u0001#(*/18:>K\u0002\u0005\u0001\u0000\u0004\u0000\u0000";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    public static String[] channelNames = {
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    };
    public static String[] modeNames = {
            "DEFAULT_MODE", "ESCAPED_MODE"
    };

    static {
        RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION);
    }

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

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }

    public DataSetSearchTokens(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[]{
                "ESCAPED_CHAR", "ESCAPE", "TARGET", "ANY", "STRING", "TARGET_SEPARATOR",
                "TERM_OR", "TERM_GROUP_START", "TERM_GROUP_END", "WS", "ESCAPED"
        };
    }

    private static String[] makeLiteralNames() {
        return new String[]{
                null, null, "'\\'", null, null, null, "':'", "','", "'('", "')'"
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, "ESCAPED_CHAR", "ESCAPE", "TARGET", "ANY", "STRING", "TARGET_SEPARATOR",
                "TERM_OR", "TERM_GROUP_START", "TERM_GROUP_END", "WS", "ESCAPED"
        };
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
    public String getGrammarFileName() {
        return "DataSetSearchTokens.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
}
