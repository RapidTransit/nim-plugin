package org.nim.grammar;

import com.intellij.lexer.DelegateLexer;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.lexer.LexerPosition;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimTokenType;
import org.nim.psi.NimTokenTypes;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;


/**
 * Should I do this in a lexer? ANTLR is a lot more flexible in the lexing and parsing ASFAIK I can't rewrite tokens
 */
public class NimIndentationLexer extends LexerBase {

    public static final IElementType CLRF = new NimTokenType("CLRF");
    private final Deque<IElementType> elements = new ArrayDeque<>();
    private final Deque<StackElement> deque = new ArrayDeque<>();
    private int indentLevel = 0;
    private static final IElementType INDENT = new NimTokenType("INDENT");
    private static final IElementType DEDENT = new NimTokenType("DEDENT");
    private final Lexer delegate;
    private StackElement token;
    private IElementType indentOrDedent;


    private class StackElement {
        private int state;
        private IElementType type;
        private int start;
        private int end;
        private CharSequence bufferSequence;
        private int bufferEnd;

        public StackElement(int state, IElementType type, int start, int end, CharSequence bufferSequence, int bufferEnd) {
            this.state = state;
            this.type = type;
            this.start = start;
            this.end = end;
            this.bufferSequence = bufferSequence;
            this.bufferEnd = bufferEnd;
        }
    }

    public NimIndentationLexer(FlexLexer original) {
        delegate = new FlexAdapter(original);
    }

    @Override
    public void advance() {
        if((this.indentOrDedent = elements.poll()) == null){
            delegate.advance();
            if(delegate.getTokenType() == NimTokenTypes.CRLF){
                final LexerPosition currentPosition = delegate.getCurrentPosition();
                delegate.advance();
                int i = 0;
                while (delegate.getTokenType() == NimTokenTypes.WHITE_SPACE){
                    i++;
                    delegate.advance();
                }
                if((i & 1) == 1){ // Its odd

                } else {
                    int result;
                    if(i == 0 || (result = i / 2) == indentLevel){
                        delegate.restore(currentPosition);
                    } else if(result < indentLevel){
                        int times = indentLevel - result;
                        for(int j = 0; j < times; j++){
                            elements.offer(DEDENT);
                        }
                    } else {
                        int times = result - indentLevel;
                        for(int j = 0; j < times; j++){
                            elements.offer(INDENT);
                        }
                    }
                }
            }
        }


    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        delegate.start(buffer, startOffset, endOffset, initialState);
    }

    @Override
    public int getState() {
        return delegate.getState();
    }

    @Override
    public IElementType getTokenType() {
        return indentOrDedent == null ? delegate.getTokenType() : indentOrDedent;
    }

    @Override
    public int getTokenStart() {

        return delegate.getTokenStart();
    }

    @Override
    public int getTokenEnd() {
        return delegate.getTokenEnd();
    }



    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return delegate.getBufferSequence();
    }

    @Override
    public int getBufferEnd() {
        return delegate.getBufferEnd();
    }

}
