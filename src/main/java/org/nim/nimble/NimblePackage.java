package org.nim.nimble;

import com.intellij.openapi.util.Version;

/**
 * = Nimble Package
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
public class NimblePackage {
    protected String name;
    /**
     * This is making an assumption everyone is using semantic versioning
     */
    protected Version version;

    public String getQualifiedPackageName(){
        return name + "-" + version;
    }
}
