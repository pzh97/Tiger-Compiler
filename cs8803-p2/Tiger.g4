grammar Tiger;

/*lexical rules */
ARRAY: 'array';
BEGIN: 'begin';
BREAK: 'break';
DO: 'do';
ELSE: 'else';
END: 'end';
ENDDO: 'enddo';
ENDIF: 'endif';
FLOAT: 'float';
FOR: 'for';
FUNCTION: 'function';
IF: 'if';
INT: 'int';
LET: 'let';
OF: 'of';
PROGRAM: 'program';
RETURN: 'return';
STATIC: 'static';
THEN: 'then';
TO: 'to';
TYPE: 'type';
VAR: 'var';
WHILE: 'while';
COMMA: ',';
COLON: ':';
SEMICOLON: ';';
OPENPAREN: '(';
CLOSEPAREN: ')';
OPENBRACK: '[';
CLOSEBRACK: ']';
ASSIGN: ':=';
TASSIGN: '=';
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
POW: '**';
EQ: '==';
NE: '!=';
LT: '<';
GT: '>';
LE: '<=';
GE: '>=';
AND: '&';
OR: '|';
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';
fragment DIGIT: '0'..'9';
ID: (LOWER|UPPER)(LOWER|UPPER|DIGIT|'_')*;
INTLIT: '0' | '1'..'9' (DIGIT)*;
FLOATLIT: INTLIT '.' DIGIT*;
COMMENT: '/*' .*? '*/' -> skip;
WS : [ \t\r\n]+ -> skip;

/*parser rules */
tiger_program: PROGRAM ID LET declaration_segment BEGIN funct_list END EOF;
declaration_segment: type_declaration_list var_declaration_list;
type_declaration_list: type_declaration type_declaration_list | /* epsilon */;
var_declaration_list: var_declaration var_declaration_list | /* epsilon */;
funct_list: funct funct_list | /* epsilon */;
type_declaration: TYPE ID TASSIGN type SEMICOLON;
type: base_type | ARRAY OPENBRACK INTLIT CLOSEBRACK OF base_type | ID;
base_type: INT | FLOAT;
var_declaration: storage_class id_list COLON type optional_init SEMICOLON;
storage_class: VAR | STATIC;
id_list: ID | ID COMMA id_list;
optional_init: ASSIGN const | /* epsilon */;
funct: FUNCTION ID OPENPAREN param_list CLOSEPAREN ret_type BEGIN stat_seq END;
param_list: param param_list_tail | /* epsilon */;
param_list_tail: COMMA param param_list_tail | /* epsilon */;
ret_type: COLON type | /* epsilon */;
param: ID COLON type;
stat_seq: stat | stat stat_seq;
stat: value ASSIGN expr SEMICOLON
      | IF expr THEN stat_seq (ELSE stat_seq)? ENDIF SEMICOLON
      | WHILE expr DO stat_seq ENDDO SEMICOLON
      | FOR ID ASSIGN expr TO expr DO stat_seq ENDDO SEMICOLON
      | optprefix ID OPENPAREN expr_list CLOSEPAREN SEMICOLON 
      | BREAK SEMICOLON 
      | RETURN optreturn SEMICOLON 
      | LET declaration_segment BEGIN stat_seq END
      ;
optreturn: expr | /* epsilon */;
optprefix: value ASSIGN | /* epsilon */;
// TODO:
expr:
  const
  | value
  | expr binary_operator=(ADD | SUB | MUL | DIV ) expr
  | <assoc=right> expr binary_operator=POW expr
  | expr binary_operator=(EQ | NE | LT | GT | LE | GE | AND | OR) expr
  | OPENPAREN expr CLOSEPAREN;
const: INTLIT | FLOATLIT;
expr_list: expr expr_list_tail | /* epsilon */;
expr_list_tail: COMMA expr expr_list_tail | /* epsilon */;
value: ID value_tail;
value_tail: OPENBRACK expr CLOSEBRACK | /* epsilon */;