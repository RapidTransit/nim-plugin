package org.nim.stubs.impl;

import com.intellij.psi.stubs.*;
import org.jetbrains.annotations.NotNull;
import org.nim.psi.NimTypeDeclaration;
import org.nim.psi.impl.NimTypeDeclarationImpl;
import org.nim.stubs.NimStubAdapter;

import java.io.IOException;

public class NimTypeStub extends StubBase<NimTypeDeclaration> {

    public static final String NAME = "TYPE";

    public static final NimStubAdapter<NimTypeStub, NimTypeDeclaration> ADAPTER =

        new NimStubAdapter<NimTypeStub, NimTypeDeclaration>(NAME) {

            @Override
            public NimTypeDeclaration createPsi(@NotNull NimTypeStub stub) {
                return new NimTypeDeclarationImpl(stub, this);
            }

            @NotNull
            @Override
            public NimTypeStub createStub(@NotNull NimTypeDeclaration psi, StubElement parentStub) {
                return new NimTypeStub(
                        parentStub,
                        this,
                        // Name
                        psi.getIdentifier().getText(),
                        // Exported
                        false,
                        // Enumeration
                        false);
            }


            @Override
            public void serialize(@NotNull NimTypeStub stub, @NotNull StubOutputStream ds) throws IOException {
                ds.writeName(stub.name);
                ds.writeBoolean(stub.exported);
                ds.writeBoolean(stub.enumeration);
            }

            @NotNull
            @Override
            public NimTypeStub deserialize(@NotNull StubInputStream ds, StubElement parentStub) throws IOException {
                return new NimTypeStub(
                        parentStub,
                        this,
                        // Name
                        ds.readNameString(),
                        // Exported
                        ds.readBoolean(),
                        // Enumeration
                        ds.readBoolean());
            }

            @Override
            public void indexStub(@NotNull NimTypeStub stub, @NotNull IndexSink sink) {
                sink.occurrence(StubIndexKey.createIndexKey(stub.name), stub);
            }
    };


    private final String name;

    private final boolean exported;

    private final boolean enumeration;

    protected NimTypeStub(StubElement parent, IStubElementType elementType,
                          String name,
                          boolean exported,
                          boolean enumeration) {
        super(parent, elementType);
        this.name = name;
        this.exported = exported;
        this.enumeration = enumeration;
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
}
