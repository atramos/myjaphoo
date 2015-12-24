grammar FilterLanguage;

filter: (GroovyCode |simpleSearchLiteral | boolexpr  );



boolexpr: brackedBoolExpr
| expr op=('<' | '>' | '=' | '<=' | '>=' | '<>' | 'like'| 'is' | 'startswith' | 'wie' | 'endswith' | 'regex' | 'regexfind' | '~') expr
| expr
| negatedBoolExpr
| boolexpr op='and' boolexpr
| boolexpr op='or' boolexpr
| SimpleRegExSearchAllStatement
| identWithSimpleRegExSearchStatement
;

simpleSearchLiteral: literal;

identWithSimpleRegExSearchStatement: qualifier SimpleRegExSearchAllStatement;

negatedBoolExpr: 'not' brackedBoolExpr;

expr: brackedExpr
| funcCall
| substIdentifier
| number
| DateLiteral
| StringLiteral
| NOTHING
| qualifier
| expr op=('*' | '/') expr
| expr op=('+' | '-') expr
| andLiteral
| orLiteral
| RegexLiteral
;

number: (INT | DOUBLE) (sizePrefix)?;

sizePrefix: KB|MB|GB;

brackedExpr: '(' expr ')';

literal: (ID | StringLiteral);

andLiteral: literal ('&' literal)*;

orLiteral: literal ('|' literal)*;

qualifier: ID ('.' ID)*;

substIdentifier: '$' literal (argList)?;

funcCall: ID argList
| ID '#' simpleArg;

simpleArg: number | literal;

argList: '(' ')' | '(' expr ( ',' expr )* ')';

brackedBoolExpr: '(' boolexpr ')';

group: (GroovyCode | groupInstruction ('--' groupInstruction)* ) ;

groupInstruction:  ('groupby')? ('group' ('by')? )? ('by')? groupExprList havingClause?;

groupExprList: groupExprPart ((','|';') groupExprPart)*;

havingClause: 'having' boolexpr;

groupExprPart:  qualifier  | '(' groupExpr ')';

groupExpr: ifStmt | groupTerm;

groupTerm: expr;

ifStmt: 'if' boolexpr thenStmt (elseIfStmt)* (elseStmt)?;

thenStmt: '{' expr '}';

elseIfStmt: 'elseif' boolexpr thenStmt;

elseStmt: 'else' thenStmt;

// compatibility to old syntax: dates could be defined as string literals with certain format:
DateLiteral
: '"'  DIGIT DIGIT DIGIT DIGIT '/' DIGIT DIGIT '/' DIGIT DIGIT '"' {setText(getText().substring(1, getText().length()-1));}
| '\'' DIGIT DIGIT DIGIT DIGIT '/' DIGIT DIGIT '/' DIGIT DIGIT '\'' {setText(getText().substring(1, getText().length()-1));}
;

StringLiteral
	: '"'  DoubleStringCharacter* '"' {setText(getText().substring(1, getText().length()-1));}
	| '\'' SingleStringCharacter* '\'' {setText(getText().substring(1, getText().length()-1));}
	;
fragment DoubleStringCharacter
	: ~('"' | '\\')
	| '\\' SingleEscapeCharacter
	;

fragment SingleStringCharacter
	: ~('\'' | '\\')
	| '\\' SingleEscapeCharacter
	;

fragment SingleEscapeCharacter
	: '\'' | '"' | '\\' | 'b' | 'f' | 'n' | 'r' | 't' | 'v' | '.' | 'w' | 's' | 'd'
	;

 fragment
 DIGIT : [0-9] ; // match single digit

GroovyCode: 'groovy' .*;

INT     : [0-9]+ ;
DOUBLE: DIGIT+ '.' DIGIT* // match 1. 39. 3.14159 etc...
| '.' DIGIT+ // match .1 .14159
;


WS : [ \t\r\n]+ -> skip ; // toss out whitespace
BlockComment
: '/*' .*? '*/'
-> skip
;
LineComment
: '//' ~[\r\n]*
-> skip
;

fragment RegexFragment: ([a-zA-Z_0-9'.*\\\?\[\]\(\)\+\-\{\}\$\|\:\^\/\&\%\§]*);

REGEX2: '~';
RegexLiteral: '~'RegexFragment | '~' StringLiteral;

SimpleRegExSearchAllStatement: '?' RegexFragment | '?' StringLiteral;

MUL : '*' ; // assigns token name to '*' used above in grammar
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;

LOWER: '<';
HIGHER: '>';
EQUAL: '=';
LOWEREQUALTHEN: '<=';
HIGHEREQUALTHEN:  '>=';
UNEQUAL: '<>';
LIKE: 'like';
IS: 'is';
STARTSWITH: 'startswith';
WIE: 'wie';
ENDSWITH: 'endswith';
REGEX: 'regex';
REGEXFIND: 'regexfind';

AND: 'and';
OR: 'or';
NOT: 'not';
NOTHING: 'nothing'|'null';


KB: 'kb'|'KB';
MB: 'mb'|'MB';
GB: 'gb'|'GB';

ID :	[a-zA-Z_][a-zA-Z_0-9']*;


