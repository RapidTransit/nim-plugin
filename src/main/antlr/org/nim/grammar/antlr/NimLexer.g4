lexer grammar NimLexer;
@lexer::header {
import java.util.*;
import jodd.util.StringUtil;

}

tokens { INDENT, DEDENT}

@lexer::members {

      private int previousIndents = -1;
      private int indentLevel = 0;
      private Queue<Token> tokens = new ArrayDeque<Token>();
private boolean textOnly = false;
  private boolean notFirst = false;
  private int p = 0;
  private int textIndent = 0;

       @Override
        public void emit(Token t) {
//          if(t.getType() > 0){
//              LOG.debug(MARKER, "Found Token: {}, Value: {}", _SYMBOLIC_NAMES[t.getType()], t.getText());
//          }
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

  private void newline(){

      int n = getText() == null ? 0 : getText().replaceAll("\r?\n", "").length() / 2;

       if(n > previousIndents && n > 0) {
         jump(INDENT);
         previousIndents = n;
         if(textOnly){
            textIndent = n;
         }
       } else if(n < previousIndents) {
         if(textOnly){
            if(n < textIndent){
              textOnly = false;
              textIndent = -1;
                popMode();
            }
         }
         int times = previousIndents - n;
//         while(object.size() > 0){
//            if(object.peekLast().getName() >= n){
//                object.removeLast();
//            } else {
//                break;
//            }
//         }
         for(int i = 0; i < times; i++){
              jump(DEDENT);
         }
         previousIndents = n;
       } else if(_input.LA(1) == EOF) {
         while(indentLevel > 0) {
           jump(DEDENT);
           if(indentLevel < textIndent){
             textOnly = false;
             textIndent = -1;
           }
         }
       } else {
            skip();
       }

  }

}

fragment Letter:  [a-zA-Z\u8000-\uFF00];

fragment Digit: '0'..'9';

fragment HexDigit: Digit | 'A'..'F' | 'a'..'f';

fragment NumberSuffix: '\''? [dDiIuUfF] ('8'|'16'|'32'|'64');
fragment Exponent: [eE][-+] DecimalLiteral;
fragment NonDecimalPrefix: '0' [bBoxX];

fragment DecimalLiteral: Digit ('_'? Digit)*;
fragment HexLiteral: NonDecimalPrefix HexDigit ('_'? HexDigit)*;
fragment FloatingLiteral: DecimalLiteral '.' DecimalLiteral;
fragment ExponentLiteral: DecimalLiteral Exponent;

NUMBER: (DecimalLiteral | HexLiteral | FloatingLiteral | ExponentLiteral) NumberSuffix?;
FORWARD_SLASH: '/';
BACK_SLASH:'\\';
IDENTIFIER: Letter ( '_'? Letter | Digit )*;
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
COMPARISON:[>|<]'='?;
DOUBLE_DOT:'..';
DOT:'.';

BACK_TICK:'`';
STAR:'*';
PLUS:'+';
MINUS:'-';
OP_OR:'|';
PERCENT:'%';
AMP:'&';
DOLLAR:'$';
AT:'@';
NEW_LINE:[\r?\n] ' '*;
DOUBLE_QUOTED_LITERAL: '"Placeholder"';
TRIPLE_QUOTE_LITERAL: '"""' .* '"""';
fragment TRIPLE_QUOTE_CHAR: ;
RAW_STRING: 'r"' RAW_STRING_CHAR*? '"';
fragment RAW_STRING_CHAR: ~'"' | '""';
mode PROCEDURE_MODE;
BACK_TICK_IDENTIFIER:[^`]+;
PM_NEW_LINE: NEW_LINE {newline();} -> popMode;