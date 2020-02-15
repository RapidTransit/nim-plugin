package org.nim.sdk;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.RootProvider;
import com.intellij.openapi.roots.impl.libraries.LibraryImpl;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.StandardFileSystems;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.CustomLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * May want to use a  CustomStepProjectGenerator I think SDK is discouraged
 */
@CustomLog
public class NimSdkOld extends UserDataHolderBase implements Sdk, SdkModificator {

    private NimSdkOld parent;
    private NimSdkType type;
    private String name;
    private String version;
    private String homePath;
    private LibraryImpl rootProvider;

    public NimSdkOld(NimSdkType type) {
        this.type = type;
    }

    @NotNull
    @Override
    public SdkTypeId getSdkType() {
        return type;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public String getVersionString() {
        if(version == null){
            version = type.getVersionString(this);
        }
        return version;
    }

    @Override
    public void setVersionString(String versionString) {
        this.version = versionString;
    }

    @Nullable
    @Override
    public String getHomePath() {
        return homePath;
    }

    @Override
    public void setHomePath(String path) {
        this.homePath = path;
    }

    @Nullable
    @Override
    public VirtualFile getHomeDirectory() {
        if (homePath == null) {
            return null;
        }
        return StandardFileSystems.local().findFileByPath(homePath);
    }

    @NotNull
    @Override
    public RootProvider getRootProvider() {
        return rootProvider;
    }

    @NotNull
    @Override
    public SdkModificator getSdkModificator() {
        return (SdkModificator) clone();
    }

    @Nullable
    @Override
    public SdkAdditionalData getSdkAdditionalData() {
        return null;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public Object clone() {
        var clone = new NimSdkOld(type);
        clone.parent = this;
        clone.version = version;
        clone.homePath = homePath;
        clone.rootProvider = rootProvider;
        return clone;
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
        rootProvider.addRoot(root, rootType);
    }

    @Override
    public void removeRoot(@NotNull VirtualFile root, @NotNull OrderRootType rootType) {
        rootProvider.removeRoot(root.getUrl(), rootType);
    }

    @Override
    public void removeRoots(@NotNull OrderRootType rootType) {
        var urls = rootProvider.getUrls(rootType);
        for(var url : urls){
            rootProvider.removeRoot(url, rootType);
        }
    }

    @Override
    public void removeAllRoots() {
        var rootTypes = OrderRootType.getAllTypes();
        for(var rootType : rootTypes){
            removeRoots(rootType);
        }
    }

    @Override
    public void commitChanges() {
        parent.type = type;
        parent.version = version;
        parent.homePath = homePath;
        parent.rootProvider = rootProvider;
    }

    @Override
    public boolean isWritable() {
        return parent != null;
    }


    public static LibraryImpl create(Project project, String name){
        return (LibraryImpl) ApplicationManager.getApplication().runWriteAction((Computable<Library>) () -> {
            var table = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
            return table.createLibrary(name);
        });
    }
}
