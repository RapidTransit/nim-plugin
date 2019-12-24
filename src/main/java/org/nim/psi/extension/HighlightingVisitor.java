package org.nim.psi.extension;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public interface HighlightingVisitor<T> extends PsiElement {

    void highlight(@NotNull AnnotationHolder holder, T information);
}
