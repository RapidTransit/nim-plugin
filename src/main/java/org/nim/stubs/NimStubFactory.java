package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import org.nim.stubs.impl.NimClassStub;

public abstract class NimStubFactory {

    public static IStubElementType build(String name){
        if(NimClassStub.NAME.equals(name)){
            return NimClassStub.ADAPTER;
        }
        return null;
    }

}
