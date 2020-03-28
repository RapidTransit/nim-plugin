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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public abstract class NimProjectRootManagerEx extends NimSdkProjectRootManager {
  public static NimProjectRootManagerEx getInstanceEx(Project project) {
    return (NimProjectRootManagerEx)getInstance(project);
  }

  public abstract void addProjectJdkListener(@NotNull ProjectNimSdkListener listener);

  public abstract void removeProjectJdkListener(@NotNull ProjectNimSdkListener listener);

  // invokes runnable surrounded by beforeRootsChange()/rootsChanged() callbacks
  public abstract void makeRootsChange(@NotNull Runnable runnable, boolean fileTypes, boolean fireEvents);

  public abstract void markRootsForRefresh();

  public abstract void mergeRootsChangesDuring(@NotNull Runnable runnable);

  public abstract void clearScopesCachesForModules();

  /**
   * @see NimProjectRootManagerEx#addProjectJdkListener(ProjectNimSdkListener)
   * @see NimProjectRootManagerEx#removeProjectJdkListener(ProjectNimSdkListener)
   */
  @FunctionalInterface
  public interface ProjectNimSdkListener extends EventListener {
    void projectJdkChanged();
  }
}