package org.nim.stubs;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class NimStubbedElementImpl<T extends StubBase<?>> extends StubBasedPsiElementBase<T> {
    public NimStubbedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimStubbedElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    public NimStubbedElementImpl(T stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }
}
