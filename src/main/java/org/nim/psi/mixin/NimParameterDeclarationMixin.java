package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.extension.TypeAssignable;
import org.nim.psi.types.NimType;
import org.nim.stubs.impl.NimParameterDeclarationStub;

public abstract class NimParameterDeclarationMixin extends NimStubbedElement<NimParameterDeclarationStub> implements TypeAssignable {
    public NimParameterDeclarationMixin(@NotNull NimParameterDeclarationStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimParameterDeclarationMixin(@NotNull ASTNode node) {
        super(node);
    }



    @Nullable
    @Override
    public NimType getType() {
        return null;
    }
}
