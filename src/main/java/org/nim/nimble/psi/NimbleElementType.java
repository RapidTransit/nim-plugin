package org.nim.nimble.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.nim.lang.NimLanguage;

public class NimbleElementType extends IElementType {
    public NimbleElementType(@NotNull @NonNls String debugName) {
        super(debugName, NimLanguage.INSTANCE);
    }
}
