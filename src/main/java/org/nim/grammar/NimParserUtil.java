package org.nim.grammar;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class NimParserUtil extends GeneratedParserUtilBase {

    enum BlockType {
        NONE, TYPE, PROC
    }

    private static final Key<ParserData> PARSER_DATA_KEY = Key.create("PARSER_DATA");
    private static final Key<Integer> INDENT_LEVEL_KEY = Key.create("INDENT_LEVEL");
    private static final Key<BlockType> BLOCK_TYPE_KEY = Key.create("BLOCK_TYPE");
    private static final Key<Integer> BLOCK_LEVEL_KEY = Key.create("MAINTAIN_BLOCK");

    public static boolean beginParsing(@NotNull PsiBuilder builder, int level){
        builder.putUserData(PARSER_DATA_KEY, new ParserData());
        return true;
    }

    public static boolean beginTypeBlock(@NotNull PsiBuilder builder, int level){
        final ParserData parserData = getParserData(builder);
        parserData.blocks.push(new Block(BlockType.TYPE, parserData.indent));
        return true;
    }

    public static boolean isInType(@NotNull PsiBuilder builder, int _level){
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return false;
        final Block peek = parserData.blocks.peek();
        return parserData.indent > peek.indent && peek.blockType == BlockType.TYPE;
    }

    public static boolean isNotInType(@NotNull PsiBuilder builder, int _level){
        boolean isNotIn =  isInType(builder, _level);
        return isNotIn;
    }
    public static boolean requiresIndent(@NotNull PsiBuilder builder, int level){
        Integer data = builder.getUserData(INDENT_LEVEL_KEY);
        if(data == null) return false;
        return data >= 1;
    }

    public static boolean requiresNoIndent(@NotNull PsiBuilder builder, int level){
        Integer data = builder.getUserData(INDENT_LEVEL_KEY);
        if(data == null){
            builder.putUserData(INDENT_LEVEL_KEY, 0);
            return true;
        }
        return data == 0;
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



    protected static Integer getIndent(PsiBuilder builder){
        return builder.getUserData(INDENT_LEVEL_KEY);
    }

    protected static Integer getBlockLevel(PsiBuilder builder){
        return builder.getUserData(BLOCK_LEVEL_KEY);
    }

    protected static BlockType getBlockType(PsiBuilder builder){
        return builder.getUserData(BLOCK_TYPE_KEY);
    }

    protected static void putIndent(PsiBuilder builder, Integer value){
        builder.putUserData(INDENT_LEVEL_KEY, value);
    }

    protected static void putBlockLevel(PsiBuilder builder, Integer value){
        builder.putUserData(BLOCK_LEVEL_KEY, value);
    }

    protected static void putBlockType(PsiBuilder builder, BlockType value){
        builder.putUserData(BLOCK_TYPE_KEY, value);
    }

    protected static ParserData getParserData(PsiBuilder builder){
        return builder.getUserData(PARSER_DATA_KEY);
    }


    protected static class ParserData implements Serializable {
        private int indent;
        private Deque<Block> blocks = new ArrayDeque<>();


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
