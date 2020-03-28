/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package org.nim.sdk.roots;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Consumer;
import com.intellij.util.NullableConsumer;
import com.intellij.util.text.UniqueNameGenerator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yole
 */
public class NimSdkConfigurationUtil {
  private static final Logger LOG = Logger.getInstance(NimSdkConfigurationUtil.class);
  private NimSdkConfigurationUtil() { }

  public static void createSdk(@Nullable final Project project,
                               @NotNull NimSdk[] existingSdks,
                               @NotNull NullableConsumer<? super NimSdk> onSdkCreatedCallBack,
                               final boolean createIfExists,
                               @NotNull NimSdkType... sdkTypes) {
    createSdk(project, existingSdks, onSdkCreatedCallBack, createIfExists, true, sdkTypes);
  }

  public static void createSdk(@Nullable final Project project,
                               @NotNull NimSdk[] existingSdks,
                               @NotNull NullableConsumer<? super NimSdk> onSdkCreatedCallBack,
                               final boolean createIfExists,
                               final boolean followSymLinks,
                               @NotNull NimSdkType... sdkTypes) {
    if (sdkTypes.length == 0) {
      onSdkCreatedCallBack.consume(null);
      return;
    }

    FileChooserDescriptor descriptor = createCompositeDescriptor(sdkTypes);
    // XXX: Workaround for PY-21787 since the native macOS dialog always follows symlinks
    if (!followSymLinks) {
      descriptor.setForcedToUseIdeaFileChooser(true);
    }
    VirtualFile suggestedDir = getSuggestedSdkRoot(sdkTypes[0]);
    FileChooser.chooseFiles(descriptor, project, suggestedDir, new FileChooser.FileChooserConsumer() {
      @Override
      public void consume(List<VirtualFile> selectedFiles) {
        for (NimSdkType sdkType : sdkTypes) {
          final String path = selectedFiles.get(0).getPath();
          if (sdkType.isValidSdkHome(path)) {
            NimSdk newSdk = null;
            if (!createIfExists) {
              for (NimSdk sdk : existingSdks) {
                if (path.equals(sdk.getHomePath())) {
                  newSdk = sdk;
                  break;
                }
              }
            }
            if (newSdk == null) {
              newSdk = setupSdk(existingSdks, selectedFiles.get(0), sdkType, false, null, null);
            }
            onSdkCreatedCallBack.consume(newSdk);
            return;
          }
        }
        onSdkCreatedCallBack.consume(null);
      }

      @Override
      public void cancelled() {
        onSdkCreatedCallBack.consume(null);
      }
    });
  }

  public static void createSdk(@Nullable final Project project,
                               @NotNull NimSdk[] existingSdks,
                               @NotNull NullableConsumer<? super NimSdk> onSdkCreatedCallBack,
                               @NotNull NimSdkType... sdkTypes) {
    createSdk(project, existingSdks, onSdkCreatedCallBack, true, sdkTypes);
  }

  @NotNull
  private static FileChooserDescriptor createCompositeDescriptor(@NotNull NimSdkType... sdkTypes) {
    return new FileChooserDescriptor(sdkTypes[0].getHomeChooserDescriptor()) {
      @Override
      public void validateSelectedFiles(@NotNull final VirtualFile[] files) throws Exception {
        if (files.length > 0) {
          for (NimSdkType type : sdkTypes) {
            if (type.isValidSdkHome(files[0].getPath())) {
              return;
            }
          }
        }
        String key = files.length > 0 && files[0].isDirectory() ? "sdk.configure.home.invalid.error" : "sdk.configure.home.file.invalid.error";
        throw new Exception(ProjectBundle.message(key, sdkTypes[0].getPresentableName()));
      }
    };
  }

  public static void addSdk(@NotNull final NimSdk sdk) {
    ApplicationManager.getApplication().runWriteAction(() -> NimProjectSdkTable.getInstance().addJdk(sdk));
  }

  public static void removeSdk(@NotNull NimSdk sdk) {
    ApplicationManager.getApplication().runWriteAction(() -> NimProjectSdkTable.getInstance().removeJdk(sdk));
  }

