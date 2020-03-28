// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.nim.sdk;

import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * @author yole
 */
public interface NimSdkTypeId {
  @NotNull
  String getName();

  @Nullable
  String getVersionString(@NotNull NimSdk sdk);

  void saveAdditionalData(@NotNull NimSdkAdditionalData additionalData, @NotNull Element additional);

  @Nullable
  NimSdkAdditionalData loadAdditionalData(@NotNull NimSdk currentSdk, @NotNull Element additional);

  /**
   * An SDK can be located on a local machine or on a remote or virtual machine. In the latter case this method returns false.
   */
  default boolean isLocalSdk(@NotNull NimSdk sdk) {
    return true;
  }

  /**
   * Note to implementors: you may need to override this method if SDKs of this type have non-trivial version strings.
   */
  @NotNull
  default Comparator<NimSdk> versionComparator() {
    return (sdk1, sdk2) -> {
      assert sdk1.getSdkType() == this : sdk1;
      assert sdk2.getSdkType() == this : sdk2;
      return StringUtil.compareVersionNumbers(sdk1.getVersionString(), sdk2.getVersionString());
    };
  }
}