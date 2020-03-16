package org.nim.nimble.psi.ext;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NimblePackageType extends Versioned, PsiNamedElement {



    default PsiElement setName(@NotNull String name) throws IncorrectOperationException {
       throw new IncorrectOperationException("Renaming this Nimble Element is unsupported");
    }
}
