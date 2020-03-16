package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.nim.psi.NimLiteral;

public class NimLiteralStub extends StubBase<NimLiteral> {
    protected NimLiteralStub(StubElement parent, IStubElementType elementType) {
        super(parent, elementType);
    }
}
