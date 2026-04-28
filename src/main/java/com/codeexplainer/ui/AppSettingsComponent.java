package com.codeexplainer.ui;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JTextField endpointField = new JTextField();
    private final JTextField modelField = new JTextField();

    public AppSettingsComponent() {
        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(buildLabel("Endpoint:", buildHelpIcon(
                        "<html>The full URL of your Ollama HTTP API.<br>" +
                                "Default: <b>http://localhost:11434/api/generate</b><br>" +
                                "Change this if Ollama runs on a different host or port.</html>"
                        )),
                        endpointField, 1, false)
                .addLabeledComponent(buildLabel("Model:",
                        buildHelpIcon("<html>The Ollama model tag to use for explanations.<br>" +
                                "Default: <b>qwen2.5-coder:7b</b><br>" +
                                "Run <tt>ollama list</tt> in a terminal to see available models.</html>")),
                        modelField, 1, false)
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

    private JPanel buildLabel(String text, JLabel helpIcon) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setOpaque(false);
        row.add(new JBLabel(text));
        row.add(Box.createHorizontalStrut(4));
        row.add(helpIcon);
        return row;
    }

    private JLabel buildHelpIcon(String text) {
        JLabel icon = new JLabel(AllIcons.General.ContextHelp);
        icon.setToolTipText(text);
        icon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        icon.setBorder(JBUI.Borders.empty(1, 2));
        return icon;
    }

}
