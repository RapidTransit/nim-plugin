package org.nim.psi.extension;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface TypeAssignable extends PsiElement {

    @Nullable
    NimType getType();
}
