lexer grammar NimLexer;
//@lexer::header {package org.nim.grammar;
//}

tokens { INDENT, DEDENT}

@lexer::members {
      private int previousIndents = -1;
      private int indentLevel = 0;
      private Queue<Token> tokens = new LinkedList<Token>();

       @Override
        public void emit(Token t) {
          if(t.getType() > 0){
              LOG.debug(MARKER, "Found Token: {}, Value: {}", _SYMBOLIC_NAMES[t.getType()], t.getText());
          }
          super.setToken(t);
          tokens.offer(t);
        }

        @Override
        public Token nextToken() {
          super.nextToken();
          return tokens.isEmpty() ? new CommonToken(EOF, "<EOF>") : tokens.poll();
        }


        private void jump(int ttype) {
          indentLevel += (ttype == DEDENT ? -1 : 1);
          CommonToken token = new CommonToken(ttype, "<" + (ttype == DEDENT ? "DEDENT" : "INDENT") + ":" + indentLevel +">");
          token.setLine(getLine());
          emit(token);
        }
}

fragment   LETTER:  [a-zA-Z\u8000-\uFF00];
INDENT: '  ';
FORWARD_SLASH: '/';
BACK_SLASH:'\\';
//OPERATOR: OP_SYMBOLS+ | OP_WORDS;
IDENTIFIER: LETTER ( '_'? LETTER | DIGIT )*;
BLOCK_COMMENT_START:'#[';
BLOCK_COMMENT_END: ']#';
TRIPLE_QUOTE: '"""';
DOUBLE_QUOTE:'"';
SINGLE_LINE_COMMENT:'#' [^\n\r]*;
ADDR: 'addr';
AND: 'and';
AS: 'as';
ASM: 'asm';
BIND: 'bind';
BLOCK: 'block';
BREAK: 'break';
CASE: 'case';
CAST: 'cast';
CONCEPT:'concept';
CONSY: 'const';
CONTINUE: 'continue';
CONVERTER:'converter';
DEFER:'defer';
DISCARD:'discard';
DISTINCT:'distinct';
DIV:'div';
DO:'do';
ELIF:'elif';
ELSE:'else';
END:'end';
ENUM:'enum';
EXCEPT:'except';
EXPORT:'export';
FINALLY:'finally';
FOR:'for';
FROM:'from';
FUNC:'func';
IF:'if';
IMPORT:'import';
IN:'in';
INCLUDE:'include';
INTERFACE:'interface';
IS:'is';
ISNOT:'isnot';
ITERATOR:'iterator';
METHOD:'method';
MIXIN:'mixin';
MOD:'mod';
NIL:'nil';
NOT:'not';
NOTIN:'notin';
OBJECT:'object';
OF:'of';
OR:'or';
OUT:'out';
PROC:'proc';
PTR:'ptr';
RAISE:'raise';
REF:'ref';
RETURN:'return';
SHL:'shl';
SHR:'shr';
STATIC:'static';
TEMPLATE:'template';
TRY:'try';

TYPE:'type';
USING:'using';
VAR:'var';
WHEN:'when';

XOR:'xor';
YIELD:'yield';
PARAN_DOT_OPEN:'(.';
PARAN_DOT_CLOSE:'.)';
BRACKET_DOT_OPEN:'[.';
BRACKET_DOT_CLOSE:'.]';
CURLY_DOT_OPEN:'{.';
CURLY_DOT_CLOSE:'.}';
PARAN_OPEN:'(';
PARAN_CLOSE:')';
BRACKET_OPEN:'[';
BRACKET_CLOSE:']';
CURLY_OPEN:'{';
CURLY_CLOSE:'}';
COMMA:',';
SEMI_COLON:';';
DOUBLE_COLON:'::';
SINGLE_COLON:':';
EQUAL:'=';
//COMPARISON:[>|<]:?
DOUBLE_DOT:'..';
DOT:'.';

BACK_TICK_IDENTIFIER:[^`]*;
BACK_TICK:'`';
STAR:'*';
PLUS:'+';
MINUS:'-';
OP_OR:'|';
PERCENT:'%';
AMP:'&';
DOLLAR:'$';
AT:'@';
CRLF:[\r?\n];


fragment DIGIT: '0'..'9';


HEX_DIGIT: DIGIT | 'A'..'F' | 'a'..'f';
OCT_DIGIT: '0'..'7';
BIN_DIGIT: '0'..'1';
HEX_LIT: '0' ('x' | 'X' ) HEX_DIGIT ( '_'? HEX_DIGIT )*;
DEC_LIT: '-' DIGIT ( '_'? DIGIT )*;
OCT_LIT: '0' 'o' OCT_DIGIT ( '_'? OCT_DIGIT )*;
BIN_LIT: '0' ('b' | 'B' ) BIN_DIGIT ( '_'? BIN_DIGIT )*;

INT_LIT: HEX_LIT
        | DEC_LIT
        | OCT_LIT
        | BIN_LIT;

INT8_LIT: INT_LIT '\''? ('i' | 'I') '8';
INT16_LIT: INT_LIT '\''? ('i' | 'I') '16';
INT32_LIT: INT_LIT '\''? ('i' | 'I') '32';
INT64_LIT: INT_LIT '\''? ('i' | 'I') '64';

UINT_LIT: INT_LIT '\''? ('u' | 'U');
UINT8_LIT: INT_LIT '\''? ('u' | 'U') '8';
UINT16_LIT: INT_LIT '\''? ('u' | 'U') '16';
UINT32_LIT: INT_LIT '\''? ('u' | 'U') '32';
UINT64_LIT: INT_LIT '\''? ('u' | 'U') '64';

EXPONENT: ('e' | 'E' ) ('+' | '-') DIGIT ( '_'? DIGIT )*;
FLOAT_LIT: DIGIT ('_'? DIGIT)* (('.' DIGIT ('_'? DIGIT)* EXPONENT?) |EXPONENT);
FLOAT32_SUFFIX: ('f' | 'F') '32'?;
FLOAT32_LIT: HEX_LIT '\'' FLOAT32_SUFFIX
            | (FLOAT_LIT | DEC_LIT | OCT_LIT | BIN_LIT) '\''? FLOAT32_SUFFIX;
FLOAT64_SUFFIX: ( ('f' | 'F') '64' ) | 'd' | 'D';
FLOAT64_LIT: HEX_LIT '\'' FLOAT64_SUFFIX
            | (FLOAT_LIT | DEC_LIT | OCT_LIT | BIN_LIT) '\''? FLOAT64_SUFFIX;

//NUM_SUFFIX: '\''? [dDiIuUfF] ('8'|'16'|'32'|'64');
//NON_DEC: '0' [bBoxX];