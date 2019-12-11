package org.nim.psi.mixin;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimTokenTypes;
import org.nim.psi.extension.NimNamedElement;
import org.nim.psi.extension.NimType;
import org.nim.stubs.impl.NimTypeStub;

public abstract class NimTypeDeclarationMixin extends StubBasedPsiElementBase<NimTypeStub> implements NimType, NimNamedElement {
    public NimTypeDeclarationMixin(@NotNull NimTypeStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimTypeDeclarationMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimTypeDeclarationMixin(NimTypeStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceType() {
        return findChildByType(NimTokenTypes.REF) != null;
    }

    @Override
    public boolean isEnum() {
        return false;
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Nullable
    @Override
    public PsiElement getIdentifier() {
        return null;
    }
}
