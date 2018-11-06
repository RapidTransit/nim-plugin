package org.nim.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.nim.psi.NimTokenType;
import static org.nim.psi.NimTokenTypes.*;
import com.intellij.psi.TokenType;

%%

%public %class NimLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

    // Digit Classes
    DEC_DIGIT = [0-9]
    OCTAL_DIGIT = [0-7]
    BINARY_DIGIT = [01]
    HEX_DIGIT = [0-9A-Fa-f]
    LETTER = [a-zA-Z\u8000-\uFF00]
    UNDERSCORE = '_'
    WHITE_SPACE=" "
    HEX_LIT =   '0' [xX] HEX_DIGIT+ ( {UNDERSCORE}? {HEX_DIGIT} )*
    DEC_LIT =   {DEC_DIGIT}+ ( {UNDERSCORE}? {HEX_DIGIT} )*
    OCT_LIT =   '0' [ocC] {OCTAL_DIGIT} ( {UNDERSCORE}? {OCTAL_DIGIT} )*
    BIN_LIT =   '0' [bB] {BINARY_DIGIT} ( {UNDERSCORE}? {BINARY_DIGIT} )*
    
    INT_LIT = HEX_LIT|DEC_LIT|OCT_LIT|BIN_LIT

    // We could colapse these
    INT8_LIT = {INT_LIT} ['\'']? ('i' | 'I') '8'
    INT16_LIT = {INT_LIT} ['\'']? ('i' | 'I') '16'
    INT32_LIT = {INT_LIT} ['\'']? ('i' | 'I') '32'
    INT64_LIT = {INT_LIT} ['\'']? ('i' | 'I') '64'

    UINT_LIT = {INT_LIT} ['\'']? ('u' | 'U')
    UINT8_LIT = {INT_LIT} ['\'']? ('u' | 'U') '8'
    UINT16_LIT = {INT_LIT} ['\'']? ('u' | 'U') '16'
    UINT32_LIT = {INT_LIT} ['\'']? ('u' | 'U') '32'
    UINT64_LIT = {INT_LIT} ['\'']? ('u' | 'U') '64'
    EXPONENT = ('e' | 'E' ) ['+' | '-'] {DEC_DIGIT} ( ['_'] {DEC_DIGIT} )*

    FLOAT_LIT = {DEC_DIGIT} (['_'] {DEC_DIGIT})* (('.' {DEC_DIGIT} (['_'] {DEC_DIGIT})* [{EXPONENT}]) |{EXPONENT})
    FLOAT32_SUFFIX = ('f' | 'F') ['32']
    FLOAT32_LIT = {HEX_LIT} '\'' {FLOAT32_SUFFIX}
                | ({FLOAT_LIT} | {DEC_LIT} | {OCT_LIT} | {BIN_LIT}) ['\''] {FLOAT32_SUFFIX}
    FLOAT64_SUFFIX = ( ('f' | 'F') '64' ) | 'd' | 'D'
    FLOAT64_LIT = {HEX_LIT} '\'' {FLOAT64_SUFFIX}
            | ({FLOAT_LIT} | {DEC_LIT} | {OCT_LIT} | {BIN_LIT}) ['\''] {FLOAT64_SUFFIX}
    RAW_STRING="r\""
    BLOCK_COMMENT_START="#["
    BLOCK_COMMENT_END="]#"
    TRIPLE_QUOTE="\"\"\""
    DOUBLE_QUOTE="\""
    HASH=#[^\n\r]*
    ADDR = "addr"
    AND = "and"
    AS = "as"
    ASM = "asm"
    BIND = "bind"
    BLOCK = "block"
    BREAK = "break"
    CASE = "case"
    CAST = "cast"
    CONCEPT ="concept"
    CONSY = "const"
    CONTINUE = "continue"
    CONVERTER="converter"
    DEFER="defer"
    DISCARD="discard"
    DISTINCT="distinct"
    DIV="div"
    DO="do"
    ELIF="elif"
    ELSE="else"
    END="end"
    ENUM="enum"
    EXCEPT="except"
    EXPORT="export"
    FINALLY="finally"
    FOR="for"
    FROM="from"
    FUNC="func"
    IF="if"
    IMPORT="import"
    IN="in"
    INCLUDE="include"
    INTERFACE="interface"
    IS="is"
    ISNOT="isnot"
    ITERATOR="iterator"
    LET="let"
    MACRO="macro"
    METHOD="method"
    MIXIN="mixin"
    MOD="mod"
    NIL="nil"
    NOT="not"
    NOTIN="notin"
    OBJECT="object"
    OF="of"
    OR="or"
    OUT="out"
    PROC="proc"
    PTR="ptr"
    RAISE="raise"
    REF="ref"
    RETURN="return"
    SHL="shl"
    SHR="shr"
    STATIC="static"
    TEMPLATE="template"
    TRY="try"
    TUPLE="tuple"
    TYPE="type"
    USING="using"
    VAR="var"
    WHEN="when"
    WHILE="while"
    XOR="xor"
    YIELD="yield"
    PARAN_DOT_OPEN ="(."
    PARAN_DOT_CLOSE=".)"
    BRACKET_DOT_OPEN="[."
    BRACKET_DOT_CLOSE=".]"
    CURLY_DOT_OPEN="{."
    CURLY_DOT_CLOSE=".}"
    PARAN_OPEN="("
    PARAN_CLOSE=")"
    BRACKET_OPEN="["
    BRACKET_CLOSE="]"
    CURLY_OPEN="{"
    CURLY_CLOSE="}"
    COMMA=","
    SEMI_COLON=";"
    DOUBLE_COLON="::"
    SINGLE_COLON=":"
    EQUAL="="
    COMPARISON=[>|<]=?
    DOUBLE_DOT=".."
    DOT="."
    BRACKET_COLON="[:"
    TILDE_IDENTIFIER='[^~]+'
    TILDE="`"
    STAR="*"
    IDENTIFIER= {LETTER} ([_] {LETTER} | {DEC_DIGIT} )*
    CRLF=[\n|\r\n]





