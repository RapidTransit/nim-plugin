package org.nim.grammar;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

public class NimParserUtil extends GeneratedParserUtilBase {

    private static final Key<Integer> INDENT_LEVEL_KEY = Key.create("INDENT_LEVEL");

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

    public static boolean putIndent(@NotNull PsiBuilder builder, int level){
        Integer data = builder.getUserData(INDENT_LEVEL_KEY);
        if(data == null){
            builder.putUserData(INDENT_LEVEL_KEY, 1);
        } else {
            builder.putUserData(INDENT_LEVEL_KEY, ++data);
        }
        return true;
    }

    public static boolean decreaseIndent(@NotNull PsiBuilder builder, int level){
        Integer data = builder.getUserData(INDENT_LEVEL_KEY);
        if(data == null){
            builder.putUserData(INDENT_LEVEL_KEY, 0);
        } else {
            builder.putUserData(INDENT_LEVEL_KEY, --data);
        }
        return true;
    }
}
