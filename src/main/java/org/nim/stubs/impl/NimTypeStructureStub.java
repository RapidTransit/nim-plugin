package org.nim.stubs.impl;

import com.intellij.psi.stubs.*;
import com.intellij.util.BitUtil;
import org.jetbrains.annotations.NotNull;

import org.nim.psi.NimTypeStructureDeclaration;

import org.nim.psi.impl.NimTypeStructureDeclarationImpl;
import org.nim.stubs.NimStubAdapter;

import java.io.IOException;

public class NimTypeStructureStub extends StubBase<NimTypeStructureDeclaration> {

    public static final String NAME = "TYPE_STRUCTURE_DECLARATION";

    public static final NimStubAdapter<NimTypeStructureStub, NimTypeStructureDeclaration> ADAPTER =

        new NimStubAdapter<NimTypeStructureStub, NimTypeStructureDeclaration>(NAME) {

            @Override
            public NimTypeStructureDeclaration createPsi(@NotNull NimTypeStructureStub stub) {
                return new NimTypeStructureDeclarationImpl(stub, this);
            }

            @NotNull
            @Override
            public NimTypeStructureStub createStub(@NotNull NimTypeStructureDeclaration psi, StubElement parentStub) {
                return new NimTypeStructureStub(
                        parentStub,
                        this,
                        // Name
                        psi.getName(),
                        // Exported, Enum, Ref Type
                        packFlags(psi.isExported(), psi.isEnum(), psi.isReferenceType()));
            }


            @Override
            public void serialize(@NotNull NimTypeStructureStub stub, @NotNull StubOutputStream ds) throws IOException {
                ds.writeName(stub.name);
                ds.writeInt(stub.flags);
            }

            @NotNull
            @Override
            public NimTypeStructureStub deserialize(@NotNull StubInputStream ds, StubElement parentStub) throws IOException {
                return new NimTypeStructureStub(
                        parentStub,
                        this,
                        // Name
                        ds.readNameString(),
                        // Flags
                        ds.readInt());
            }

            @Override
            public void indexStub(@NotNull NimTypeStructureStub stub, @NotNull IndexSink sink) {
                sink.occurrence(StubIndexKey.createIndexKey(stub.name), stub);
            }
    };

    private static final int EXPORTED = 0x01;
    private static final int ENUMERATION = 0x02;
    private static final int REFERENCE_TYPE = 0x04;

    private final String name;

    private final int flags;

    protected NimTypeStructureStub(StubElement parent, IStubElementType elementType,
                                   String name, int flags) {
        super(parent, elementType);
        this.name = name;
        this.flags = flags;
    }

    public String getName() {
        return name;
    }

    public boolean isExported() {
        return BitUtil.isSet(flags, EXPORTED);
    }

    public boolean isEnumeration() {
        return BitUtil.isSet(flags, ENUMERATION);
    }

    public boolean isReferenceType() {
        return BitUtil.isSet(flags, REFERENCE_TYPE);
    }


    private static int packFlags(boolean exported, boolean enumeration, boolean referenceType){
        int flags = 0;
        if(exported) flags |= EXPORTED;
        if(enumeration) flags |= ENUMERATION;
        if(referenceType) flags |= REFERENCE_TYPE;
        return flags;
    }

}
