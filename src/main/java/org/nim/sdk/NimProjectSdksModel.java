// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.MasterDetailsComponent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.Consumer;
import com.intellij.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.roots.NimSdkValidatableSdkAdditionalData;

import javax.swing.*;
import java.util.*;

/**
 * @author anna
 */
public class NimProjectSdksModel implements NimSdkModel {
  private static final Logger LOG = Logger.getInstance(NimProjectSdksModel.class);

  private final Map<NimSdk, NimSdk> myProjectSdks = new HashMap<NimSdk, NimSdk>();
  private final EventDispatcher<Listener> mySdkEventsDispatcher = EventDispatcher.create(Listener.class);

  private boolean myModified;

  private NimSdk myProjectSdk;
  private boolean myInitialized;

  @NotNull
  @Override
  public Listener getMulticaster() {
    return mySdkEventsDispatcher.getMulticaster();
  }

  @NotNull
  @Override
  public NimSdk[] getSdks() {
    return myProjectSdks.values().toArray(new NimSdk[0]);
  }

  @Override
  @Nullable
  public NimSdk findSdk(@NotNull String sdkName) {
    for (NimSdk projectJdk : myProjectSdks.values()) {
      if (sdkName.equals(projectJdk.getName())) return projectJdk;
    }
    return null;
  }

  @Override
  public void addListener(@NotNull Listener listener) {
    mySdkEventsDispatcher.addListener(listener);
  }

  @Override
  public void removeListener(@NotNull Listener listener) {
    mySdkEventsDispatcher.removeListener(listener);
  }

  public void reset(@Nullable Project project) {
    myProjectSdks.clear();
    final NimSdk[] projectSdks = NimProjectSdkTable.getInstance().getAllJdks();
    for (NimSdk sdk : projectSdks) {
      try {
        myProjectSdks.put(sdk, (NimSdk)sdk.clone());
      }
      catch (CloneNotSupportedException e) {
        LOG.error(e);
      }
    }
    if (project != null) {
      String sdkName = ProjectRootManager.getInstance(project).getProjectSdkName();
      myProjectSdk = sdkName == null ? null : findSdk(sdkName);
    }
    myModified = false;
    myInitialized = true;
  }

  public void disposeUIResources() {
    myProjectSdks.clear();
    myInitialized = false;
  }

  @NotNull
  public Map<NimSdk, NimSdk> getProjectSdks() {
    return myProjectSdks;
  }

  public boolean isModified() {
    return myModified;
  }

  public void apply() throws ConfigurationException {
    apply(null);
  }

  public void apply(@Nullable MasterDetailsComponent configurable) throws ConfigurationException {
    apply(configurable, false);
  }

  public void apply(@Nullable MasterDetailsComponent configurable, boolean addedOnly) throws ConfigurationException {
    String[] errorString = new String[1];
    if (!canApply(errorString, configurable, addedOnly)) {
      throw new ConfigurationException(errorString[0]);
    }

    doApply();
    myModified = false;
  }

  private void doApply() {
    ApplicationManager.getApplication().runWriteAction(() -> {
      final ArrayList<NimSdk> itemsInTable = new ArrayList<>();
      final NimProjectSdkTable jdkTable = NimProjectSdkTable.getInstance();
      final NimSdk[] allFromTable = jdkTable.getAllJdks();

      // Delete removed and fill itemsInTable
      for (final NimSdk tableItem : allFromTable) {
        if (myProjectSdks.containsKey(tableItem)) {
          itemsInTable.add(tableItem);
        }
        else {
          jdkTable.removeJdk(tableItem);
        }
      }

      // Now all removed items are deleted from table, itemsInTable contains all items in table
      for (NimSdk originalJdk : itemsInTable) {
        final NimSdk modifiedJdk = myProjectSdks.get(originalJdk);
        LOG.assertTrue(modifiedJdk != null);
        LOG.assertTrue(originalJdk != modifiedJdk);
        jdkTable.updateJdk(originalJdk, modifiedJdk);
      }
      // Add new items to table
      final NimSdk[] allJdks = jdkTable.getAllJdks();
      for (final NimSdk projectJdk : myProjectSdks.keySet()) {
        LOG.assertTrue(projectJdk != null);
        if (ArrayUtilRt.find(allJdks, projectJdk) == -1) {
          jdkTable.addJdk(projectJdk);
          jdkTable.updateJdk(projectJdk, myProjectSdks.get(projectJdk));
        }
      }
    });
  }

  private boolean canApply(@NotNull String[] errorString, @Nullable MasterDetailsComponent rootConfigurable, boolean addedOnly) throws ConfigurationException {
    Map<NimSdk, NimSdk> sdks = new LinkedHashMap<>(myProjectSdks);
    if (addedOnly) {
      NimSdk[] allJdks = NimProjectSdkTable.getInstance().getAllJdks();
      for (NimSdk jdk : allJdks) {
        sdks.remove(jdk);
      }
    }
    ArrayList<String> allNames = new ArrayList<>();
    NimSdk itemWithError = null;
    for (NimSdk currItem : sdks.values()) {
      String currName = currItem.getName();
      if (currName.isEmpty()) {
        itemWithError = currItem;
        errorString[0] = ProjectBundle.message("sdk.list.name.required.error");
        break;
      }
      if (allNames.contains(currName)) {
        itemWithError = currItem;
        errorString[0] = ProjectBundle.message("sdk.list.unique.name.required.error");
        break;
      }
      final NimSdkAdditionalData sdkAdditionalData = currItem.getSdkAdditionalData();
      if (sdkAdditionalData instanceof NimSdkValidatableSdkAdditionalData) {
        try {
          ((NimSdkValidatableSdkAdditionalData)sdkAdditionalData).checkValid(this);
        }
        catch (ConfigurationException e) {
          if (rootConfigurable != null) {
            final Object projectJdk = rootConfigurable.getSelectedObject();
            if (!(projectJdk instanceof NimSdk) ||
                !Comparing.strEqual(((NimSdk)projectJdk).getName(), currName)) { //do not leave current item with current name
              rootConfigurable.selectNodeInTree(currName);
            }
          }
          throw new ConfigurationException(ProjectBundle.message("sdk.configuration.exception", currName) + " " + e.getMessage());
        }
      }
      allNames.add(currName);
    }
    if (itemWithError == null) return true;
    if (rootConfigurable != null) {
      rootConfigurable.selectNodeInTree(itemWithError.getName());
    }
    return false;
  }

