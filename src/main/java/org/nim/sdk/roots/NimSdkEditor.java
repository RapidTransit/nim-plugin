// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk.roots;

import com.google.common.collect.Lists;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.ui.PathEditor;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.TabbedPaneWrapper;
import com.intellij.ui.navigation.History;
import com.intellij.ui.navigation.Place;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;

/**
 * @author MYakovlev
 */
public class NimSdkEditor implements Configurable, Place.Navigator {
  private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.projectRoots.ui.SdkEditor");
  private static final String SDK_TAB = "sdkTab";

  private NimSdk mySdk;
  private final Map<OrderRootType, NimSdkPathEditor> myPathEditors = new HashMap<>();

  private TextFieldWithBrowseButton myHomeComponent;
  private final Map<NimSdkType, List<NimJdkAdditionalDataConfigurable>> myAdditionalDataConfigurables = new HashMap<>();
  private final Map<NimJdkAdditionalDataConfigurable, JComponent> myAdditionalDataComponents = new HashMap<>();
  private JPanel myAdditionalDataPanel;
  private final NimSdkModificator myEditedSdkModificator = new EditedSdkModificator();

  // GUI components
  private JPanel myMainPanel;
  private TabbedPaneWrapper myTabbedPane;
  private final Project myProject;
  private final NimSdkModel mySdkModel;
  private JLabel myHomeFieldLabel;
  private String myVersionString;

  private String myInitialName;
  private String myInitialPath;
  private final History myHistory;

  private final Disposable myDisposable = Disposer.newDisposable();

  public NimSdkEditor(Project project, NimSdkModel sdkModel, History history, final NimProjectSdkImpl sdk) {
    myProject = project;
    mySdkModel = sdkModel;
    myHistory = history;
    mySdk = sdk;
    createMainPanel();
    initSdk(sdk);
  }

  private void initSdk(NimSdk sdk) {
    mySdk = sdk;
    if (mySdk != null) {
      myInitialName = mySdk.getName();
      myInitialPath = mySdk.getHomePath();
    }
    else {
      myInitialName = "";
      myInitialPath = "";
    }
    for (final NimJdkAdditionalDataConfigurable additionalDataConfigurable : getAdditionalDataConfigurable()) {
      additionalDataConfigurable.setSdk(sdk);
    }
    if (myMainPanel != null) {
      reset();
    }
  }

  @Override
  public String getDisplayName() {
    return ProjectBundle.message("sdk.configure.editor.title");
  }

  @Override
  public String getHelpTopic() {
    return null;
  }

  @Override
  public JComponent createComponent() {
    return myMainPanel;
  }

  private void createMainPanel() {
    myMainPanel = new JPanel(new GridBagLayout());

    myTabbedPane = new TabbedPaneWrapper(myDisposable);
    for (OrderRootType type : OrderRootType.getAllTypes()) {
      if (mySdk == null || showTabForType(type)) {
        //@todo: Fix
//        final NonJdkSdkPathEditor pathEditor = OrderRootTypeUIFactory.FACTORY.getByKey(type).createPathEditor(mySdk);
//        if (pathEditor != null) {
//          pathEditor.setAddBaseDir(mySdk.getHomeDirectory());
//          myTabbedPane.addTab(pathEditor.getDisplayName(), pathEditor.createComponent());
//          myPathEditors.put(type, pathEditor);
//        }
      }
    }

    myTabbedPane.addChangeListener(e -> myHistory.pushQueryPlace());

    myHomeComponent = createHomeComponent();
    myHomeComponent.getTextField().setEditable(false);
    myHomeFieldLabel = new JLabel(getHomeFieldLabelValue());
    myMainPanel.add(myHomeFieldLabel, new GridBagConstraints(
      0, GridBagConstraints.RELATIVE, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, JBUI.insets(2, 10, 2, 2), 0, 0));
    myMainPanel.add(myHomeComponent, new GridBagConstraints(
      1, GridBagConstraints.RELATIVE, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, JBUI.insets(2, 2, 2, 10), 0, 0));

    myAdditionalDataPanel = new JPanel(new BorderLayout());
    myMainPanel.add(myAdditionalDataPanel, new GridBagConstraints(
      0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, JBUI.insets(2, 10, 0, 10), 0, 0));

    myMainPanel.add(myTabbedPane.getComponent(), new GridBagConstraints(
      0, GridBagConstraints.RELATIVE, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, JBUI.insetsTop(2), 0, 0));
  }

