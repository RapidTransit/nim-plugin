package org.nim.color;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum NimColors {

    KEYWORD("Keyword", DefaultLanguageHighlighterColors.KEYWORD),
    FIELD("Types//Field", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
    ;


    private final String name;
    private final TextAttributesKey _default;

    public String getName() {
        return name;
    }

    public TextAttributesKey getDefault() {
        return _default;
    }
}
