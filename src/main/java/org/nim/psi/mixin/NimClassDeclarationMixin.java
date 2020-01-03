package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimTypeDeclaration;
import org.nim.psi.extension.NimNamedElement;
import org.nim.psi.types.NimType;
import org.nim.stubs.impl.NimClassStub;

public abstract class NimClassDeclarationMixin extends NimStubbedElement<NimClassStub> implements NimTypeDeclaration, NimType, NimNamedElement {

    public NimClassDeclarationMixin(@NotNull NimClassStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimClassDeclarationMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimClassDeclarationMixin(NimClassStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Nullable
    @Override
    //@todo: Fix this
    public PsiElement getNameIdentifier() {
        if(getReferenceExpressionList().isEmpty()){
            return null;
        }
        return getReferenceExpressionList().get(0);
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceType() {
        NimClassStub stub = getStub();
        if (stub != null) {
            return stub.isReferenceType();
        }
        return getRef() != null;
    }

    @Override
    public boolean isEnum() {
        NimClassStub stub = getStub();
        if (stub != null) {
            return stub.isEnumeration();
        }
        return getEnum() != null;
    }

    @Override
    public boolean isExported() {
        NimClassStub stub = getStub();
        if (stub != null) {
            return stub.isExported();
        }
        return getStar() != null;
    }


}
