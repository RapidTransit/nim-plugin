// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk.roots;

import com.intellij.ide.highlighter.ArchiveFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.*;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.projectRoots.impl.UnknownSdkType;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.ThreeState;
import com.intellij.util.containers.SmartHashSet;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.nim.sdk.*;

import java.io.File;
import java.util.*;

@State(
  name = "NimProjectSdkTable",
  storages = @Storage(value = "nim-sdk.table.xml", roamingType = RoamingType.DISABLED, useSaveThreshold = ThreeState.NO)
)
public class NimProjectSdkTableImpl extends NimProjectSdkTable implements ExportableComponent, PersistentStateComponent<Element> {
  private final List<NimSdk> mySdks = new ArrayList<>();

  @NonNls
  private static final String ELEMENT_JDK = "sdk";

  private final Map<String, NimProjectSdkImpl> myCachedProjectJdks = new HashMap<>();
  private final MessageBus myMessageBus;

  // constructor is public because it is accessed from Upsource
  public NimProjectSdkTableImpl() {
    myMessageBus = ApplicationManager.getApplication().getMessageBus();
    // support external changes to jdk libraries (Endorsed Standards Override)
    final MessageBusConnection connection = ApplicationManager.getApplication().getMessageBus().connect();
    connection.subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
      private final FileTypeManager myFileTypeManager = FileTypeManager.getInstance();

      @Override
      public void after(@NotNull List<? extends VFileEvent> events) {
        if (!events.isEmpty()) {
          final Set<NimSdk> affected = new SmartHashSet<>();
          for (VFileEvent event : events) {
            addAffectedJavaSdk(event, affected);
          }
          if (!affected.isEmpty()) {
            for (NimSdk sdk : affected) {
              ((NimSdkType)sdk.getSdkType()).setupSdkPaths(sdk);
            }
          }
        }
      }

      private void addAffectedJavaSdk(VFileEvent event, Set<? super NimSdk> affected) {
        CharSequence fileName = null;
        if (event instanceof VFileCreateEvent) {
          if (((VFileCreateEvent)event).isDirectory()) return;
          fileName = ((VFileCreateEvent)event).getChildName();
        }
        else {
          final VirtualFile file = event.getFile();

          if (file != null && file.isValid()) {
            if (file.isDirectory()) {
              return;
            }
            fileName = file.getNameSequence();
          }
        }
        if (fileName == null) {
          final String eventPath = event.getPath();
          fileName = VfsUtil.extractFileName(eventPath);
        }
        if (fileName != null) {
          // avoid calling getFileType() because it will try to detect file type from content for unknown/text file types
          // consider only archive files that may contain libraries
          if (!ArchiveFileType.INSTANCE.equals(myFileTypeManager.getFileTypeByFileName(fileName))) {
            return;
          }
        }

        for (NimSdk sdk : mySdks) {
          if (sdk.getSdkType() instanceof JavaSdkType && !affected.contains(sdk)) {
            final String homePath = sdk.getHomePath();
            final String eventPath = event.getPath();
            if (!StringUtil.isEmpty(homePath) && FileUtil.isAncestor(homePath, eventPath, true)) {
              affected.add(sdk);
            }
          }
        }
      }
    });
  }

  @Override
  @NotNull
  public File[] getExportFiles() {
    return new File[]{PathManager.getOptionsFile("nim-sdk.table")};
  }

  @Override
  @NotNull
  public String getPresentableName() {
    return ProjectBundle.message("sdk.table.settings");
  }

  @Override
  @Nullable
  public NimSdk findJdk(@NotNull String name) {
    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, len = mySdks.size(); i < len; ++i) { // avoid foreach,  it instantiates ArrayList$Itr, this traversal happens very often
      final NimSdk jdk = mySdks.get(i);
      if (Comparing.strEqual(name, jdk.getName())) {
        return jdk;
      }
    }
    return null;
  }

  @Override
  @Nullable
  public NimSdk findJdk(@NotNull String name, @NotNull String type) {
    NimSdk projectJdk = findJdk(name);
    if (projectJdk != null) {
      return projectJdk;
    }
    final String uniqueName = type + "." + name;
    projectJdk = myCachedProjectJdks.get(uniqueName);
    if (projectJdk != null) return projectJdk;

    @NonNls final String jdkPrefix = "sdk.";
    final String jdkPath = System.getProperty(jdkPrefix + name);
    if (jdkPath == null) return null;

    final NimSdkType[] sdkTypes = NimSdkType.getAllTypes();
    for (NimSdkType sdkType : sdkTypes) {
      if (Comparing.strEqual(type, sdkType.getName())) {
        if (sdkType.isValidSdkHome(jdkPath)) {
          NimProjectSdkImpl projectJdkImpl = new NimProjectSdkImpl(name, sdkType);
          projectJdkImpl.setHomePath(jdkPath);
          sdkType.setupSdkPaths(projectJdkImpl);
          myCachedProjectJdks.put(uniqueName, projectJdkImpl);
          return projectJdkImpl;
        }
        break;
      }
    }
    return null;
  }

  @NotNull
  @Override
  public NimSdk[] getAllJdks() {
    return mySdks.toArray(new NimSdk[0]);
  }

  @NotNull
  @Override
  public List<NimSdk> getSdksOfType(@NotNull final NimSdkTypeId type) {
    List<NimSdk> result = new ArrayList<>();
    final NimSdk[] sdks = getAllJdks();
    for (NimSdk sdk : sdks) {
      if (sdk.getSdkType() == type) {
        result.add(sdk);
      }
    }
    return result;
  }

  @TestOnly
  public void addTestJdk(@NotNull NimSdk jdk, @NotNull Disposable parentDisposable) {
    ApplicationManager.getApplication().assertWriteAccessAllowed();
    mySdks.add(jdk);
    Disposer.register(parentDisposable, () -> WriteAction.runAndWait(()-> mySdks.remove(jdk)));
  }

  @Override
  public void addJdk(@NotNull NimSdk jdk) {
    ApplicationManager.getApplication().assertWriteAccessAllowed();
    mySdks.add(jdk);
    myMessageBus.syncPublisher(JDK_TABLE_TOPIC).jdkAdded(jdk);
  }

  @Override
  public void removeJdk(@NotNull NimSdk jdk) {
    ApplicationManager.getApplication().assertWriteAccessAllowed();
    myMessageBus.syncPublisher(JDK_TABLE_TOPIC).jdkRemoved(jdk);
    mySdks.remove(jdk);
    if (jdk instanceof Disposable) {
      Disposer.dispose((Disposable)jdk);
    }
  }

  @Override
  public void updateJdk(@NotNull NimSdk originalJdk, @NotNull NimSdk modifiedJdk) {
    final String previousName = originalJdk.getName();
    final String newName = modifiedJdk.getName();

    ((NimProjectSdkImpl)modifiedJdk).copyTo((NimProjectSdkImpl)originalJdk);

    if (!previousName.equals(newName)) {
      // fire changes because after renaming JDK its name may match the associated jdk name of modules/project
      myMessageBus.syncPublisher(JDK_TABLE_TOPIC).jdkNameChanged(originalJdk, previousName);
    }
  }

  @Override
  @NotNull
  public NimSdkTypeId getDefaultSdkType() {
    return UnknownNimSdkType.getInstance("");
  }

  @Override
  @NotNull
  public NimSdkTypeId getSdkTypeByName(@NotNull String sdkTypeName) {
    return findSdkTypeByName(sdkTypeName);
  }

  @NotNull
  private static NimSdkTypeId findSdkTypeByName(@NotNull String sdkTypeName) {
    final NimSdkType[] allSdkTypes = NimSdkType.getAllTypes();
    for (final NimSdkType type : allSdkTypes) {
      if (type.getName().equals(sdkTypeName)) {
        return type;
      }
    }
    return UnknownNimSdkType.getInstance(sdkTypeName);
  }

  @NotNull
  @Override
  public NimSdk createSdk(@NotNull final String name, @NotNull final NimSdkTypeId sdkType) {
    return new NimProjectSdkImpl(name, sdkType);
  }

  @Override
  public void loadState(@NotNull Element element) {
    mySdks.clear();

    for (Element child : element.getChildren(ELEMENT_JDK)) {
      NimProjectSdkImpl jdk = new NimProjectSdkImpl(null, null);
      jdk.readExternal(child, this);
      mySdks.add(jdk);
    }
  }

  @Override
  public Element getState() {
    Element element = new Element("state");
    for (NimSdk jdk : mySdks) {
      if (jdk instanceof NimProjectSdkImpl) {
        Element e = new Element(ELEMENT_JDK);
        ((NimProjectSdkImpl)jdk).writeExternal(e);
        element.addContent(e);
      }
    }
    return element;
  }
}
