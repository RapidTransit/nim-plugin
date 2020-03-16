package org.nim.psi.mixin;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimVarAssignment;
import org.nim.psi.extension.NimVarAssignmentEx;
import org.nim.stubs.impl.NimVarAssignmentStub;

public abstract class NimVarAssignmentMixin extends NimStubbedElement<NimVarAssignmentStub>
        implements NimVarAssignmentEx, NimVarAssignment {

    public NimVarAssignmentMixin(@NotNull NimVarAssignmentStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimVarAssignmentMixin(@NotNull ASTNode node) {
        super(node);
    }



    @Override
    public int getStatementIndex() {
        return 0;
    }


}