  protected TextFieldWithBrowseButton createHomeComponent() {
    return new TextFieldWithBrowseButton(e -> doSelectHomePath());
  }

  protected boolean showTabForType(@NotNull OrderRootType type) {
    return ((SdkType)mySdk.getSdkType()).isRootTypeApplicable(type);
  }

  private String getHomeFieldLabelValue() {
    return mySdk != null ? ((SdkType)mySdk.getSdkType()).getHomeFieldLabel() : ProjectBundle.message("sdk.configure.general.home.path");
  }

  @Override
  public boolean isModified() {
    boolean isModified = !Comparing.equal(mySdk == null ? null : mySdk.getName(), myInitialName);
    isModified =
      isModified || !Comparing.equal(FileUtil.toSystemIndependentName(getHomeValue()), FileUtil.toSystemIndependentName(myInitialPath));
    for (PathEditor pathEditor : myPathEditors.values()) {
      isModified = isModified || pathEditor.isModified();
    }
    for (final NimJdkAdditionalDataConfigurable configurable : getAdditionalDataConfigurable()) {
      isModified = isModified || configurable.isModified();
    }
    return isModified;
  }

  @Override
  public void apply() throws ConfigurationException {
    if (!Comparing.equal(myInitialName, mySdk == null ? "" : mySdk.getName())) {
      if (mySdk == null || mySdk.getName().isEmpty()) {
        throw new ConfigurationException(ProjectBundle.message("sdk.list.name.required.error"));
      }
    }
    if (mySdk != null) {
      myInitialName = mySdk.getName();
      myInitialPath = mySdk.getHomePath();
      NimSdkModificator sdkModificator = mySdk.getSdkModificator();
      sdkModificator.setHomePath(FileUtil.toSystemIndependentName(getHomeValue()));
      for (NimSdkPathEditor pathEditor : myPathEditors.values()) {
        pathEditor.apply(sdkModificator);
      }
      ApplicationManager.getApplication().runWriteAction(sdkModificator::commitChanges);
      for (final NimJdkAdditionalDataConfigurable configurable : getAdditionalDataConfigurable()) {
        if (configurable != null) {
          configurable.apply();
        }
      }
    }
  }

