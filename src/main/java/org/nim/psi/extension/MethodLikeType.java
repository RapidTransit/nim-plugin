package org.nim.psi.extension;

import com.intellij.psi.PsiElement;
import org.nim.grammar.MethodType;
import org.nim.psi.types.NimType;

public interface MethodLikeType extends PsiElement {
    MethodType getMethodType();

    NimType getReturnType();
}
