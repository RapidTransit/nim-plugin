package org.nim.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.stubs.NimNamedElement;
import org.nim.stubs.NimTypeStub;

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
