// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.EventListener;
import java.util.List;

public abstract class NimProjectSdkTable {
  public static NimProjectSdkTable getInstance() {
    return ServiceManager.getService(NimProjectSdkTable.class);
  }

  @Nullable
  public abstract NimSdk findJdk(@NotNull String name);

  @Nullable
  public abstract NimSdk findJdk(@NotNull String name, @NotNull String type);

  @NotNull
  public abstract NimSdk[] getAllJdks();

  @NotNull
  public abstract List<NimSdk> getSdksOfType(@NotNull NimSdkTypeId type);

  @Nullable
  public NimSdk findMostRecentSdkOfType(@NotNull NimSdkTypeId type) {
    return getSdksOfType(type).stream().max(type.versionComparator()).orElse(null);
  }

  /** @deprecated comparing version strings across SDK types makes no sense; use {@link #findMostRecentSdkOfType} */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "2020.3")
  public NimSdk findMostRecentSdk(@NotNull Condition<? super NimSdk> condition) {
    NimSdk found = null;
    for (NimSdk each : getAllJdks()) {
      if (condition.value(each) &&
          (found == null || Comparing.compare(each.getVersionString(), found.getVersionString()) > 0)) {
        found = each;
      }
    }
    return found;
  }

  public abstract void addJdk(@NotNull NimSdk jdk);

  @TestOnly
  public void addJdk(@NotNull NimSdk jdk, @NotNull Disposable parentDisposable) {
    addJdk(jdk);
    Disposer.register(parentDisposable, () -> WriteAction.runAndWait(()-> removeJdk(jdk)));
  }

  public abstract void removeJdk(@NotNull NimSdk jdk);

  public abstract void updateJdk(@NotNull NimSdk originalJdk, @NotNull NimSdk modifiedJdk);

  public interface Listener extends EventListener {
    void jdkAdded(@NotNull NimSdk jdk);
    void jdkRemoved(@NotNull NimSdk jdk);
    void jdkNameChanged(@NotNull NimSdk jdk, @NotNull String previousName);
  }

  public static class Adapter implements Listener {
    @Override public void jdkAdded(@NotNull NimSdk jdk) { }
    @Override public void jdkRemoved(@NotNull NimSdk jdk) { }
    @Override public void jdkNameChanged(@NotNull NimSdk jdk, @NotNull String previousName) { }
  }

  @NotNull
  public abstract NimSdkTypeId getDefaultSdkType();

  @NotNull
  public abstract NimSdkTypeId getSdkTypeByName(@NotNull String name);

  @NotNull
  public abstract NimSdk createSdk(@NotNull String name, @NotNull NimSdkTypeId sdkType);

  public static final Topic<Listener> JDK_TABLE_TOPIC = Topic.create("Project JDK table", Listener.class);
}