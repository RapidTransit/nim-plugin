package org.nim.color;

import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.nim.lang.NimLanguage;

import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimTokenTypes;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class NimSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("NIM_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey OPIDENT = createTextAttributesKey("NIM_OPIDENT", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION);
    public static final TextAttributesKey STRING = createTextAttributesKey("NIM_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey COMMENT = createTextAttributesKey("NIM_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("NIM_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);

    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] OPIDENT_KEYS = new TextAttributesKey[]{OPIDENT};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return LanguageParserDefinitions.INSTANCE.forLanguage(NimLanguage.INSTANCE).createLexer(null);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (//tokenType.equals(NimTokenTypes.K) ||
            tokenType.equals(NimTokenTypes.ADDR) ||
            tokenType.equals(NimTokenTypes.VAR) ||
            tokenType.equals(NimTokenTypes.LET) ||
            tokenType.equals(NimTokenTypes.CONST) ||
            tokenType.equals(NimTokenTypes.IMPORT) ||
            tokenType.equals(NimTokenTypes.PROC) ||
            tokenType.equals(NimTokenTypes.IF) ||
            tokenType.equals(NimTokenTypes.ELIF) ||
            tokenType.equals(NimTokenTypes.ELSE) ||
            tokenType.equals(NimTokenTypes.FOR) ||
            tokenType.equals(NimTokenTypes.IN) ||
            tokenType.equals(NimTokenTypes.WHILE) ||
            tokenType.equals(NimTokenTypes.CASE) ||
            tokenType.equals(NimTokenTypes.OF) ||
            tokenType.equals(NimTokenTypes.BREAK) ||
            tokenType.equals(NimTokenTypes.NIL) ||
//            tokenType.equals(NimTokenTypes.) ||
//            tokenType.equals(NimTokenTypes.FALSE) ||
            tokenType.equals(NimTokenTypes.BLOCK) ||
            tokenType.equals(NimTokenTypes.DISCARD) ||
            tokenType.equals(NimTokenTypes.RETURN)
           ) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(NimTokenTypes.OPERATOR)) {
            return OPIDENT_KEYS;
        } else if (tokenType.equals(NimTokenTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(NimTokenTypes.STRING_LITERAL)) {
            return STRING_KEYS;
        } else if(tokenType.equals(NimTokenTypes.IDENTIFIER)){
            return IDENTIFIER_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
