package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.nim.psi.NimParameterDeclaration;

public class NimParameterDeclarationStub extends StubBase<NimParameterDeclaration> {
    protected NimParameterDeclarationStub(StubElement parent, IStubElementType elementType) {
        super(parent, elementType);
    }
}
