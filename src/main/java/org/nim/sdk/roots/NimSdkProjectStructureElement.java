package org.nim.sdk.roots;

import com.intellij.openapi.roots.ui.configuration.projectRoot.StructureConfigurableContext;
import com.intellij.openapi.roots.ui.configuration.projectRoot.daemon.ProjectStructureElement;
import com.intellij.openapi.roots.ui.configuration.projectRoot.daemon.ProjectStructureElementUsage;
import com.intellij.openapi.roots.ui.configuration.projectRoot.daemon.ProjectStructureProblemsHolder;
import org.nim.sdk.NimSdk;

import java.util.Collections;
import java.util.List;

/**
 * @author nik
 */
public class NimSdkProjectStructureElement extends ProjectStructureElement {
  private final NimSdk mySdk;

  public NimSdkProjectStructureElement(StructureConfigurableContext context, NimSdk sdk) {
    super(context);
    mySdk = getModifiableSdk(sdk);
  }

  private NimSdk getModifiableSdk(NimSdk sdk) {
    NimSdk modifiableSdk = NimSdkProjectStructureConfigurable.getInstance(myContext.getProject()).getProjectJdksModel().getProjectSdks().get(sdk);
    return modifiableSdk != null? modifiableSdk : sdk;
  }

  public NimSdk getSdk() {
    return mySdk;
  }

  @Override
  public void check(ProjectStructureProblemsHolder problemsHolder) {
  }

  @Override
  public List<ProjectStructureElementUsage> getUsagesInElement() {
    return Collections.emptyList();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof NimSdkProjectStructureElement)) return false;
    return mySdk.equals(((NimSdkProjectStructureElement)o).mySdk);

  }

  @Override
  public int hashCode() {
    return mySdk.hashCode();
  }

  @Override
  public String getPresentableName() {
    return mySdk.getName();
  }

  @Override
  public String getTypeName() {
    return "SDK";
  }

  @Override
  public String getId() {
    return "sdk:" + mySdk.getName();
  }
}
