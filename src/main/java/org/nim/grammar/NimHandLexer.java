package org.nim.grammar;

import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimTokenType;

import java.util.ArrayDeque;
import java.util.Deque;

public class NimHandLexer extends LexerBase {
    private FlexLexer lexer;
    private char[] charArray;
    private int index;
    private int tokenEndOffset;
    private int bufferEnd;
    private Deque<IElementType> previous = new ArrayDeque<>();
    private int indentLevel = 0;
    private static final IElementType INDENT = new NimTokenType("<<INDENT>>");
    private static final IElementType DEDENT = new NimTokenType("<<DEDENT>>");

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        
    }

    @Override
    public int getState() {
        return 0;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        return null;
    }

    @Override
    public int getTokenStart() {
        return 0;
    }

    @Override
    public int getTokenEnd() {
        return 0;
    }

    @Override
    public void advance() {

    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return null;
    }

    @Override
    public int getBufferEnd() {
        return 0;
    }
}
