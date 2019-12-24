package org.nim.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Super Class to all stubs PsiElements with stubs
 * @param <T>
 */
public abstract class NimStubbedElement<T extends StubBase<?>> extends StubBasedPsiElementBase<T> {
    public NimStubbedElement(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimStubbedElement(@NotNull ASTNode node) {
        super(node);
    }

    public NimStubbedElement(T stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + getNode().getElementType().toString() + ")";
    }
}
