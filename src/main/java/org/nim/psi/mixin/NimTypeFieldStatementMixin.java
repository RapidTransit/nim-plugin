package org.nim.psi.mixin;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimTypeFieldStatements;
import org.nim.psi.extension.NimNamedElement;
import org.nim.stubs.impl.NimTypeFieldStatementStub;

public abstract class NimTypeFieldStatementMixin extends NimStubbedElement<NimTypeFieldStatementStub>
        implements NimNamedElement, NimTypeFieldStatements {

    public NimTypeFieldStatementMixin(@NotNull NimTypeFieldStatementStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimTypeFieldStatementMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimTypeFieldStatementMixin(NimTypeFieldStatementStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Override
    public boolean isExported() {
        return false;
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
