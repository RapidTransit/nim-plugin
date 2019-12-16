package org.nim.module;

import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.nim.lang.NimIcons;

import javax.swing.*;

public class NimModuleType extends ModuleType<NimModuleBuilder> {

    protected NimModuleType() {
        super("NIM_MODULE");
    }

    @NotNull
    @Override
    public NimModuleBuilder createModuleBuilder() {
        return new NimModuleBuilder();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @NotNull
    @Override
    public String getName() {
        return "Nim";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getDescription() {
        return "Nim module";
    }

    @NotNull
    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return NimIcons.FILE;
    }
}
