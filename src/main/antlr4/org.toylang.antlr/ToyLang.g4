// a very early version of the grammar

grammar ToyLang;

// PARSER


toyFile
    :   packageDef? (importStatement*) statement* EOF
    ;
statement
    :   block
    |   forStatement
    |   varDeclaration
    |   annotationDeclaration
    |   methodDeclaration
    |   ifStatement
    |   goStatement
    |   whileStatement
    |   returnStatement
    |   classDef
    |   expression SEMI
    |   CONTINUE SEMI
    |   BREAK SEMI
    |   SEMI
    ;
packageDef
    :   PACK qualifiedName SEMI
    ;
returnStatement
    :   RETURN expression SEMI
    |   RETURN SEMI
    ;
ifStatement
    :   IF expression statement (ELSE statement)?
    ;
goStatement
    :   GO funCall SEMI
    ;
whileStatement
    :   WHILE expression statement
    |   DO statement WHILE expression SEMI
    ;
forStatement
    :   FOR '('? IDENTIFIER range ')'? statement
    |   FOR '('? (decl=varDeclaration? | (init=expression? SEMI)) cond=expression? SEMI after=paramList? ')'? statement
    ;
range
    :   'range' expression (inc|dec) expression
    ;
inc
    :   'upto'
    |   'to'
    ;
dec :   'downto'
    ;
importStatement
    :  'import' qualifiedName ('.' '*')? SEMI
    ;
qualifiedName
    :   IDENTIFIER ('.' IDENTIFIER)*
    ;
modifier
    :   (PUB|PRIV)
    ;
annotation
    :   AT qualifiedName ('(' paramDef (',' paramDef)* ')')?
    ;
annotationDeclaration
    :   AT INTER '{' (paramDef (',' paramDef)*)? '}'
    ;
methodDeclaration
    :   (annotation*) (modifier*) FUN IDENTIFIER '(' (paramDef (',' paramDef)*)? ')' block
    |   FUN? IDENTIFIER '(' (paramDef (',' paramDef)*)? ')' ASSIGNMENT expression SEMI
    ;
paramDef
    :   IDENTIFIER
    ;
block
    :   '{' statement* '}'
    ;
varDeclaration
    :   (modifier*) VAR IDENTIFIER ASSIGNMENT expression SEMI
    |   (modifier*) VAR IDENTIFIER SEMI
    ;
classDef
    :   (modifier*) CLASS IDENTIFIER ('(' fields=paramList ')')? (':' impl=paramList)? block
    ;
expression
    :   literal
    |   expression '.' qualifiedName
    |   expression '.' funCall
    |   funCall
    |   qualifiedName
    |   listIdx
    |   list
    |   dict
    |   (ADD|SUB|NOT) expression
    |   '(' expression ')'
    |   expression (EXP) expression
    |   expression (MULT|DIV|MOD) expression
    |   expression (ADD|SUB)  expression
    |   expression (GT|LT|GTE|LTE|EQUALS|NOT_EQUAL) expression
    |   expression (AND|OR) expression
    |   varAssignment
    ;
varAssignment
    :   <assoc=right>
        qualifiedName
        (   ASSIGNMENT
        |   ADD_ASSIGNMENT
        |   SUB_ASSIGNMENT
        |   MULT_ASSIGNMENT
        |   DIV_ASSIGNMENT
        |   MOD_ASSIGNMENT
        |   EXP_ASSIGNMENT
        )
        expression
        |
        listIdx ASSIGNMENT expression
    ;
funCall
    :   IDENTIFIER '(' paramList? ')'
    ;
paramList
    :   param (',' param)*
    ;
listIdx
    :   qualifiedName ('[' expression ']')*
    ;
list
    :   '[' paramList? ']'
    ;
dict
    :   '{' dictParamList? '}'
    ;
dictParamList
    :   dictParam (',' dictParam)*
    ;
dictParam
    :   expression ':' expression
    ;
param
    :   expression
    ;
literal
    :   number
    |   booleanLiteral
    |   stringLiteral
    |   'null'
    ;
stringLiteral
    :   StringLiteral
    ;
booleanLiteral
    :   TRUE
    |   FALSE
    ;
number
    : INT | HEX | FLOAT | HEX_FLOAT
    ;
// LEXER
StringLiteral
    :   '"' StringCharacters? '"'
    ;
fragment
StringCharacters
    :   StringCharacter+
    ;
fragment
StringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;
// §3.10.6 Escape Sequences for Character and String Literals
fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;
fragment
OctalDigit
    :   [0-7]
    ;
fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

// Keywords

FUN     :   'fun';
IF      :   'if';
DO      :   'do';
GO      :   'go';
ELSE    :   'else';
WHILE   :   'while';
FOR     :   'for';
VAR     :   'var';
TRUE    :   'true';
BREAK   :   'break';
FALSE   :   'false';
RETURN  :   'return';
CLASS   :   'class';
PUB     :   'public';
PRIV    :   'private';
PACK    :   'package';
CONTINUE:   'continue';
ANNO    :   'annotation';
INTER   :   'interface';


NULL    :   'null';

IMPORT  :   'import';
SEMI    :   ';';




IDENTIFIER
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

WS  :  [ \t\r\n\u000C]+ -> skip
    ;


COMMENT
    :   '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> channel(HIDDEN)
    ;

INT
    : Digit+
    ;

HEX
    : '0' [xX] HexDigit+
    ;

FLOAT
    : Digit+ '.' Digit* ExponentPart?
    | '.' Digit+ ExponentPart?
    | Digit+ ExponentPart
    ;

HEX_FLOAT
    : '0' [xX] HexDigit+ '.' HexDigit* HexExponentPart?
    | '0' [xX] '.' HexDigit+ HexExponentPart?
    | '0' [xX] HexDigit+ HexExponentPart
    ;

fragment
ExponentPart
    : [eE] [+-]? Digit+
    ;

fragment
HexExponentPart
    : [pP] [+-]? Digit+
    ;

fragment
Digit
    : [0-9]
    ;

fragment
HexDigit
    : [0-9a-fA-F]
    ;

// Operators
ASSIGNMENT
    : '='
    ;
GT
    : '>'
    ;
GTE
    : '>='
    ;
LTE
    : '<='
    ;
LT
    : '<'
    ;
EQUALS
    : '=='
    ;
NOT
    : '!'
    ;
NOT_EQUAL
    : '!='
    ;
COLON
    : ':'
    ;
AND
    : '&&'
    ;
OR
    : '||'
    ;
ADD
    : '+'
    ;
SUB
    : '-'
    ;
MULT
    : '*'
    ;
DIV
    : '/'
    ;
MOD
    : '%'
    ;
EXP
    : '**'
    ;
BT_LS
    : '<<'
    ;
BT_RS
    : '>>'
    ;
BT_ARS
    : '>>>'
    ;
BT_NOT
    : '~'
    ;
BT_AND
    : '&'
    ;
BT_OR
    : '|'
    ;
BT_XOR
    : '^'
    ;
ADD_ASSIGNMENT
    : '+='
    ;
SUB_ASSIGNMENT
    : '-='
    ;
MULT_ASSIGNMENT
    : '*='
    ;
DIV_ASSIGNMENT
    : '/='
    ;
MOD_ASSIGNMENT
    : '%='
    ;
EXP_ASSIGNMENT
    : '**='
    ;
AT
    : '@'
    ;