%state CALLABLE CALLABLE_TILDE CALLABLE_ARGUMENTS IN_STRING IN_TRIPLE_STRING COMMENT MULTILINE_COMMENT

%%
   <YYINITIAL> {PROC} {yybegin(CALLABLE);return PROC;}
<YYINITIAL> {

         {PROC} {yybegin(CALLABLE); return PROC;}
         {FLOAT_LIT} {return FLOAT_LIT;}
      {FLOAT32_LIT} {return FLOAT32_LIT;}
      {FLOAT64_LIT} {return FLOAT64_LIT;}

         {INT8_LIT} {return INT8_LIT;}

          {INT16_LIT} {return INT16_LIT;}
          {INT32_LIT} {return INT32_LIT;}
          {INT64_LIT} {return INT64_LIT;}
          {STAR} {return STAR;}
          {UINT_LIT} {return UINT_LIT;}
          {UINT8_LIT} {return UINT8_LIT;}
          {UINT16_LIT} {return UINT16_LIT;}
          {UINT32_LIT} {return UINT32_LIT;}
          {UINT64_LIT} {return UINT64_LIT;}
          {RAW_STRING} {return RAW_STRING;}
          {BLOCK_COMMENT_START} {return BLOCK_COMMENT_START;}
          {BLOCK_COMMENT_END} {return BLOCK_COMMENT_END;}
          {COMPARISON} {return COMPARISON;}
          {HASH} {return HASH;}
          {ADDR} {return ADDR;}
          {AND} {return AND;}
          {AS} {return AS;}
          {ASM} {return ASM;}
          {BIND} {return BIND;}
          {BLOCK} {return BLOCK;}
          {BREAK} {return BREAK;}
          {CASE} {return CASE;}
          {CAST} {return CAST;}
          {CONCEPT} {return CONCEPT;}
          {CONSY} {return CONSY;}
          {CONTINUE} {return CONTINUE;}
          {CONVERTER} {return CONVERTER;}
          {DEFER} {return DEFER;}
          {DISCARD} {return DISCARD;}
          {DISTINCT} {return DISTINCT;}
          {DIV} {return DIV;}
          {DO} {return DO;}
          {ELIF} {return ELIF;}
          {ELSE} {return ELSE;}
          {END} {return END;}
          {ENUM} {return ENUM;}
          {EXCEPT} {return EXCEPT;}
          {EXPORT} {return EXPORT;}
          {FINALLY} {return FINALLY;}
          {FOR} {return FOR;}
          {FROM} {return FROM;}
          {FUNC} {return FUNC;}
          {IF} {return IF;}
          {IMPORT} {return IMPORT;}
          {IN} {return IN;}
          {INCLUDE} {return INCLUDE;}
          {INTERFACE} {return INTERFACE;}
          {IS} {return IS;}
          {ISNOT} {return ISNOT;}
          {ITERATOR} {return ITERATOR;}
          {LET} {return LET;}
          {MACRO} {return MACRO;}
          {METHOD} {return METHOD;}
          {MIXIN} {return MIXIN;}
          {MOD} {return MOD;}
          {NIL} {return NIL;}
          {NOT} {return NOT;}
          {NOTIN} {return NOTIN;}
          {OBJECT} {return OBJECT;}
          {OF} {return OF;}
          {OR} {return OR;}
          {OUT} {return OUT;}
          {PTR} {return PTR;}
          {RAISE} {return RAISE;}
          {REF} {return REF;}
          {RETURN} {return RETURN;}
          {SHL} {return SHL;}
          {SHR} {return SHR;}
          {STATIC} {return STATIC;}
          {TEMPLATE} {return TEMPLATE;}
          {TRY} {return TRY;}
          {TUPLE} {return TUPLE;}
          {TYPE} {return TYPE;}
          {USING} {return USING;}
          {VAR} {return VAR;}
          {WHEN} {return WHEN;}
          {WHILE} {return WHILE;}
          {XOR} {return XOR;}
          {YIELD} {return YIELD;}
          {PARAN_DOT_OPEN} {return PARAN_DOT_OPEN;}
          {PARAN_DOT_CLOSE} {return PARAN_DOT_CLOSE;}
          {BRACKET_DOT_OPEN} {return BRACKET_DOT_OPEN;}
          {BRACKET_DOT_CLOSE} {return BRACKET_DOT_CLOSE;}
          {CURLY_DOT_OPEN} {return CURLY_DOT_OPEN;}
          {CURLY_DOT_CLOSE} {return CURLY_DOT_CLOSE;}
          {PARAN_OPEN} {return PARAN_OPEN;}
          {PARAN_CLOSE} {return PARAN_CLOSE;}
          {BRACKET_OPEN} {return BRACKET_OPEN;}
          {BRACKET_CLOSE} {return BRACKET_CLOSE;}
          {CURLY_OPEN} {return CURLY_OPEN;}
          {CURLY_CLOSE} {return CURLY_CLOSE;}
          {COMMA} {return COMMA;}
          {SEMI_COLON} {return SEMI_COLON;}
          {DOUBLE_COLON} {return DOUBLE_COLON;}
          {SINGLE_COLON} {return SINGLE_COLON;}
          {EQUAL} {return EQUAL;}
          {DOUBLE_DOT} {return DOUBLE_DOT;}
          {DOT} {return DOT;}
          {BRACKET_COLON} {return BRACKET_COLON;}
          {TILDE} {return TILDE;}

          {CRLF} {return CRLF;}
          {WHITE_SPACE} {return WHITE_SPACE;}
             {IDENTIFIER} {return IDENTIFIER;}
}

