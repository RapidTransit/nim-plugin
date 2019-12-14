package org.nim.psi.extension;

import com.intellij.psi.PsiElement;
import org.nim.grammar.NimParserUtil;

public interface VarName extends PsiElement {

    NimParserUtil.VariableType getDeclarationType();

    NimType getType();

    int getIndex();
}
