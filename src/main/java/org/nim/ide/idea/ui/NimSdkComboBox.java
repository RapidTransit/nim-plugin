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
package org.nim.ide.idea.ui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.CellAppearanceEx;
import com.intellij.openapi.roots.ui.FileAppearanceService;
import com.intellij.openapi.roots.ui.OrderEntryAppearanceService;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.JdkListConfigurable;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.roots.ui.util.CompositeAppearance;
import com.intellij.openapi.ui.ComboBoxWithWidePopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ScreenUtil;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.NimSdk;
import org.nim.sdk.NimSdksModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

import static com.intellij.openapi.projectRoots.SimpleJavaSdkType.notSimpleJavaSdkType;

/**
 * @author Eugene Zhuravlev
 * Modified for Nim SDK types
 */
public class NimSdkComboBox extends ComboBoxWithWidePopup<NimSdkComboBox.SdkComboBoxItem> {

  @SuppressWarnings("UnstableApiUsage")
  private static final Icon EMPTY_ICON = JBUI.scale(EmptyIcon.create(1, 16));

  @Nullable
  private final Condition<? super Sdk> myFilter;
  @Nullable
  private final Condition<SdkTypeId> myCreationFilter;
  private JButton mySetUpButton;
  private final Condition<? super SdkTypeId> mySdkTypeFilter;

  public NimSdkComboBox(@NotNull final NimSdksModel jdkModel) {
    this(jdkModel, null);
  }

  public NimSdkComboBox(@NotNull final NimSdksModel jdkModel,
                        @Nullable Condition<? super SdkTypeId> filter) {
    this(jdkModel, filter, getSdkFilter(filter), filter, false);
  }

