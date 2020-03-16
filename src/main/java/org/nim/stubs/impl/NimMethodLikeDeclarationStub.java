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

public class NimMethodLikeDeclarationStub extends NamedStubBase<NimMethodLikeDeclarationMixin> {

    public static final String NAME = "METHOD_LIKE_DECLARATION";

    public static final NimStubAdapter<NimMethodLikeDeclarationStub, NimMethodLikeDeclaration> ADAPTER =

            new NimStubAdapter<>(NAME) {
                @Override
                public NimMethodLikeDeclaration createPsi(@NotNull NimMethodLikeDeclarationStub stub) {
                    return new NimMethodLikeDeclarationImpl(stub, this);
                }

                @NotNull
                @Override
                public NimMethodLikeDeclarationStub createStub(@NotNull NimMethodLikeDeclaration psi, StubElement parentStub) {
                    return new NimMethodLikeDeclarationStub(
                            parentStub,
                            this,
                            psi.getName(),
                            packFlags(
                                    psi.isExported(),
                                    psi.getMethodType()),
                            new TypeInfo(psi.getReturnType().getStructureName()));
                }

                @Override
                public void serialize(@NotNull NimMethodLikeDeclarationStub stub, @NotNull StubOutputStream ds) throws IOException {
                    ds.writeName(stub.getName());
                    ds.writeInt(stub.flags);
                    ds.writeUTFFast(stub.returnType.getName());
                }

                @NotNull
                @Override
                public NimMethodLikeDeclarationStub deserialize(@NotNull StubInputStream ds, StubElement parentStub) throws IOException {
                    return new NimMethodLikeDeclarationStub(
                            parentStub,
                            this,
                            ds.readName(),
                            ds.readInt(),
                            new TypeInfo(ds.readUTFFast()));
                }

                @Override
                public void indexStub(@NotNull NimMethodLikeDeclarationStub stub, @NotNull IndexSink sink) {
                    if(stub.getName() != null) {
                        sink.occurrence(StubIndexKey.createIndexKey(stub.getName()), stub);
                    }
                }
            };


    private static final int EXPORTED = 0x04;

    private final int flags;
    private final TypeInfo returnType;

    protected NimMethodLikeDeclarationStub(StubElement parent, @NotNull IStubElementType elementType,
                                           @Nullable StringRef name, int flags, TypeInfo returnType) {
        super(parent, elementType, name);
        this.flags = flags;
        this.returnType = returnType;
    }

    protected NimMethodLikeDeclarationStub(StubElement parent, @NotNull IStubElementType elementType,
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
