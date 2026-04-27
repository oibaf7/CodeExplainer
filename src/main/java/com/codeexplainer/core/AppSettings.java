package com.codeexplainer.core;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@State(
        name = "com.codeexplainer.core.AppSettings",
        storages = @Storage("SdkSettingsPlugin.xml")
)
public class AppSettings implements PersistentStateComponent<AppSettings.State> {

    public static class State {
        public String endpoint;
        public String model;
    }

    private State state = new State();

    /**
     * Utility methods for other classes to gain access to an instance of the class
     * @return an instance of AppSettings
     */
    public static AppSettings getInstance() {
        return ApplicationManager.getApplication()
                .getService(AppSettings.class);
    }

    @Override
    public @Nullable State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
    }

}
