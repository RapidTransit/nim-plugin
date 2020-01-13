package org.nim.stubs.impl;

import com.intellij.psi.stubs.*;
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
                        // Exported
                        false,
                        // Enumeration
                        false, false);
            }


            @Override
            public void serialize(@NotNull NimTypeStructureStub stub, @NotNull StubOutputStream ds) throws IOException {
                ds.writeName(stub.name);
                ds.writeBoolean(stub.exported);
                ds.writeBoolean(stub.enumeration);
                ds.writeBoolean(stub.referenceType);
            }

            @NotNull
            @Override
            public NimTypeStructureStub deserialize(@NotNull StubInputStream ds, StubElement parentStub) throws IOException {
                return new NimTypeStructureStub(
                        parentStub,
                        this,
                        // Name
                        ds.readNameString(),
                        // Exported
                        ds.readBoolean(),
                        // Enumeration
                        ds.readBoolean(),
                        //referenceType
                        ds.readBoolean());
            }

            @Override
            public void indexStub(@NotNull NimTypeStructureStub stub, @NotNull IndexSink sink) {
                sink.occurrence(StubIndexKey.createIndexKey(stub.name), stub);
            }
    };


    private final String name;

    private final boolean exported;

    private final boolean enumeration;

    private final boolean referenceType;

    protected NimTypeStructureStub(StubElement parent, IStubElementType elementType,
                                   String name,
                                   boolean exported,
                                   boolean enumeration, boolean referenceType) {
        super(parent, elementType);
        this.name = name;
        this.exported = exported;
        this.enumeration = enumeration;
        this.referenceType = referenceType;
    }

    public String getName() {
        return name;
    }

    public boolean isExported() {
        return exported;
    }

    public boolean isEnumeration() {
        return enumeration;
    }

    public boolean isReferenceType() {
        return referenceType;
    }
}
