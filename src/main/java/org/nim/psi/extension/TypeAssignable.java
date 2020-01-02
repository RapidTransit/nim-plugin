package org.nim.psi.extension;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.types.NimType;

public interface TypeAssignable extends PsiElement {

    @Nullable
    NimType getType();
}
