// Generated from java-escape by ANTLR 4.11.1

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class DataSetSearch extends Parser {
    public static final int
            WS = 1, TERM_OR = 2, TARGET_SEPARATOR = 3, TARGET = 4, STRING = 5, TERM_GROUP_START = 6,
            TERM_GROUP_END = 7, ANY = 8;
    public static final int
            RULE_search = 0, RULE_simpleTerm = 1, RULE_andTerm = 2, RULE_orTerm = 3,
            RULE_term = 4, RULE_termTarget = 5, RULE_termValueGroup = 6, RULE_simpleValue = 7,
            RULE_termValue = 8, RULE_andValue = 9, RULE_orValue = 10, RULE_unprotectedOrValue = 11;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\u0004\u0001\b\u00a8\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
                    "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002" +
                    "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002" +
                    "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0001" +
                    "\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u001c\b\u0000\n\u0000\f\u0000" +
                    "\u001f\t\u0000\u0003\u0000!\b\u0000\u0001\u0000\u0005\u0000$\b\u0000\n" +
                    "\u0000\f\u0000\'\t\u0000\u0001\u0000\u0003\u0000*\b\u0000\u0001\u0000" +
                    "\u0005\u0000-\b\u0000\n\u0000\f\u00000\t\u0000\u0001\u0000\u0001\u0000" +
                    "\u0001\u0001\u0005\u00015\b\u0001\n\u0001\f\u00018\t\u0001\u0001\u0001" +
                    "\u0001\u0001\u0005\u0001<\b\u0001\n\u0001\f\u0001?\t\u0001\u0001\u0002" +
                    "\u0004\u0002B\b\u0002\u000b\u0002\f\u0002C\u0001\u0002\u0001\u0002\u0001" +
                    "\u0003\u0005\u0003I\b\u0003\n\u0003\f\u0003L\t\u0003\u0001\u0003\u0001" +
                    "\u0003\u0004\u0003P\b\u0003\u000b\u0003\f\u0003Q\u0001\u0003\u0001\u0003" +
                    "\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004Y\b\u0004\n\u0004\f\u0004" +
                    "\\\t\u0004\u0003\u0004^\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001" +
                    "\u0005\u0001\u0006\u0001\u0006\u0005\u0006f\b\u0006\n\u0006\f\u0006i\t" +
                    "\u0006\u0001\u0006\u0001\u0006\u0005\u0006m\b\u0006\n\u0006\f\u0006p\t" +
                    "\u0006\u0001\u0006\u0001\u0006\u0005\u0006t\b\u0006\n\u0006\f\u0006w\t" +
                    "\u0006\u0001\u0006\u0005\u0006z\b\u0006\n\u0006\f\u0006}\t\u0006\u0003" +
                    "\u0006\u007f\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006\u0084" +
                    "\b\u0006\n\u0006\f\u0006\u0087\t\u0006\u0003\u0006\u0089\b\u0006\u0001" +
                    "\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0004\t\u0090\b\t\u000b\t\f" +
                    "\t\u0091\u0001\t\u0001\t\u0001\n\u0005\n\u0097\b\n\n\n\f\n\u009a\t\n\u0001" +
                    "\n\u0001\n\u0005\n\u009e\b\n\n\n\f\n\u00a1\t\n\u0001\n\u0001\n\u0001\u000b" +
                    "\u0001\u000b\u0001\u000b\u0001\u000b\u0000\u0000\f\u0000\u0002\u0004\u0006" +
                    "\b\n\f\u000e\u0010\u0012\u0014\u0016\u0000\u0002\u0001\u0000\u0004\u0005" +
                    "\u0002\u0000\u0004\u0005\b\b\u00b3\u0000 \u0001\u0000\u0000\u0000\u0002" +
                    "6\u0001\u0000\u0000\u0000\u0004A\u0001\u0000\u0000\u0000\u0006J\u0001" +
                    "\u0000\u0000\u0000\b]\u0001\u0000\u0000\u0000\na\u0001\u0000\u0000\u0000" +
                    "\f\u0088\u0001\u0000\u0000\u0000\u000e\u008a\u0001\u0000\u0000\u0000\u0010" +
                    "\u008c\u0001\u0000\u0000\u0000\u0012\u008f\u0001\u0000\u0000\u0000\u0014" +
                    "\u0098\u0001\u0000\u0000\u0000\u0016\u00a4\u0001\u0000\u0000\u0000\u0018" +
                    "\u001d\u0003\u0002\u0001\u0000\u0019\u001c\u0003\u0004\u0002\u0000\u001a" +
                    "\u001c\u0003\u0006\u0003\u0000\u001b\u0019\u0001\u0000\u0000\u0000\u001b" +
                    "\u001a\u0001\u0000\u0000\u0000\u001c\u001f\u0001\u0000\u0000\u0000\u001d" +
                    "\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e" +
                    "!\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000 \u0018\u0001" +
                    "\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000!%\u0001\u0000\u0000\u0000" +
                    "\"$\u0005\u0001\u0000\u0000#\"\u0001\u0000\u0000\u0000$\'\u0001\u0000" +
                    "\u0000\u0000%#\u0001\u0000\u0000\u0000%&\u0001\u0000\u0000\u0000&)\u0001" +
                    "\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000(*\u0005\u0002\u0000\u0000" +
                    ")(\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*.\u0001\u0000\u0000" +
                    "\u0000+-\u0005\u0001\u0000\u0000,+\u0001\u0000\u0000\u0000-0\u0001\u0000" +
                    "\u0000\u0000.,\u0001\u0000\u0000\u0000./\u0001\u0000\u0000\u0000/1\u0001" +
                    "\u0000\u0000\u00000.\u0001\u0000\u0000\u000012\u0005\u0000\u0000\u0001" +
                    "2\u0001\u0001\u0000\u0000\u000035\u0005\u0001\u0000\u000043\u0001\u0000" +
                    "\u0000\u000058\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u000067\u0001" +
                    "\u0000\u0000\u000079\u0001\u0000\u0000\u000086\u0001\u0000\u0000\u0000" +
                    "9=\u0003\b\u0004\u0000:<\u0005\u0001\u0000\u0000;:\u0001\u0000\u0000\u0000" +
                    "<?\u0001\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000" +
                    "\u0000>\u0003\u0001\u0000\u0000\u0000?=\u0001\u0000\u0000\u0000@B\u0005" +
                    "\u0001\u0000\u0000A@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000" +
                    "CA\u0001\u0000\u0000\u0000CD\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000" +
                    "\u0000EF\u0003\b\u0004\u0000F\u0005\u0001\u0000\u0000\u0000GI\u0005\u0001" +
                    "\u0000\u0000HG\u0001\u0000\u0000\u0000IL\u0001\u0000\u0000\u0000JH\u0001" +
                    "\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KM\u0001\u0000\u0000\u0000" +
                    "LJ\u0001\u0000\u0000\u0000MO\u0005\u0002\u0000\u0000NP\u0005\u0001\u0000" +
                    "\u0000ON\u0001\u0000\u0000\u0000PQ\u0001\u0000\u0000\u0000QO\u0001\u0000" +
                    "\u0000\u0000QR\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000ST\u0003" +
                    "\b\u0004\u0000T\u0007\u0001\u0000\u0000\u0000UV\u0003\n\u0005\u0000VZ" +
                    "\u0005\u0003\u0000\u0000WY\u0005\u0001\u0000\u0000XW\u0001\u0000\u0000" +
                    "\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001\u0000" +
                    "\u0000\u0000[^\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000]U\u0001" +
                    "\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^_\u0001\u0000\u0000\u0000" +
                    "_`\u0003\f\u0006\u0000`\t\u0001\u0000\u0000\u0000ab\u0007\u0000\u0000" +
                    "\u0000b\u000b\u0001\u0000\u0000\u0000cg\u0005\u0006\u0000\u0000df\u0005" +
                    "\u0001\u0000\u0000ed\u0001\u0000\u0000\u0000fi\u0001\u0000\u0000\u0000" +
                    "ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000h~\u0001\u0000\u0000" +
                    "\u0000ig\u0001\u0000\u0000\u0000jn\u0003\u000e\u0007\u0000km\u0005\u0001" +
                    "\u0000\u0000lk\u0001\u0000\u0000\u0000mp\u0001\u0000\u0000\u0000nl\u0001" +
                    "\u0000\u0000\u0000no\u0001\u0000\u0000\u0000ou\u0001\u0000\u0000\u0000" +
                    "pn\u0001\u0000\u0000\u0000qt\u0003\u0012\t\u0000rt\u0003\u0014\n\u0000" +
                    "sq\u0001\u0000\u0000\u0000sr\u0001\u0000\u0000\u0000tw\u0001\u0000\u0000" +
                    "\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000v{\u0001\u0000" +
                    "\u0000\u0000wu\u0001\u0000\u0000\u0000xz\u0005\u0001\u0000\u0000yx\u0001" +
                    "\u0000\u0000\u0000z}\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000" +
                    "{|\u0001\u0000\u0000\u0000|\u007f\u0001\u0000\u0000\u0000}{\u0001\u0000" +
                    "\u0000\u0000~j\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000" +
                    "\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0089\u0005\u0007\u0000\u0000" +
                    "\u0081\u0085\u0003\u000e\u0007\u0000\u0082\u0084\u0003\u0016\u000b\u0000" +
                    "\u0083\u0082\u0001\u0000\u0000\u0000\u0084\u0087\u0001\u0000\u0000\u0000" +
                    "\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000" +
                    "\u0086\u0089\u0001\u0000\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000" +
                    "\u0088c\u0001\u0000\u0000\u0000\u0088\u0081\u0001\u0000\u0000\u0000\u0089" +
                    "\r\u0001\u0000\u0000\u0000\u008a\u008b\u0003\u0010\b\u0000\u008b\u000f" +
                    "\u0001\u0000\u0000\u0000\u008c\u008d\u0007\u0001\u0000\u0000\u008d\u0011" +
                    "\u0001\u0000\u0000\u0000\u008e\u0090\u0005\u0001\u0000\u0000\u008f\u008e" +
                    "\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u008f" +
                    "\u0001\u0000\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u0092\u0093" +
                    "\u0001\u0000\u0000\u0000\u0093\u0094\u0003\u0010\b\u0000\u0094\u0013\u0001" +
                    "\u0000\u0000\u0000\u0095\u0097\u0005\u0001\u0000\u0000\u0096\u0095\u0001" +
                    "\u0000\u0000\u0000\u0097\u009a\u0001\u0000\u0000\u0000\u0098\u0096\u0001" +
                    "\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009b\u0001" +
                    "\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000\u0000\u009b\u009f\u0005" +
                    "\u0002\u0000\u0000\u009c\u009e\u0005\u0001\u0000\u0000\u009d\u009c\u0001" +
                    "\u0000\u0000\u0000\u009e\u00a1\u0001\u0000\u0000\u0000\u009f\u009d\u0001" +
                    "\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a2\u0001" +
                    "\u0000\u0000\u0000\u00a1\u009f\u0001\u0000\u0000\u0000\u00a2\u00a3\u0003" +
                    "\u0010\b\u0000\u00a3\u0015\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005\u0002" +
                    "\u0000\u0000\u00a5\u00a6\u0003\u0010\b\u0000\u00a6\u0017\u0001\u0000\u0000" +
                    "\u0000\u0018\u001b\u001d %).6=CJQZ]gnsu{~\u0085\u0088\u0091\u0098\u009f";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

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

    public DataSetSearch(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[]{
                "search", "simpleTerm", "andTerm", "orTerm", "term", "termTarget", "termValueGroup",
                "simpleValue", "termValue", "andValue", "orValue", "unprotectedOrValue"
        };
    }

    private static String[] makeLiteralNames() {
        return new String[]{
        };
    }

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, "WS", "TERM_OR", "TARGET_SEPARATOR", "TARGET", "STRING", "TERM_GROUP_START",
                "TERM_GROUP_END", "ANY"
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
        return "java-escape";
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
    public ATN getATN() {
        return _ATN;
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
                switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                    case 1: {
                        setState(24);
                        simpleTerm();
                        setState(29);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                            if (_alt == 1) {
                                {
                                    setState(27);
                                    _errHandler.sync(this);
                                    switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                                        case 1: {
                                            setState(25);
                                            andTerm();
                                        }
                                        break;
                                        case 2: {
                                            setState(26);
                                            orTerm();
                                        }
                                        break;
                                    }
                                }
                            }
                            setState(31);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                        }
                    }
                    break;
                }
                setState(37);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(34);
                                match(WS);
                            }
                        }
                    }
                    setState(39);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                }
                setState(41);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == TERM_OR) {
                    {
                        setState(40);
                        match(TERM_OR);
                    }
                }

                setState(46);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == WS) {
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                while (_la == WS) {
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
                _alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(58);
                                match(WS);
                            }
                        }
                    }
                    setState(63);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                } while (_la == WS);
                setState(69);
                term();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                while (_la == WS) {
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
                } while (_la == WS);
                setState(83);
                term();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                switch (getInterpreter().adaptivePredict(_input, 12, _ctx)) {
                    case 1: {
                        setState(85);
                        termTarget();
                        setState(86);
                        match(TARGET_SEPARATOR);
                        setState(90);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == WS) {
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                if (!(_la == TARGET || _la == STRING)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                    while (_la == WS) {
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
                    if (((_la) & ~0x3f) == 0 && ((1L << _la) & 304L) != 0) {
                        {
                            setState(106);
                            simpleValue();
                            setState(110);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
                            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                                if (_alt == 1) {
                                    {
                                        {
                                            setState(107);
                                            match(WS);
                                        }
                                    }
                                }
                                setState(112);
                                _errHandler.sync(this);
                                _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
                            }
                            setState(117);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                                if (_alt == 1) {
                                    {
                                        setState(115);
                                        _errHandler.sync(this);
                                        switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
                                            case 1: {
                                                setState(113);
                                                andValue();
                                            }
                                            break;
                                            case 2: {
                                                setState(114);
                                                orValue();
                                            }
                                            break;
                                        }
                                    }
                                }
                                setState(119);
                                _errHandler.sync(this);
                                _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                            }
                            setState(123);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == WS) {
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
                case TARGET:
                case STRING:
                case ANY:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(129);
                    simpleValue();
                    setState(133);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                            {
                                {
                                    setState(130);
                                    unprotectedOrValue();
                                }
                            }
                        }
                        setState(135);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
                    }
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                if (!(((_la) & ~0x3f) == 0 && ((1L << _la) & 304L) != 0)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                } while (_la == WS);
                setState(147);
                termValue();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
                while (_la == WS) {
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
                while (_la == WS) {
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
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
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SearchContext extends ParserRuleContext {
        public SearchContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode EOF() {
            return getToken(DataSetSearch.EOF, 0);
        }

        public SimpleTermContext simpleTerm() {
            return getRuleContext(SimpleTermContext.class, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        public TerminalNode TERM_OR() {
            return getToken(DataSetSearch.TERM_OR, 0);
        }

        public List<AndTermContext> andTerm() {
            return getRuleContexts(AndTermContext.class);
        }

        public AndTermContext andTerm(int i) {
            return getRuleContext(AndTermContext.class, i);
        }

        public List<OrTermContext> orTerm() {
            return getRuleContexts(OrTermContext.class);
        }

        public OrTermContext orTerm(int i) {
            return getRuleContext(OrTermContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_search;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterSearch(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitSearch(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitSearch(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SimpleTermContext extends ParserRuleContext {
        public SimpleTermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TermContext term() {
            return getRuleContext(TermContext.class, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_simpleTerm;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterSimpleTerm(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitSimpleTerm(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitSimpleTerm(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class AndTermContext extends ParserRuleContext {
        public AndTermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TermContext term() {
            return getRuleContext(TermContext.class, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_andTerm;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterAndTerm(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitAndTerm(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitAndTerm(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OrTermContext extends ParserRuleContext {
        public OrTermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode TERM_OR() {
            return getToken(DataSetSearch.TERM_OR, 0);
        }

        public TermContext term() {
            return getRuleContext(TermContext.class, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orTerm;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterOrTerm(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitOrTerm(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitOrTerm(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TermContext extends ParserRuleContext {
        public TermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TermValueGroupContext termValueGroup() {
            return getRuleContext(TermValueGroupContext.class, 0);
        }

        public TermTargetContext termTarget() {
            return getRuleContext(TermTargetContext.class, 0);
        }

        public TerminalNode TARGET_SEPARATOR() {
            return getToken(DataSetSearch.TARGET_SEPARATOR, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_term;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterTerm(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitTerm(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitTerm(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TermTargetContext extends ParserRuleContext {
        public TermTargetContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode TARGET() {
            return getToken(DataSetSearch.TARGET, 0);
        }

        public TerminalNode STRING() {
            return getToken(DataSetSearch.STRING, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_termTarget;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterTermTarget(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitTermTarget(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitTermTarget(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TermValueGroupContext extends ParserRuleContext {
        public TermValueGroupContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode TERM_GROUP_START() {
            return getToken(DataSetSearch.TERM_GROUP_START, 0);
        }

        public TerminalNode TERM_GROUP_END() {
            return getToken(DataSetSearch.TERM_GROUP_END, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        public SimpleValueContext simpleValue() {
            return getRuleContext(SimpleValueContext.class, 0);
        }

        public List<AndValueContext> andValue() {
            return getRuleContexts(AndValueContext.class);
        }

        public AndValueContext andValue(int i) {
            return getRuleContext(AndValueContext.class, i);
        }

        public List<OrValueContext> orValue() {
            return getRuleContexts(OrValueContext.class);
        }

        public OrValueContext orValue(int i) {
            return getRuleContext(OrValueContext.class, i);
        }

        public List<UnprotectedOrValueContext> unprotectedOrValue() {
            return getRuleContexts(UnprotectedOrValueContext.class);
        }

        public UnprotectedOrValueContext unprotectedOrValue(int i) {
            return getRuleContext(UnprotectedOrValueContext.class, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_termValueGroup;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterTermValueGroup(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitTermValueGroup(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitTermValueGroup(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SimpleValueContext extends ParserRuleContext {
        public SimpleValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TermValueContext termValue() {
            return getRuleContext(TermValueContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_simpleValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterSimpleValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitSimpleValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitSimpleValue(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TermValueContext extends ParserRuleContext {
        public TermValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode STRING() {
            return getToken(DataSetSearch.STRING, 0);
        }

        public TerminalNode TARGET() {
            return getToken(DataSetSearch.TARGET, 0);
        }

        public TerminalNode ANY() {
            return getToken(DataSetSearch.ANY, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_termValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterTermValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitTermValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitTermValue(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class AndValueContext extends ParserRuleContext {
        public AndValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TermValueContext termValue() {
            return getRuleContext(TermValueContext.class, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_andValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterAndValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitAndValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitAndValue(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OrValueContext extends ParserRuleContext {
        public OrValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode TERM_OR() {
            return getToken(DataSetSearch.TERM_OR, 0);
        }

        public TermValueContext termValue() {
            return getRuleContext(TermValueContext.class, 0);
        }

        public List<TerminalNode> WS() {
            return getTokens(DataSetSearch.WS);
        }

        public TerminalNode WS(int i) {
            return getToken(DataSetSearch.WS, i);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).enterOrValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener) ((DataSetSearchListener) listener).exitOrValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitOrValue(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class UnprotectedOrValueContext extends ParserRuleContext {
        public UnprotectedOrValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode TERM_OR() {
            return getToken(DataSetSearch.TERM_OR, 0);
        }

        public TermValueContext termValue() {
            return getRuleContext(TermValueContext.class, 0);
        }

        @Override
        public int getRuleIndex() {
            return RULE_unprotectedOrValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener)
                ((DataSetSearchListener) listener).enterUnprotectedOrValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof DataSetSearchListener)
                ((DataSetSearchListener) listener).exitUnprotectedOrValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof DataSetSearchVisitor)
                return ((DataSetSearchVisitor<? extends T>) visitor).visitUnprotectedOrValue(this);
            else return visitor.visitChildren(this);
        }
    }
}
