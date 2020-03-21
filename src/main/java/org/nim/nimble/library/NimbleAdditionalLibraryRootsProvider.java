package org.nim.nimble.library;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.util.Version;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.nim.sdk.NimSdk;

import java.util.*;

public class NimbleAdditionalLibraryRootsProvider extends AdditionalLibraryRootsProvider {

    @NotNull
    @Override
    public Collection<SyntheticLibrary> getAdditionalProjectLibraries(@NotNull Project project) {

        List<SyntheticLibrary> libraries = new ArrayList<>();

        return libraries;
    }

    @NotNull
    @Override
    public Collection<VirtualFile> getRootsToWatch(@NotNull Project project) {
        return super.getRootsToWatch(project);
    }
}
