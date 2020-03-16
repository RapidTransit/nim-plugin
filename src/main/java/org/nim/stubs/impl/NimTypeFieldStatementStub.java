package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.nim.psi.NimTypeFieldStatements;
import org.nim.stubs.NimNamedStub;

public class NimTypeFieldStatementStub extends NimNamedStub<NimTypeFieldStatements> {
    protected NimTypeFieldStatementStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, boolean _public) {
        super(parent, elementType, name, _public);
    }

    protected NimTypeFieldStatementStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, boolean _public) {
        super(parent, elementType, name, _public);
    }
}