  public void removeSdk(@NotNull NimSdk editableObject) {
    NimSdk projectJdk = null;
    for (NimSdk jdk : myProjectSdks.keySet()) {
      if (myProjectSdks.get(jdk) == editableObject) {
        projectJdk = jdk;
        break;
      }
    }
    if (projectJdk != null) {
      myProjectSdks.remove(projectJdk);
      mySdkEventsDispatcher.getMulticaster().beforeSdkRemove(projectJdk);
      myModified = true;
    }
  }

  public void createAddActions(@NotNull DefaultActionGroup group, @NotNull JComponent parent, @NotNull Consumer<NimSdk> updateTree) {
    createAddActions(group, parent, updateTree, null);
  }

  public void createAddActions(@NotNull DefaultActionGroup group,
                               @NotNull final JComponent parent,
                               @NotNull final Consumer<NimSdk> updateTree,
                               @Nullable Condition<? super NimSdkTypeId> filter) {
    createAddActions(group, parent, null, updateTree, filter);
  }

  public void createAddActions(@NotNull DefaultActionGroup group,
                               @NotNull final JComponent parent,
                               @Nullable final NimSdk selectedSdk,
                               @NotNull final Consumer<NimSdk> updateTree,
                               @Nullable Condition<? super NimSdkTypeId> filter) {
    final NimSdkType[] types = NimSdkType.getAllTypes();
    for (final NimSdkType type : types) {
      if (!type.allowCreationByUser()) continue;
      if (filter != null && !filter.value(type)) continue;
      final AnAction addAction = new DumbAwareAction(type.getPresentableName(), null, type.getIconForAddAction()) {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
          doAdd(parent, selectedSdk, type, updateTree);
        }
      };
      group.add(addAction);
    }
  }

  public void doAdd(@NotNull JComponent parent, @NotNull final NimSdkType type, @NotNull final Consumer<NimSdk> callback) {
    doAdd(parent, null, type, callback);
  }

  public void doAdd(@NotNull JComponent parent, @Nullable final NimSdk selectedSdk, @NotNull final NimSdkType type, @NotNull final Consumer<NimSdk> callback) {
    myModified = true;
    if (type.supportsCustomCreateUI()) {
      type.showCustomCreateUI(this, parent, selectedSdk, sdk -> setupSdk(sdk, callback));
    }
    else {
      NimSdkConfigurationUtil.selectSdkHome(type, home -> addSdk(type, home, callback));
    }
  }

  public void addSdk(@NotNull NimSdkType type, @NotNull String home, @Nullable Consumer<NimSdk> callback) {
    String newSdkName = NimSdkConfigurationUtil.createUniqueSdkName(type, home, myProjectSdks.values());
    final NimProjectSdkImpl newJdk = new NimProjectSdkImpl(newSdkName, type);
    newJdk.setHomePath(home);
    setupSdk(newJdk, callback);
  }

  private void setupSdk(@NotNull NimSdk newJdk, @Nullable Consumer<NimSdk> callback) {
    String home = newJdk.getHomePath();
    NimSdkType sdkType = (NimSdkType)newJdk.getSdkType();
    if (!sdkType.setupSdkPaths(newJdk, this)) return;

    if (newJdk.getVersionString() == null) {
      String message = ProjectBundle.message("sdk.java.corrupt.error", home);
      Messages.showMessageDialog(message, ProjectBundle.message("sdk.java.corrupt.title"), Messages.getErrorIcon());
    }

    doAdd(newJdk, callback);
  }

  @Override
  public void addSdk(@NotNull NimSdk sdk) {
    doAdd(sdk, null);
  }

  public void doAdd(@NotNull NimSdk newSdk, @Nullable Consumer<NimSdk> updateTree) {
    myModified = true;
    try {
      NimSdk editableCopy = (NimSdk)newSdk.clone();
      myProjectSdks.put(newSdk, editableCopy);
      if (updateTree != null) {
        updateTree.consume(editableCopy);
      }
      mySdkEventsDispatcher.getMulticaster().sdkAdded(editableCopy);
    }
    catch (CloneNotSupportedException e) {
      LOG.error(e);
    }
  }

  @Nullable
  public NimSdk findSdk(@Nullable final NimSdk modelJdk) {
    for (Map.Entry<NimSdk, NimSdk> entry : myProjectSdks.entrySet()) {
      NimSdk jdk = entry.getKey();
      if (Comparing.equal(entry.getValue(), modelJdk)) {
        return jdk;
      }
    }
    return null;
  }

  @Nullable
  public NimSdk getProjectSdk() {
    if (!myProjectSdks.containsValue(myProjectSdk)) return null;
    return myProjectSdk;
  }

  public void setProjectSdk(final NimSdk projectSdk) {
    myProjectSdk = projectSdk;
  }

  public boolean isInitialized() {
    return myInitialized;
  }


}
