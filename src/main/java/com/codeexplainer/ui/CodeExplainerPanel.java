package com.codeexplainer.ui;


import com.codeexplainer.core.OllamaClient;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.net.http.HttpClient;

public class CodeExplainerPanel {
    private final JPanel contentPanel;
    private final JEditorPane response = new JEditorPane("text/html", "");
    private final OllamaClient ollamaClient;
    public static CodeExplainerPanel instance;

    public CodeExplainerPanel(ToolWindow toolWindow) {
        instance = this;
        ollamaClient = new OllamaClient(HttpClient.newHttpClient());

        // Response area
        response.setEditable(false);
        response.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        response.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JScrollPane scrollPane = new JBScrollPane(response);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Center column with max width for readability
        JPanel centerColumn = new JPanel(new BorderLayout());
        centerColumn.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        centerColumn.add(scrollPane, BorderLayout.CENTER);
        centerColumn.setPreferredSize(new Dimension(600, 0));

        // Wrapper to horizontally center the column
        JPanel wrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        wrapper.add(centerColumn, gbc);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        contentPanel.add(wrapper, BorderLayout.CENTER);
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    /**
     * Shows explanation returned from the AI model
     * @param explanation in HTML format
     */
    public void showExplanation(String explanation) {
        String answer = "<html><body style='" +
                "font-family: Arial, sans-serif;" +
                "font-size: 13px;" +
                "color: #BBBBBB;" +
                "background-color: #2B2B2B;" +
                "padding: 12px 16px;" +
                "line-height: 1.6;" +
                "'>" + explanation + "</body></html>";
        response.setText(explanation);
    }

    /**
     * Calls the Ollama client and returns the resulting parsed json
     * @param code selected by user
     */
    public void explain(String code) {
        response.setText("Explaining...");
        ollamaClient.explain(code).thenAccept(result ->
                SwingUtilities.invokeLater(() -> showExplanation(result)));
    }
}
