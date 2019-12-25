package org.nim.nimble;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static org.nim.nimble.psi.NimbleTokenTypes.*;

%%

%public %class NimbleLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType


%%
<YYINITIAL> {
       \s+ {return WS;}
      '#'.+ {return COMMENT;}
      "name" {return NAME;}
      "requires" {return REQUIRES;}
      "=" {return EQUAL;}
      "\"" {return D_QUOTE;}
      "." {return DOT;}
      [0-9]+ {return NUMBER;}
      [><]"="? {return OPERATOR;}
      "&" {return AMP;}
}

