package org.nim.psi.extension;

import org.nim.grammar.NimParserUtil;

public interface VariableDeclaration {

    NimParserUtil.VariableType getDeclarationType();

    int getIndex();
}
