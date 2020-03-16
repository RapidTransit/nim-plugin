package org.nim.color;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimMethodLikeDeclaration;


import java.util.Optional;

public class HighlightingAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if(element instanceof NimMethodLikeDeclaration){
            var proc = (NimMethodLikeDeclaration) element;

            Optional.ofNullable(proc.getSymbol())
                    .ifPresent(nimSymbol ->
                            holder.createInfoAnnotation(nimSymbol, null)
                                    .setTextAttributes(DefaultLanguageHighlighterColors.INSTANCE_METHOD));


            if(proc.getGenerics() != null){
                holder.createInfoAnnotation(proc.getGenerics(), null).setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);
            }
        }
    }


}
