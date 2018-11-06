package org.nim.grammar;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

public class NimParserUtil extends GeneratedParserUtilBase {

    private static final Key<Integer> INDENT_LEVEL_KEY = Key.create("INDENT_LEVEL");


    public static boolean indent(@NotNull PsiBuilder builder_, int level, Character c){
        final Integer userData = builder_.getUserData(INDENT_LEVEL_KEY);
        return true;
    }
}
