package org.nim.stubs;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.lang.NimLanguage;

public abstract class NimStubAdapter<StubT extends StubElement, PsiT extends PsiElement>
        extends IStubElementType<StubT, PsiT> {

    public NimStubAdapter(@NotNull String debugName) {
        super(debugName, NimLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public final String getExternalId() {
        return "nim" + super.toString();
    }
}
