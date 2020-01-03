package org.nim.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimStubbedElement;
import org.nim.psi.NimTokenTypes;
import org.nim.psi.extension.TypeAssignable;
import org.nim.psi.types.NimPrimitiveType;
import org.nim.psi.types.NimType;
import org.nim.psi.types.PrimitiveKind;
import org.nim.stubs.impl.NimLiteralStub;

import java.util.Optional;

public abstract class NimLiteralMixin extends NimStubbedElement<NimLiteralStub> implements TypeAssignable {

    private static final TokenSet LITERALS = TokenSet.create(
            NimTokenTypes.INT_LIT,
            NimTokenTypes.INT8_LIT,
            NimTokenTypes.INT16_LIT,
            NimTokenTypes.INT32_LIT,
            NimTokenTypes.INT64_LIT,
            NimTokenTypes.UINT_LIT,
            NimTokenTypes.UINT8_LIT,
            NimTokenTypes.UINT16_LIT,
            NimTokenTypes.UINT32_LIT,
            NimTokenTypes.UINT64_LIT,
            NimTokenTypes.FLOAT_LIT,
            NimTokenTypes.FLOAT32_LIT,
            NimTokenTypes.FLOAT64_LIT,
            NimTokenTypes.STRING_LITERAL,
            NimTokenTypes.RAW_STRING_LITERAL,
            NimTokenTypes.TRIPLE_QUOTE_STRING_LITERAL,
            NimTokenTypes.NIL
            );

    public NimLiteralMixin(@NotNull NimLiteralStub stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public NimLiteralMixin(@NotNull ASTNode node) {
        super(node);
    }

    public NimLiteralMixin(NimLiteralStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Nullable
    @Override
    public NimType getType() {
        return Optional.ofNullable(findChildByType(LITERALS))
                .map(x-> PrimitiveKind.getKindForElement(((PsiElement) x).getNode().getElementType()))
                .map(x-> x.getType())
                .orElse(null);
    }
}
