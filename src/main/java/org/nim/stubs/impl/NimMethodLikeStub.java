package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.BitUtil;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.grammar.MethodType;
import org.nim.psi.mixin.NimMethodLikeDeclarationMixin;
import org.nim.stubs.TypeInfo;

public class NimMethodLikeStub extends NamedStubBase<NimMethodLikeDeclarationMixin> {

    private static final int EXPORTED = 0x04;

    private final long value;
    private final TypeInfo returnType;

    protected NimMethodLikeStub(StubElement parent, @NotNull IStubElementType elementType,
                                @Nullable StringRef name, long value, TypeInfo returnType) {
        super(parent, elementType, name);
        this.value = value;
        this.returnType = returnType;
    }

    protected NimMethodLikeStub(StubElement parent, @NotNull IStubElementType elementType,
                                @Nullable String name, long value, TypeInfo returnType) {
        super(parent, elementType, name);
        this.value = value;
        this.returnType = returnType;
    }

    public TypeInfo getReturnType() {
        return returnType;
    }

    public MethodType getType(){
        return MethodType.TYPES[(int) value & 3];
    }

    public boolean isExported(){
        return BitUtil.isSet(value, EXPORTED);
    }

}
