package org.nim.nimble.psi.ext;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface IntegerLiteral extends PsiElement {

    @Nullable
    Integer parseInteger();
}
