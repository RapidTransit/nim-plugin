package org.nim.psi.extension;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import org.nim.psi.NimImportSection;
import org.nim.psi.NimMethodLikeDeclaration;
import org.nim.psi.NimTypeStructureDeclaration;
import org.nim.psi.NimVariableDeclaration;

import java.util.List;

public interface NimModule extends PsiNameIdentifierOwner, NavigationItem {

    List<PsiElement> getIncludes();

    List<PsiElement> additionalExport();

    List<NimImportSection> imports();

    String getPackage();

    List<NimTypeStructureDeclaration> getTypes();

    List<NimMethodLikeDeclaration> getMethods();

    List<NimVariableDeclaration> getGlobalVariables();
}
