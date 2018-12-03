package org.nim.grammar;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimTokenTypes;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class NimParserUtil extends GeneratedParserUtilBase {

    enum BlockType {
        NONE, TYPE, TYPE_DEFINITION, METHOD
    }

    private static final Key<ParserData> PARSER_DATA_KEY = Key.create("PARSER_DATA");

    /**
     * Of Can be used as an operator or as a branch statement
     * @param builder
     * @param level
     * @return true if it is the first non whitespace character
     */
    public static boolean isNotOperator(@NotNull PsiBuilder builder, int level){
        IElementType couldBeWhiteSpace = builder.rawLookup(-1);
        IElementType shouldntBeNewLine = builder.rawLookup(-2);
        if(couldBeWhiteSpace == NimTokenTypes.WHITE_SPACE){
            return shouldntBeNewLine == NimTokenTypes.CRLF;
        }
        return shouldntBeNewLine == NimTokenTypes.CRLF;
    }

    /**
     * Of Can be used as an operator or as a branch statement ie:
     *
     * From marshal.nim
     * proc storeAny(s: Stream, a: Any, stored: var IntSet) =
     *   case a.kind
     *   of akNone: assert false
     *   of akBool: s.write($getBool(a))
     *   of akChar:
     *     let ch = getChar(a)
     *     if ch < '\128':
     *       s.write(escapeJson($ch))
     *     else:
     *       s.write($int(ch))
     *
     * @param builder
     * @param level
     * @return true if it is not the first non whitespace character
     */
    public static boolean isOperator(@NotNull PsiBuilder builder, int level){
        IElementType couldBeWhiteSpace = builder.rawLookup(-1);
        IElementType shouldntBeNewLine = builder.rawLookup(-2);
        if(couldBeWhiteSpace == NimTokenTypes.WHITE_SPACE){
            return shouldntBeNewLine != NimTokenTypes.CRLF;
        }
        return shouldntBeNewLine != NimTokenTypes.CRLF;
    }

    /**
     * Tuple Disambiguation
     * @param builder
     * @param level
     * @return
     */
    public static boolean noWhiteSpace(@NotNull PsiBuilder builder, int level){
        IElementType shouldntBeWhitespace = builder.rawLookup(-1);
        return shouldntBeWhitespace != NimTokenTypes.WHITE_SPACE;
    }

    // For Debug Stepping
    public static boolean endCurrent(@NotNull PsiBuilder builder, int level){
        final IElementType type = builder.lookAhead(0);
        final IElementType type1 = builder.lookAhead(1);
        final IElementType type2 = builder.lookAhead(2);
        final IElementType type3 = builder.lookAhead(3);
        final IElementType type4 = builder.lookAhead(4);
        final IElementType type5 = builder.lookAhead(5);
        final IElementType type6 = builder.lookAhead(6);
        return true;
    }

    public static boolean beginParsing(@NotNull PsiBuilder builder, int level){
        builder.putUserData(PARSER_DATA_KEY, new ParserData());
        return true;
    }

    public static boolean beginTypeDefinitionBlock(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.blocks.push(new Block(BlockType.TYPE_DEFINITION, parserData.indent));
        return true;
    }

    public static boolean endTypeDefinitionBlock(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        final Block peek = parserData.blocks.peek();
        if(peek != null && peek.blockType == BlockType.TYPE_DEFINITION){
            parserData.blocks.poll();
        }
        return true;
    }

    public static boolean isInTypeDefinition(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return false;
        final Block peek = parserData.blocks.peek();
        return parserData.indent > peek.indent && peek.blockType == BlockType.TYPE_DEFINITION;
    }

    public static boolean beginTypeBlock(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.blocks.push(new Block(BlockType.TYPE, parserData.indent));
        return true;
    }

    public static boolean startProcExpression(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.inProcExpression = true;
        return true;
    }

    public static boolean endProcExpression(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.inProcExpression = false;
        return true;
    }
    public static boolean inProcExpression(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        return parserData.inProcExpression;
    }


    public static boolean endTypeBlock(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        final Block peek = parserData.blocks.peek();
        if(peek != null && peek.blockType == BlockType.TYPE){
            parserData.blocks.poll();
        }
        return true;
    }

    public static boolean isInType(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return false;
        final Block peek = parserData.blocks.peek();
        return parserData.indent > peek.indent && peek.blockType == BlockType.TYPE;
    }

    public static boolean isNotInType(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return true;
        final Block peek = parserData.blocks.peek();
        boolean notIn =  parserData.indent > peek.indent && peek.blockType == BlockType.TYPE;
        return notIn;
    }

    public static boolean increaseIndent(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.indent++;
        return true;
    }

    public static boolean decreaseIndent(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.indent--;
        return true;
    }

    public static boolean enterMode(@NotNull PsiBuilder builder_, @SuppressWarnings("UnusedParameters") int level, String mode) {
        TObjectIntHashMap<String> flags = getParserData(builder_).flags;
        if (!flags.increment(mode)) flags.put(mode, 1);
        return true;
    }

//    public static boolean braceMatchEnter(@NotNull PsiBuilder builder_, @SuppressWarnings("UnusedParameters") int level) {
//        builder_.
//        return true;
//    }

    private static boolean exitMode(@NotNull PsiBuilder builder_, @SuppressWarnings("UnusedParameters") int level, String mode, boolean safe) {
        TObjectIntHashMap<String> flags = getParserData(builder_).flags;
        int count = flags.get(mode);
        if (count == 1) flags.remove(mode);
        else if (count > 1) flags.put(mode, count - 1);
        else if (!safe) builder_.error("Could not exit inactive '" + mode + "' mode at offset " + builder_.getCurrentOffset());
        return true;
    }

    public static boolean exitMode(@NotNull PsiBuilder builder_, @SuppressWarnings("UnusedParameters") int level, String mode) {
        return exitMode(builder_, level,mode, false);
    }

    public static boolean exitModeSafe(@NotNull PsiBuilder builder_, @SuppressWarnings("UnusedParameters") int level, String mode) {
        return exitMode(builder_, level,mode, true);
    }

    protected static ParserData getParserData(PsiBuilder builder){
        return builder.getUserData(PARSER_DATA_KEY);
    }


    protected static class ParserData implements Serializable {
        private int indent;
        private Deque<Block> blocks = new ArrayDeque<>();
        TObjectIntHashMap<String> flags = new TObjectIntHashMap<>();
        private boolean inProcExpression;
    }

    protected static class Block implements Serializable {
        private final BlockType blockType;
        private final int indent;

        public Block(BlockType blockType, int indent) {
            this.blockType = blockType;
            this.indent = indent;
        }
    }
}
