package org.nim.grammar;

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
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.nim.NimFile;
import org.nim.lang.NimLanguage;
import org.nim.psi.NimTokenType;
import org.nim.psi.NimTokenTypes;

import org.jetbrains.annotations.NotNull;

import java.io.Reader;

public class NimParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(NimTokenTypes.WHITE_SPACE, NimTokenTypes.CRLF);
    public static final TokenSet COMMENTS = TokenSet.create(NimTokenTypes.HASH);

    public static final IFileElementType FILE = new IFileElementType(Language.findInstance(NimLanguage.class));

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new NimIndentationLexer(new NimLexer(null));
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
        return new NimParserWrapper();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new NimFile(viewProvider);
    }

    public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return NimTokenTypes.Factory.createElement(node);
    }
}