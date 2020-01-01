package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimParameter;

@Getter
public class NimParameterStub extends NamedStubBase<NimParameter> {

    private final String type;

    protected NimParameterStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, String type) {
        super(parent, elementType, name);
        this.type = type;
    }

    protected NimParameterStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, String type) {
        super(parent, elementType, name);
        this.type = type;
    }
}
