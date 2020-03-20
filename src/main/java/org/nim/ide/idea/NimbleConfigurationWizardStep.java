package org.nim.ide.idea;

import com.intellij.ide.util.importProject.ProjectDescriptor;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.ui.components.JBLabel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.nim.ide.idea.ui.NimSdkComboBox;
import org.nim.sdk.NimProjectSdksModel;

import javax.swing.*;

@RequiredArgsConstructor
public class NimbleConfigurationWizardStep extends ModuleWizardStep {

    private final WizardContext context;

    @Nullable
    private final ProjectDescriptor projectDescriptor;

    private SelectSDK form;

    @Override
    public JComponent getComponent() {
        return getForm().frame;
    }

    @Override
    public void updateDataModel() {

    }


    @Override
    public void disposeUIResources() {
        form = null;
    }

    SelectSDK getForm(){
        if(form == null){
            form = new SelectSDK();
        }
        return form;
    }

    class SelectSDK {


        JPanel frame = new JPanel();
        JBLabel sdkLabel = new JBLabel("Project SDK:");
        JButton create = new JButton("New...");
        NimSdkComboBox comboBox = new NimSdkComboBox(new NimProjectSdksModel());

        public SelectSDK() {
            frame.add(comboBox);
        }
    }
}
