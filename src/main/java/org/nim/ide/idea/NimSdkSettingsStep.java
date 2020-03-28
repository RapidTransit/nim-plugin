/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nim.ide.idea;

import com.intellij.CommonBundle;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.ui.configuration.JdkComboBox;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.ide.idea.ui.NimSdkComboBox;
import org.nim.module.NimModuleBuilder;
import org.nim.sdk.*;
import org.nim.sdk.roots.NimSdkProjectRootManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import static java.awt.GridBagConstraints.*;

/**
 * @author Dmitry Avdeev
 */
public class NimSdkSettingsStep extends ModuleWizardStep {

  public static final Key<NimSdk> NIM_SDK_KEY = Key.create("NIM_SDK");

  protected final NimSdkComboBox myJdkComboBox;
  protected final WizardContext myWizardContext;
  protected final NimProjectSdksModel myModel;
  private final ModuleBuilder myModuleBuilder;
  private final JPanel myJdkPanel;

  public NimSdkSettingsStep(SettingsStep settingsStep, @NotNull ModuleBuilder moduleBuilder,
                            @NotNull Condition<? super NimSdkTypeId> sdkTypeIdFilter) {
    this(settingsStep, moduleBuilder, sdkTypeIdFilter, null);
  }

  public NimSdkSettingsStep(SettingsStep settingsStep,
                            @NotNull ModuleBuilder moduleBuilder,
                            @NotNull Condition<? super NimSdkTypeId> sdkTypeIdFilter,
                            @Nullable Condition<? super NimSdk> sdkFilter) {
    this(settingsStep.getContext(), moduleBuilder, sdkTypeIdFilter, sdkFilter);
    if (!isEmpty()) {
      settingsStep.addSettingsField(getSdkFieldLabel(settingsStep.getContext().getProject()), myJdkPanel);
    }
  }

