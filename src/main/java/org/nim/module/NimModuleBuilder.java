package org.nim.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;

public class NimModuleBuilder extends ModuleBuilder {
    @Override
    public ModuleType<?> getModuleType() {
        return ModuleTypeManager.getInstance().findByID("NIM_MODULE");
    }
}
