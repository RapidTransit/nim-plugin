package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.nim.psi.NimTypeDeclaration;

public class NimTypeStub extends StubBase<NimTypeDeclaration> {

    protected NimTypeStub(StubElement parent, IStubElementType elementType) {
        super(parent, elementType);
    }
}
