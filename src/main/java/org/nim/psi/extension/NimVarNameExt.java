package org.nim.psi.extension;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nullable;
import org.nim.grammar.VariableType;

public interface NimVarNameExt extends PsiElement, PsiNamedElement {

    @Nullable
    VariableType getDeclarationType();


    NimType getType();



    /**
     * Nim Allows multiple var names for tuple unpacking this is the index of each one:
     * ```
     * let
     *  b,a = tuple  <--- Statement Index 0
     *  ^ ^
     *  | |
     *  | +--- Positional Index 1
     *  +----- Positional Index 0
     *  c = callSomeMethod() <--- Statement Index 1
     *  ^
     *  |
     *  +----- Positional Index 0
     * ```
     * @return index
     */
    int getPositionalIndex();


    /**
     * Nim Allows multiple var names for tuple unpacking this is the index of each one:
     * ```
     * let
     *  b,a = tuple  <--- Statement Index 0
     *  ^ ^
     *  | |
     *  | +--- Positional Index 1
     *  +----- Positional Index 0
     *  c = callSomeMethod() <--- Statement Index 1
     *  ^
     *  |
     *  +----- Positional Index 0
     * ```
     * @return index
     */
    int getStatementIndex();
}
