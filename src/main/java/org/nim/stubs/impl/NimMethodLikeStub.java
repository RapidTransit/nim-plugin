package org.nim.stubs.impl;

import com.intellij.psi.stubs.*;
import com.intellij.util.BitUtil;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.grammar.MethodType;
import org.nim.psi.NimMethodLikeDeclaration;
import org.nim.psi.impl.NimMethodLikeDeclarationImpl;
import org.nim.psi.mixin.NimMethodLikeDeclarationMixin;
import org.nim.stubs.NimStubAdapter;
import org.nim.stubs.TypeInfo;

import java.io.IOException;

public class NimMethodLikeStub extends NamedStubBase<NimMethodLikeDeclarationMixin> {

    public static final String NAME = "METHOD_LIKE";

    public static final NimStubAdapter<NimMethodLikeStub, NimMethodLikeDeclaration> ADAPTER =

            new NimStubAdapter<NimMethodLikeStub, NimMethodLikeDeclaration>(NAME) {
                @Override
                public NimMethodLikeDeclaration createPsi(@NotNull NimMethodLikeStub stub) {
                    return new NimMethodLikeDeclarationImpl(stub, this);
                }

                @NotNull
                @Override
                public NimMethodLikeStub createStub(@NotNull NimMethodLikeDeclaration psi, StubElement parentStub) {
                    return null;
                }

                @Override
                public void serialize(@NotNull NimMethodLikeStub stub, @NotNull StubOutputStream dataStream) throws IOException {

                }

                @NotNull
                @Override
                public NimMethodLikeStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
                    return null;
                }

                @Override
                public void indexStub(@NotNull NimMethodLikeStub stub, @NotNull IndexSink sink) {

                }
            };


    private static final int EXPORTED = 0x04;

    private final int flags;
    private final TypeInfo returnType;

    protected NimMethodLikeStub(StubElement parent, @NotNull IStubElementType elementType,
                                @Nullable StringRef name, int flags, TypeInfo returnType) {
        super(parent, elementType, name);
        this.flags = flags;
        this.returnType = returnType;
    }

    protected NimMethodLikeStub(StubElement parent, @NotNull IStubElementType elementType,
                                @Nullable String name, int flags, TypeInfo returnType) {
        super(parent, elementType, name);
        this.flags = flags;
        this.returnType = returnType;
    }

    public TypeInfo getReturnType() {
        return returnType;
    }

    public MethodType getType(){
        return MethodType.TYPES[ flags & 3];
    }

    public boolean isExported(){
        return BitUtil.isSet(flags, EXPORTED);
    }

    private static int packFlags(boolean exported, MethodType type){
        int flags = 0;
        if(exported) flags |= EXPORTED;
        flags += type.ordinal();
        return flags;
    }
}
