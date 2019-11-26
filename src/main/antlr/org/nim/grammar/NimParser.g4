parser grammar NimParser;
options{tokenVocab=NimLexer;}


//@parser::header {package org.nim.grammar;}

pathSeparator: FORWARD_SLASH | BACK_SLASH;

symbol
    : BACK_TICK BACK_TICK_IDENTIFIER BACK_TICK
    | IDENTIFIER;

importSymbol: symbol (pathSeparator symbol)* | stringLiteral;

importAs: importSymbol (AS IDENTIFIER)?;

importSection
    : IMPORT (importAs (COMMA importAs)* )
    | IMPORT importAs EXCEPT importSymbol (COMMA importSymbol)*
    | FROM importAs IMPORT importSymbol (COMMA importSymbol)*
    ;

stringLiteral: doubleQuotedStringLiteral | rawStringLiteral | tripleQuoteStringLiteral;

doubleQuotedStringLiteral: DOUBLE_QUOTED_LITERAL;
rawStringLiteral: RAW_STRING;
tripleQuoteStringLiteral: TRIPLE_DOUBLE_STR;