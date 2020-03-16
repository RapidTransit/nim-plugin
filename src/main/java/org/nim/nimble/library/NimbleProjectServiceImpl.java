package org.nim.nimble.library;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "NimbleProject", storages = @Storage(StoragePathMacros.WORKSPACE_FILE))
public class NimbleProjectServiceImpl implements PersistentStateComponent<NimbleState> {



    @Nullable
    @Override
    public NimbleState getState() {
        return null;
    }

    @Override
    public void loadState(@NotNull NimbleState state) {

    }
}
