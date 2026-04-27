package com.codeexplainer.ui;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JTextField endpointField = new JTextField();
    private final JTextField modelField = new JTextField();

    public AppSettingsComponent() {
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Endpoint:"), endpointField, 1, false)
                .addLabeledComponent(new JBLabel("Model:"), modelField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return endpointField;
    }

    public void setEndpointFieldText(@NotNull String text) {
        endpointField.setText(text);
    }

    public void setModelField(@NotNull String text) {
        modelField.setText(text);
    }

    @NotNull
    public String getEndpoint() {
        return endpointField.getText();
    }

    @NotNull
    public String getModel() {
        return modelField.getText();
    }

}
