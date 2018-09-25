package org.nim.lang.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.MergingLexerAdapter
import com.intellij.psi.tree.TokenSet
/*
Probably Copy Pasta from Python?
 */
class NimIndentingProcessor(original: Lexer?, tokensToMerge: TokenSet?) : MergingLexerAdapter(original, tokensToMerge) {
}