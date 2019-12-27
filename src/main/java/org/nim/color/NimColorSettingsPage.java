package org.nim.color;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.util.io.StreamUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NimColorSettingsPage implements ColorSettingsPage {



    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new NimSyntaxHighlighter();
    }

    @NotNull
    @Override
    @SneakyThrows
    public String getDemoText() {
        InputStream demoText = NimColorSettingsPage.class.getClassLoader()
                .getResourceAsStream("org/nim/colors/demoText.nim");
        if (demoText != null) {
            return StreamUtil.readText(demoText, StandardCharsets.UTF_8);
        } else {
            return ProjectBundle.message("nim.error.demoText");
        }

    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return new AttributesDescriptor[0];
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return new ColorDescriptor[0];
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Nim";
    }
}
