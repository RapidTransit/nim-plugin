package org.nim.lang.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.impl.PsiBuilderAdapter

class NimPsiBuilder(delegate: PsiBuilder) : PsiBuilderAdapter(delegate) {
    var currInd = 0
    var firstTok = false
    var hasProgress = false;
    var inPragma = 0;
    var inSemiStmtList = 0;
    var emptyNode: Any? = null;

}