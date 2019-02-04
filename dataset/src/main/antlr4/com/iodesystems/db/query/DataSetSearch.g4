grammar DataSetSearch;

// term, term, term, target:term target:

search: (simpleTerm (andTerm|orTerm)*)? WS* TERM_OR? WS* EOF;
simpleTerm: WS* term WS*;
andTerm: WS+ term;
orTerm: WS* TERM_OR WS+ term;
term: (termTarget TARGET_SEPARATOR WS*)? termValueGroup;
termTarget: TARGET | STRING;
termValueGroup
: '(' WS* (simpleValue WS* (andValue|orValue)* WS*)? ')'
| simpleValue unprotectedOrValue*;
simpleValue: termValue;
termValue: (STRING | TARGET | ANY);
andValue: WS+ termValue;
orValue: WS* TERM_OR WS* termValue;
unprotectedOrValue: TERM_OR termValue;

STRING: '"' ('\\"' | ~'"')* '"' | '\'' ('\\\'' | ~'\'')* '\'';
TARGET: [A-Za-z][a-zA-Z0-9_]*;
TARGET_SEPARATOR: ':';
TERM_OR: ',';
TERM_GROUP_START: '(';
TERM_GROUP_END: ')';
ANY: ~( ' ' | ':' | ',' | '(' | ')')+;
WS: [ \t\r\n]+;
