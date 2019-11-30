package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;

public class StubAdapter {
    public static IStubElementType adapt(String s){
        return NimStubTypes.INSTANCE.stubFactory(s);
    }
}
