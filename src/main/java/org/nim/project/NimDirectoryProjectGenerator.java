package org.nim.project;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.DirectoryProjectGeneratorBase;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.lang.NimIcons;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;

public class NimDirectoryProjectGenerator extends DirectoryProjectGeneratorBase<ProjectData> {
    @NotNull
    @Override
    public String getName() {
        return "Nim";
    }

    @Nullable
    @Override
    public Icon getLogo() {
        return NimIcons.FILE;
    }

    @Override
    public void generateProject(@NotNull Project project, @NotNull VirtualFile baseDir, @NotNull ProjectData settings,
                                @NotNull Module module) {
        FileTemplate template = FileTemplateManager.getInstance(project).getTemplate("nimble/nimble.ft");
        try {
            String data = template.getText(Collections.singletonMap("data", settings));
            VirtualFile childData = baseDir.createChildData(this, settings.getName() + ".nimble");
            childData.setBinaryContent(data.getBytes(Charsets.UTF_8));
            baseDir.createChildDirectory(this, "src");
            baseDir.createChildDirectory(this, "test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