<CALLABLE>{
      {PROC} {return PROC;}
      {TILDE} {yybegin(CALLABLE_TILDE);}
      {IDENTIFIER} {return IDENTIFIER;}
      {BRACKET_OPEN} {return BRACKET_OPEN;}
      {BRACKET_CLOSE} {return BRACKET_CLOSE;}
      {PARAN_OPEN} {yybegin(CALLABLE_ARGUMENTS); return PARAN_OPEN; }
      {PARAN_CLOSE} {return PARAN_CLOSE; }
      {STAR} {return STAR;}
      {CURLY_DOT_OPEN} {return CURLY_DOT_OPEN; }
      {CURLY_DOT_CLOSE} {return CURLY_DOT_CLOSE; }
      {EQUAL} { yybegin(YYINITIAL); return EQUAL;}
         {WHITE_SPACE} {return WHITE_SPACE;}
}
<CALLABLE_ARGUMENTS>{
   {WHITE_SPACE} {return WHITE_SPACE;}
      {PROC} {return PROC;}

      {BRACKET_OPEN} {return BRACKET_OPEN;}
      {BRACKET_CLOSE} {return BRACKET_CLOSE;}
      {COMMA} {return COMMA; }
      {PARAN_OPEN} {return PARAN_OPEN; }
      {PARAN_CLOSE} {yybegin(CALLABLE); return PARAN_CLOSE;}
       {SINGLE_COLON} {return SINGLE_COLON;}
      {VAR} {return VAR; }
      {EQUAL} {return EQUAL; }
            {IDENTIFIER} {return IDENTIFIER;}
}