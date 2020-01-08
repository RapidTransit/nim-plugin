package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import lombok.Getter;
import org.nim.psi.NimParameterDeclaration;
import org.nim.stubs.TypeInfo;

@Getter
public class NimParameterDeclarationStub extends StubBase<NimParameterDeclaration> {

    private final TypeInfo type;

    protected NimParameterDeclarationStub(StubElement parent, IStubElementType elementType, TypeInfo type) {
        super(parent, elementType);
        this.type = type;
    }


}