  public NimSdkSettingsStep(WizardContext context,
                            @NotNull ModuleBuilder moduleBuilder,
                            @NotNull Condition<? super NimSdkTypeId> sdkTypeIdFilter,
                            @Nullable Condition<? super NimSdk> sdkFilter) {
    myModuleBuilder = moduleBuilder;

    myWizardContext = context;
    myModel = new NimProjectSdksModel();
    Project project = myWizardContext.getProject();
    myModel.reset(project);

    if (sdkFilter == null) {
      sdkFilter = NimSdkComboBox.getSdkFilter(sdkTypeIdFilter);
    }

    myJdkComboBox = new NimSdkComboBox(myModel, sdkTypeIdFilter, sdkFilter, sdkTypeIdFilter, true);
    myJdkPanel = new JPanel(new GridBagLayout());

    final PropertiesComponent component = project == null ? PropertiesComponent.getInstance() : PropertiesComponent.getInstance(project);
    ModuleType moduleType = moduleBuilder.getModuleType();
    final String selectedJdkProperty = "jdk.selected." + (moduleType == null ? "" : moduleType.getId());
    myJdkComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        NimSdk jdk = myJdkComboBox.getSelectedJdk();
        if (jdk != null) {
          component.setValue(selectedJdkProperty, jdk.getName());
        }
        onSdkSelected(jdk);
      }
    });

    NimSdk sdk = getPreselectedSdk(project, component.getValue(selectedJdkProperty), sdkTypeIdFilter);
    myJdkComboBox.setSelectedJdk(sdk);

    JButton button = new JButton("Ne\u001Bw...");
    myJdkComboBox.setSetupButton(button, project, myModel,
                                 project == null ? new NimSdkComboBox.NoneJdkComboBoxItem() : new NimSdkComboBox.ProjectJdkComboBoxItem(),
                                 null,
                                 false);

    myJdkPanel.add(myJdkComboBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, HORIZONTAL, JBUI.emptyInsets(), 0, 0));
    myJdkPanel.add(myJdkComboBox.getSetUpButton(), new GridBagConstraints(1, 0, 1, 1, 0, 0, WEST, NONE, JBUI.insetsLeft(4), 0, 0));
    if (myJdkComboBox.getItemCount() == 0) {
      NimSdkType type = ContainerUtil.find(NimSdkType.getAllTypes(), sdkTypeIdFilter);
      if (type != null && type.getDownloadSdkUrl() != null) {
        HyperlinkLabel label = new HyperlinkLabel("Download " + type.getPresentableName());
        label.setHyperlinkTarget(type.getDownloadSdkUrl());
        myJdkPanel.add(label, new GridBagConstraints(0, 1, 1, 1, 0, 0, WEST, NONE, JBUI.insetsTop(4), 0, 0));
      }
    }
  }

  private NimSdk getPreselectedSdk(Project project, String lastUsedSdk, Condition<? super NimSdkTypeId> sdkFilter) {
    if (project != null) {
      NimSdk sdk = NimSdkProjectRootManager.getInstance(project).getProjectSdk();
      if (sdk != null /**&& myModuleBuilder.isSuitableSdkType(sdk.getSdkType())**/) {
        // use project SDK
        myJdkComboBox.insertItemAt(new NimSdkComboBox.ProjectJdkComboBoxItem(), 0);
        return null;
      }
    }
    if (lastUsedSdk != null) {
      NimSdk sdk = NimProjectSdkTable.getInstance().findJdk(lastUsedSdk);
      //@todo: fix
      if (sdk != null /**&& myModuleBuilder.isSuitableSdkType(sdk.getSdkType())**/) {
        return sdk;
      }
    }
    // set default project SDK
    Project defaultProject = ProjectManager.getInstance().getDefaultProject();
    NimSdk selected = NimSdkProjectRootManager.getInstance(defaultProject).getProjectSdk();
    if (selected != null && sdkFilter.value(selected.getSdkType())) {
      return selected;
    }
    return myJdkComboBox.getSelectedJdk();
  }

  protected void onSdkSelected(NimSdk sdk) {}

  public boolean isEmpty() {
    return myJdkPanel.getComponentCount() == 0;
  }

  @NotNull
  protected String getSdkFieldLabel(@Nullable Project project) {
    return (project == null ? "Project" : "Module") + " \u001BSDK:";
  }

  @Override
  public JComponent getComponent() {
    return myJdkPanel;
  }


  @Override
  public void updateDataModel() {
    Project project = myWizardContext.getProject();
    NimSdk jdk = myJdkComboBox.getSelectedJdk();
    if (project == null) {
      ((NimModuleBuilder) myModuleBuilder).setNimSdk(jdk);
      //myWizardContext.putUserData(NIM_SDK_KEY, jdk);
    }
    else {
      ((NimModuleBuilder) myModuleBuilder).setNimSdk(jdk);
    }
  }

  @Override
  public boolean validate() throws ConfigurationException {
    NimSdkComboBox.SdkComboBoxItem item = myJdkComboBox.getSelectedItem();
    if (myJdkComboBox.getSelectedJdk() == null &&
        !(item instanceof NimSdkComboBox.ProjectJdkComboBoxItem) &&
        !(item instanceof NimSdkComboBox.SuggestedJdkItem)) {
      if (Messages.showDialog(getNoSdkMessage(),
                                       IdeBundle.message("title.no.jdk.specified"),
                                       new String[]{CommonBundle.getYesButtonText(), CommonBundle.getNoButtonText()}, 1, Messages.getWarningIcon()) != Messages.YES) {
        return false;
      }
    }
    try {
      if (item instanceof NimSdkComboBox.SuggestedJdkItem) {
        NimSdkType type = ((NimSdkComboBox.SuggestedJdkItem)item).getSdkType();
        String path = ((NimSdkComboBox.SuggestedJdkItem)item).getPath();
        myModel.addSdk(type, path, sdk -> {
          myJdkComboBox.reloadModel(new NimSdkComboBox.ActualSdkComboBoxItem(sdk), myWizardContext.getProject());
          myJdkComboBox.setSelectedJdk(sdk);
        });
      }
      myModel.apply(null, true);
    } catch (ConfigurationException e) {
      //IDEA-98382 We should allow Next step if user has wrong SDK
      if (Messages.showDialog(e.getMessage() + "\n\nDo you want to proceed?",
                                       e.getTitle(),
                                       new String[]{CommonBundle.getYesButtonText(), CommonBundle.getNoButtonText()}, 1, Messages.getWarningIcon()) != Messages.YES) {
        return false;
      }
    }
    return true;
  }

  protected String getNoSdkMessage() {
    return IdeBundle.message("prompt.confirm.project.no.jdk");
  }
}
