package org.nim.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.Nullable;
import org.nim.ide.idea.NimbleConfigurationWizardStep;

public class NimModuleBuilder extends ModuleBuilder {

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        var wizard = new NimbleConfigurationWizardStep(context, null);
        Disposer.register(parentDisposable, wizard::disposeUIResources);
        return wizard;
    }

    @Override
    public ModuleType<?> getModuleType() {
        return ModuleTypeManager.getInstance().findByID("NIM_MODULE");
    }
}
