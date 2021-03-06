package org.nim.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.grammar.VariableType;
import org.nim.psi.NimVarName;

@Getter
public class NimVarNameStub extends NamedStubBase<NimVarName> {


    private final VariableType type;
    private final int positionalIndex;
    private final int statementIndex;


    protected NimVarNameStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable StringRef name, VariableType type, int positionalIndex, int statementIndex) {
        super(parent, elementType, name);
        this.type = type;
        this.positionalIndex = positionalIndex;
        this.statementIndex = statementIndex;
    }

    protected NimVarNameStub(StubElement parent, @NotNull IStubElementType elementType, @Nullable String name, VariableType type, int positionalIndex, int statementIndex) {
        super(parent, elementType, name);

        this.type = type;
        this.positionalIndex = positionalIndex;
        this.statementIndex = statementIndex;
    }

    public VariableType getType() {
        return type;
    }


}