  @Override
  public void reset() {
    if (mySdk == null) {
      setHomePathValue("");
      for (NimSdkPathEditor pathEditor : myPathEditors.values()) {
        pathEditor.reset(null);
      }
    }
    else {
      final NimSdkModificator sdkModificator = mySdk.getSdkModificator();
      for (OrderRootType type : myPathEditors.keySet()) {
        myPathEditors.get(type).reset(sdkModificator);
      }
      sdkModificator.commitChanges();
      setHomePathValue(FileUtil.toSystemDependentName(ObjectUtils.notNull(mySdk.getHomePath(), "")));
    }
    myVersionString = null;
    myHomeFieldLabel.setText(getHomeFieldLabelValue());
    updateAdditionalDataComponent();

    for (final NimJdkAdditionalDataConfigurable configurable : getAdditionalDataConfigurable()) {
      configurable.reset();
    }

    myHomeComponent.setEnabled(mySdk != null);

    for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
      myTabbedPane.setEnabledAt(i, mySdk != null);
    }
  }

  @Override
  public void disposeUIResources() {
    for (final NimSdkType sdkType : myAdditionalDataConfigurables.keySet()) {
      for (final NimJdkAdditionalDataConfigurable configurable : myAdditionalDataConfigurables.get(sdkType)) {
        configurable.disposeUIResources();
      }
    }
    myAdditionalDataConfigurables.clear();
    myAdditionalDataComponents.clear();

    Disposer.dispose(myDisposable);
  }

  private String getHomeValue() {
    return myHomeComponent.getText().trim();
  }

  private void clearAllPaths() {
    for (PathEditor editor : myPathEditors.values()) {
      editor.clearList();
    }
  }

  private void setHomePathValue(String absolutePath) {
    myHomeComponent.setText(absolutePath);
    final Color fg;
    if (absolutePath != null && !absolutePath.isEmpty() && mySdk != null && mySdk.getSdkType().isLocalSdk(mySdk)) {
      final File homeDir = new File(absolutePath);
      boolean homeMustBeDirectory = ((NimSdkType)mySdk.getSdkType()).getHomeChooserDescriptor().isChooseFolders();
      fg = homeDir.exists() && homeDir.isDirectory() == homeMustBeDirectory
           ? UIUtil.getFieldForegroundColor()
           : PathEditor.INVALID_COLOR;
    }
    else {
      fg = UIUtil.getFieldForegroundColor();
    }
    myHomeComponent.getTextField().setForeground(fg);
  }

  private void doSelectHomePath() {
    final NimSdkType sdkType = (NimSdkType)mySdk.getSdkType();
    NimSdkConfigurationUtil.selectSdkHome(sdkType, path -> doSetHomePath(path, sdkType));
  }

  private void doSetHomePath(final String homePath, final NimSdkType sdkType) {
    if (homePath == null) {
      return;
    }
    setHomePathValue(homePath.replace('/', File.separatorChar));

    final String newSdkName = suggestSdkName(homePath);
    ((NimProjectSdkImpl)mySdk).setName(newSdkName);

    try {
      final NimSdk dummySdk = (NimSdk)mySdk.clone();
      NimSdkModificator sdkModificator = dummySdk.getSdkModificator();
      sdkModificator.setHomePath(homePath);
      sdkModificator.removeAllRoots();
      sdkModificator.commitChanges();

      sdkType.setupSdkPaths(dummySdk, mySdkModel);

      clearAllPaths();
      myVersionString = dummySdk.getVersionString();
      if (myVersionString == null) {
        Messages.showMessageDialog(ProjectBundle.message("sdk.java.corrupt.error", homePath),
                                   ProjectBundle.message("sdk.java.corrupt.title"), Messages.getErrorIcon());
      }
      sdkModificator = dummySdk.getSdkModificator();
      for (OrderRootType type : myPathEditors.keySet()) {
        NimSdkPathEditor pathEditor = myPathEditors.get(type);
        pathEditor.setAddBaseDir(dummySdk.getHomeDirectory());
        pathEditor.addPaths(sdkModificator.getRoots(type));
      }
      mySdkModel.getMulticaster().sdkHomeSelected(dummySdk, homePath);
    }
    catch (CloneNotSupportedException e) {
      LOG.error(e); // should not happen in normal program
    }
  }

  private String suggestSdkName(final String homePath) {
    final String currentName = mySdk.getName();
    final String suggestedName = ((SdkType)mySdk.getSdkType()).suggestSdkName(currentName, homePath);
    if (Comparing.equal(currentName, suggestedName)) return currentName;
    String newSdkName = suggestedName;
    final Set<String> allNames = new HashSet<>();
    NimSdk[] sdks = mySdkModel.getSdks();
    for (NimSdk sdk : sdks) {
      allNames.add(sdk.getName());
    }
    int i = 0;
    while (allNames.contains(newSdkName)) {
      newSdkName = suggestedName + " (" + ++i + ")";
    }
    return newSdkName;
  }

  private void updateAdditionalDataComponent() {
    myAdditionalDataPanel.removeAll();
    for (NimJdkAdditionalDataConfigurable configurable : getAdditionalDataConfigurable()) {
      JComponent component = myAdditionalDataComponents.get(configurable);
      if (component == null) {
        component = configurable.createComponent();
        myAdditionalDataComponents.put(configurable, component);
      }
      if (component != null) {
        if (configurable.getTabName() != null) {
          for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
            if (configurable.getTabName().equals(myTabbedPane.getTitleAt(i))) {
              myTabbedPane.removeTabAt(i);
            }
          }
          myTabbedPane.addTab(configurable.getTabName(), component);
        }
        else {
          myAdditionalDataPanel.add(component, BorderLayout.CENTER);
        }
      }
    }
  }

  @NotNull
  private List<NimJdkAdditionalDataConfigurable> getAdditionalDataConfigurable() {
    if (mySdk == null) {
      return ContainerUtil.emptyList();
    }
    return initAdditionalDataConfigurable(mySdk);
  }

  @NotNull
  private List<NimJdkAdditionalDataConfigurable> initAdditionalDataConfigurable(NimSdk sdk) {
    final NimSdkType sdkType = (NimSdkType)sdk.getSdkType();
    List<NimJdkAdditionalDataConfigurable> configurables = myAdditionalDataConfigurables.get(sdkType);
    if (configurables == null) {
      configurables = Lists.newArrayList();
      myAdditionalDataConfigurables.put(sdkType, configurables);


      NimJdkAdditionalDataConfigurable sdkConfigurable = sdkType.createAdditionalDataConfigurable(mySdkModel, myEditedSdkModificator);
      if (sdkConfigurable != null) {
        configurables.add(sdkConfigurable);
      }

      //@todo: Fix
//      for (SdkEditorAdditionalOptionsProvider factory : SdkEditorAdditionalOptionsProvider.getSdkOptionsFactory(mySdk.getSdkType())) {
//        NonJdkAdditionalDataConfigurable options = factory.createOptions(myProject, mySdk);
//        if (options != null) {
//          configurables.add(options);
//        }
//      }
    }

    return configurables;
  }

  private class EditedSdkModificator implements NimSdkModificator {
    @NotNull
    @Override
    public String getName() {
      return mySdk.getName();
    }

    @Override
    public void setName(@NotNull String name) {
      ((NimProjectSdkImpl)mySdk).setName(name);
    }

    @Override
    public String getHomePath() {
      return getHomeValue();
    }

    @Override
    public void setHomePath(String path) {
      doSetHomePath(path, (NimSdkType)mySdk.getSdkType());
    }

    @Override
    public String getVersionString() {
      return myVersionString != null ? myVersionString : mySdk.getVersionString();
    }

    @Override
    public void setVersionString(String versionString) {
      throw new UnsupportedOperationException(); // not supported for this editor
    }

    @Override
    public NimSdkAdditionalData getSdkAdditionalData() {
      return mySdk.getSdkAdditionalData();
    }

    @Override
    public void setSdkAdditionalData(NimSdkAdditionalData data) {
      throw new UnsupportedOperationException(); // not supported for this editor
    }

    @NotNull
    @Override
    public VirtualFile[] getRoots(@NotNull OrderRootType rootType) {
      final PathEditor editor = myPathEditors.get(rootType);
      if (editor == null) throw new IllegalStateException("no editor for root type " + rootType);
      return editor.getRoots();
    }

    @Override
    public void addRoot(@NotNull VirtualFile root, @NotNull OrderRootType rootType) {
      myPathEditors.get(rootType).addPaths(root);
    }

    @Override
    public void removeRoot(@NotNull VirtualFile root, @NotNull OrderRootType rootType) {
      myPathEditors.get(rootType).removePaths(root);
    }

    @Override
    public void removeRoots(@NotNull OrderRootType rootType) {
      myPathEditors.get(rootType).clearList();
    }

    @Override
    public void removeAllRoots() {
      for (PathEditor editor : myPathEditors.values()) {
        editor.clearList();
      }
    }

    @Override
    public void commitChanges() { }

    @Override
    public boolean isWritable() {
      return true;
    }
  }

  @Override
  public ActionCallback navigateTo(@Nullable final Place place, final boolean requestFocus) {
    if (place == null) return ActionCallback.DONE;
    myTabbedPane.setSelectedTitle((String)place.getPath(SDK_TAB));
    return ActionCallback.DONE;
  }

  @Override
  public void queryPlace(@NotNull final Place place) {
    place.putPath(SDK_TAB, myTabbedPane.getSelectedTitle());
  }
}