  @Nullable
  public static NimSdk setupSdk(@NotNull NimSdk[] allSdks,
                                @NotNull VirtualFile homeDir,
                                @NotNull NimSdkType sdkType,
                                final boolean silent,
                                @Nullable final NimSdkAdditionalData additionalData,
                                @Nullable final String customSdkSuggestedName) {
    NimProjectSdkImpl sdk = null;
    try {
      sdk = createSdk(Arrays.asList(allSdks), homeDir, sdkType, additionalData, customSdkSuggestedName);

      sdkType.setupSdkPaths(sdk);
    }
    catch (Throwable e) {
      LOG.warn("Error creating or configuring sdk: homeDir=[" + homeDir + "]; " +
               "sdkType=[" + sdkType + "]; " +
               "additionalData=[" + additionalData + "]; " +
               "customSdkSuggestedName=[" + customSdkSuggestedName + "]; " +
               "sdk=[" + sdk + "]", e);
      if (!silent) {
        Messages.showErrorDialog("Error configuring SDK: " +
                                 e.getMessage() +
                                 ".\nPlease make sure that " +
                                 FileUtil.toSystemDependentName(homeDir.getPath()) +
                                 " is a valid home path for this SDK type.", "Error Configuring SDK");
      }
      return null;
    }
    return sdk;
  }

