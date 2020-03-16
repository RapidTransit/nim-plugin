package org.nim.ide.idea;

import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.roots.ui.configuration.JdkComboBox;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBComboBoxLabel;
import com.intellij.ui.components.JBLabel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.nim.settings.NimSettingsForm;

import javax.swing.*;

@RequiredArgsConstructor
public class NimbleConfigurationWizardStep extends ModuleWizardStep {


    private final WizardContext context;

    @Nullable
    private final ProjectDescriptor projectDescriptor;

    private NimSettingsForm form;

    @Override
    public JComponent getComponent() {
        return getForm().getMainPanel();
    }

    @Override
    public void updateDataModel() {

    }


    @Override
    public void disposeUIResources() {
        form = null;
    }

    NimSettingsForm getForm(){
        if(form == null){
            form = new NimSettingsForm();
        }
        return form;
    }

    static class SelectSDK {

        JFrame frame = new JFrame();
        JBLabel sdkLabel = new JBLabel("Project SDK:");
        JButton create = new JButton("New...");
        JdkComboBox comboBox = new JdkComboBox();
    }
}
