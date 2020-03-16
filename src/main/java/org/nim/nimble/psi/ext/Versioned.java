package org.nim.nimble.psi.ext;

import com.intellij.openapi.util.Version;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface Versioned extends PsiElement {

    @Nullable
    Version getVersion();
}
