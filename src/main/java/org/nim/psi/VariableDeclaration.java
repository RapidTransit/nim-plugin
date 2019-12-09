package org.nim.psi;

import org.nim.grammar.NimParserUtil;

public interface VariableDeclaration {

    NimParserUtil.VariableType getDeclarationType();

    int getIndex();
}
