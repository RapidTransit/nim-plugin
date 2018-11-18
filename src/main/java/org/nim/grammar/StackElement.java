package org.nim.grammar;

import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;

class StackElement {
    private final int state;
    private final IElementType type;
    private final int start;
    private final int end;

    public StackElement(Lexer delegate) {
        this.state = delegate.getState();
        this.type = delegate.getTokenType();
        this.start = delegate.getTokenStart();
        this.end = delegate.getTokenEnd();

    }

    public StackElement(int state, IElementType type, int start) {
        this.state = state;
        this.type = type;
        this.start = start;
        this.end = start;
    }

    public int getState() {
        return state;
    }

    public IElementType getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

}
