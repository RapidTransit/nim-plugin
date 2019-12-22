package org.nim.psi.extension;

import com.intellij.lang.annotation.AnnotationHolder;
import org.jetbrains.annotations.NotNull;

public interface HighlightingVisitor<T> {

    void highlight(@NotNull AnnotationHolder holder, T information);
}
