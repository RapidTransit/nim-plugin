package org.nim.sdk;

import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import jodd.util.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.roots.NimJdkAdditionalDataConfigurable;

import java.io.File;
import java.util.Optional;

//@CustomLog
public class NimSdkTypeImpl extends NimSdkType {


    public NimSdkTypeImpl() {
        super("NimSDK");
    }

    @Nullable

    public String suggestHomePath() {
        return SystemInfo.isLinux ? "/usr/lib/nim" : null;
    }


    public boolean isValidSdkHome(String path) {
        if(path != null){
            File root = new File(FileUtil.toSystemDependentName(path));
            return new File(root, "system.nim").isFile() && new File(root, "stdlib.nimble").isFile();
        }
        return false;
    }

    @NotNull

    //todo: Try to get the version
    public String suggestSdkName(@Nullable String currentSdkName, String sdkHome) {
        return "NimSDK";
    }

    @Nullable

    public NimJdkAdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull NimSdkModel sdkModel, @NotNull NimSdkModificator sdkModificator) {
        return null;
    }

    @NotNull

    public String getPresentableName() {
        //return ProjectBundle.message("nim.sdk.name");
        return "Nim SDK";
    }


    public void saveAdditionalData(@NotNull NimSdkAdditionalData additionalData, @NotNull Element additional) {

    }


    public boolean setupSdkPaths(@NotNull NimSdk sdk, @NotNull NimSdkModel sdkModel) {
        return super.setupSdkPaths(sdk, sdkModel);
    }

    @Nullable

    public String getVersionString(String sdkHome) {
        if(StringUtil.isNotEmpty(sdkHome)) {
            //log.info("SDK Home was empty");
            VirtualFileSystem fileSystem = VirtualFileManager.getInstance().getFileSystem("file");
            if (fileSystem != null) {
                var path = sdkHome + File.separatorChar + "system.nim";
                VirtualFile fileByPath = fileSystem.findFileByPath(path);
                if(fileByPath != null && fileByPath.exists()){
                    return Optional.ofNullable(NimVersionUtil.findVersion(fileByPath))
                            .map(x->x.toCompactString())
                            .orElse(null);
                } else {
                   // log.error("Path `" + path + "` did not contain the system.nim file");
                }

            } else {
                // For Debug Purposes
             //   log.error("File system with the key: `file` does not exist in the VirtualFileManager");
            }
        }
        return null;
    }
}
