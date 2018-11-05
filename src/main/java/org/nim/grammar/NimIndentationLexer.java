//package org.nim.grammar;
//
//import com.intellij.lexer.FlexLexer;
//import com.intellij.lexer.Lexer;
//import com.intellij.lexer.MergingLexerAdapter;
//import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.TokenSet;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.coverage.gnu.trove.TIntStack;
//import org.nim.psi.NimTokenType;
//import org.nim.psi.NimTypes;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Stack;
//
///**
// * Some copy-pasta from Python Plugin
// */
//public class NimIndentationLexer extends MergingLexerAdapter {
//
//    protected final TIntStack myIndentStack = new TIntStack();
//    protected int myBraceLevel;
//    protected boolean myLineHasSignificantTokens;
//    protected int myLastNewLineIndent = -1;
//    private int myCurrentNewLineIndent = 0;
//
//    protected List<PendingToken> myTokenQueue = new ArrayList<>();
//    private int myLineBreakBeforeFirstCommentIndex = -1;
//    protected boolean myProcessSpecialTokensPending = false;
//
//    private final Stack<String> myFStringStack = new Stack<>();
//    private static final boolean DUMP_TOKENS = false;
////    private final TokenSet RECOVERY_TOKENS = TokenSet.create(DEF_KEYWORD, CLASS_KEYWORD, RETURN_KEYWORD, WITH_KEYWORD, WHILE_KEYWORD, BREAK_KEYWORD, CONTINUE_KEYWORD,
////            RAISE_KEYWORD, TRY_KEYWORD, EXCEPT_KEYWORD, FINALLY_KEYWORD);
//
//
//    public NimIndentationLexer(FlexLexer original, TokenSet tokensToMerge) {
//        super(new NimLexerAdapter(original), tokensToMerge);
//    }
//
//    protected static class PendingToken {
//        private IElementType _type;
//        private final int _start;
//        private final int _end;
//
//        public PendingToken(IElementType type, int start, int end) {
//            _type = type;
//            _start = start;
//            _end = end;
//        }
//
//        public IElementType getType() {
//            return _type;
//        }
//
//        public int getStart() {
//            return _start;
//        }
//
//        public int getEnd() {
//            return _end;
//        }
//
//        public void setType(IElementType type) {
//            _type = type;
//        }
//
//        @Override
//        public String toString() {
//            return _type + ":" + _start + "-" + _end;
//        }
//    }
//
//    private static class PendingCommentToken extends PendingToken {
//        private final int myIndent;
//
//        PendingCommentToken(IElementType type, int start, int end, int indent) {
//            super(type, start, end);
//            myIndent = indent;
//        }
//
//        public int getIndent() {
//            return myIndent;
//        }
//    }
//
//    @Nullable
//    protected IElementType getBaseTokenType() {
//        return super.getTokenType();
//    }
//
//    protected int getBaseTokenStart() {
//        return super.getTokenStart();
//    }
//
//    protected int getBaseTokenEnd() {
//        return super.getTokenEnd();
//    }
//
//    @NotNull
//    private String getBaseTokenText() {
//        return getBufferSequence().subSequence(getBaseTokenStart(), getBaseTokenEnd()).toString();
//    }
//
//    private boolean isBaseAt(IElementType tokenType) {
//        return getBaseTokenType() == tokenType;
//    }
//
//    @Override
//    public IElementType getTokenType() {
//        if (myTokenQueue.size() > 0) {
//            return myTokenQueue.get(0).getType();
//        }
//        return super.getTokenType();
//    }
//
//    @Override
//    public int getTokenStart() {
//        if (myTokenQueue.size() > 0) {
//            return myTokenQueue.get(0).getStart();
//        }
//        return super.getTokenStart();
//    }
//
//    @Override
//    public int getTokenEnd() {
//        if (myTokenQueue.size() > 0) {
//            return myTokenQueue.get(0).getEnd();
//        }
//        return super.getTokenEnd();
//    }
//
//    @Override
//    public void advance() {
//        if (getTokenType() == NimTokenType.LINE_BREAK) {
//            final String text = getTokenText();
//            int spaces = 0;
//            for (int i = text.length() - 1; i >= 0; i--) {
//                if (text.charAt(i) == ' ') {
//                    spaces++;
//                }
//                else if (text.charAt(i) == '\t') {
//                    spaces += 8;
//                }
//            }
//            myCurrentNewLineIndent = spaces;
//        }
//        else if (getTokenType() == NimTypes.TAB) {
//            myCurrentNewLineIndent += 8;
//        }
//        if (myTokenQueue.size() > 0) {
//            myTokenQueue.remove(0);
//            if (myProcessSpecialTokensPending) {
//                myProcessSpecialTokensPending = false;
//                processSpecialTokens();
//            }
//        }
//        else {
//            advanceBase();
//            processSpecialTokens();
//        }
//        adjustBraceLevel();
//        if (DUMP_TOKENS) {
//            if (getTokenType() != null) {
//                System.out.print(getTokenStart() + "-" + getTokenEnd() + ":" + getTokenType());
//                if (getTokenType() == NimTypes.LINE_BREAK) {
//                    System.out.println("{" + myBraceLevel + "}");
//                }
//                else {
//                    System.out.print(" ");
//                }
//            }
//        }
//    }
//
//    protected void advanceBase() {
//        super.advance();
//        checkSignificantTokens();
//        checkFString();
//    }
//
//    private void checkFString() {
//        final String tokenText = getBaseTokenText();
//        if (isBaseAt(NimTypes.FSTRING_START)) {
//            final int prefixLength = PyStringLiteralUtil.getPrefixLength(tokenText);
//            final String openingQuotes = tokenText.substring(prefixLength);
//            assert !openingQuotes.isEmpty();
//            myFStringStack.push(openingQuotes);
//        }
//        else if (isBaseAt(NimTypes.FSTRING_END)) {
//            while (!myFStringStack.isEmpty()) {
//                final String lastOpeningQuotes = myFStringStack.pop();
//                if (lastOpeningQuotes.equals(tokenText)) {
//                    break;
//                }
//            }
//        }
//    }
//
//    protected void pushToken(IElementType type, int start, int end) {
//        myTokenQueue.add(new PendingToken(type, start, end));
//    }
//
//    @Override
//    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
//        checkStartState(startOffset, initialState);
//        super.start(buffer, startOffset, endOffset, initialState);
//        setStartState();
//    }
//
//    protected void checkStartState(int startOffset, int initialState) {
//        if (DUMP_TOKENS) {
//            System.out.println("\n--- LEXER START---");
//        }
//    }
//
//    private void setStartState() {
//        myIndentStack.clear();
//        myIndentStack.push(0);
//        myBraceLevel = 0;
//        adjustBraceLevel();
//        myLineHasSignificantTokens = false;
//        checkSignificantTokens();
//        checkFString();
//        if (isBaseAt(NimTypes.SPACE)) {
//            processIndent(0, NimTypes.SPACE);
//        }
//    }
//
//    private void adjustBraceLevel() {
//        final IElementType tokenType = getTokenType();
//        if (NimTypes.OPEN_BRACES.contains(tokenType)) {
//            myBraceLevel++;
//        }
//        else if (NimTypes.CLOSE_BRACES.contains(tokenType)) {
//            myBraceLevel--;
//        }
//        else if (myBraceLevel != 0 && RECOVERY_TOKENS.contains(tokenType)) {
//            myBraceLevel = 0;
//            final int pos = getTokenStart();
//            pushToken(NimTypes.STATEMENT_BREAK, pos, pos);
//            final int indents = myIndentStack.size();
//            for (int i = 0; i < indents - 1; i++) {
//                final int indent = myIndentStack.peek();
//                if (myCurrentNewLineIndent >= indent) {
//                    break;
//                }
//                if (myIndentStack.size() > 1) {
//                    myIndentStack.pop();
//                    pushToken(NimTypes.DEDENT, pos, pos);
//                }
//            }
//            pushToken(NimTypes.LINE_BREAK, pos, pos);
//        }
//    }
//
//    protected void checkSignificantTokens() {
//        IElementType tokenType = getBaseTokenType();
//        if (!NimTypes.WHITESPACE_OR_LINEBREAK.contains(tokenType) && tokenType != getCommentTokenType()) {
//            myLineHasSignificantTokens = true;
//        }
//    }
//
//    protected void processSpecialTokens() {
//        int tokenStart = getBaseTokenStart();
//        if (isBaseAt(NimTypes.LINE_BREAK)) {
//            processLineBreak(tokenStart);
//            if (isBaseAt(getCommentTokenType())) {
//                myLineBreakBeforeFirstCommentIndex = myTokenQueue.size() - 1;
//                while (isBaseAt(getCommentTokenType())) {
//                    // comment at start of line; maybe we need to generate dedent before the comments
//                    final int commentEnd = getBaseTokenEnd();
//                    myTokenQueue.add(new PendingCommentToken(getBaseTokenType(), getBaseTokenStart(), commentEnd, myLastNewLineIndent));
//                    advanceBase();
//                    if (isBaseAt(NimTypes.LINE_BREAK)) {
//                        processLineBreak(getBaseTokenStart());
//                    }
//                    // Treat EOF as an indent of size 0
//                    else if (getBaseTokenType() == null) {
//                        closeDanglingSuitesWithComments(0, commentEnd);
//                    }
//                    else {
//                        break;
//                    }
//                }
//                myLineBreakBeforeFirstCommentIndex = -1;
//            }
//        }
//        else if (isBaseAt(NimTypes.BACKSLASH)) {
//            processBackslash(tokenStart);
//        }
//        else if (isBaseAt(NimTypes.SPACE)) {
//            processSpace();
//        }
//    }
//
//    private void processSpace() {
//        int start = getBaseTokenStart();
//        int end = getBaseTokenEnd();
//        while (getBaseTokenType() == NimTypes.SPACE) {
//            end = getBaseTokenEnd();
//            advanceBase();
//        }
//        if (getBaseTokenType() == NimTypes.LINE_BREAK) {
//            processLineBreak(start);
//        }
//        else if (getBaseTokenType() == NimTypes.BACKSLASH) {
//            processBackslash(start);
//        }
//        else {
//            myTokenQueue.add(new PendingToken(NimTypes.SPACE, start, end));
//        }
//    }
//
//    private void processBackslash(int tokenStart) {
//        PendingToken backslashToken = new PendingToken(getBaseTokenType(), tokenStart, getBaseTokenEnd());
//        myTokenQueue.add(backslashToken);
//        advanceBase();
//        while (NimTypes.WHITESPACE.contains(getBaseTokenType())) {
//            pushCurrentToken();
//            advanceBase();
//        }
//        if (getBaseTokenType() == NimTypes.LINE_BREAK) {
//            backslashToken.setType(NimTypes.SPACE);
//            processInsignificantLineBreak(getBaseTokenStart(), true);
//        }
//        myProcessSpecialTokensPending = true;
//    }
//
//    protected void processLineBreak(int startPos) {
//        // See https://www.python.org/dev/peps/pep-0498/#expression-evaluation
//        final boolean allFStringsAreTripleQuoted = ContainerUtil.and(myFStringStack, quotes -> quotes.length() == 3);
//        final boolean insideImplicitFragmentParentheses = !myFStringStack.isEmpty() && allFStringsAreTripleQuoted;
//        final boolean shouldTerminateFStrings = !myFStringStack.isEmpty() && !allFStringsAreTripleQuoted;
//        if ((myBraceLevel == 0 && !insideImplicitFragmentParentheses) || shouldTerminateFStrings) {
//            if (myLineHasSignificantTokens || shouldTerminateFStrings) {
//                pushToken(NimTypes.STATEMENT_BREAK, startPos, startPos);
//                myFStringStack.clear();
//            }
//            myLineHasSignificantTokens = false;
//            advanceBase();
//            processIndent(startPos, NimTypes.LINE_BREAK);
//        }
//        else {
//            processInsignificantLineBreak(startPos, false);
//        }
//    }
//
//    protected void processInsignificantLineBreak(int startPos,
//                                                 boolean breakStatementOnLineBreak) {
//        // merge whitespace following the line break character into the
//        // line break token
//        int end = getBaseTokenEnd();
//        advanceBase();
//        while (getBaseTokenType() == NimTokenType.SPACE || getBaseTokenType() == NimTypes.TAB ||
//                (!breakStatementOnLineBreak && getBaseTokenType() == NimTokenType.LINE_BREAK)) {
//            end = getBaseTokenEnd();
//            advanceBase();
//        }
//        myTokenQueue.add(new PendingToken(NimTokenType.LINE_BREAK, startPos, end));
//        myProcessSpecialTokensPending = true;
//    }
//
//    protected void processIndent(int whiteSpaceStart, IElementType whitespaceTokenType) {
//        int lastIndent = myIndentStack.peek();
//        int indent = getNextLineIndent();
//        myLastNewLineIndent = indent;
//        // don't generate indent/dedent tokens if a line contains only end-of-line comment and whitespace
//        if (getBaseTokenType() == getCommentTokenType()) {
//            indent = lastIndent;
//        }
//        int whiteSpaceEnd = (getBaseTokenType() == null) ? super.getBufferEnd() : getBaseTokenStart();
//        if (indent > lastIndent) {
//            myIndentStack.push(indent);
//            myTokenQueue.add(new PendingToken(whitespaceTokenType, whiteSpaceStart, whiteSpaceEnd));
//            int insertIndex = skipPrecedingCommentsWithIndent(indent, myTokenQueue.size() - 1);
//            int indentOffset = insertIndex == myTokenQueue.size() ? whiteSpaceEnd : myTokenQueue.get(insertIndex).getStart();
//            myTokenQueue.add(insertIndex, new PendingToken(NimTypes.INDENT, indentOffset, indentOffset));
//        }
//        else if (indent < lastIndent) {
//            closeDanglingSuitesWithComments(indent, whiteSpaceStart);
//            myTokenQueue.add(new PendingToken(whitespaceTokenType, whiteSpaceStart, whiteSpaceEnd));
//        }
//        else {
//            myTokenQueue.add(new PendingToken(whitespaceTokenType, whiteSpaceStart, whiteSpaceEnd));
//        }
//    }
//
//    private void closeDanglingSuitesWithComments(int indent, int whiteSpaceStart) {
//        int lastIndent = myIndentStack.peek();
//
//        int insertIndex = myLineBreakBeforeFirstCommentIndex == -1 ? myTokenQueue.size() : myLineBreakBeforeFirstCommentIndex;
//        int lastSuiteIndent;
//        while (indent < lastIndent) {
//            lastSuiteIndent = myIndentStack.pop();
//            lastIndent = myIndentStack.peek();
//            int dedentOffset = whiteSpaceStart;
//            if (indent > lastIndent) {
//                myTokenQueue.add(new PendingToken(NimTypes.INCONSISTENT_DEDENT, whiteSpaceStart, whiteSpaceStart));
//                insertIndex = myTokenQueue.size();
//            }
//            else {
//                insertIndex = skipPrecedingCommentsWithSameIndentOnSuiteClose(lastSuiteIndent, insertIndex);
//            }
//            if (insertIndex != myTokenQueue.size()) {
//                dedentOffset = myTokenQueue.get(insertIndex).getStart();
//            }
//            myTokenQueue.add(insertIndex, new PendingToken(NimTypes.DEDENT, dedentOffset, dedentOffset));
//            insertIndex++;
//        }
//    }
//
//    protected int skipPrecedingCommentsWithIndent(int indent, int index) {
//        // insert the DEDENT before previous comments that have the same indent as the current token indent
//        boolean foundComment = false;
//        while(index > 0 && myTokenQueue.get(index-1) instanceof PendingCommentToken) {
//            final PendingCommentToken commentToken = (PendingCommentToken)myTokenQueue.get(index - 1);
//            if (commentToken.getIndent() != indent) {
//                break;
//            }
//            foundComment = true;
//            index--;
//            if (index > 1 &&
//                    myTokenQueue.get(index - 1).getType() == NimTypes.LINE_BREAK &&
//                    myTokenQueue.get(index - 2) instanceof PendingCommentToken) {
//                index--;
//            }
//        }
//        return foundComment ? index : myTokenQueue.size();
//    }
//
//    protected int skipPrecedingCommentsWithSameIndentOnSuiteClose(int indent, int anchorIndex) {
//        int result = anchorIndex;
//        for (int i = anchorIndex; i < myTokenQueue.size(); i++) {
//            final PendingToken token = myTokenQueue.get(i);
//            if (token instanceof PendingCommentToken) {
//                if (((PendingCommentToken)token).getIndent() < indent) {
//                    break;
//                }
//                result = i + 1;
//            }
//        }
//        return result;
//    }
//
//    protected int getNextLineIndent() {
//        int indent = 0;
//        while (getBaseTokenType() != null && NimTypes.WHITESPACE_OR_LINEBREAK.contains(getBaseTokenType())) {
//            if (getBaseTokenType() == NimTypes.TAB) {
//                indent = ((indent / 8) + 1) * 8;
//            }
//            else if (getBaseTokenType() == NimTypes.SPACE) {
//                indent++;
//            }
//            else if (getBaseTokenType() == NimTypes.LINE_BREAK) {
//                indent = 0;
//            }
//            advanceBase();
//        }
//        if (getBaseTokenType() == null) {
//            return 0;
//        }
//        return indent;
//    }
//
//    private void pushCurrentToken() {
//        myTokenQueue.add(new PendingToken(getBaseTokenType(), getBaseTokenStart(), getBaseTokenEnd()));
//    }
//
//
//    protected IElementType getCommentTokenType() {
//        return NimTypes.END_OF_LINE_COMMENT;
//    }
//}