  public NimSdkComboBox(@NotNull final NimSdksModel jdkModel,
                        @Nullable Condition<? super SdkTypeId> sdkTypeFilter,
                        @Nullable Condition<? super Sdk> filter,
                        @Nullable Condition<? super SdkTypeId> creationFilter,
                        boolean addSuggestedItems) {
    super(new JdkComboBoxModel(jdkModel, sdkTypeFilter, filter, addSuggestedItems));
    myFilter = filter;
    mySdkTypeFilter = sdkTypeFilter;
    myCreationFilter = getCreationFilter(creationFilter);
    setRenderer(new ColoredListCellRenderer<SdkComboBoxItem>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends SdkComboBoxItem> list,
                                           SdkComboBoxItem value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {

        if (NimSdkComboBox.this.isEnabled()) {
          setIcon(EMPTY_ICON);    // to fix vertical size
          if (value instanceof InvalidJdkComboBoxItem) {
            final String str = value.toString();
            append(str, SimpleTextAttributes.ERROR_ATTRIBUTES);
          }
          else if (value instanceof ProjectJdkComboBoxItem) {
            final Sdk jdk = jdkModel.getProjectSdk();
            if (jdk != null) {
              setIcon(((SdkType)jdk.getSdkType()).getIcon());
              append(ProjectBundle.message("project.roots.project.jdk.inherited"), SimpleTextAttributes.REGULAR_ATTRIBUTES);
              append(" (" + jdk.getName() + ")", SimpleTextAttributes.GRAYED_ATTRIBUTES);
            }
            else {
              final String str = value.toString();
              append(str, SimpleTextAttributes.ERROR_ATTRIBUTES);
            }
          }
          else if (value instanceof SuggestedJdkItem) {
            SdkType type = ((SuggestedJdkItem)value).getSdkType();
            String home = ((SuggestedJdkItem)value).getPath();
            setIcon(type.getIconForAddAction());
            String version = type.getVersionString(home);
            append(version == null ? type.getPresentableName() : version);
            append(" (" + home + ")", SimpleTextAttributes.GRAYED_ATTRIBUTES);
          }
          else if (value != null) {
            forJdk(value.getSdk(), false, selected, true).customize(this);
          }
          else {
            customizeCellRenderer(list, new NoneJdkComboBoxItem(), index, selected, hasFocus);
          }
        }
      }
    });
  }

  @NotNull
  private static Condition<SdkTypeId> getCreationFilter(@Nullable Condition<? super SdkTypeId> creationFilter) {
    return notSimpleJavaSdkType(creationFilter);
  }

  @Override
  public Dimension getPreferredSize() {
    final Rectangle rec = ScreenUtil.getScreenRectangle(0, 0);
    final Dimension size = super.getPreferredSize();
    final int maxWidth = rec.width / 4;
    if (size.width > maxWidth) {
      size.width = maxWidth; 
    }
    return size;
  }

  @Override
  public Dimension getMinimumSize() {
    final Dimension minSize = super.getMinimumSize();
    final Dimension prefSize = getPreferredSize();
    if (minSize.width > prefSize.width) {
      minSize.width = prefSize.width;
    }
    return minSize;
  }

  public void setSetupButton(final JButton setUpButton,
                                @Nullable final Project project,
                                final ProjectSdksModel jdksModel,
                                final SdkComboBoxItem firstItem,
                                @Nullable final Condition<? super Sdk> additionalSetup,
                                final boolean moduleJdkSetup) {
    setSetupButton(setUpButton, project, jdksModel, firstItem, additionalSetup,
                   ProjectBundle.message("project.roots.set.up.jdk.title", moduleJdkSetup ? 1 : 2));
  }

  public void setSetupButton(final JButton setUpButton,
                                @Nullable final Project project,
                                final ProjectSdksModel jdksModel,
                                final SdkComboBoxItem firstItem,
                                @Nullable final Condition<? super Sdk> additionalSetup,
                                final String actionGroupTitle) {

    mySetUpButton = setUpButton;
    mySetUpButton.addActionListener(e -> {
      DefaultActionGroup group = new DefaultActionGroup();
      jdksModel.createAddActions(group, this, getSelectedJdk(), jdk -> {
        if (project != null) {
          final JdkListConfigurable configurable = JdkListConfigurable.getInstance(project);
          configurable.addJdkNode(jdk, false);
        }
        reloadModel(new ActualSdkComboBoxItem(jdk), project);
        setSelectedJdk(jdk); //restore selection
        if (additionalSetup != null) {
          if (additionalSetup.value(jdk)) { //leave old selection
            setSelectedJdk(firstItem.getSdk());
          }
        }
      }, myCreationFilter);
      final DataContext dataContext = DataManager.getInstance().getDataContext(this);
      if (group.getChildrenCount() > 1) {
        JBPopupFactory.getInstance()
          .createActionGroupPopup(actionGroupTitle, group, dataContext, JBPopupFactory.ActionSelectionAid.MNEMONICS, false)
          .showUnderneathOf(setUpButton);
      }
      else {
        final AnActionEvent event =
          new AnActionEvent(null, dataContext, ActionPlaces.UNKNOWN, new Presentation(""), ActionManager.getInstance(), 0);
        group.getChildren(event)[0].actionPerformed(event);
      }
    });
  }

  public void setEditButton(final JButton editButton, final Project project, final Computable<? extends Sdk> retrieveJDK){
    editButton.addActionListener(e -> {
      final Sdk projectJdk = retrieveJDK.compute();
      if (projectJdk != null) {
        ProjectStructureConfigurable.getInstance(project).select(projectJdk, true);
      }
    });
    addActionListener(e -> {
      final SdkComboBoxItem selectedItem = getSelectedItem();
      if (selectedItem instanceof ProjectJdkComboBoxItem) {
        editButton.setEnabled(ProjectStructureConfigurable.getInstance(project).getProjectJdksModel().getProjectSdk() != null);
      }
      else {
        editButton.setEnabled(!(selectedItem instanceof InvalidJdkComboBoxItem) && selectedItem != null && selectedItem.getSdk() != null);
      }
    });
  }

  public JButton getSetUpButton() {
    return mySetUpButton;
  }

  @Override
  public SdkComboBoxItem getSelectedItem() {
    return (SdkComboBoxItem)super.getSelectedItem();
  }

  @Nullable
  public NimSdk getSelectedJdk() {
    final SdkComboBoxItem selectedItem = getSelectedItem();
    return selectedItem != null? selectedItem.getSdk() : null;
  }

  public void setSelectedJdk(NimSdk jdk) {
    final int index = indexOf(jdk);
    if (index >= 0) {
      setSelectedIndex(index);
    }
  }

  public void setInvalidJdk(String name) {
    removeInvalidElement();
    addItem(new InvalidJdkComboBoxItem(name));
    setSelectedIndex(getModel().getSize() - 1);
  }

  private int indexOf(NimSdk jdk) {
    final JdkComboBoxModel model = (JdkComboBoxModel)getModel();
    final int count = model.getSize();
    for (int idx = 0; idx < count; idx++) {
      final SdkComboBoxItem elementAt = model.getElementAt(idx);
      if (jdk == null) {
        if (elementAt instanceof NoneJdkComboBoxItem || elementAt instanceof ProjectJdkComboBoxItem) {
          return idx;
        }
      }
      else {
        var elementAtJdk = elementAt.getSdk();
        if (elementAtJdk != null && jdk.getName().equals(elementAtJdk.getName())) {
          return idx;
        }
      }
    }
    return -1;
  }

  private void removeInvalidElement() {
    final JdkComboBoxModel model = (JdkComboBoxModel)getModel();
    final int count = model.getSize();
    for (int idx = 0; idx < count; idx++) {
      final SdkComboBoxItem elementAt = model.getElementAt(idx);
      if (elementAt instanceof InvalidJdkComboBoxItem) {
        removeItemAt(idx);
        break;
      }
    }
  }

  public void reloadModel(SdkComboBoxItem firstItem, @Nullable Project project) {
    final JdkComboBoxModel model = (JdkComboBoxModel)getModel();
    if (project == null) {
      model.addElement(firstItem);
      return;
    }
    model.reload(firstItem, ProjectStructureConfigurable.getInstance(project).getProjectJdksModel(), mySdkTypeFilter, myFilter, false);
  }

  private static class JdkComboBoxModel extends DefaultComboBoxModel<SdkComboBoxItem> {
    JdkComboBoxModel(@NotNull final ProjectSdksModel jdksModel, @Nullable Condition<? super SdkTypeId> sdkTypeFilter,
                     @Nullable Condition<? super Sdk> sdkFilter, boolean addSuggested) {
      reload(null, jdksModel, sdkTypeFilter, sdkFilter, addSuggested);
    }

    void reload(@Nullable final SdkComboBoxItem firstItem,
                @NotNull final ProjectSdksModel jdksModel,
                @Nullable Condition<? super SdkTypeId> sdkTypeFilter,
                @Nullable Condition<? super Sdk> sdkFilter,
                boolean addSuggested) {
      removeAllElements();
      if (firstItem != null) addElement(firstItem);

      Sdk[] jdks = sortSdks(jdksModel.getSdks());
      for (Sdk jdk : jdks) {
        if (sdkFilter == null || sdkFilter.value(jdk)) {
          addElement(new ActualSdkComboBoxItem(jdk));
        }
      }
      if (addSuggested) {
        addSuggestedItems(sdkTypeFilter, jdks);
      }
    }

    @NotNull
    private static Sdk[] sortSdks(@NotNull final Sdk[] sdks) {
      Sdk[] clone = sdks.clone();
      Arrays.sort(clone, (sdk1, sdk2) -> {
        SdkType sdkType1 = (SdkType)sdk1.getSdkType();
        SdkType sdkType2 = (SdkType)sdk2.getSdkType();
        if (!sdkType1.getComparator().equals(sdkType2.getComparator())) return StringUtil.compare(sdkType1.getPresentableName(), sdkType2.getPresentableName(), true);
        return sdkType1.getComparator().compare(sdk1, sdk2);
      });
      return clone;
    }

    void addSuggestedItems(@Nullable Condition<? super SdkTypeId> sdkTypeFilter, Sdk[] jdks) {
      SdkType[] types = SdkType.getAllTypes();
      for (SdkType type : types) {
        if (sdkTypeFilter == null || sdkTypeFilter.value(type) && ContainerUtil.find(jdks, sdk -> sdk.getSdkType() == type) == null) {
          Collection<String> paths = type.suggestHomePaths();
          for (String path : paths) {
            if (path != null && type.isValidSdkHome(path)) {
              addElement(new SuggestedJdkItem(type, path));
            }
          }
        }
      }
    }
  }

  public static Condition<Sdk> getSdkFilter(@Nullable final Condition<? super SdkTypeId> filter) {
    return filter == null ? Conditions.alwaysTrue() : sdk -> filter.value(sdk.getSdkType());
  }

  public abstract static class SdkComboBoxItem {
    @Nullable
    public NimSdk getSdk() {
      return null;
    }

    @Nullable
    public String getSdkName() {
      return null;
    }
  }

  public static class ActualSdkComboBoxItem extends SdkComboBoxItem {
    private final NimSdk sdk;

    public ActualSdkComboBoxItem(@NotNull NimSdk sdk) {
      this.sdk = sdk;
    }

    @Override
    public String toString() {
      return sdk.getName();
    }

    @Nullable
    @Override
    public NimSdk getSdk() {
      return sdk;
    }

    @Nullable
    @Override
    public String getSdkName() {
      return sdk.getName();
    }
  }

  public static class ProjectJdkComboBoxItem extends SdkComboBoxItem {
    public String toString() {
      return ProjectBundle.message("jdk.combo.box.project.item");
    }
  }

  public static class NoneJdkComboBoxItem extends SdkComboBoxItem {
    public String toString() {
      return ProjectBundle.message("jdk.combo.box.none.item");
    }
  }

  private static class InvalidJdkComboBoxItem extends SdkComboBoxItem {
    private final String mySdkName;

    InvalidJdkComboBoxItem(String name) {
      mySdkName = name;
    }

    @Override
    public String getSdkName() {
      return mySdkName;
    }

    public String toString() {
      return ProjectBundle.message("jdk.combo.box.invalid.item", mySdkName);
    }
  }

  public static class SuggestedJdkItem extends SdkComboBoxItem {
    private final SdkType mySdkType;
    private final String myPath;

    SuggestedJdkItem(@NotNull SdkType sdkType, @NotNull String path) {
      mySdkType = sdkType;
      myPath = path;
    }

    @NotNull
    public SdkType getSdkType() {
      return mySdkType;
    }

    @NotNull
    public String getPath() {
      return myPath;
    }

    @Override
    public String toString() {
      return myPath;
    }
  }

  private static final String NO_JDK = ProjectBundle.message("jdk.missing.item");
  @NotNull

  public CellAppearanceEx forJdk(@Nullable final NimSdk jdk, final boolean isInComboBox, final boolean selected, final boolean showVersion) {
    if (jdk == null) {
      return FileAppearanceService.getInstance().forInvalidUrl(NO_JDK);
    }

    String name = jdk.getName();
    CompositeAppearance appearance = new CompositeAppearance();
    SdkType sdkType = (SdkType)jdk.getSdkType();
    appearance.setIcon(sdkType.getIcon());
    SimpleTextAttributes attributes = getTextAttributes(sdkType.sdkHasValidPath(jdk), selected);
    CompositeAppearance.DequeEnd ending = appearance.getEnding();
    ending.addText(name, attributes);

    if (showVersion) {
      String versionString = jdk.getVersionString();
      if (versionString != null && !versionString.equals(name)) {
        SimpleTextAttributes textAttributes = isInComboBox && !selected ? SimpleTextAttributes.SYNTHETIC_ATTRIBUTES :
                SystemInfo.isMac && selected ? new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN,
                        Color.WHITE) : SimpleTextAttributes.GRAY_ATTRIBUTES;
        ending.addComment(versionString, textAttributes);
      }
    }

    return ending.getAppearance();
  }

  private static SimpleTextAttributes getTextAttributes(final boolean valid, final boolean selected) {
    if (!valid) {
      return SimpleTextAttributes.ERROR_ATTRIBUTES;
    }
    else if (selected && !(SystemInfo.isWinVistaOrNewer && UIManager.getLookAndFeel().getName().contains("Windows"))) {
      return SimpleTextAttributes.SELECTED_SIMPLE_CELL_ATTRIBUTES;
    }
    else {
      return SimpleTextAttributes.SIMPLE_CELL_ATTRIBUTES;
    }
  }
}
