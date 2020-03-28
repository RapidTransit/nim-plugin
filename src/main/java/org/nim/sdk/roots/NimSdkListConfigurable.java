// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk.roots;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.ui.configuration.projectRoot.BaseStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.RemoveConfigurableHandler;
import com.intellij.openapi.roots.ui.configuration.projectRoot.daemon.ProjectStructureElement;
import com.intellij.openapi.ui.MasterDetailsComponent;
import com.intellij.openapi.ui.NamedConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.NimProjectSdksModel;
import org.nim.sdk.NimSdk;
import org.nim.sdk.NimSdkModel;
import org.nim.sdk.NimProjectSdkImpl;

import javax.swing.tree.TreePath;
import java.util.*;

import static com.intellij.openapi.projectRoots.SimpleJavaSdkType.notSimpleJavaSdkType;

public class NimSdkListConfigurable extends BaseStructureConfigurable {
  @NotNull
  private final NimProjectSdksModel myJdksTreeModel;
  private final NimSdkModel.Listener myListener = new NimSdkModel.Listener() {
    @Override
    public void sdkChanged(@NotNull NimSdk sdk, String previousName) {
      updateName();
    }

    @Override
    public void sdkHomeSelected(@NotNull NimSdk sdk, @NotNull String newSdkHome) {
      updateName();
    }

    private void updateName() {
      final TreePath path = myTree.getSelectionPath();
      if (path != null) {
        final NamedConfigurable configurable = ((MyNode)path.getLastPathComponent()).getConfigurable();
        if (configurable instanceof NimSdkConfigurable) {
          configurable.updateName();
        }
      }
    }
  };

  public NimSdkListConfigurable(@NotNull Project project) {
    super(project);
    myJdksTreeModel = NimSdkProjectStructureConfigurable.getInstance(project).getProjectJdksModel();
    myJdksTreeModel.addListener(myListener);
  }

  @Override
  protected String getComponentStateKey() {
    return "JdkListConfigurable.UI";
  }

  @Override
  @Nls
  public String getDisplayName() {
    return "SDKs";
  }

  @Override
  @Nullable
  @NonNls
  public String getHelpTopic() {
    return myCurrentConfigurable != null ? myCurrentConfigurable.getHelpTopic() : "reference.settingsdialog.project.structure.jdk";
  }

  @Override
  @NotNull
  @NonNls
  public String getId() {
    return "nim-sdk.list";
  }

  @Override
  protected void loadTree() {
    final Map<NimSdk, NimSdk> sdks = myJdksTreeModel.getProjectSdks();
    for (NimSdk sdk : sdks.keySet()) {
      final NimSdkConfigurable configurable = new NimSdkConfigurable((NimProjectSdkImpl)sdks.get(sdk), myJdksTreeModel, TREE_UPDATER, myHistory,
                                                               myProject);
      addNode(new MyNode(configurable), myRoot);
    }
  }

  @NotNull
  @Override
  protected Collection<? extends ProjectStructureElement> getProjectStructureElements() {
    final List<ProjectStructureElement> result = new ArrayList<>();
    for (NimSdk sdk : myJdksTreeModel.getProjectSdks().values()) {
      result.add(new NimSdkProjectStructureElement(myContext, sdk));
    }
    return result;
  }

  public boolean addJdkNode(final NimSdk jdk, final boolean selectInTree) {
    if (!myUiDisposed) {
      myContext.getDaemonAnalyzer().queueUpdate(new NimSdkProjectStructureElement(myContext, jdk));
      addNode(new MyNode(new NimSdkConfigurable((NimProjectSdkImpl)jdk, myJdksTreeModel, TREE_UPDATER, myHistory, myProject)), myRoot);
      if (selectInTree) {
        selectNodeInTree(MasterDetailsComponent.findNodeByObject(myRoot, jdk));
      }
      return true;
    }
    return false;
  }

  @Override
  public void dispose() {
    myJdksTreeModel.removeListener(myListener);
    myJdksTreeModel.disposeUIResources();
  }

  @NotNull
  public NimProjectSdksModel getJdksTreeModel() {
    return myJdksTreeModel;
  }

  @Override
  public void reset() {
    super.reset();
    myTree.setRootVisible(false);
  }

  @Override
  public void apply() throws ConfigurationException {
    boolean modifiedJdks = false;
    for (int i = 0; i < myRoot.getChildCount(); i++) {
      final NamedConfigurable configurable = ((MyNode)myRoot.getChildAt(i)).getConfigurable();
      if (configurable.isModified()) {
        configurable.apply();
        modifiedJdks = true;
      }
    }

    if (myJdksTreeModel.isModified() || modifiedJdks) myJdksTreeModel.apply(this);
    myJdksTreeModel.setProjectSdk(NimSdkProjectRootManager.getInstance(myProject).getProjectSdk());
  }

  @Override
  public boolean isModified() {
    return super.isModified() || myJdksTreeModel.isModified();
  }

  public static NimSdkListConfigurable getInstance(Project project) {
    return ServiceManager.getService(project, NimSdkListConfigurable.class);
  }

  @Override
  public AbstractAddGroup createAddAction() {
    return new AbstractAddGroup(ProjectBundle.message("add.new.jdk.text")) {
      @NotNull
      @Override
      public AnAction[] getChildren(@Nullable final AnActionEvent e) {
        DefaultActionGroup group = new DefaultActionGroup(ProjectBundle.message("add.new.jdk.text"), true);
        myJdksTreeModel.createAddActions(group, myTree, projectJdk -> addJdkNode(projectJdk, true),
                //@todo: Don't know what this does
                i-> false);
        return group.getChildren(null);
      }
    };
  }

  @Override
  protected List<? extends RemoveConfigurableHandler<?>> getRemoveHandlers() {
    return Collections.singletonList(new SdkRemoveHandler());
  }

  @Override
  protected
  @Nullable
  String getEmptySelectionString() {
    return "Select an SDK to view or edit its details here";
  }

  private class SdkRemoveHandler extends RemoveConfigurableHandler<NimSdk> {
    SdkRemoveHandler() {
      super(NimSdkConfigurable.class);
    }

    @Override
    public boolean remove(@NotNull Collection<? extends NimSdk> sdks) {
      for (NimSdk sdk : sdks) {
        myJdksTreeModel.removeSdk(sdk);
        myContext.getDaemonAnalyzer().removeElement(new NimSdkProjectStructureElement(myContext, sdk));
      }
      return true;
    }
  }
}
