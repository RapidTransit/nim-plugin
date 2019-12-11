package org.nim.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimTokenTypes;
import org.nim.psi.extension.NimNamedElement;

public abstract class NimNamedElementImpl<T extends NimNamedStub<?>> extends NimStubbedElementImpl<T> implements NimNamedElement {
    public NimNamedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public NimNamedElementImpl(T stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Override
    public boolean isPublic() {
       T stub = getStub();
       return stub != null ? stub.isPublic() : (findChildByType(NimTokenTypes.STAR) != null);
    }

    @Nullable
    @Override
    public PsiElement getIdentifier() {
        return null;
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
}
