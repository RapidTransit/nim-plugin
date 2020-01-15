package org.nim.stubs;

import com.intellij.psi.stubs.IStubElementType;
import org.nim.stubs.impl.NimMethodLikeStub;
import org.nim.stubs.impl.NimTypeStructureStub;

public abstract class NimStubFactory {

    public static IStubElementType<?, ?> build(String name){
        if(NimTypeStructureStub.NAME.equals(name)){
            return NimTypeStructureStub.ADAPTER;
        } else if(NimMethodLikeStub.NAME.equals(name)){
            return NimMethodLikeStub.ADAPTER;
        }
        return null;
    }

}
