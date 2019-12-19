package org.nim.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NimbleFileType extends LanguageFileType {
    public static final NimbleFileType INSTANCE = new NimbleFileType();

    private NimbleFileType() {
        super(NimLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Nimble file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Nimble build file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "nimble";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return NimIcons.NIMBLE;
    }
}