package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.extension.NimNamedElement;

public abstract class NimNamedStub<T extends NimNamedElement> extends NamedStubBase<T> {

    private final boolean exported;

    protected NimNamedStub(StubElement parent,
                           @NotNull IStubElementType elementType,
                           @Nullable StringRef name,
                           boolean exported) {
        super(parent, elementType, name);
        this.exported = exported;
    }

    protected NimNamedStub(StubElement parent,
                           @NotNull IStubElementType elementType,
                           @Nullable String name,
                           boolean exported) {
        super(parent, elementType, name);
        this.exported = exported;
    }

    public boolean isExported() {
        return exported;
    }
}
