package org.nim.grammar;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import com.intellij.psi.tree.IElementType;
import gnu.trove.TObjectIntHashMap;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimTokenTypes;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class NimParserUtil extends GeneratedParserUtilBase {

    enum BlockType {
        NONE, TYPE, TYPE_DEFINITION, METHOD_LIKE, CALL
    }

    private static final Key<ParserData> PARSER_DATA_KEY = Key.create("PARSER_DATA");
    public static final Key<VariableType> VARIABLE_TYPE_KEY = Key.create("VARIABLE_TYPE");
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

    public static boolean debug(@NotNull PsiBuilder builder, int level){
        ParserData parserData = getParserData(builder);
        return true;
    }

    public static boolean enterVariableType(@NotNull PsiBuilder builder, int level, VariableType type){
        builder.putUserData(VARIABLE_TYPE_KEY, type);
        return true;
    }

    public static boolean enterVar(@NotNull PsiBuilder builder, int level){
        enterVariableType(builder, level, VariableType.VAR);
        return true;
    }

    public static boolean enterLet(@NotNull PsiBuilder builder, int level){
        enterVariableType(builder, level, VariableType.LET);
        return true;
    }
    public static boolean enterConst(@NotNull PsiBuilder builder, int level){
        enterVariableType(builder, level, VariableType.CONST);
        return true;
    }
    public static boolean exitVariable(@NotNull PsiBuilder builder, int level){
        builder.putUserData(VARIABLE_TYPE_KEY, null);
        return true;
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

    public static boolean addType(@NotNull PsiBuilder builder, int level, ASTNode node){

        node.putUserData(VARIABLE_TYPE_KEY, builder.getUserData(VARIABLE_TYPE_KEY));
        return true;
    }


    public static boolean beginParsing(@NotNull PsiBuilder builder, int level){
        builder.putUserData(PARSER_DATA_KEY, new ParserData());
        return true;
    }




    public static boolean beginCall(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.blocks.push(new Block(BlockType.CALL, parserData.indent, level));
        return true;
    }

    public static boolean endCall(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        final Block peek = parserData.blocks.peek();
        if(peek != null && peek.blockType == BlockType.CALL){
            parserData.blocks.poll();
        }
        return true;
    }


    public static boolean callIndent(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        var block = parserData.blocks.peek();
        return block != null && block.callIndents;
    }

    public static boolean isInCall(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return false;
        final Block peek = parserData.blocks.peek();
        return peek.blockType == BlockType.CALL;
    }


    public static boolean beginMethodLike(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.blocks.push(new Block(BlockType.METHOD_LIKE, parserData.indent, level));
        return true;
    }

    public static boolean endMethodLike(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        final Block peek = parserData.blocks.peek();
        if(peek != null && peek.blockType == BlockType.METHOD_LIKE){
            parserData.blocks.poll();
        }
        return true;
    }

    public static boolean isInMethodLike(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return false;
        final Block peek = parserData.blocks.peek();
        return peek.blockType == BlockType.METHOD_LIKE;
    }

    public static boolean beginTypeDefinitionBlock(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.blocks.push(new Block(BlockType.TYPE_DEFINITION, parserData.indent, level));
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
        parserData.blocks.push(new Block(BlockType.TYPE, parserData.indent, level));
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



    public static boolean hasTrailingParanthesis(@NotNull PsiBuilder builder, int level){
        IElementType type = builder.rawLookup(0);
        return type == NimTokenTypes.PARAN_CLOSE;
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
        var block = parserData.blocks.peek();
        if(block != null && block.blockType == BlockType.CALL){
            block.callIndents = true;
        }
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
        private boolean callIndents;
        private final int level;
        public Block(BlockType blockType, int indent, int level) {
            this.blockType = blockType;
            this.indent = indent;
            this.level = level;
        }
    }
}
