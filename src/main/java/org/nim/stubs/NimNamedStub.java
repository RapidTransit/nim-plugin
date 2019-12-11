package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.extension.NimNamedElement;

public abstract class NimNamedStub<T extends NimNamedElement> extends NamedStubBase<T> {

    private final boolean _public;

    protected NimNamedStub(StubElement parent,
                           @NotNull IStubElementType elementType,
                           @Nullable StringRef name,
                           boolean _public) {
        super(parent, elementType, name);
        this._public = _public;
    }

    protected NimNamedStub(StubElement parent,
                           @NotNull IStubElementType elementType,
                           @Nullable String name,
                           boolean _public) {
        super(parent, elementType, name);
        this._public = _public;
    }

    public boolean isPublic() {
        return _public;
    }
}
