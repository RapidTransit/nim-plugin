package org.nim.color;

import com.intellij.codeInsight.daemon.impl.HighlightVisitor;
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class NimHighlightVisitor implements HighlightVisitor {
    @Override
    public boolean suitableForFile(@NotNull PsiFile file) {
        return false;
    }

    @Override
    public void visit(@NotNull PsiElement element) {

    }

    @Override
    public boolean analyze(@NotNull PsiFile file, boolean updateWholeFile, @NotNull HighlightInfoHolder holder, @NotNull Runnable action) {
        return false;
    }

    @NotNull
    @Override
    public HighlightVisitor clone() {
        return null;
    }
}
