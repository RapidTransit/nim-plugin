package org.nim.nimble.library;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nim.sdk.NimProjectSdkImpl;
import org.nim.sdk.NimSdk;

@State(name = "NimbleProject", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class NimbleProjectServiceImpl implements PersistentStateComponent<NimbleState> {

    private NimSdk sdk;

    @Nullable
    @Override
    public NimbleState getState() {
        return null;
    }

    @Override
    public void loadState(@NotNull NimbleState state) {

    }

    public NimSdk getSdk() {
        return sdk;
    }

    public void setSdk(NimSdk sdk) {
        this.sdk = sdk;
    }
}