  /**
   * @deprecated Use {@link NimSdkConfigurationUtil#createSdk(Collection, VirtualFile, NimSdkType, NimSdkAdditionalData, String)} instead.
   * This method will be removed in 2020.1.
   */
  @NotNull
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "2020.1")
  public static NimProjectSdkImpl createSdk(@NotNull NimSdk[] allSdks,
                                            @NotNull VirtualFile homeDir,
                                            @NotNull NimSdkType sdkType,
                                            @Nullable NimSdkAdditionalData additionalData,
                                            @Nullable String customSdkSuggestedName) {
    return createSdk(Arrays.asList(allSdks), homeDir, sdkType, additionalData, customSdkSuggestedName);
  }

  @NotNull
  public static NimProjectSdkImpl createSdk(@NotNull Collection<? extends NimSdk> allSdks,
                                            @NotNull VirtualFile homeDir,
                                            @NotNull NimSdkType sdkType,
                                            @Nullable NimSdkAdditionalData additionalData,
                                            @Nullable String customSdkSuggestedName) {
    return createSdk(allSdks, sdkType.sdkPath(homeDir), sdkType, additionalData, customSdkSuggestedName);
  }

  @NotNull
  public static NimProjectSdkImpl createSdk(@NotNull Collection<? extends NimSdk> allSdks,
                                            @NotNull String homePath,
                                            @NotNull NimSdkType sdkType,
                                            @Nullable NimSdkAdditionalData additionalData,
                                            @Nullable String customSdkSuggestedName) {
    final String sdkName = customSdkSuggestedName == null
                           ? createUniqueSdkName(sdkType, homePath, allSdks)
                           : createUniqueSdkName(customSdkSuggestedName, allSdks);

    final NimProjectSdkImpl sdk = new NimProjectSdkImpl(sdkName, sdkType);

    if (additionalData != null) {
      // additional initialization.
      // E.g. some ruby sdks must be initialized before
      // setupSdkPaths() method invocation
      sdk.setSdkAdditionalData(additionalData);
    }

    sdk.setHomePath(homePath);
    return sdk;
  }

  public static void setDirectoryProjectSdk(@NotNull final Project project, @Nullable final NimSdk sdk) {
    ApplicationManager.getApplication().runWriteAction(() -> {
      NimSdkProjectRootManager.getInstance(project).setProjectSdk(sdk);
      final Module[] modules = ModuleManager.getInstance(project).getModules();
      if (modules.length > 0) {
        ModuleRootModificationUtil.setSdkInherited(modules[0]);
      }
    });
  }

  public static void configureDirectoryProjectSdk(@NotNull Project project,
                                                  @Nullable Comparator<? super NimSdk> preferredSdkComparator,
                                                  @NotNull NimSdkType... sdkTypes) {
    NimSdk existingSdk = NimSdkProjectRootManager.getInstance(project).getProjectSdk();
    if (existingSdk != null && ArrayUtil.contains(existingSdk.getSdkType(), sdkTypes)) {
      return;
    }

    NimSdk sdk = findOrCreateSdk(preferredSdkComparator, sdkTypes);
    if (sdk != null) {
      setDirectoryProjectSdk(project, sdk);
    }
  }

  @Nullable
  public static NimSdk findOrCreateSdk(@Nullable Comparator<? super NimSdk> comparator, @NotNull NimSdkType... sdkTypes) {
    final Project defaultProject = ProjectManager.getInstance().getDefaultProject();
    final NimSdk sdk = NimSdkProjectRootManager.getInstance(defaultProject).getProjectSdk();
    if (sdk != null) {
      for (NimSdkType type : sdkTypes) {
        if (sdk.getSdkType() == type) {
          return sdk;
        }
      }
    }
    for (NimSdkType type : sdkTypes) {
      List<NimSdk> sdks = NimProjectSdkTable.getInstance().getSdksOfType(type);
      if (!sdks.isEmpty()) {
        if (comparator != null) {
          Collections.sort(sdks, comparator);
        }
        return sdks.get(0);
      }
    }
    for (NimSdkType sdkType : sdkTypes) {
      final String suggestedHomePath = sdkType.suggestHomePath();
      if (suggestedHomePath != null && sdkType.isValidSdkHome(suggestedHomePath)) {
        NimSdk an_sdk = createAndAddSDK(suggestedHomePath, sdkType);
        if (an_sdk != null) return an_sdk;
      }
    }
    return null;
  }

  /**
   * Tries to create an SDK identified by path; if successful, add the SDK to the global SDK table.
   * <p>
   * Must be called from the EDT (because it uses {@link WriteAction#compute} under the hood).
   *
   * @param path    identifies the SDK
   * @return newly created SDK, or null.
   */
  @Nullable
  public static NimSdk createAndAddSDK(@NotNull String path, @NotNull NimSdkType sdkType) {
    VirtualFile sdkHome =
      WriteAction.compute(() -> LocalFileSystem.getInstance().refreshAndFindFileByPath(path));
    if (sdkHome != null) {
      final NimSdk newSdk = setupSdk(NimProjectSdkTable.getInstance().getAllJdks(), sdkHome, sdkType, true, null, null);
      if (newSdk != null) {
        addSdk(newSdk);
      }
      return newSdk;
    }
    return null;
  }

  @NotNull
  public static String createUniqueSdkName(@NotNull NimSdkType type, String home, final Collection<? extends NimSdk> sdks) {
    return createUniqueSdkName(type.suggestSdkName(null, home), sdks);
  }

  @NotNull
  public static String createUniqueSdkName(@NotNull String suggestedName, @NotNull Collection<? extends NimSdk> sdks) {
    Set<String> nameList = sdks.stream().map( jdk -> jdk.getName()).collect(Collectors.toSet());

    return UniqueNameGenerator.generateUniqueName(suggestedName, "", "", " (", ")", o -> !nameList.contains(o));
  }

  public static void selectSdkHome(@NotNull final NimSdkType sdkType, @NotNull final Consumer<? super String> consumer) {
    final FileChooserDescriptor descriptor = sdkType.getHomeChooserDescriptor();
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      NimSdk sdk = NimProjectSdkTable.getInstance().findMostRecentSdkOfType(sdkType);
      if (sdk == null) throw new RuntimeException("No SDK of type " + sdkType + " found");
      consumer.consume(sdk.getHomePath());
      return;
    }
    FileChooser.chooseFiles(descriptor, null, getSuggestedSdkRoot(sdkType), chosen -> {
      final String path = chosen.get(0).getPath();
      if (sdkType.isValidSdkHome(path)) {
        consumer.consume(path);
        return;
      }

      final String adjustedPath = sdkType.adjustSelectedSdkHome(path);
      if (sdkType.isValidSdkHome(adjustedPath)) {
        consumer.consume(adjustedPath);
      }
    });
  }

  @Nullable
  public static VirtualFile getSuggestedSdkRoot(@NotNull NimSdkType sdkType) {
    final String homePath = sdkType.suggestHomePath();
    return homePath == null ? null : LocalFileSystem.getInstance().findFileByPath(homePath);
  }

  @NotNull
  public static List<String> filterExistingPaths(@NotNull NimSdkType sdkType, Collection<String> sdkHomes, final NimSdk[] sdks) {
    List<String> result = new ArrayList<>();
    for (String sdkHome : sdkHomes) {
      if (findByPath(sdkType, sdks, sdkHome) == null) {
        result.add(sdkHome);
      }
    }
    return result;
  }

  @Nullable
  private static NimSdk findByPath(@NotNull NimSdkType sdkType, @NotNull NimSdk[] sdks, @NotNull String sdkHome) {
    for (NimSdk sdk : sdks) {
      final String path = sdk.getHomePath();
      if (sdk.getSdkType() == sdkType && path != null &&
          FileUtil.pathsEqual(FileUtil.toSystemIndependentName(path), FileUtil.toSystemIndependentName(sdkHome))) {
        return sdk;
      }
    }
    return null;
  }
}
