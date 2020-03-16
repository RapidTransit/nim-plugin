package org.nim.nimble;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class NimbleFile extends PsiFileBase {

    protected NimbleFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, NimbleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return NimbleFileType.INSTANCE;
    }
}
