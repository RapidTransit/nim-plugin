package org.nim.lexer;

import com.intellij.lexer.Lexer;
import com.intellij.testFramework.LexerTestCase;
import com.intellij.testFramework.fixtures.IdeaTestExecutionPolicy;
import org.nim.grammar.NimIndentationLexer;
import org.nim.grammar.NimLexer;

import java.io.File;

public class LexerTest extends LexerTestCase {


    public void testSystem(){
        doFileTest("nim");
    }


    @Override
    protected Lexer createLexer() {
        return new NimIndentationLexer(new NimLexer(null));
    }

    @Override
    protected String getDirPath() {
        return "testData/grammar";
    }

    protected String getPathToTestDataFile(String extension) {
        return getDirPath() + "/" + getTestName(true) + extension;
    }
}
