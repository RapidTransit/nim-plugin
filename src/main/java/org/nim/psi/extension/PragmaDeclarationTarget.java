package org.nim.psi.extension;


import org.nim.psi.NimPragmaDeclaration;

import java.util.List;
import java.util.stream.Collectors;

public interface PragmaDeclarationTarget {

    List<NimPragmaDeclaration> getPragmaDeclaration();


    default List<String> getPragmaNames(){
        return getPragmaDeclaration()
                .stream()
                .map(x-> x.getIdentifier().getText())
                .collect(Collectors.toList());
    }
}
