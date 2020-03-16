package org.nim.psi.extension;

import org.nim.grammar.VariableType;
import org.nim.psi.NimVariableDeclaration;

public interface NimVariableDeclarationExtension extends NimVariableDeclaration {
    VariableType getVariableType();
}
