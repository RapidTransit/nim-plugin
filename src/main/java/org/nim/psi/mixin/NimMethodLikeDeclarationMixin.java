package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.grammar.MethodType;
import org.nim.psi.NimMemberTypeAssignment;
import org.nim.psi.NimMethodLikeDeclaration;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.extension.MethodLikeType;
import org.nim.psi.extension.NimNamedElement;
import org.nim.psi.types.NimPrimitiveType;
import org.nim.psi.types.NimType;
import org.nim.stubs.impl.NimMethodLikeStub;
import org.nim.stubs.impl.NimStructureType;

import java.util.Optional;

public abstract class NimMethodLikeDeclarationMixin extends NimStubbedElement<NimMethodLikeStub>
        implements NimNamedElement, NimMethodLikeDeclaration, MethodLikeType {

    private final NotNullLazyValue<NimType> returnType;

    public NimMethodLikeDeclarationMixin(@NotNull NimMethodLikeStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
        returnType = new AtomicNotNullLazyValue<NimType>() {
            @NotNull
            @Override
            protected NimType compute() {
                return new NimStructureType(stub.getReturnType().getName());
            }
        };
    }

    public NimMethodLikeDeclarationMixin(@NotNull ASTNode node) {
        super(node);
        returnType = new AtomicNotNullLazyValue<NimType>() {
            @NotNull
            @Override
            protected NimType compute() {
                NimMemberTypeAssignment returnType;
                if((returnType = getMemberTypeAssignment()) != null){
                    returnType.getIdentifier();
                }
                return NimPrimitiveType.VOID;
            }
        };
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

    @Override
    public MethodType getMethodType() {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getIdentifyingElement() {
        return null;
    }

    @Override
    public NimType getReturnType() {
        return null;
    }
}
