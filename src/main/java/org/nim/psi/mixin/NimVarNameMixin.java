package org.nim.psi.mixin;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.nim.grammar.NimParserUtil;
import org.nim.psi.extension.NimType;
import org.nim.psi.extension.VarName;
import org.nim.stubs.impl.NimVarNameStub;

public abstract class NimVarNameMixin extends StubBasedPsiElementBase<NimVarNameStub> implements VarName {

    public NimVarNameMixin(@NotNull NimVarNameStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimVarNameMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimVarNameMixin(NimVarNameStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Override
    public NimParserUtil.VariableType getDeclarationType() {
        return null;
    }

    @Override
    public NimType getType() {
        return null;
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
