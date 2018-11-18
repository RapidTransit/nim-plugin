package org.nim.grammar;

import com.intellij.lang.TokenWrapper;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.coverage.gnu.trove.TIntStack;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.nim.psi.NimTokenTypes.*;


/**
 * Should I do this in a lexer? ANTLR is a lot more flexible in the lexing and parsing ASFAIK I can't rewrite tokens
 */
public class NimIndentationLexer extends LexerBase {

    private static final Logger logger = Logger.getInstance(NimIndentationLexer.class);

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
    private int previousSignificantSpace = 0;
    private final Lexer delegate;
    private StackElement token;
    private boolean first = true;
    private boolean end = false;
    public NimIndentationLexer(NimLexer original) {
        delegate = new FlexAdapter(original);
    }

    @Override
    public void advance() {

        if(elements.isEmpty() && !end){
            tryToAddToStack();
        }
        this.token = elements.poll();
    }




    private void tryToAddToStack() {
        delegate.advance();
        if(delegate.getTokenType() == CRLF && delegate.getState() != NimLexer.CALLABLE_ARGUMENTS) {
            StackElement holder = new StackElement(delegate);

            List<StackElement> nonSignificantTokens = new ArrayList<>();
            int ws = 0;
            while (delegate.getTokenType() == CRLF){
                holder = new StackElement(delegate);
                nonSignificantTokens.add(holder);

                delegate.advance();
                ws = 0;
                while ( delegate.getTokenType() == WHITE_SPACE
                        || delegate.getTokenType() == DOC_RUNNABLE
                        || delegate.getTokenType() == SINGLE_LINE_COMMENT
                        || delegate.getTokenType() == EXAMPLE){
                    if(delegate.getTokenType() == WHITE_SPACE){
                        ws = delegate.getTokenEnd() - delegate.getTokenStart();
                    }
                    nonSignificantTokens.add(new StackElement(delegate));
                    delegate.advance();
                }

            }
            StackElement lastToken = nonSignificantTokens.get(nonSignificantTokens.size() - 1);

            int whiteSpaceCount = ws;
            logger.info("White Space Count: " + ws);
            if (delegate.getTokenType() == null) {
                logger.info("Reached the end");
                logger.info("Stack Size: " + indentStack.size());
                while (indentStack.size() > 0) {

                    indentStack.pop();
                    logger.info("Stack Size: " + indentStack.size());
                    elements.offer(new StackElement(lastToken.getState(), DEDENT, lastToken.getStart()));
                }
            } else { // Need to Add Last Token
                if (previousSignificantSpace == whiteSpaceCount) {
                    dumpTokens(nonSignificantTokens);
                    elements.offer(new StackElement(delegate));
                } else if (previousSignificantSpace > whiteSpaceCount) {
                    logger.info("More Previous Significant Space, previous: " + previousSignificantSpace);
                    if(nonSignificantTokens.size() > 1){
                        dumpTokens(nonSignificantTokens);
                    }

                    int previous = previousSignificantSpace;
                    while (previous > whiteSpaceCount) {
                        logger.info("Adding Dedent, previous: "+ previous);
                        previous = previous - indentStack.pop();
                        elements.offer(new StackElement(lastToken.getState(), DEDENT, lastToken.getEnd()));
                    }
                    elements.offer(new StackElement(delegate));
                    this.previousSignificantSpace = previous;
                } else {
                    if(nonSignificantTokens.size() > 1){
                        dumpTokens(nonSignificantTokens);
                    }
                    elements.offer(
                            new StackElement(lastToken.getState(), INDENT, lastToken.getEnd())
                    );
                    elements.offer(new StackElement(delegate));
                    this.indentStack.push(whiteSpaceCount - previousSignificantSpace);
                    this.previousSignificantSpace = whiteSpaceCount;
                }


            }

        } else if(delegate.getTokenType() == null){
            end = true;
            logger.info("Reached the end");
            logger.info("Stack Size: " + indentStack.size());
                while(indentStack.size() > 0) {

                    indentStack.pop();
                    logger.info("Stack Size: " + indentStack.size());
                    elements.offer(new StackElement(0, DEDENT, getTokenEnd()));
                }
        } else {
            elements.offer(new StackElement(delegate));
        }
    }

    private void dumpTokens(List<StackElement> newLines) {
        elements.addAll(newLines);
    }


    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        delegate.start(buffer, startOffset, endOffset, initialState);

    }

    @Override
    public int getState() {

        return  /** token == null ? delegate.getState() :*/ token.getState();
    }

    @Override
    public IElementType getTokenType() {

        if(token != null){
            return token.getType();
        } else {
        return delegate.getTokenType();
    }
    }

    @Override
    public int getTokenStart() {
        return token == null ? delegate.getTokenStart() :  token.getStart();
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
                : delegate.getBufferSequence().subSequence(token.getStart(), token.getEnd());
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
