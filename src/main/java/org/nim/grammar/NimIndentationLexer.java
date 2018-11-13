package org.nim.grammar;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.coverage.gnu.trove.TIntStack;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

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
            List<StackElement> internal = new ArrayList<>();
            internal.add(new StackElement(delegate));
            int state = delegate.getState();
            int start = delegate.getTokenStart();
            int bufferEnd = delegate.getBufferEnd();
            delegate.advance();


            int i = 0;
            while (delegate.getTokenType() == WHITE_SPACE){
                internal.add(new StackElement(delegate));
                i++;
                delegate.advance();
            }
            if(NimParserDefinition.COMMENTS.contains(delegate.getTokenType())
                || CRLF == delegate.getTokenType()){
                internal.forEach(elements::offer);
                delegate.advance();
                elements.offer(new StackElement(delegate));
                return;
            } else {
//                elements.offer(new StackElement(delegate));
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


    protected void skipInsignificant(){
        while (NimParserDefinition.COMMENTS.contains(delegate.getTokenType())){
            delegate.advance();
        }
    }

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        delegate.start(buffer, startOffset, endOffset, initialState);
    }

    @Override
    public int getState() {
        return token == null ? delegate.getState() : token.getState();
    }

    @Override
    public IElementType getTokenType() {
        return token == null ? delegate.getTokenType() : Objects.requireNonNull(token.getType());
    }

    @Override
    public int getTokenStart() {

        return token == null ? delegate.getTokenStart() : token.getStart();
    }

    @Override
    public int getTokenEnd() {
        return token == null ? delegate.getTokenEnd() : token.getEnd();
    }


    @NotNull
    @Override
    public CharSequence getTokenSequence() {
        return token == null ?
                delegate.getBufferSequence().subSequence(delegate.getTokenStart(), delegate.getTokenEnd())
                : token.getBufferSequence().subSequence(token.getStart(), token.getEnd());
    }



    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return token == null ? delegate.getBufferSequence() : token.getBufferSequence();
    }

    @Override
    public int getBufferEnd() {
        return token == null ? delegate.getBufferEnd() : token.getBufferEnd();
    }

}
