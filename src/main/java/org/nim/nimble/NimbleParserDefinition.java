package org.nim.nimble;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.nim.NimFile;
import org.nim.grammar.NimIndentationLexer;
import org.nim.grammar.NimLexer;
import org.nim.grammar.NimParserWrapper;
import org.nim.lang.NimLanguage;
import org.nim.nimble.psi.NimbleTokenTypes;
import org.nim.psi.NimTokenTypes;

public class NimbleParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(NimbleTokenTypes.WS, NimbleTokenTypes.CRLF);
    public static final TokenSet COMMENTS = TokenSet.create(NimbleTokenTypes.COMMENT);

    public static final IFileElementType FILE = new IFileElementType(Language.findInstance(NimbleLanguage.class));

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new NimbleLexer(null));
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiParser createParser(final Project project) {
        return new NimbleParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new NimbleFile(viewProvider);
    }

    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return NimTokenTypes.Factory.createElement(node);
    }
}