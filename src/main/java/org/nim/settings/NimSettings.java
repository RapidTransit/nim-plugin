package org.nim.settings;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NimSettings implements SearchableConfigurable, Disposable {


    private NimSettingsForm form;

    @Override
    public void dispose() {
        form = null;
    }

    @NotNull
    @Override
    public String getId() {
        return "Settings.Nim";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Nim";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return getForm().getMainPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    NimSettingsForm getForm(){
        if(form == null){
            form = new NimSettingsForm();
        }
        return form;
    }
}
