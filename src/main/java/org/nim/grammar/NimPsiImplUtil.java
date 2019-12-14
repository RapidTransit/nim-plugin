package org.nim.grammar;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.psi.NimDeclarationColonEquals;
import org.nim.stubs.DeclarationColonEqualsStub;

public class NimPsiImplUtil {

    @Nullable
    public static String getName(@NotNull NimDeclarationColonEquals o){
        final DeclarationColonEqualsStub stub = o.getStub();
        if(stub != null){
            return stub.getName();
        }
        return o.getName();
    }
}
