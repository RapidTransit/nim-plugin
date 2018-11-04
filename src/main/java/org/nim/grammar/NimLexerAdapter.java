package org.nim.grammar;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;

public class NimLexerAdapter extends FlexAdapter {

    public NimLexerAdapter(@NotNull FlexLexer flex) {
        super(flex);
    }

    public NimLexerAdapter() {
        super(new NimLexer(null));
    }
}
