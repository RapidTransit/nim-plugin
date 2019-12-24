package org.nim.psi.extension;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.jetbrains.annotations.Nullable;

public interface NimNamedElement extends PsiNameIdentifierOwner, NavigationItem {
    boolean isExported();
}
