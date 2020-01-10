package org.nim.sdk;

import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class NimSdkType extends SdkType {


    public NimSdkType() {
        super("NimSDK");
    }

    @Nullable
    @Override
    public String suggestHomePath() {
        return SystemInfo.isLinux ? "/usr/lib/nim" : null;
    }

    @Override
    public boolean isValidSdkHome(String path) {
        if(path != null){
            File root = new File(FileUtil.toSystemDependentName(path));
            return new File(root, "system.nim").isFile() && new File(root, "stdlib.nim").isFile();
        }
        return false;
    }

    @NotNull
    @Override
    //todo: Try to get the version
    public String suggestSdkName(@Nullable String currentSdkName, String sdkHome) {
        return "NimSDK";
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull SdkModel sdkModel, @NotNull SdkModificator sdkModificator) {
        return null;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return ProjectBundle.message("nim.sdk.name");
    }

    @Override
    public void saveAdditionalData(@NotNull SdkAdditionalData additionalData, @NotNull Element additional) {

    }

    @Override
    public boolean setupSdkPaths(@NotNull Sdk sdk, @NotNull SdkModel sdkModel) {
        return super.setupSdkPaths(sdk, sdkModel);
    }
}
