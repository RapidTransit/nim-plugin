package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.nim.psi.NimVarAssignment;

public class NimVarAssignmentStub extends StubBase<NimVarAssignment> {

    protected NimVarAssignmentStub(StubElement parent, IStubElementType elementType) {
        super(parent, elementType);
    }
}
