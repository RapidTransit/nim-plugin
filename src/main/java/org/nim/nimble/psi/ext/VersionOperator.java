package org.nim.nimble.psi.ext;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.nim.nimble.psi.VersionComparator;

public interface VersionOperator extends PsiElement {

    @Nullable
    VersionComparator getComparator();
}
