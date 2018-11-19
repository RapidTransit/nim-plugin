package org.nim.grammar;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import gnu.trove.TObjectIntHashMap;
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
