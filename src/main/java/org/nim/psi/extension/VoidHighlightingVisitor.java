package org.nim.psi.extension;

import com.intellij.lang.annotation.AnnotationHolder;
import org.jetbrains.annotations.NotNull;

public interface VoidHighlightingVisitor extends HighlightingVisitor<Void> {


    void highlight(@NotNull AnnotationHolder holder);

    @Override
    default void highlight(@NotNull AnnotationHolder holder, Void information){
        highlight(holder);
    }
}
