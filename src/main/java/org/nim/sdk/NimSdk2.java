package org.nim.sdk;

import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import jodd.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Optional;

/**
 * Acts similar to IntelliJ's SDK related but Intellij is tied to JDK implementations this should work better across
 * IDEs
 */
//@CustomLog
public class NimSdk2 implements NimSdk {

    @Getter
    @Setter
    private String name;

    private String homePath;

    private String version;

    public String getVersionString(String sdkHome) {
        if(StringUtil.isNotEmpty(sdkHome)) {
            //log.info("SDK Home was empty");
            VirtualFileSystem fileSystem = VirtualFileManager.getInstance().getFileSystem("file");
            if (fileSystem != null) {
                var path = sdkHome + File.pathSeparator + "system.nim";
                VirtualFile fileByPath = fileSystem.findFileByPath(path);
                if(fileByPath != null && fileByPath.exists()){
                    return Optional.ofNullable(NimVersionUtil.findVersion(fileByPath))
                            .map(x->x.toCompactString())
                            .orElse(null);
                } else {
                  //  log.error("Path `" + path + "` did not contain the system.nim file");
                }

            } else {
                // For Debug Purposes
              //  log.error("File system with the key: `file` does not exist in the VirtualFileManager");
            }
        }
        return null;
    }
    public boolean isValidSdkHome(String path) {
        if(path != null){
            File root = new File(FileUtil.toSystemDependentName(path));
            return new File(root, "system.nim").isFile() && new File(root, "stdlib.nim").isFile();
        }
        return false;
    }

    public String suggestHomePath() {
        return SystemInfo.isLinux ? "/usr/lib/nim" : null;
    }

    public String suggestSdkName(@Nullable String currentSdkName, String sdkHome) {
        return "NimSDK";
    }

    public String getPresentableName() {
        return ProjectBundle.message("nim.sdk.name");
    }

    @NotNull
    @Override
    public NimSdkTypeId getSdkType() {
        return null;
    }

    @Nullable
    @Override
    public String getVersionString() {
        return null;
    }

    @Nullable
    @Override
    public String getHomePath() {
        return null;
    }

    public VirtualFile getHomeDirectory() {
        if (homePath == null) {
            return null;
        }
        return StandardFileSystems.local().findFileByPath(homePath);
    }

    @NotNull
    @Override
    public RootProvider getRootProvider() {
        return null;
    }

    @NotNull
    @Override
    public NimSdkModificator getSdkModificator() {
        return null;
    }

    @Nullable
    @Override
    public NimSdkAdditionalData getSdkAdditionalData() {
        return null;
    }

    @NotNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

    }
}
