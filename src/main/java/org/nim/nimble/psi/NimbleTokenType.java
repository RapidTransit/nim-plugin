package org.nim.nimble.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.nim.lang.NimLanguage;

public class NimbleTokenType extends IElementType {
    public NimbleTokenType(@NotNull @NonNls String debugName) {
        super(debugName, NimLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "NimbleTokenType." + super.toString();
    }
}
