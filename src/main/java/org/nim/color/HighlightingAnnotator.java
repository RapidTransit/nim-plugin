package org.nim.color;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimMethodLike;
import org.nim.psi.NimProcedureMethod;

public class HighlightingAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if(element instanceof NimMethodLike){
            System.out.println("Nim Proc");
            var proc = (NimMethodLike) element;
            holder.createInfoAnnotation(proc.getSymbol(), null).setTextAttributes(DefaultLanguageHighlighterColors.INSTANCE_METHOD);
            if(proc.getGenerics() != null){
                holder.createInfoAnnotation(proc.getGenerics(), null).setTextAttributes(DefaultLanguageHighlighterColors.KEYWORD);
            }
        }
    }
}
