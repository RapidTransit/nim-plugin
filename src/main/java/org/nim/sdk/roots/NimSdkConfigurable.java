/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package org.nim.sdk.roots;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ModuleStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectStructureElementConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.StructureConfigurableContext;
import com.intellij.openapi.roots.ui.configuration.projectRoot.daemon.ProjectStructureElement;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.ui.navigation.History;
import com.intellij.ui.navigation.Place;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.NimProjectSdksModel;
import org.nim.sdk.NimSdk;
import org.nim.sdk.NimProjectSdkImpl;

import javax.swing.*;

public class NimSdkConfigurable extends ProjectStructureElementConfigurable<NimSdk> implements Place.Navigator {
  private final NimProjectSdkImpl myProjectJdk;
  private final NimSdkEditor mySdkEditor;
  private final NimSdkProjectStructureElement myProjectStructureElement;

  public NimSdkConfigurable(@NotNull NimProjectSdkImpl projectJdk,
                            @NotNull NimProjectSdksModel sdksModel,
                            @NotNull Runnable updateTree,
                            @NotNull History history, @NotNull Project project) {
    super(true, updateTree);
    myProjectJdk = projectJdk;
    mySdkEditor = createSdkEditor(project, sdksModel, history, myProjectJdk);
    final StructureConfigurableContext context = ModuleStructureConfigurable.getInstance(project).getContext();
    myProjectStructureElement = new NimSdkProjectStructureElement(context, myProjectJdk);
  }

  protected NimSdkEditor createSdkEditor(@NotNull Project project, NimProjectSdksModel sdksModel, History history, NimProjectSdkImpl projectJdk) {
    return new NimSdkEditor(project, sdksModel, history, projectJdk);
  }

  @Override
  public ProjectStructureElement getProjectStructureElement() {
    return myProjectStructureElement;
  }

  @Override
  public void setDisplayName(final String name) {
    myProjectJdk.setName(name);
  }

  @Override
  public NimSdk getEditableObject() {
    return myProjectJdk;
  }

  @Override
  public String getBannerSlogan() {
    return ProjectBundle.message("project.roots.jdk.banner.text", myProjectJdk.getName());
  }

  @Override
  public String getDisplayName() {
    return myProjectJdk.getName();
  }

  @Override
  public Icon getIcon(boolean open) {
    return ((SdkType) myProjectJdk.getSdkType()).getIcon();
  }

  @Override
  @Nullable
  @NonNls
  public String getHelpTopic() {
    return ((SdkType) myProjectJdk.getSdkType()).getHelpTopic();
  }


  @Override
  public JComponent createOptionsPanel() {
    return mySdkEditor.createComponent();
  }

  @Override
  public boolean isModified() {
    return mySdkEditor.isModified();
  }

  @Override
  public void apply() throws ConfigurationException {
    mySdkEditor.apply();
  }

  @Override
  public void reset() {
    mySdkEditor.reset();
  }

  @Override
  public void disposeUIResources() {
    mySdkEditor.disposeUIResources();
  }

  @Override
  public ActionCallback navigateTo(@Nullable final Place place, final boolean requestFocus) {
    return mySdkEditor.navigateTo(place, requestFocus);
  }

  @Override
  public void queryPlace(@NotNull final Place place) {
    mySdkEditor.queryPlace(place);
  }
}
