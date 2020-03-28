package org.nim.grammar;

import com.intellij.lang.ParserDefinition;
import com.intellij.mock.MockVirtualFile;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestLoggerFactory;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class GrammarTest extends ParsingTestCase {
    static {
        Logger.setFactory(TestLoggerFactory.class);
    }
    public GrammarTest() {
        super("grammar", "nim", true, new NimParserDefinition());
    }

    public void testSystem1(){
        doTest(true);
    }
    public void testSystem(){
        doTest(true);
    }
    public void testNimhcr(){
        doTest(true);
    }
    public void testAlgorithm(){
        doTest(true);
    }
    public void testAlgorithm1(){
        doTest(true);
    }
    public void testAlgorithm2(){
        doTest(true);
    }
    public void testAlgorithm3(){
        doTest(true);
    }
    public void testReverse(){
        doTest(true);
    }
    public void testLogging(){
        doTest(true);
    }

    public void testMarshal(){
        doTest(true);
    }
    public void testMarshal1(){
        doTest(true);
    }
    public void testTuples(){
        doTest(true);
    }
    public void testTemplate(){
        doTest(true);
    }

    public void testSimple(){
        doTest(true);
    }
    public void testOfs(){
        doTest(true);
    }

    public void testTypes(){
        doTest(true);
    }

    public void testType(){
        doTest(true);
    }

    @Override
    protected void doTest(boolean checkResult) {
        super.doTest(checkResult);
        if (checkResult) {
            assertFalse(
                    "PsiFile contains error elements",
                    toParseTreeText(myFile, skipSpaces(), includeRanges()).contains("PsiErrorElement")
            );
        }
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
