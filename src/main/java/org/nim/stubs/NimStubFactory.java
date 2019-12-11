package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import org.nim.stubs.impl.NimTypeStub;

public abstract class NimStubFactory {

    public static IStubElementType build(String name){
        if(NimTypeStub.NAME.equals(name)){
            return NimTypeStub.ADAPTER;
        }
        return null;
    }

}
