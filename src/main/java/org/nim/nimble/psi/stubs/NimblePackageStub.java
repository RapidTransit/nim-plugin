package org.nim.nimble.psi.stubs;

import com.intellij.openapi.util.Version;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import lombok.Getter;
import org.nim.nimble.psi.NimblePackage;

@Getter
public class NimblePackageStub extends StubBase<NimblePackage> {

    private final String name;

    private final Version version;

    protected NimblePackageStub(StubElement parent, IStubElementType elementType, String name, Version version) {
        super(parent, elementType);
        this.name = name;
        this.version = version;
    }
}
