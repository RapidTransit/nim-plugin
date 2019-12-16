package org.nim.psi.extension;

import org.nim.grammar.NimParserUtil;
import org.nim.psi.NimVariableDeclaration;

public interface NimVariableDeclarationExtension extends NimVariableDeclaration {
    NimParserUtil.VariableType getVariableType();
}
