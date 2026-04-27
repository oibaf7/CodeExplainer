package com.codeexplainer.ui;

import com.codeexplainer.core.AppSettings;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent settingsComponent;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "CodeExplainer Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingsComponent = new AppSettingsComponent();
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance().getState());
        return !Objects.equals(state.endpoint, settingsComponent.getEndpoint())
                || !Objects.equals(state.model, settingsComponent.getModel());
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance().getState());
        state.endpoint = settingsComponent.getEndpoint();
        state.model = settingsComponent.getModel();
    }

    @Override
    public void reset() {
        AppSettings.State state = Objects.requireNonNull(AppSettings.getInstance().getState());
        settingsComponent.setEndpointFieldText(state.endpoint);
        settingsComponent.setModelField(state.model);
    }
}
