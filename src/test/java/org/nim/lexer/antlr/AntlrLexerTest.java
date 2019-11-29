package org.nim.lexer.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;
import org.nim.grammar.antlr.NimLexer;

import java.io.IOException;

public class AntlrLexerTest {

    @Test
    public void testLexer() throws IOException {
        NimLexer nimLexer = new NimLexer(CharStreams.fromFileName("C:\\Users\\Jonathan\\IdeaProjects\\nim-plugin\\testData\\grammar\\algorithm.nim"));
        assert nimLexer.nextToken() != null;
    }
}
