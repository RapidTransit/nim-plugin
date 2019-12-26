package org.nim.nimble.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.nimble.psi.NimbleVersionOperator;
import org.nim.nimble.psi.VersionComparator;
import org.nim.nimble.psi.ext.VersionOperator;

public abstract class NimbleVersionOperatorMixin extends ASTWrapperPsiElement implements VersionOperator, NimbleVersionOperator {

    public NimbleVersionOperatorMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    @Override
    public VersionComparator getComparator() {
        if(getVopGt() != null){
            return VersionComparator.GREATER_THAN;
        } else if(getVopGte()!= null){
            return VersionComparator.GREATER_THAN_OR_EQUAL;
        } else if(getVopLt() != null){
            return VersionComparator.LESS_THAN;
        } else if(getVopLte() != null) {
            return VersionComparator.LESS_THAN_OR_EQUAL;
        }
        return null;
    }
}
