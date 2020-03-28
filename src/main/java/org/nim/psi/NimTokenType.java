package org.nim.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILeafElementType;
import org.nim.lang.NimLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NimTokenType extends IElementType {
    public NimTokenType(@NotNull @NonNls String debugName) {
        super(debugName, NimLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "NimTokenType." + super.toString();
    }

    public static class NimLeafTokenType extends NimTokenType implements ILeafElementType {

        public NimLeafTokenType(@NotNull String debugName) {
            super(debugName);
        }

        @NotNull
        @Override
        public ASTNode createLeafNode(@NotNull CharSequence leafText) {
            return new LeafPsiElement(this, "");
        }
    }
}
