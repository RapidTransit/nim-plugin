package org.nim.nimble.library;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.roots.SyntheticLibrary;
import com.intellij.openapi.util.Version;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.lang.NimIcons;

import javax.swing.*;
import java.util.Collection;
/**
 * = Nimble Library
 *
 * This represents a package that is found in the $USERDIR/.nimble/pkgs
 * I assume they are using Semantic versioning
 * [quote, https://github.com/nim-lang/nimble[From the README]]
 * ____
 * When Nimble installs a library, it will copy all of its files into $nimbleDir/pkgs/pkgname-ver. It's up to the package
 * creator to make sure that the package directory layout is correct, this is so that users of the package can correctly
 * import the package.
 * ____
 */
@AllArgsConstructor
public class NimbleLibrary extends SyntheticLibrary implements ItemPresentation, Navigatable {

    private final String name;
    @Getter
    private final Version version;
    private final Collection<VirtualFile> sourcesRoots;


    @Nullable
    @Override
    public String getPresentableText() {
        return name + "-" + version.toCompactString();
    }

    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean unused) {
        return NimIcons.NIMBLE;
    }

    @NotNull
    @Override
    public Collection<VirtualFile> getSourceRoots() {
        return sourcesRoots;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof NimbleLibrary){
            return sourcesRoots.equals(((NimbleLibrary) o).sourcesRoots);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return sourcesRoots.hashCode();
    }

    @Override
    public void navigate(boolean requestFocus) {

    }

    @Override
    public boolean canNavigate() {
        return false;
    }

    @Override
    public boolean canNavigateToSource() {
        return false;
    }
}
