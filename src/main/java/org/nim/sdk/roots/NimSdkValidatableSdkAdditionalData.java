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
package org.nim.sdk.roots;

import com.intellij.openapi.options.ConfigurationException;
import org.nim.sdk.NimSdkAdditionalData;
import org.nim.sdk.NimSdkModel;

/**
 * @author yole
 */
public interface NimSdkValidatableSdkAdditionalData extends NimSdkAdditionalData {
  /**
   * Checks if the SDK properties are configured correctly, and throws an exception
   * if they are not.
   *
   * @param sdkModel the model containing all configured SDKs.
   * @throws ConfigurationException if the SDK is not configured correctly.
   */
  void checkValid(NimSdkModel sdkModel) throws ConfigurationException;
}
