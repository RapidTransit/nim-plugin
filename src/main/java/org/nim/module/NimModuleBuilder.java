package org.nim.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.ide.idea.NimSdkSettingsStep;
import org.nim.ide.idea.NimbleConfigurationWizardStep;
import org.nim.nimble.library.NimbleProjectServiceImpl;
import org.nim.sdk.NimSdk;
import org.nim.sdk.NimSdkTypeId;

public class NimModuleBuilder extends ModuleBuilder {

    private NimSdk nimSdk;

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {

        var wiz = new NimSdkSettingsStep(context, this, (i)-> true, (d)->true);
        //var wizard = new NimbleConfigurationWizardStep(context, null);
        Disposer.register(parentDisposable, wiz::disposeUIResources);
        return wiz;
    }

    public boolean isSuitableSdkType(NimSdkTypeId sdkType) {
        return true;
    }

    public NimSdk getNimSdk() {
        return nimSdk;
    }

    public void setNimSdk(NimSdk nimSdk) {
        this.nimSdk = nimSdk;
    }


    @Override
    public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        modifiableRootModel.getProject().getComponent(NimbleProjectServiceImpl.class).setSdk(nimSdk);
        ContentEntry contentEntry = doAddContentEntry(modifiableRootModel);
    }

    @Override
    public ModuleType<?> getModuleType() {
        return ModuleTypeManager.getInstance().findByID("NIM_MODULE");
    }
}
