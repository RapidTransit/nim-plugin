package org.nim.grammar;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.nim.psi.NimElementType;
import org.nim.psi.NimTokenTypes;

public class NimParserWrapper extends NimParser {



    @Override
    public ASTNode parse(IElementType root_, PsiBuilder builder_) {
        Remapper remapper = new Remapper(builder_);
        builder_.setDebugMode(true);
     //   builder_.setTokenTypeRemapper(remapper);

            ASTNode node = super.parse(root_, builder_);
            return node;

    }

    static class Remapper implements ITokenTypeRemapper {

        private final PsiBuilder builder;

        Remapper(PsiBuilder builder) {
            this.builder = builder;
        }

        @Override
        public IElementType filter(IElementType source, int start, int end, CharSequence text) {
//            if(source == NimTokenTypes.OP5
//                && text.charAt(start) == 'o'){
//                final IElementType possibleSpace = builder.rawLookup(-1);
//                final IElementType possibleNewLine = builder.rawLookup(-2);
//                if(possibleSpace == NimTokenTypes.WHITE_SPACE && possibleNewLine == NimTokenTypes.CRLF){
//                    return NimTokenTypes.OF;
//                }
//            }
            return source;
        }
    }

}
