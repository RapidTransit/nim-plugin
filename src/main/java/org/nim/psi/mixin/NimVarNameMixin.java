package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.grammar.VariableType;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimVarAssignment;
import org.nim.psi.NimVariableDeclaration;
import org.nim.psi.extension.NimType;
import org.nim.psi.extension.NimVariableDeclarationExtension;
import org.nim.psi.extension.NimVarNameExt;
import org.nim.stubs.impl.NimVarNameStub;

import java.util.List;

public abstract class NimVarNameMixin extends NimStubbedElement<NimVarNameStub> implements NimVarNameExt {

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
    public VariableType getDeclarationType() {
        NimVarNameStub stub = getStub();
        if (stub != null) {
            return stub.getType();
        }
        PsiElement parent = getParent();
        if(parent instanceof NimVariableDeclarationExtension){
            ((NimVariableDeclarationExtension) parent).getVariableType();
        }
        return null;
    }

    @Override
    public NimType getType() {
        return null;
    }

    @Override
    public int getPositionalIndex() {
        NimVarNameStub stub = getStub();
        if (stub != null) {
            return stub.getPositionalIndex();
        }
        PsiElement parent = getParent();
        if(parent instanceof NimVariableDeclaration){
            List<NimVarAssignment> varAssignmentList = ((NimVariableDeclaration) parent).getVarAssignmentList();
        }
        return -1;
    }

    @Override
    public int getStatementIndex() {
        NimVarNameStub stub = getStub();
        if (stub != null) {
            return stub.getStatementIndex();
        }
        PsiElement parent = getParent();
        if(parent instanceof NimVariableDeclaration){
            List<NimVarAssignment> varAssignmentList = ((NimVariableDeclaration) parent).getVarAssignmentList();
        }
        return -1;
    }


    @Nullable
    private NimVarAssignment getStatement() {
        PsiElement parent = getParent();
        if(parent instanceof NimVariableDeclaration){
            List<NimVarAssignment> varAssignmentList = ((NimVariableDeclaration) parent).getVarAssignmentList();
            for(var assignment : varAssignmentList){
                if(assignment.getVarNameList().contains(this)){

                }
            }
        }
        return null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

}
