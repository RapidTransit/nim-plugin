package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimParameter;
import org.nim.stubs.TypeInfo;


public class NimParameterStub extends NamedStubBase<NimParameter> {


    protected NimParameterStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name) {
        super(parent, elementType, name);

    }

    protected NimParameterStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable String name) {
        super(parent, elementType, name);
    }

    public TypeInfo getType(){
        if(myParent instanceof NimParameterDeclarationStub){
            return ((NimParameterDeclarationStub) myParent).getType();
        }
        return null;
    }
}
