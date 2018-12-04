package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimDeclarationColonEquals;

public class DeclarationColonEqualsStub extends NimNamedStub<NimDeclarationColonEquals>{
    protected DeclarationColonEqualsStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, boolean _public) {
        super(parent, elementType, name, _public);
    }

    protected DeclarationColonEqualsStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, boolean _public) {
        super(parent, elementType, name, _public);
    }
}
