package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimTypeStructureDeclaration;
import org.nim.psi.extension.NimNamedElement;
import org.nim.psi.types.NimType;
import org.nim.stubs.impl.NimTypeStructureStub;

public abstract class NimTypeStructureDeclarationMixin extends NimStubbedElement<NimTypeStructureStub>
        implements NimTypeStructureDeclaration, NimType, NimNamedElement {

    public NimTypeStructureDeclarationMixin(@NotNull NimTypeStructureStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimTypeStructureDeclarationMixin(@NotNull ASTNode node) {
        super(node);
    }



    @Nullable
    @Override
    //@todo: Fix this
    public PsiElement getNameIdentifier() {
        return getIdentifier();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceType() {
        NimTypeStructureStub stub = getStub();
        if (stub != null) {
            return stub.isReferenceType();
        }
        return getReference() != null;
    }

    @Override
    public boolean isEnum() {
        NimTypeStructureStub stub = getStub();
        if (stub != null) {
            return stub.isEnumeration();
        }
        return getEnum() != null;
    }

    @Override
    public boolean isExported() {
        NimTypeStructureStub stub = getStub();
        if (stub != null) {
            return stub.isExported();
        }
        return getStar() != null;
    }


}
