package org.nim.psi.extension;

import com.intellij.psi.PsiElement;

public interface Exportable extends PsiElement {
    boolean isExported();
}
