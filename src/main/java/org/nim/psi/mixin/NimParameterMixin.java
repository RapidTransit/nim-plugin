package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.extension.TypeAssignable;
import org.nim.psi.types.NimType;
import org.nim.stubs.impl.NimParameterStub;

public abstract class NimParameterMixin extends NimStubbedElement<NimParameterStub> implements PsiNamedElement, TypeAssignable {
    public NimParameterMixin(@NotNull NimParameterStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimParameterMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Nullable
    @Override
    public NimType getType() {
        return null;
    }
}
