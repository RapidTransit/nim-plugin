package org.nim.nimble.library;

import com.intellij.openapi.components.PersistentStateComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
