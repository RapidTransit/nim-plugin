package org.nim.nimble.psi.mixin;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Base Class for all Nimble Stubbed Psi Elements
 *
 * All Mixins for Nimble Psi Elements that Have a stub in nimble.bnf will extend this
 * @param <T>
 */
public abstract class NimbleStubbedElement<T extends StubBase<?>> extends StubBasedPsiElementBase<T> {
    public NimbleStubbedElement(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimbleStubbedElement(@NotNull ASTNode node) {
        super(node);
    }

    public NimbleStubbedElement(T stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + getNode().getElementType().toString() + ")";
    }
}
