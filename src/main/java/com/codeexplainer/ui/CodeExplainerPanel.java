package com.codeexplainer.ui;


import com.codeexplainer.core.OllamaClient;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.net.http.HttpClient;

import static com.intellij.ui.ColorUtil.toHex;

public class CodeExplainerPanel {
    private final JPanel contentPanel;
    private final JEditorPane response = new JEditorPane("text/html", "");
    private final OllamaClient ollamaClient;
    public static CodeExplainerPanel instance;

    //color palette
    private static final JBColor COLOR_BG      = new JBColor(new Color(0xF7F8FA), new Color(0x1E1F22));
    private static final JBColor COLOR_FG      = new JBColor(new Color(0x1A1A1A), new Color(0xD4D4D4));
    private static final JBColor COLOR_MUTED   = new JBColor(new Color(0x6C6C6C), new Color(0x787878));
    private static final JBColor COLOR_SURROUND = new JBColor(new Color(0xB0B1B5), new Color(0x4A4D52));
    private static final JBColor COLOR_BORDER   = new JBColor(new Color(0xB0B1B5), new Color(0x4A4D52));

    public CodeExplainerPanel(ToolWindow toolWindow) {
        instance = this;
        ollamaClient = new OllamaClient(HttpClient.newHttpClient());

        // Response area
        response.setEditable(false);
        response.setOpaque(true);
        response.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        response.setBorder(JBUI.Borders.empty(12, 16));
        showPlaceholder();

        JScrollPane scrollPane = new JBScrollPane(response);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        scrollPane.setOpaque(false);

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
        contentPanel.setBackground(COLOR_SURROUND);
        contentPanel.setBorder(JBUI.Borders.empty(20, 16));
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
        String fg    = toHex(COLOR_FG);
        String bg    = toHex(COLOR_BG);
        String style = "font-family: " + UIUtil.getLabelFont().getFamily() + ", Arial, sans-serif;" +
                "font-size: 11px;" +
                "color: " + fg + ";" +
                "background-color: " + bg + ";" +
                "padding: 4px 2px;" +
                "line-height: 1.65;" +
                "word-wrap: break-word;";;
        response.setBackground(COLOR_BG);
        response.setText(
                "<html><body style='" + style + "'>" +
                        explanation +
                        "</body></html>"
        );
        response.setCaretPosition(0);
    }

    /** Renders the idle placeholder (shown before the first explain call). */
    private void showPlaceholder() {
        String fg    = toHex(COLOR_MUTED);
        String bg    = toHex(COLOR_BG);
        String style = "font-family: " + UIUtil.getLabelFont().getFamily() + ", Arial, sans-serif;" +
                "font-size: 11px;" +
                "color: " + fg + ";" +
                "background-color: " + bg + ";" +
                "padding: 4px 2px;" +
                "line-height: 1.65;" +
                "word-wrap: break-word;";
        response.setBackground(COLOR_BG);
        response.setText(
                "<html><body style='" + style + "'>" +
                        "<p style='text-align:center; margin-top: 40px;'>" +
                        "Select code in the editor and choose<br>" +
                        "<b>Explain Code</b> from the right-click menu." +
                        "</p></body></html>"
        );
    }

    /**
     * Calls the Ollama client and returns the resulting parsed json
     * @param code selected by user
     */
    public void explain(String code) {
        String fg    = toHex(COLOR_MUTED);
        String bg    = toHex(COLOR_BG);
        String style = "font-family: " + UIUtil.getLabelFont().getFamily() + ", Arial, sans-serif;" +
                "font-size: 11px;" +
                "color: " + fg + ";" +
                "background-color: " + bg + ";" +
                "padding: 4px 2px;" +
                "line-height: 1.65;" +
                "word-wrap: break-word;";
        response.setBackground(COLOR_BG);
        response.setText(
                "<html><body style='" + style + "'>" +
                        "<p style='text-align:center; margin-top: 40px;'>Analysing…</p>" +
                        "</body></html>"
        );
        ollamaClient.explain(code).thenAccept(result ->
                SwingUtilities.invokeLater(() -> showExplanation(result))
        );
    }
}
