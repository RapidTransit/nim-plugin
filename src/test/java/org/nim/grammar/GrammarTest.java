package org.nim.grammar;

import com.intellij.lang.ParserDefinition;
import com.intellij.mock.MockVirtualFile;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class GrammarTest extends ParsingTestCase {
    public GrammarTest() {
        super("grammar", "nim", true, new NimParserDefinition());
    }

    public void testAlgorithm(){
        doTest(true);
    }

    @Override
    protected String getTestDataPath() {
        return new File("testData").getAbsoluteFile().getAbsolutePath();
    }

    @Override
    protected void checkResult(String targetDataName, PsiFile file) throws IOException {
        doCheckResult(myFullDataPath, file, checkAllPsiRoots(),
                "expected" + File.separator + targetDataName, skipSpaces(),
                includeRanges());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final VirtualFile m = new MockVirtualFile(true, myFullDataPath);
        myProject.setBaseDir(m);
    }
}
