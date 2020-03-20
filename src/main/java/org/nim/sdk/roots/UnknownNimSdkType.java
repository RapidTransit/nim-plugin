// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk.roots;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.ProjectBundle;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.*;

import javax.swing.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used as a plug for all SDKs which type cannot be determined (for example, plugin that registered a custom type has been deinstalled)
 * @author Eugene Zhuravlev
 */
public class UnknownNimSdkType extends NimSdkType {
  private static final Map<String, UnknownNimSdkType> ourTypeNameToInstanceMap = new ConcurrentHashMap<>();

  /**
   * @param typeName the name of the SDK type that this SDK serves as a plug for
   */
  private UnknownNimSdkType(@NotNull String typeName) {
    super(typeName);
  }

  @NotNull
  public static UnknownNimSdkType getInstance(@NotNull String typeName) {
    return ourTypeNameToInstanceMap.computeIfAbsent(typeName, UnknownNimSdkType::new);
  }

  @Override
  public String suggestHomePath() {
    return null;
  }

  @Override
  public boolean isValidSdkHome(String path) {
    return false;
  }

  @Override
  public String getVersionString(String sdkHome) {
    return "";
  }

  @NotNull
  @Override
  public String suggestSdkName(@Nullable String currentSdkName, String sdkHome) {
    return currentSdkName != null ? currentSdkName : "";
  }

  @Override
  public NimJdkAdditionalDataConfigurable createAdditionalDataConfigurable(@NotNull NimSdkModel sdkModel, @NotNull NimSdkModificator sdkModificator) {
    return null;
  }

  public String getBinPath(NimSdk sdk) {
    return null;
  }

  public String getToolsPath(NimSdk sdk) {
    return null;
  }

  public String getVMExecutablePath(NimSdk sdk) {
    return null;
  }

  @Override
  public void saveAdditionalData(@NotNull NimSdkAdditionalData additionalData, @NotNull Element additional) {
  }

  @NotNull
  @Override
  public String getPresentableName() {
    return ProjectBundle.message("sdk.unknown.name");
  }

  @Override
  public Icon getIcon() {
    return AllIcons.Nodes.UnknownJdk;
  }
}
