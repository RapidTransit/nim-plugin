package org.nim.grammar;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

public class NimParserWrapper extends NimParser {

    private ITokenTypeRemapper remapper = new ITokenTypeRemapper() {
        @Override
        public IElementType filter(IElementType source, int start, int end, CharSequence text) {
            return source;
        }
    };

    @Override
    public ASTNode parse(IElementType root_, PsiBuilder builder_) {
        builder_.setDebugMode(true);
        builder_.setTokenTypeRemapper(remapper);

            ASTNode node = super.parse(root_, builder_);
            return node;

    }
}
