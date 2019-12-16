package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.nim.grammar.NimParserUtil;
import org.nim.psi.NimVarName;


public class NimVarNameStub extends StubBase<NimVarName> {


    private final NimParserUtil.VariableType type;
    private final int index;

    protected NimVarNameStub(StubElement parent, IStubElementType elementType, NimParserUtil.VariableType type, int index) {
        super(parent, elementType);
        this.type = type;
        this.index = index;
    }

    public NimParserUtil.VariableType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
