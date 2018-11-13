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
        final ParserData parserData = getParserData(builder);
        if(parserData.blocks.isEmpty()) return false;
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
