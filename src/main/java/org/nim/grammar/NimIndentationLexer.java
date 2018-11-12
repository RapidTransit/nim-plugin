package org.nim.grammar;

import com.intellij.lang.ForeignLeafType;
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
import org.jetbrains.coverage.gnu.trove.TIntStack;
import org.nim.psi.NimTokenType;
import org.nim.psi.NimTokenTypes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static org.nim.psi.NimTokenTypes.*;


/**
 * Should I do this in a lexer? ANTLR is a lot more flexible in the lexing and parsing ASFAIK I can't rewrite tokens
 */
public class NimIndentationLexer extends LexerBase {


    /**
     *
     * To deal with this little gem:
     * proc reversed*[T](a: openArray[T]): seq[T] =
     *   ## returns the reverse of the container ``a``.
     *   runnableExamples:
     *       let
     *         a = [1, 2, 3, 4, 5, 6]
     *         b = reversed(a)
     *       doAssert b == @[6, 5, 4, 3, 2, 1]
     */
    private final TIntStack indentStack = new TIntStack();
    private final Deque<StackElement> elements = new ArrayDeque<>();
    private final NimLexer original;
    private int indentLevel = 0;
    private int spaces = 0;

    private final Lexer delegate;
    private StackElement token;



    private static class StackElement {
        private final int state;
        private final IElementType type;
        private final int start;
        private final int end;
        private final CharSequence bufferSequence;
        private final int bufferEnd;


        public StackElement(Lexer delegate) {
            this.state = delegate.getState();
            this.type = delegate.getTokenType();
            this.start = delegate.getTokenStart();
            this.end = delegate.getTokenEnd();
            this.bufferSequence = delegate.getBufferSequence();
            this.bufferEnd = delegate.getBufferEnd();
        }

        public StackElement(int state, IElementType type, int start,  int bufferEnd) {
            this.state = state;
            this.type = type;
            this.start = start;
            this.end = start;
            this.bufferSequence = " ";
            this.bufferEnd = bufferEnd;
        }
    }

    public NimIndentationLexer(NimLexer original) {
        this.original = original;
        delegate = new FlexAdapter(original);
    }

    @Override
    public void advance() {
        this.token = elements.poll();
        if(this.token == null){
            delegate.advance();
            tryToAddToStack();
            this.token = elements.poll();
        }


    }

    private void tryToAddToStack() {


        if(delegate.getTokenType() == CRLF){
            elements.offer(new StackElement(delegate));
            int state = delegate.getState();
            int start = delegate.getTokenStart();
            int bufferEnd = delegate.getBufferEnd();
            delegate.advance();

            List<StackElement> internal = new ArrayList<>();
            int i = 0;
            while (delegate.getTokenType() == WHITE_SPACE){
                internal.add(new StackElement(delegate));
                i++;
                delegate.advance();
            }
            if(delegate.getTokenType() == CRLF){
                return;
            }
            if((i & 1) == 1) { // Its odd

            } else if(i == 0){

                for(int j = 0; indentLevel > 0; j++){
                    indentLevel--;
                    elements.offer(
                            new StackElement(state,
                                    DEDENT, start, bufferEnd)
                    );
                }
            } else {
                int result = i / 2;
                if(result < indentLevel){
                    int times = indentLevel - result;
                    for(int j = 0; j < times; j++){
                        indentLevel--;
                        elements.offer(
                                new StackElement(state,
                                DEDENT, start, bufferEnd)
                        );
                    }
                } else {
                    int times = result - indentLevel;
                    for(int j = 0; j < times; j++){
                        indentLevel++;
                        elements.offer(
                                new StackElement(state,
                                       INDENT, start, bufferEnd));
                    }
                }
            }
            internal.forEach(elements::offer);
            elements.offer(new StackElement(delegate));
        } else if(delegate.getTokenType() == null){
            for(int j = 0; indentLevel > 0; j++){
                int state = delegate.getState();
                int start = delegate.getTokenStart();
                int bufferEnd = delegate.getBufferEnd();
                indentLevel--;
                elements.offer(
                        new StackElement(state,
                                DEDENT, start, bufferEnd)
                );
            }
        }
    }


    protected void findSignificantIndent(){

    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        delegate.start(buffer, startOffset, endOffset, initialState);
    }

    @Override
    public int getState() {
        return token == null ? delegate.getState() : token.state;
    }

    @Override
    public IElementType getTokenType() {
        return token == null ? delegate.getTokenType() : Objects.requireNonNull(token.type);
    }

    @Override
    public int getTokenStart() {

        return token == null ? delegate.getTokenStart() : token.start;
    }

    @Override
    public int getTokenEnd() {
        return token == null ? delegate.getTokenEnd() : token.end;
    }


    @NotNull
    @Override
    public CharSequence getTokenSequence() {
        return token == null ? delegate.getBufferSequence().subSequence(delegate.getTokenStart(), delegate.getTokenEnd()) : token.bufferSequence.subSequence(token.start, token.end);
    }



    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return token == null ? delegate.getBufferSequence() : token.bufferSequence;
    }

    @Override
    public int getBufferEnd() {
        return token == null ? delegate.getBufferEnd() : token.bufferEnd;
    }

}
