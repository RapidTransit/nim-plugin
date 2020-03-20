// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk.roots;

import com.intellij.compiler.server.BuildManager;
import com.intellij.facet.Facet;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.AccessToken;
import com.intellij.openapi.application.TransactionGuard;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.LibraryOrderEntry;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar;
import com.intellij.openapi.roots.ui.configuration.*;
import com.intellij.openapi.roots.ui.configuration.artifacts.ArtifactsStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.*;
import com.intellij.openapi.ui.DetailsComponent;
import com.intellij.openapi.ui.MasterDetailsComponent;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ex.IdeFocusTraversalPolicy;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.panels.Wrapper;
import com.intellij.ui.navigation.BackAction;
import com.intellij.ui.navigation.ForwardAction;
import com.intellij.ui.navigation.History;
import com.intellij.ui.navigation.Place;
import com.intellij.util.io.storage.HeavyProcessLatch;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.NimProjectSdksModel;
import org.nim.sdk.NimSdk;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurableFilter.ConfigurableId;

public class NimSdkProjectStructureConfigurable implements SearchableConfigurable, Place.Navigator,
                                                                              Configurable.NoMargin, Configurable.NoScroll {
  private static final Logger LOG = Logger.getInstance("#com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable");

  public static final DataKey<NimSdkProjectStructureConfigurable> KEY = DataKey.create("NimProjectStructureConfiguration");

  protected final UIState myUiState = new UIState();
  private JBSplitter mySplitter;
  private JComponent myToolbarComponent;
  @NonNls public static final String CATEGORY = "category";
  private JComponent myToFocus;

  public static class UIState {
    public float proportion;
    public float sideProportion;

    public String lastEditedConfigurable;
  }

  private final Project myProject;
  private final FacetStructureConfigurable myFacetStructureConfigurable;
  private final ArtifactsStructureConfigurable myArtifactsStructureConfigurable;

  private History myHistory = new History(this);
  private SidePanel mySidePanel;

  private JPanel myComponent;
  private final Wrapper myDetails = new Wrapper();

  private Configurable mySelectedConfigurable;

  private final NimProjectSdksModel myProjectJdksModel = new NimProjectSdksModel();

  private NimSdkProjectConfigurable myProjectConfig;
  private final ProjectLibrariesConfigurable myProjectLibrariesConfig;
  private final GlobalLibrariesConfigurable myGlobalLibrariesConfig;
  private ModuleStructureConfigurable myModulesConfig;

  private boolean myUiInitialized;

  private final List<Configurable> myName2Config = new ArrayList<>();
  private final StructureConfigurableContext myContext;
  private final ModulesConfigurator myModuleConfigurator;
  private JdkListConfigurable myJdkListConfig;

  private final JLabel myEmptySelection = new JLabel("<html><body><center>Select a setting to view or edit its details here</center></body></html>",
                                                     SwingConstants.CENTER);

  private final ObsoleteLibraryFilesRemover myObsoleteLibraryFilesRemover;

  public NimSdkProjectStructureConfigurable(@NotNull Project project) {
    myProject = project;
    myFacetStructureConfigurable = FacetStructureConfigurable.getInstance(project);
    myArtifactsStructureConfigurable = project.getService(ArtifactsStructureConfigurable.class);

    myModuleConfigurator = new ModulesConfigurator(myProject);
    myContext = new StructureConfigurableContext(myProject, myModuleConfigurator);
    myModuleConfigurator.setContext(myContext);

    myProjectLibrariesConfig = ProjectLibrariesConfigurable.getInstance(project);
    myGlobalLibrariesConfig = GlobalLibrariesConfigurable.getInstance(project);
    myModulesConfig = ModuleStructureConfigurable.getInstance(project);

    myProjectLibrariesConfig.init(myContext);
    myGlobalLibrariesConfig.init(myContext);
    myModulesConfig.init(myContext);
    myFacetStructureConfigurable.init(myContext);
    if (!project.isDefault()) {
      myArtifactsStructureConfigurable.init(myContext, myModulesConfig, myProjectLibrariesConfig, myGlobalLibrariesConfig);
    }

    final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(myProject);
    myUiState.lastEditedConfigurable = propertiesComponent.getValue("project.structure.last.edited");
    final String proportion = propertiesComponent.getValue("project.structure.proportion");
    myUiState.proportion = proportion != null ? Float.parseFloat(proportion) : 0;
    final String sideProportion = propertiesComponent.getValue("project.structure.side.proportion");
    myUiState.sideProportion = sideProportion != null ? Float.parseFloat(sideProportion) : 0;
    myObsoleteLibraryFilesRemover = new ObsoleteLibraryFilesRemover(project);
  }

  @Override
  @NotNull
  @NonNls
  public String getId() {
    return "project.structure";
  }

  @Override
  @Nls
  public String getDisplayName() {
    return ProjectBundle.message("project.settings.display.name");
  }

  @Override
  @Nullable
  @NonNls
  public String getHelpTopic() {
    return mySelectedConfigurable != null ? mySelectedConfigurable.getHelpTopic() : "";
  }

  @Override
  public JComponent createComponent() {
    myComponent = new MyPanel();

    mySplitter = new OnePixelSplitter(false, .15f);
    mySplitter.setSplitterProportionKey("ProjectStructure.TopLevelElements");
    mySplitter.setHonorComponentsMinimumSize(true);

    initSidePanel();

    final JPanel left = new JPanel(new BorderLayout()) {
      @Override
      public Dimension getMinimumSize() {
        final Dimension original = super.getMinimumSize();
        return new Dimension(Math.max(original.width, 100), original.height);
      }
    };

    final DefaultActionGroup toolbarGroup = new DefaultActionGroup();
    toolbarGroup.add(new BackAction(myComponent, myContext));
    toolbarGroup.add(new ForwardAction(myComponent, myContext));
    final ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("ProjectStructure", toolbarGroup, true);
    toolbar.setTargetComponent(myComponent);
    myToolbarComponent = toolbar.getComponent();
    left.setBackground(UIUtil.SIDE_PANEL_BACKGROUND);
    myToolbarComponent.setBackground(UIUtil.SIDE_PANEL_BACKGROUND);
    left.add(myToolbarComponent, BorderLayout.NORTH);
    left.add(mySidePanel, BorderLayout.CENTER);

    mySplitter.setFirstComponent(left);
    mySplitter.setSecondComponent(myDetails);

    myComponent.add(mySplitter, BorderLayout.CENTER);

    myUiInitialized = true;

    return myComponent;
  }

  private void initSidePanel() {
    boolean isDefaultProject = myProject == ProjectManager.getInstance().getDefaultProject();

    mySidePanel = new SidePanel(this);
    mySidePanel.addSeparator("Project Settings");
    addProjectConfig();
    if (!isDefaultProject) {
      addModulesConfig();
    }
    addProjectLibrariesConfig();

    if (!isDefaultProject) {
      addFacetsConfig();
      addArtifactsConfig();
    }

    ProjectStructureConfigurableContributor[] adders = ProjectStructureConfigurableContributor.EP_NAME.getExtensions();
    for (ProjectStructureConfigurableContributor adder : adders) {
      for (Configurable configurable : adder.getExtraProjectConfigurables(myProject, myContext)) {
        addConfigurable(configurable, true);
      }
    }

    mySidePanel.addSeparator("Platform Settings");
    addJdkListConfig();
    addGlobalLibrariesConfig();

    for (ProjectStructureConfigurableContributor adder : adders) {
      for (Configurable configurable : adder.getExtraPlatformConfigurables(myProject, myContext)) {
        addConfigurable(configurable, true);
      }
    }

    mySidePanel.addSeparator("--");
    addErrorPane();
  }

  private void addArtifactsConfig() {
    addConfigurable(myArtifactsStructureConfigurable, ConfigurableId.ARTIFACTS);
  }

  private void addConfigurable(final Configurable configurable, final ConfigurableId configurableId) {
    addConfigurable(configurable, isAvailable(configurableId));
  }

  private boolean isAvailable(ConfigurableId id) {
    for (ProjectStructureConfigurableFilter filter : ProjectStructureConfigurableFilter.EP_NAME.getExtensions()) {
      if (!filter.isAvailable(id, myProject)) {
        return false;
      }
    }
    return true;
  }

  public ArtifactsStructureConfigurable getArtifactsStructureConfigurable() {
    return myArtifactsStructureConfigurable;
  }

  private void addFacetsConfig() {
    if (myFacetStructureConfigurable.isVisible()) {
      addConfigurable(myFacetStructureConfigurable, ConfigurableId.FACETS);
    }
  }

  private void addJdkListConfig() {
    if (myJdkListConfig == null) {
      myJdkListConfig = JdkListConfigurable.getInstance(myProject);
      myJdkListConfig.init(myContext);
    }
    addConfigurable(myJdkListConfig, ConfigurableId.JDK_LIST);
  }

  private void addProjectConfig() {
    myProjectConfig = new NimSdkProjectConfigurable(myProject, myContext, myModuleConfigurator, myProjectJdksModel);
    addConfigurable(myProjectConfig, ConfigurableId.PROJECT);
  }

  private void addProjectLibrariesConfig() {
    addConfigurable(myProjectLibrariesConfig, ConfigurableId.PROJECT_LIBRARIES);
  }

  private void addErrorPane() {
    addConfigurable(new ErrorPaneConfigurable(myProject, myContext, () -> mySidePanel.getList().repaint()), true);
  }

  private void addGlobalLibrariesConfig() {
    addConfigurable(myGlobalLibrariesConfig, ConfigurableId.GLOBAL_LIBRARIES);
  }

  private void addModulesConfig() {
    myModulesConfig = ModuleStructureConfigurable.getInstance(myProject);
    addConfigurable(myModulesConfig, ConfigurableId.MODULES);
  }

  @Override
  public boolean isModified() {
    for (Configurable each : myName2Config) {
      if (each.isModified()) return true;
    }

    return false;
  }

  @Override
  public void apply() throws ConfigurationException {
    LOG.assertTrue(TransactionGuard.getInstance().getContextTransaction() != null, "Project Structure should be shown in a transaction, see AnAction#startInTransaction");

    for (Configurable each : myName2Config) {
      if (each instanceof BaseStructureConfigurable && each.isModified()) {
        ((BaseStructureConfigurable)each).checkCanApply();
      }
    }
    final Ref<ConfigurationException> exceptionRef = Ref.create();
    try {
      for (Configurable each : myName2Config) {
        if (each.isModified()) {
          each.apply();
        }
      }
    }
    catch (ConfigurationException e) {
      exceptionRef.set(e);
    }

    if (!exceptionRef.isNull()) {
      throw exceptionRef.get();
    }

    myObsoleteLibraryFilesRemover.deleteFiles();
    myContext.getDaemonAnalyzer().clearCaches();
    BuildManager.getInstance().scheduleAutoMake();
  }

  @Override
  public void reset() {
    // need this to ensure VFS operations will not block because of storage flushing
    // and other maintenance IO tasks run in background
    AccessToken token = HeavyProcessLatch.INSTANCE.processStarted("Resetting Project Structure");

    try {

      myContext.reset();

      myProjectJdksModel.reset(myProject);

      Configurable toSelect = null;
      for (Configurable each : myName2Config) {
        if (myUiState.lastEditedConfigurable != null && myUiState.lastEditedConfigurable.equals(each.getDisplayName())) {
          toSelect = each;
        }
        if (each instanceof MasterDetailsComponent) {
          ((MasterDetailsComponent)each).setHistory(myHistory);
        }
        each.reset();
      }

      myHistory.clear();

      if (toSelect == null && myName2Config.size() > 0) {
        toSelect = myName2Config.iterator().next();
      }

      removeSelected();

      navigateTo(toSelect != null ? createPlaceFor(toSelect) : null, false);

      if (myUiState.proportion > 0) {
        mySplitter.setProportion(myUiState.proportion);
      }
    }
    finally {
      token.finish();
    }
  }

  @Override
  public void disposeUIResources() {
    if (!myUiInitialized) return;
    final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(myProject);
    propertiesComponent.setValue("project.structure.last.edited", myUiState.lastEditedConfigurable);
    propertiesComponent.setValue("project.structure.proportion", String.valueOf(myUiState.proportion));
    propertiesComponent.setValue("project.structure.side.proportion", String.valueOf(myUiState.sideProportion));

    myUiState.proportion = mySplitter.getProportion();
    saveSideProportion();
    myContext.getDaemonAnalyzer().stop();
    for (Configurable each : myName2Config) {
      each.disposeUIResources();
    }
    myContext.clear();
    myName2Config.clear();

    myModuleConfigurator.getFacetsConfigurator().clearMaps();

    myUiInitialized = false;
  }

  public boolean isUiInitialized() {
    return myUiInitialized;
  }

  public History getHistory() {
    return myHistory;
  }

  @Override
  public void setHistory(final History history) {
    myHistory = history;
  }

  @Override
  public void queryPlace(@NotNull final Place place) {
    place.putPath(CATEGORY, mySelectedConfigurable);
    Place.queryFurther(mySelectedConfigurable, place);
  }

  public ActionCallback selectProjectGeneralSettings(final boolean requestFocus) {
    return navigateTo(createProjectConfigurablePlace(), requestFocus);
  }

  public Place createProjectConfigurablePlace() {
    return createPlaceFor(myProjectConfig);
  }

  public ActionCallback select(@Nullable final String moduleToSelect, @Nullable String editorNameToSelect, final boolean requestFocus) {
    Place place = createModulesPlace();
    if (moduleToSelect != null) {
      final Module module = ModuleManager.getInstance(myProject).findModuleByName(moduleToSelect);
      assert module != null;
      place = place.putPath(MasterDetailsComponent.TREE_OBJECT, module).putPath(ModuleEditor.SELECTED_EDITOR_NAME, editorNameToSelect);
    }
    return navigateTo(place, requestFocus);
  }

  public Place createModulesPlace() {
    return createPlaceFor(myModulesConfig);
  }

  public Place createModulePlace(@NotNull Module module) {
    return createModulesPlace().putPath(MasterDetailsComponent.TREE_OBJECT, module);
  }

  public ActionCallback select(@Nullable final Facet facetToSelect, final boolean requestFocus) {
    Place place = createModulesPlace();
    if (facetToSelect != null) {
      place = place.putPath(MasterDetailsComponent.TREE_OBJECT, facetToSelect);
    }
    return navigateTo(place, requestFocus);
  }

  public ActionCallback select(@NotNull NimSdk sdk, final boolean requestFocus) {
    Place place = createPlaceFor(myJdkListConfig);
    place.putPath(MasterDetailsComponent.TREE_NAME, sdk.getName());
    return navigateTo(place, requestFocus);
  }

  public ActionCallback selectGlobalLibraries(final boolean requestFocus) {
    Place place = createPlaceFor(myGlobalLibrariesConfig);
    return navigateTo(place, requestFocus);
  }

  public ActionCallback selectProjectOrGlobalLibrary(@NotNull Library library, boolean requestFocus) {
    Place place = createProjectOrGlobalLibraryPlace(library);
    return navigateTo(place, requestFocus);
  }

  public Place createProjectOrGlobalLibraryPlace(Library library) {
    Place place = createPlaceFor(getConfigurableFor(library));
    place.putPath(MasterDetailsComponent.TREE_NAME, library.getName());
    return place;
  }

  public ActionCallback select(@Nullable Artifact artifact, boolean requestFocus) {
    Place place = createArtifactPlace(artifact);
    return navigateTo(place, requestFocus);
  }

  public Place createArtifactPlace(Artifact artifact) {
    Place place = createPlaceFor(myArtifactsStructureConfigurable);
    if (artifact != null) {
      place.putPath(MasterDetailsComponent.TREE_NAME, artifact.getName());
    }
    return place;
  }

  public ActionCallback select(@NotNull LibraryOrderEntry libraryOrderEntry, final boolean requestFocus) {
    final Library lib = libraryOrderEntry.getLibrary();
    if (lib == null || lib.getTable() == null) {
      return selectOrderEntry(libraryOrderEntry.getOwnerModule(), libraryOrderEntry);
    }
    Place place = createPlaceFor(getConfigurableFor(lib));
    place.putPath(MasterDetailsComponent.TREE_NAME, libraryOrderEntry.getLibraryName());
    return navigateTo(place, requestFocus);
  }

  public ActionCallback selectOrderEntry(@NotNull final Module module, @Nullable final OrderEntry orderEntry) {
    return ModuleStructureConfigurable.getInstance(myProject).selectOrderEntry(module, orderEntry);
  }

  @Override
  public ActionCallback navigateTo(@Nullable final Place place, final boolean requestFocus) {
    final Configurable toSelect = (Configurable)place.getPath(CATEGORY);

    JComponent detailsContent = myDetails.getTargetComponent();

    if (mySelectedConfigurable != toSelect) {
      if (mySelectedConfigurable instanceof BaseStructureConfigurable) {
        ((BaseStructureConfigurable)mySelectedConfigurable).onStructureUnselected();
      }
      saveSideProportion();
      removeSelected();

      if (toSelect != null) {
        detailsContent = toSelect.createComponent();
        myDetails.setContent(detailsContent);
      }

      mySelectedConfigurable = toSelect;
      if (mySelectedConfigurable != null) {
        myUiState.lastEditedConfigurable = mySelectedConfigurable.getDisplayName();
      }

      if (toSelect instanceof MasterDetailsComponent) {
        final MasterDetailsComponent masterDetails = (MasterDetailsComponent)toSelect;
        if (myUiState.sideProportion > 0) {
          masterDetails.getSplitter().setProportion(myUiState.sideProportion);
        }
        masterDetails.setHistory(myHistory);
      }

      if (toSelect instanceof DetailsComponent.Facade) {
        ((DetailsComponent.Facade)toSelect).getDetailsComponent().setBannerMinHeight(myToolbarComponent.getPreferredSize().height);
      }

      if (toSelect instanceof BaseStructureConfigurable) {
        ((BaseStructureConfigurable)toSelect).onStructureSelected();
      }
    }



    if (detailsContent != null) {
      JComponent toFocus = IdeFocusTraversalPolicy.getPreferredFocusedComponent(detailsContent);
      if (toFocus == null) {
        toFocus = detailsContent;
      }
      if (requestFocus) {
        myToFocus = toFocus;
        UIUtil.requestFocus(toFocus);
      }
    }

    final ActionCallback result = new ActionCallback();
    Place.goFurther(toSelect, place, requestFocus).notifyWhenDone(result);

    myDetails.revalidate();
    myDetails.repaint();

    if (toSelect != null) {
      mySidePanel.select(createPlaceFor(toSelect));
    }

    if (!myHistory.isNavigatingNow() && mySelectedConfigurable != null) {
      myHistory.pushQueryPlace();
    }

    return result;
  }

  private void saveSideProportion() {
    if (mySelectedConfigurable instanceof MasterDetailsComponent) {
      myUiState.sideProportion = ((MasterDetailsComponent)mySelectedConfigurable).getSplitter().getProportion();
    }
  }

  private void removeSelected() {
    myDetails.removeAll();
    mySelectedConfigurable = null;
    myUiState.lastEditedConfigurable = null;

    myDetails.add(myEmptySelection, BorderLayout.CENTER);
  }

  public static NimSdkProjectStructureConfigurable getInstance(final Project project) {
    return ServiceManager.getService(project, NimSdkProjectStructureConfigurable.class);
  }

  @NotNull
  public NimProjectSdksModel getProjectJdksModel() {
    return myProjectJdksModel;
  }

  public JdkListConfigurable getJdkConfig() {
    return myJdkListConfig;
  }

  public ModuleStructureConfigurable getModulesConfig() {
    return myModulesConfig;
  }

  public NimSdkProjectConfigurable getProjectConfig() {
    return myProjectConfig;
  }

  public void registerObsoleteLibraryRoots(@NotNull Collection<? extends VirtualFile> roots) {
    myObsoleteLibraryFilesRemover.registerObsoleteLibraryRoots(roots);
  }

  private void addConfigurable(Configurable configurable, boolean addToSidePanel) {
    myName2Config.add(configurable);

    if (addToSidePanel) {
      mySidePanel.addPlace(createPlaceFor(configurable), new Presentation(configurable.getDisplayName()));
    }
  }

  private static Place createPlaceFor(final Configurable configurable) {
    return new Place().putPath(CATEGORY, configurable);
  }


  public StructureConfigurableContext getContext() {
    return myContext;
  }

  private class MyPanel extends JPanel implements DataProvider {
    MyPanel() {
      super(new BorderLayout());
    }

    @Override
    @Nullable
    public Object getData(@NotNull @NonNls final String dataId) {
      if (KEY.is(dataId)) {
        return NimSdkProjectStructureConfigurable.this;
      } else if (History.KEY.is(dataId)) {
        return getHistory();
      } else {
        return null;
      }
    }

    @Override
    public Dimension getPreferredSize() {
      return JBUI.size(1024, 768);
    }
  }

  public BaseLibrariesConfigurable getConfigurableFor(final Library library) {
    if (LibraryTablesRegistrar.PROJECT_LEVEL.equals(library.getTable().getTableLevel())) {
      return myProjectLibrariesConfig;
    } else {
      return myGlobalLibrariesConfig;
    }
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return myToFocus;
  }
}
