package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimStubbedElement;
import org.nim.stubs.impl.NimParameterStub;

public abstract class NimParameterMixin extends NimStubbedElement<NimParameterStub> implements PsiNamedElement {
    public NimParameterMixin(@NotNull NimParameterStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimParameterMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimParameterMixin(NimParameterStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }
}
