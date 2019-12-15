package org.nim.sdk;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NimSdk extends UserDataHolderBase implements Sdk, SdkModificator {


    @NotNull
    @Override
    public SdkTypeId getSdkType() {
        return null;
    }

    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(@NotNull String name) {

    }

    @Nullable
    @Override
    public String getVersionString() {
        return null;
    }

    @Override
    public void setVersionString(String versionString) {

    }

    @Nullable
    @Override
    public String getHomePath() {
        return null;
    }

    @Override
    public void setHomePath(String path) {

    }

    @Nullable
    @Override
    public VirtualFile getHomeDirectory() {
        return null;
    }

    @NotNull
    @Override
    public RootProvider getRootProvider() {
        return null;
    }

    @NotNull
    @Override
    public SdkModificator getSdkModificator() {
        return null;
    }

    @Nullable
    @Override
    public SdkAdditionalData getSdkAdditionalData() {
        return null;
    }

    @NotNull
    @Override
    public Object clone() {
        return null;
    }

    @Override
    public void setSdkAdditionalData(SdkAdditionalData data) {

    }

    @NotNull
    @Override
    public VirtualFile[] getRoots(@NotNull OrderRootType rootType) {
        return new VirtualFile[0];
    }

    @Override
    public void addRoot(@NotNull VirtualFile root, @NotNull OrderRootType rootType) {

    }

    @Override
    public void removeRoot(@NotNull VirtualFile root, @NotNull OrderRootType rootType) {

    }

    @Override
    public void removeRoots(@NotNull OrderRootType rootType) {

    }

    @Override
    public void removeAllRoots() {

    }

    @Override
    public void commitChanges() {

    }

    @Override
    public boolean isWritable() {
        return false;
    }
}
