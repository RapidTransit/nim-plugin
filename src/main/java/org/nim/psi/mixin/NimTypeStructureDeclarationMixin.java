package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.model.SymbolResolveResult;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.NimFile;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimTypeStructureDeclaration;
import org.nim.psi.extension.NimNamedElement;
import org.nim.psi.types.NimType;
import org.nim.stubs.impl.NimTypeStructureStub;



public abstract class NimTypeStructureDeclarationMixin extends NimStubbedElement<NimTypeStructureStub>
        implements NimTypeStructureDeclaration, NimType, NimNamedElement, PsiReference {

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

    @Override
    public PsiReference getReference() {
        return this;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return getNameIdentifier();
    }

    @NotNull
    @Override
    public PsiElement getElement() {
        return getIdentifier();
    }

    @NotNull
    @Override
    public TextRange getRangeInElement() {
        return getIdentifier().getTextRange();
    }

    @NotNull
    @Override
    //todo: Need to Get Package Also
    public String getCanonicalText() {
       return getIdentifier().getText();
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return false;
    }

    @Override
    public boolean isSoft() {
        return false;
    }

    @Override
    public String getStructureName() {
        return null;
    }

    @Override
    public boolean isAssignableFrom(NimType other) {
        return false;
    }
}
