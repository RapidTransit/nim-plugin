package org.nim.psi.mixin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.nim.grammar.NimParserUtil;
import org.nim.psi.extension.VariableDeclaration;

public abstract class NimVariableMixin extends ASTWrapperPsiElement implements VariableDeclaration {
    public NimVariableMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public NimParserUtil.VariableType getDeclarationType() {
        return getNode().getUserData(NimParserUtil.VARIABLE_TYPE_KEY);
    }

    @Override
    public int getIndex() {
        return 0;
    }
}
