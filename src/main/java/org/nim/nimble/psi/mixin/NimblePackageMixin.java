package org.nim.nimble.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Version;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.nimble.psi.NimblePackage;
import org.nim.nimble.psi.ext.NimblePackageType;
import org.nim.nimble.psi.stubs.NimblePackageStub;

public abstract class NimblePackageMixin extends NimbleStubbedElement<NimblePackageStub> implements NimblePackageType, NimblePackage {
    public NimblePackageMixin(@NotNull NimblePackageStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimblePackageMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimblePackageMixin(NimblePackageStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }



    @Nullable
    @Override
    public Version getVersion() {
        NimblePackageStub stub = getStub();
        if (stub != null) {
            Version version = stub.getVersion(); // Not sure if I should check if it is null and try to get it from the AST
            return version;
        }
        return null;
    }
}
