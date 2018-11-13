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
    private final NimLexer original;
    private int previousSignificantSpace = 0;
    private final Lexer delegate;
    private StackElement token;


    public NimIndentationLexer(NimLexer original) {
        this.original = original;
        delegate = new FlexAdapter(original);
    }

    @Override
    public void advance() {
        if(elements.isEmpty()){
            tryToAddToStack();
        }
        this.token = elements.poll();
    }




    private void tryToAddToStack() {
        delegate.advance();
        if(delegate.getTokenType() == CRLF) {
            StackElement holder = new StackElement(delegate);
            List<List<StackElement>> spaces = new ArrayList<>();
            List<StackElement> newLines = new ArrayList<>();

            while (delegate.getTokenType() == CRLF){
                holder = new StackElement(delegate);
                newLines.add(holder);
                List<StackElement> stack = new ArrayList<>();
                spaces.add(stack);
                delegate.advance();
                while (delegate.getTokenType() == WHITE_SPACE){
                    stack.add(new StackElement(delegate));
                    delegate.advance();
                }

            }
            StackElement lastLine = newLines.get(newLines.size() - 1);
            List<StackElement> spaceElements = spaces.get(newLines.size() - 1);
            int whiteSpaceCount = spaceElements.size();
            if (delegate.getTokenType() == null) {
                while (indentStack.size() > 0) {
                    indentStack.pop();
                    elements.offer(new StackElement(holder.getState(), DEDENT, holder.getStart(), holder.getEnd()));
                }
            } else { // Need to Add Last Token
                if (previousSignificantSpace == whiteSpaceCount) {
                    dumpTokens(spaces, newLines);
                    elements.offer(new StackElement(delegate));
                } else if (previousSignificantSpace > whiteSpaceCount) {
                    if(newLines.size() > 1){
                        dumpAllButLastTokens(spaces, newLines);
                    }
                    int previous = previousSignificantSpace;
                    while (previous > whiteSpaceCount) {
                        previous = previous - indentStack.pop();

                            elements.offer(new StackElement(holder.getState(), DEDENT, holder.getStart(), holder.getEnd()));

                    }
                    elements.offer(new StackElement(delegate));
                    this.previousSignificantSpace = previous;
                } else {
                    if(newLines.size() > 1){
                        dumpAllButLastTokens(spaces, newLines);
                    }
                    elements.offer(
                            new StackElement(lastLine.getState(), INDENT, lastLine.getStart(), spaceElements.get(spaceElements.size() -1).getEnd())
                    );
                    elements.offer(new StackElement(delegate));
                    this.indentStack.push(whiteSpaceCount - previousSignificantSpace);
                    this.previousSignificantSpace = whiteSpaceCount;
                }


            }

        } else if(delegate.getTokenType() == null){
                while(indentStack.size() > 0) {
                    indentStack.pop();
                    elements.offer(new StackElement(0, DEDENT, getTokenEnd(), getTokenEnd()));
                }
        } else {
            elements.offer(new StackElement(delegate));
        }
    }

    private void dumpTokens(List<List<StackElement>> spaces, List<StackElement> newLines) {
        int i = 0;
        for(List<StackElement> _spaces : spaces){
            elements.offer(newLines.get(i++));
            _spaces.forEach(elements::offer);
        }
    }

    private void dumpAllButLastTokens(List<List<StackElement>> spaces, List<StackElement> newLines) {
        dumpTokens(spaces.subList(0, spaces.size() - 1), newLines.subList(0, newLines.size()- 1));
    }


    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        delegate.start(buffer, startOffset, endOffset, initialState);
    }

    @Override
    public int getState() {
        return token.getState();
    }

    @Override
    public IElementType getTokenType() {
        if(token != null){
            return token.getType();
        } else {
            final IElementType tokenType = delegate.getTokenType();
            if(tokenType != null){
                token = new StackElement(delegate);
                return tokenType;
            }
            return null;
        }
    }

    @Override
    public int getTokenStart() {
        return  token.getStart();
    }

    @Override
    public int getTokenEnd() {
        return token.getEnd();
    }


    @NotNull
    @Override
    public CharSequence getTokenSequence() {
        return delegate.getBufferSequence().subSequence(token.getStart(), token.getEnd());
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
