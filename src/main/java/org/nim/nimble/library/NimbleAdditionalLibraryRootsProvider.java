package org.nim.nimble.library;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.util.Version;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.nim.sdk.NimSdk;
import org.nim.sdk.roots.NimSdkProjectRootManager;

import java.util.*;

public class NimbleAdditionalLibraryRootsProvider extends AdditionalLibraryRootsProvider {

    @NotNull
    @Override
    public Collection<SyntheticLibrary> getAdditionalProjectLibraries(@NotNull Project project) {
        List<SyntheticLibrary> libraries = new ArrayList<>();
        NimSdkProjectRootManager component = NimSdkProjectRootManager.getInstance(project);
        NimSdk projectSdk = component.getProjectSdk();
        if(projectSdk != null){
            var lib = new NimbleLibrary(projectSdk.getName(),
                    Version.parseVersion(projectSdk.getVersionString()),
                    Collections.singletonList(projectSdk.getHomeDirectory()));
            libraries.add(lib);
        }
        return libraries;
    }

    @NotNull
    @Override
    public Collection<VirtualFile> getRootsToWatch(@NotNull Project project) {
        return super.getRootsToWatch(project);
    }
}
